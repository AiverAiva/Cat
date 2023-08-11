package me.weikuwu.cute.modules.macros;

import me.weikuwu.cute.config.Config;
import me.weikuwu.cute.events.PlayerInteractEvent;
import me.weikuwu.cute.events.Render3DEvent;
import me.weikuwu.cute.events.SoundEvent;
import me.weikuwu.cute.utils.MouseControl;
import me.weikuwu.cute.utils.client.KeybindUtils;
import me.weikuwu.cute.utils.client.Rotation;
import me.weikuwu.cute.utils.client.RotationUtils;
import me.weikuwu.cute.utils.gui.ChatUtils;
import me.weikuwu.cute.utils.gui.InventoryUtils;
import me.weikuwu.cute.utils.math.RandomUtils;
import me.weikuwu.cute.utils.math.Time;
import me.weikuwu.cute.utils.math.Vec;
import me.weikuwu.cute.utils.network.Requests;
import me.weikuwu.cute.utils.render.Render3D;
import me.weikuwu.cute.utils.skyblock.SkyblockUtils;
import net.minecraft.client.*;
import net.minecraft.entity.item.*;
import net.minecraft.entity.*;
import net.minecraft.client.settings.*;
import net.minecraft.client.gui.*;
import net.minecraft.entity.player.*;
import net.minecraft.world.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.entity.projectile.*;
import java.util.*;
import net.minecraft.network.play.server.*;
import net.minecraft.util.*;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AutoFish {
    private static Minecraft mc;
    private static List<String> fishingMobs;
    private static Time throwTimer;
    private static Time inWaterTimer;
    private static Time killTimer;
    private static Time recoverTimer;
    private static EntityArmorStand curScStand;
    private static Entity curSc;
    private static boolean killing;
    private static int clicksLeft;
    private static boolean flash;
    private static BlockPos oldPos;
    private static double oldBobberPosY;
    private static boolean oldBobberInWater;
    private static int ticks;
    private static Vec3 startPos;
    private static Rotation startRot;
    private static List<ParticleEntry> particleList;
    private AutoFishState afs;
    private AAState aaState;

    private final EntityPlayer player = Minecraft.getMinecraft().thePlayer;

    int rodSlot;
    int weaponSlot;

    public static boolean Enabled;
    public static int Status = 0;

    public void onEnable() {
        this.resetVariables();
        RotationUtils.reset();
//        if (this.assistMode.isEnabled()) {
//            this.afs = AutoFishState.IN_WATER;
//            return;
//        }
        if (Config.hypDamage <= 0) {
            ChatUtils.send("Configure your HypDamage pls ty");
            Enabled = false;
            return;
        }

        String[] rodNames = { "Rod" };
        rodSlot = InventoryUtils.findHotbarItemSlot(player, rodNames);
        String[] weaponNames = { "Hyperion", "Astraea", "Valkyrie", "Scylla", "Spirit Sceptre" };
        weaponSlot = InventoryUtils.findHotbarItemSlot(player, weaponNames);

        if (rodSlot == -1) {
            ChatUtils.send("You dont have any rod");
            Enabled = false;
            return;
        }
        if (weaponSlot == -1) {
            ChatUtils.send("You dont have any weapon includes: Hyperion, Astraea, Valkyrie, Scylla, Spirit Sceptre");
            Enabled = false;
            return;
        }
        if (AutoFish.fishingMobs.isEmpty()) {
            ChatUtils.send("An error occured while getting Fishing Mobs, reloading...");
            AutoFish.fishingMobs = Requests.getListFromUrl("https://gist.githubusercontent.com/Apfelmus1337/da641d3805bddf800eef170cbb0068ec/raw", "mobs");
            Enabled = false;
            return;
        }

        AutoFish.startPos = AutoFish.mc.thePlayer.getPositionVector();
        AutoFish.startRot = new Rotation(AutoFish.mc.thePlayer.rotationYaw, AutoFish.mc.thePlayer.rotationPitch);
        KeyBinding.setKeyBindState(AutoFish.mc.gameSettings.keyBindSneak.getKeyCode(), Config.sneak);
        MouseControl.ungrabMouse();
    }


    public void onDisable() {
        if (Config.sneak) {
            KeyBinding.setKeyBindState(AutoFish.mc.gameSettings.keyBindSneak.getKeyCode(), false);
        }
        KeybindUtils.stopMovement();
        RotationUtils.reset();
        MouseControl.regrabMouse();
    }

    @SubscribeEvent
    public void onInteract(final PlayerInteractEvent event) {
        AutoFish.throwTimer.reset();
        AutoFish.inWaterTimer.reset();
    }

    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        if (!Enabled) {
            return;
        }
        if (Status == 0){
            Status++;
            onEnable();
        }else if (Status == 2){
            Status++;
            onDisable();
        }else if (Status == 4){
            Status = 0;
        }
        if (Config.antiAfk) {
            KeybindUtils.stopMovement();
            if (++AutoFish.ticks > 40) {
                AutoFish.ticks = 0;
                final List<KeyBinding> neededPresses = Vec.getNeededKeyPresses(AutoFish.mc.thePlayer.getPositionVector(), AutoFish.startPos);
                neededPresses.forEach(v -> KeyBinding.setKeyBindState(v.getKeyCode(), true));
                if (RotationUtils.done) {
                    switch (this.aaState) {
                        case AWAY: {
                            final Rotation afk = new Rotation(AutoFish.startRot.getYaw(), AutoFish.startRot.getPitch());
                            afk.addYaw((float)(Math.random() * 4.0 - 2.0));
                            afk.addPitch((float)(Math.random() * 4.0 - 2.0));
                            RotationUtils.setup(afk, (long) RandomUtils.randBetween(400, 600));
                            this.aaState = AAState.BACK;
                            break;
                        }
                        case BACK: {
                            RotationUtils.setup(AutoFish.startRot, (long)RandomUtils.randBetween(400, 600));
                            this.aaState = AAState.AWAY;
                            break;
                        }
                    }
                }
            }
            if (weaponSlot != -1) {
                if (AutoFish.curScStand == null || AutoFish.curScStand.isDead || AutoFish.curSc == null || AutoFish.curSc.isDead) {
                    AutoFish.curScStand = null;
                    AutoFish.curSc = null;
                }
                if (AutoFish.curScStand != null && SkyblockUtils.getMobHp(AutoFish.curScStand) <= 0) {
                    AutoFish.mc.thePlayer.inventory.currentItem = rodSlot;
                    AutoFish.curScStand = null;
                    AutoFish.curSc = null;
                }
                if (AutoFish.curSc == null && AutoFish.killing) {
                    RotationUtils.setup(AutoFish.startRot, (long) Config.aimSpeed);
                    AutoFish.killing = false;
                }
            }
        }
        AutoFish.particleList.removeIf(v -> System.currentTimeMillis() - v.timeAdded > 1000L);
        if (AutoFish.recoverTimer.hasReached(5000L)) {
            String recoverMsg = "";
            if (!recoverMsg.equals("")) {
                this.resetVariables();
                RotationUtils.reset();
                AutoFish.mc.thePlayer.sendChatMessage(recoverMsg);
            }
            AutoFish.recoverTimer.reset();
        }
        if ((this.afs == AutoFishState.THROWING || this.afs == AutoFishState.IN_WATER || this.afs == AutoFishState.FISH_BITE)) {
//            final int rongo = this.maxPlayerRange.getCurrent();
//            if (rongo != 0) {
//                String warpName = "";
//                boolean warpOut = false;
//                for (final Entity e : AutoFish.mc.theWorld.getEntitiesInAABBexcluding((Entity)AutoFish.mc.thePlayer, AutoFish.mc.thePlayer.getEntityBoundingBox().expand((double)rongo, (double)(rongo >> 1), (double)rongo), a -> a instanceof EntityOtherPlayerMP || a instanceof EntityArmorStand)) {
//                    if (e instanceof EntityArmorStand) {
//                        final ItemStack bushSlot = ((EntityArmorStand)e).getEquipmentInSlot(4);
//                        if (bushSlot != null && Item.getItemFromBlock((Block)Blocks.deadbush) == bushSlot.getItem()()) {
//                            warpOut = true;
//                            warpName = "Dead Bush";
//                            break;
//                        }
//                        continue;
//                    }
//                    else {
//                        if (!(e instanceof EntityOtherPlayerMP)) {
//                            continue;
//                        }
//                        if (AutoFish.fishingMobs.stream().anyMatch(v -> e.getName().contains(v))) {
//                            continue;
//                        }
//                        final String formatted = e.getDistanceToEntity().getFormattedText();
//                        if (StringUtils.stripControlCodes(formatted).equals(formatted)) {
//                            continue;
//                        }
//                        if (formatted.startsWith("§r") && !formatted.startsWith("§r§")) {
//                            continue;
//                        }
//                        warpOut = true;
//                        warpName = e.getName();
//                    }
//                }
//            }
            if (Config.sneak) {
                KeyBinding.setKeyBindState(AutoFish.mc.gameSettings.keyBindSneak.getKeyCode(), true);
            }
            if (Config.killPrio) {
                this.findAndSetCurrentSeaCreature();
                if (AutoFish.curSc != null) {
                    this.afs = AutoFishState.THROWING;
                    AutoFish.throwTimer.reset();
                }
            }
            else if (AutoFish.curScStand != null && SkyblockUtils.getMobHp(AutoFish.curScStand) <= 0) {
                AutoFish.curScStand = null;
                AutoFish.curSc = null;
                AutoFish.mc.thePlayer.inventory.currentItem = rodSlot;
            }
        }
        switch (this.afs) {
            case THROWING: {
                if (AutoFish.mc.thePlayer.fishEntity == null && AutoFish.throwTimer.hasReached(Config.recastDelay)) {
                    AutoFish.mc.thePlayer.inventory.currentItem = rodSlot;
                    AutoFish.mc.playerController.sendUseItem(AutoFish.mc.thePlayer, AutoFish.mc.theWorld, AutoFish.mc.thePlayer.getHeldItem());
                    AutoFish.throwTimer.reset();
                    AutoFish.inWaterTimer.reset();
                    AutoFish.flash = false;
                    this.afs = AutoFishState.IN_WATER;
                    break;
                }
                if (AutoFish.throwTimer.hasReached(2500L) && AutoFish.mc.thePlayer.fishEntity != null) {
                    this.afs = AutoFishState.FISH_BITE;
                    break;
                }
                break;
            }
            case IN_WATER: {
                final ItemStack heldItem = AutoFish.mc.thePlayer.getHeldItem();
                if (heldItem != null && heldItem.getItem() == Items.fishing_rod) {
                    if (AutoFish.throwTimer.hasReached(500L) && AutoFish.mc.thePlayer.fishEntity != null) {
                        if (Config.debugMode) {
                            ChatUtils.send("throwTimer reached 500");
                        }
                        if (AutoFish.mc.thePlayer.fishEntity.isInWater() || AutoFish.mc.thePlayer.fishEntity.isInLava()) {
                            if (!Config.killPrio) {
                                this.findAndSetCurrentSeaCreature();
                            }
                            if (!AutoFish.oldBobberInWater) {
//                                if (this.petSwap.isEnabled()) {
//                                    KeybindUtils.stopMovement();
//                                    CF4M.INSTANCE.moduleManager.toggle("PetSwap");
//                                }
                                AutoFish.oldBobberInWater = true;
                                AutoFish.inWaterTimer.reset();
                            }
                            final EntityFishHook bobber = AutoFish.mc.thePlayer.fishEntity;
                            if (Config.debugMode) {
                                ChatUtils.send("inWaterTimer reached: " + AutoFish.inWaterTimer.hasReached(Config.slugMode ? 30000L : 2500L));
                                ChatUtils.send("second bool: " + ((Math.abs(bobber.motionX) < 0.01 && Math.abs(bobber.motionZ) < 0.01) || AutoFish.flash));
                            }
                            if (AutoFish.inWaterTimer.hasReached(Config.slugMode ? 30000L : 2500L) && ((Math.abs(bobber.motionX) < 0.01 && Math.abs(bobber.motionZ) < 0.01) || AutoFish.flash)) {
                                final double movement = bobber.posY - AutoFish.oldBobberPosY;
                                AutoFish.oldBobberPosY = bobber.posY;
                                if ((movement < -0.04 && this.bobberIsNearParticles(bobber)) || bobber.caughtEntity != null) {
                                    if (Config.debugMode) {
                                        ChatUtils.send("Fish bite detected");
                                    }
                                    this.afs = AutoFishState.FISH_BITE;
                                }
                            }
                            break;
                        }
                        if (AutoFish.inWaterTimer.hasReached(2500L)) {
                            this.afs = AutoFishState.FISH_BITE;
                            break;
                        }
                        break;
                    }
                    else {
                        if (AutoFish.throwTimer.hasReached(1000L) && AutoFish.mc.thePlayer.fishEntity == null) {
                            AutoFish.throwTimer.reset();
                            this.afs = AutoFishState.THROWING;
                            break;
                        }
                        break;
                    }
                }
                else {
                    if (Config.killPrio) {
                        RotationUtils.setup(AutoFish.startRot, (long)Config.recastDelay);
                        AutoFish.oldBobberInWater = false;
                        AutoFish.throwTimer.reset();
                        this.afs = AutoFishState.THROWING;
                        break;
                    }
                    AutoFish.mc.thePlayer.inventory.currentItem = rodSlot;
                    break;
                }
            }
            case FISH_BITE: {
                AutoFish.mc.thePlayer.inventory.currentItem = rodSlot;
                AutoFish.mc.playerController.sendUseItem((EntityPlayer)AutoFish.mc.thePlayer, (World)AutoFish.mc.theWorld, AutoFish.mc.thePlayer.getHeldItem());
                RotationUtils.setup(AutoFish.startRot, (long) Config.recastDelay);
                AutoFish.oldBobberInWater = false;
                AutoFish.throwTimer.reset();
                AutoFish.inWaterTimer.reset();
                this.afs = AutoFishState.THROWING;
                break;
            }
        }
    }

    private void findAndSetCurrentSeaCreature() {
        final int ranga = Config.scRange;
        final List<Entity> mobs = (List<Entity>)AutoFish.mc.theWorld.getEntitiesInAABBexcluding((Entity)AutoFish.mc.thePlayer, AutoFish.mc.thePlayer.getEntityBoundingBox().expand((double)ranga, (double)(ranga >> 1), (double)ranga), e -> e instanceof EntityArmorStand);
        final Optional<Entity> filtered = mobs.stream().filter(v -> v.getDistanceToEntity((Entity)AutoFish.mc.thePlayer) < ranga && !v.getName().contains(AutoFish.mc.thePlayer.getName()) && AutoFish.fishingMobs.stream().anyMatch(a -> v.getCustomNameTag().contains(a))).min(Comparator.comparing(v -> v.getDistanceToEntity((Entity)AutoFish.mc.thePlayer)));
        if (filtered.isPresent()) {
            AutoFish.curScStand = (EntityArmorStand)filtered.get();
            AutoFish.curSc = SkyblockUtils.getEntityCuttingOtherEntity((Entity)AutoFish.curScStand, null);
            AutoFish.clicksLeft = (int)Math.ceil(SkyblockUtils.getMobHp(AutoFish.curScStand) / (float) Config.hypDamage);
            if (AutoFish.curSc != null && SkyblockUtils.getMobHp(AutoFish.curScStand) > 0) {
                AutoFish.killing = true;
                RotationUtils.setup(new Rotation(AutoFish.mc.thePlayer.rotationYaw, 90.0f), (long) Config.aimSpeed);
            }
            else if (SkyblockUtils.getMobHp(AutoFish.curScStand) <= 0) {
                AutoFish.curScStand = null;
                AutoFish.curSc = null;
                AutoFish.mc.thePlayer.inventory.currentItem = rodSlot;
            }
        }
        else if (RotationUtils.done) {
            AutoFish.curScStand = null;
            AutoFish.curSc = null;
            AutoFish.mc.thePlayer.inventory.currentItem = rodSlot;
        }
    }

    @SubscribeEvent
    public void onPlaySound(SoundEvent event) {
        if (event.name.equals("mob.guardian.elder.idle")) {
            if (Config.debugMode) {
                ChatUtils.send("Flash proc detected");
            }
            AutoFish.flash = true;
            AutoFish.inWaterTimer.setLastMS(System.currentTimeMillis() - 5000L);
        }
    }

    @SubscribeEvent
    public void onRenderTick(final Render3DEvent event) {
        if (AutoFish.curSc != null) {
            Render3D.renderBoundingBox(AutoFish.curSc, event.partialTicks, -16776961);
        }
        if (AutoFish.mc.currentScreen != null && !(AutoFish.mc.currentScreen instanceof GuiChat)) {
            return;
        }
        if (!RotationUtils.done) {
            RotationUtils.update();
        }
        if (AutoFish.curSc != null) {
            if (AutoFish.killTimer.hasReached(125L)) {
                AutoFish.mc.thePlayer.inventory.currentItem = weaponSlot;
                if (AutoFish.mc.thePlayer.rotationPitch > 89.0f) {
                    if (AutoFish.clicksLeft-- > 0) {
                        KeybindUtils.rightClick();
                    }
                    AutoFish.killTimer.reset();
                }
            }
        }
    }

    @SubscribeEvent
    public void onChat(final ClientChatReceivedEvent event) {
        if (event.type == 2) {
            return;
        }
        final String unformatted = event.message.getUnformattedText();
        final String stripped = StringUtils.stripControlCodes(unformatted);
        if (stripped.startsWith(" \u2620 You ")) {
            Enabled = false;
        }
    }

    public static void handleParticles(final S2APacketParticles packet) {
        if (packet.getParticleType() == EnumParticleTypes.WATER_WAKE || packet.getParticleType() == EnumParticleTypes.SMOKE_NORMAL) {
            AutoFish.particleList.add(new ParticleEntry(new Vec3(packet.getXCoordinate(), packet.getYCoordinate(), packet.getZCoordinate()), System.currentTimeMillis()));
        }
    }

    private boolean bobberIsNearParticles(final EntityFishHook bobber) {
        return AutoFish.particleList.stream().anyMatch(v -> Vec.getHorizontalDistance(bobber.getPositionVector(), v.position) < 0.2);
    }

    private void resetVariables() {
        this.afs = AutoFishState.THROWING;
        this.aaState = AAState.AWAY;
        AutoFish.throwTimer.reset();
        AutoFish.inWaterTimer.reset();
        AutoFish.recoverTimer.reset();
        AutoFish.ticks = 0;
        AutoFish.oldBobberPosY = 0.0;
        AutoFish.oldBobberInWater = false;
        AutoFish.curScStand = null;
        AutoFish.curSc = null;
        AutoFish.killing = true;
        AutoFish.clicksLeft = 0;
        AutoFish.flash = false;
        AutoFish.particleList.clear();
    }

    static {
        AutoFish.mc = Minecraft.getMinecraft();
        AutoFish.fishingMobs = Requests.getListFromUrl("https://cdn.weikuwu.me/src/skyblock/sea_creatures.json", "mobs");
        AutoFish.throwTimer = new Time();
        AutoFish.inWaterTimer = new Time();
        AutoFish.killTimer = new Time();
        AutoFish.recoverTimer = new Time();
        AutoFish.curScStand = null;
        AutoFish.curSc = null;
        AutoFish.killing = false;
        AutoFish.clicksLeft = 0;
        AutoFish.flash = false;
        AutoFish.oldPos = null;
        AutoFish.oldBobberPosY = 0.0;
        AutoFish.oldBobberInWater = false;
        AutoFish.ticks = 0;
        AutoFish.startPos = null;
        AutoFish.startRot = null;
        AutoFish.particleList = new ArrayList<ParticleEntry>();
    }

    private enum AutoFishState {
        THROWING,
        IN_WATER,
        FISH_BITE;
    }

    private enum AAState
    {
        AWAY,
        BACK;
    }

    private static class ParticleEntry
    {
        public Vec3 position;
        public long timeAdded;

        public ParticleEntry(final Vec3 position, final long timeAdded) {
            this.position = position;
            this.timeAdded = timeAdded;
        }
    }
}
