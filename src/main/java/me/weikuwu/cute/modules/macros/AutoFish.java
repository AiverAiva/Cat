package me.weikuwu.cute.modules.macros;

import me.weikuwu.cute.config.Config;
import me.weikuwu.cute.events.PlayerInteractEvent;
import me.weikuwu.cute.utils.MouseControl;
import me.weikuwu.cute.utils.client.KeybindUtils;
import me.weikuwu.cute.utils.client.Rotation;
import me.weikuwu.cute.utils.client.RotationUtils;
import me.weikuwu.cute.utils.gui.ChatUtils;
import me.weikuwu.cute.utils.gui.InventoryUtils;
import me.weikuwu.cute.utils.math.Time;
import me.weikuwu.cute.utils.skyblock.Location;
import me.weikuwu.cute.utils.skyblock.SkyblockUtils;
import net.minecraft.client.*;
import net.minecraft.entity.item.*;
import net.minecraft.entity.*;
import net.minecraft.client.settings.*;
import net.minecraft.client.gui.*;
import net.minecraft.block.*;
import net.minecraft.client.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.world.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.entity.projectile.*;
import java.util.*;
import net.minecraft.network.play.server.*;
import net.minecraft.util.*;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.sound.SoundEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.stream.*;

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

    @Enable
    public void onEnable() {
        this.resetVariables();
        RotationUtils.reset();
//        if (this.assistMode.isEnabled()) {
//            this.afs = AutoFishState.IN_WATER;
//            return;
//        }
//        if (this.killMode.getCurrent().equals("Hyperion") && this.hypDamage.getCurrent() <= 0) {
//            ChatUtils.send("Configure your HypDamage pls ty");
//            CF4M.INSTANCE.moduleManager.toggle(this);
//            return;
//        }

        String[] rodNames = { "Rod" };
        rodSlot = InventoryUtils.findHotbarItemSlot(player, rodNames);
        String[] weaponNames = { "Hyperion", "Astraea", "Valkyrie", "Scylla", "Spirit Sceptre" };
        weaponSlot = InventoryUtils.findHotbarItemSlot(player, weaponNames);

        if (rodSlot == -1) {
            ChatUtils.send("You dont have any rod");
            Config.autofishing = false;
            return;
        }
        if (weaponSlot == -1) {
            ChatUtils.send("You dont have any weapon includes: Hyperion, Astraea, Valkyrie, Scylla, Spirit Sceptre");
            Config.autofishing = false;
            return;
        }
//        if (this.whipSlot.getCurrent() != 0 && AutoFish.fishingMobs.isEmpty()) {
//            ChatUtils.send("An error occured while getting Fishing Mobs, reloading...");
//            AutoFish.fishingMobs = JsonUtils.getListFromUrl("https://gist.githubusercontent.com/Apfelmus1337/da641d3805bddf800eef170cbb0068ec/raw", "mobs");
//            CF4M.INSTANCE.moduleManager.toggle(this);
//            return;
//        }

        AutoFish.startPos = AutoFish.mc.thePlayer.getPositionVector();
        AutoFish.startRot = new Rotation(AutoFish.mc.thePlayer.rotationYaw, AutoFish.mc.thePlayer.rotationPitch);
        KeyBinding.setKeyBindState(AutoFish.mc.gameSettings.keyBindSneak.getKeyCode(), this.sneak.isEnabled());
        MouseControl.ungrabMouse();
    }

    @Disable
    public void onDisable() {
        if (this.assistMode.isEnabled()) {
            return;
        }
        if (this.sneak.isEnabled()) {
            KeyBinding.setKeyBindState(AutoFish.mc.gameSettings.keyBindSneak.getKeyCode(), false);
        }
        KeybindUtils.stopMovement();
        RotationUtils.reset();
        if (this.ungrab.isEnabled()) {
            MouseControl.regrabMouse();
        }
    }

    @SubscribeEvent
    public void onInteract(final PlayerInteractEvent event) {
        AutoFish.throwTimer.reset();
        AutoFish.inWaterTimer.reset();
    }

    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        if (AutoFish.mc.currentScreen != null && !(AutoFish.mc.currentScreen instanceof GuiChat)) {
            return;
        }
        if (!this.assistMode.isEnabled()) {
            if (this.antiAfk.isEnabled() && this.afs != AutoFishState.WARP_ISLAND && this.afs != AutoFishState.WARP_SPOT && this.afs != AutoFishState.NAVIGATING) {
                KeybindUtils.stopMovement();
                if (++AutoFish.ticks > 40) {
                    AutoFish.ticks = 0;
                    final List<KeyBinding> neededPresses = VecUtils.getNeededKeyPresses(AutoFish.mc.thePlayer.getPositionVector(), AutoFish.startPos);
                    neededPresses.forEach(v -> KeyBinding.setKeyBindState(v.getKeyCode(), true));
                    if (RotationUtils.done) {
                        switch (this.aaState) {
                            case AWAY: {
                                final Rotation afk = new Rotation(AutoFish.startRot.getYaw(), AutoFish.startRot.getPitch());
                                afk.addYaw((float)(Math.random() * 4.0 - 2.0));
                                afk.addPitch((float)(Math.random() * 4.0 - 2.0));
                                RotationUtils.setup(afk, (long)RandomUtil.randBetween(400, 600));
                                this.aaState = AAState.BACK;
                                break;
                            }
                            case BACK: {
                                RotationUtils.setup(AutoFish.startRot, (long)RandomUtil.randBetween(400, 600));
                                this.aaState = AAState.AWAY;
                                break;
                            }
                        }
                    }
                }
            }
            if (this.whipSlot.getCurrent() != 0) {
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
        if (!this.assistMode.isEnabled()) {
            final Location.Locations sbLoc = Location.getLocation();
            if (AutoFish.recoverTimer.hasReached(5000L)) {
                String recoverMsg = "";
                switch (sbLoc) {
                    case LOBBY: {
                        ChatUtils.send("Detected player in Lobby, re-warping");
                        recoverMsg = "/play skyblock";
                        break;
                    }
                    case LIMBO: {
                        ChatUtils.send("Detected player in Limbo, re-warping");
                        recoverMsg = "/l";
                        break;
                    }
                }
                if (!recoverMsg.equals("")) {
                    this.resetVariables();
                    RotationUtils.reset();
                    AutoFish.mc.thePlayer.func_71165_d(recoverMsg);
                    this.afs = AutoFishState.WARP_ISLAND;
                    this.warpState = WarpState.SETUP;
                }
                AutoFish.recoverTimer.reset();
            }
        }
        if ((this.afs == AutoFishState.THROWING || this.afs == AutoFishState.IN_WATER || this.afs == AutoFishState.FISH_BITE) && !this.assistMode.isEnabled()) {
//            final int rongo = this.maxPlayerRange.getCurrent();
//            if (rongo != 0) {
//                String warpName = "";
//                boolean warpOut = false;
//                for (final Entity e : AutoFish.mc.theWorld.getEntitiesInAABBexcluding((Entity)AutoFish.mc.thePlayer, AutoFish.mc.thePlayer.getEntityBoundingBox().func_72314_b((double)rongo, (double)(rongo >> 1), (double)rongo), a -> a instanceof EntityOtherPlayerMP || a instanceof EntityArmorStand)) {
//                    if (e instanceof EntityArmorStand) {
//                        final ItemStack bushSlot = ((EntityArmorStand)e).func_71124_b(4);
//                        if (bushSlot != null && Item.func_150898_a((Block)Blocks.field_150330_I) == bushSlot.getItem()()) {
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
//                        final String formatted = e.getDistanceToEntity().func_150254_d();
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
            case WARP_ISLAND: {
                if (AutoFish.warpTimer.hasReached(5000L)) {
                    final Optional<Location> loc = AutoFish.fishingJson.locations.stream().filter(v -> v.name.equals(this.fishingSpot.getCurrent())).findFirst();
                    loc.ifPresent(location -> AutoFish.currentLocation = location);
                    if (AutoFish.currentLocation != null) {
                        ChatUtils.send("Navigating to: " + AutoFish.currentLocation.name.split(" - ")[1]);
                        AutoFish.mc.thePlayer.func_71165_d("/warp home");
                        AutoFish.warpTimer.reset();
                        this.afs = AutoFishState.WARP_SPOT;
                    }
                    else {
                        ChatUtils.send("Couldn't determine location, very weird");
                        CF4M.INSTANCE.moduleManager.toggle(this);
                    }
                    break;
                }
                break;
            }
            case WARP_SPOT: {
                if (AutoFish.warpTimer.hasReached(5000L)) {
                    final String warpLoc = AutoFish.currentLocation.name.split(" - ")[0];
                    AutoFish.mc.thePlayer.func_71165_d(warpLoc);
                    AutoFish.warpTimer.reset();
                    AutoFish.path = null;
                    this.afs = AutoFishState.NAVIGATING;
                    break;
                }
                break;
            }
            case THROWING: {
                if (AutoFish.mc.thePlayer.fishEntity == null && AutoFish.throwTimer.hasReached(this.recastDelay.getCurrent())) {
                    if (this.rodSlot.getCurrent() > 0 && this.rodSlot.getCurrent() <= 8) {
                        AutoFish.mc.thePlayer.inventory.currentItem = this.rodSlot.getCurrent() - 1;
                    }
                    AutoFish.mc.playerController.func_78769_a((EntityPlayer)AutoFish.mc.thePlayer, (World)AutoFish.mc.theWorld, AutoFish.mc.thePlayer.getHeldItem());
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
                if (heldItem != null && heldItem.getItem() == Items.field_151112_aM) {
                    if (AutoFish.throwTimer.hasReached(500L) && AutoFish.mc.thePlayer.fishEntity != null) {
                        if (this.debug.isEnabled()) {
                            ChatUtils.send("throwTimer reached 500");
                        }
                        if (AutoFish.mc.thePlayer.fishEntity.func_70090_H() || AutoFish.mc.thePlayer.fishEntity.func_180799_ab()) {
                            if (!this.assistMode.isEnabled() && !this.killPrio.isEnabled()) {
                                this.findAndSetCurrentSeaCreature();
                            }
                            if (!AutoFish.oldBobberInWater) {
                                if (this.petSwap.isEnabled()) {
                                    KeybindUtils.stopMovement();
                                    CF4M.INSTANCE.moduleManager.toggle("PetSwap");
                                }
                                AutoFish.oldBobberInWater = true;
                                AutoFish.inWaterTimer.reset();
                            }
                            final EntityFishHook bobber = AutoFish.mc.thePlayer.fishEntity;
                            if (this.debug.isEnabled()) {
                                ChatUtils.send("inWaterTimer reached: " + AutoFish.inWaterTimer.hasReached(this.slugMode.isEnabled() ? 30000L : 2500L));
                                ChatUtils.send("second bool: " + ((Math.abs(bobber.motionX) < 0.01 && Math.abs(bobber.motionZ) < 0.01) || AutoFish.flash));
                            }
                            if (AutoFish.inWaterTimer.hasReached(this.slugMode.isEnabled() ? 30000L : 2500L) && ((Math.abs(bobber.motionX) < 0.01 && Math.abs(bobber.motionZ) < 0.01) || AutoFish.flash)) {
                                final double movement = bobber.posY - AutoFish.oldBobberPosY;
                                AutoFish.oldBobberPosY = bobber.posY;
                                if ((movement < -0.04 && this.bobberIsNearParticles(bobber)) || bobber.field_146043_c != null) {
                                    if (this.debug.isEnabled()) {
                                        ChatUtils.send("Fish bite detected");
                                    }
                                    this.afs = AutoFishState.FISH_BITE;
                                }
                            }
                            break;
                        }
                        if (AutoFish.inWaterTimer.hasReached(2500L) && !this.assistMode.isEnabled()) {
                            this.afs = AutoFishState.FISH_BITE;
                            break;
                        }
                        break;
                    }
                    else {
                        if (AutoFish.throwTimer.hasReached(1000L) && AutoFish.mc.thePlayer.fishEntity == null && !this.assistMode.isEnabled()) {
                            AutoFish.throwTimer.reset();
                            this.afs = AutoFishState.THROWING;
                            break;
                        }
                        break;
                    }
                }
                else {
                    if (this.killPrio.isEnabled() && !this.assistMode.isEnabled()) {
                        RotationUtils.setup(AutoFish.startRot, (long)this.recastDelay.getCurrent());
                        AutoFish.oldBobberInWater = false;
                        AutoFish.throwTimer.reset();
                        this.afs = AutoFishState.THROWING;
                        break;
                    }
                    if (this.rodSlot.getCurrent() > 0 && this.rodSlot.getCurrent() <= 8 && !this.assistMode.isEnabled()) {
                        AutoFish.mc.thePlayer.inventory.currentItem = this.rodSlot.getCurrent() - 1;
                        break;
                    }
                    break;
                }
                break;
            }
            case FISH_BITE: {
                if (!this.assistMode.isEnabled() && this.rodSlot.getCurrent() > 0 && this.rodSlot.getCurrent() <= 8) {
                    AutoFish.mc.thePlayer.inventory.currentItem = this.rodSlot.getCurrent() - 1;
                }
                AutoFish.mc.playerController.func_78769_a((EntityPlayer)AutoFish.mc.thePlayer, (World)AutoFish.mc.theWorld, AutoFish.mc.thePlayer.getHeldItem());
                if (!this.assistMode.isEnabled()) {
                    RotationUtils.setup(AutoFish.startRot, (long)this.recastDelay.getCurrent());
                }
                AutoFish.oldBobberInWater = false;
                AutoFish.throwTimer.reset();
                AutoFish.inWaterTimer.reset();
                this.afs = (this.assistMode.isEnabled() ? AutoFishState.IN_WATER : AutoFishState.THROWING);
                break;
            }
        }
    }

    private void findAndSetCurrentSeaCreature() {
        final int ranga = Config.scRange;
        final List<Entity> mobs = (List<Entity>)AutoFish.mc.theWorld.getEntitiesInAABBexcluding((Entity)AutoFish.mc.thePlayer, AutoFish.mc.thePlayer.getEntityBoundingBox().func_72314_b((double)ranga, (double)(ranga >> 1), (double)ranga), e -> e instanceof EntityArmorStand);
        final Optional<Entity> filtered = mobs.stream().filter(v -> v.getDistanceToEntity((Entity)AutoFish.mc.thePlayer) < ranga && !v.getName().contains(AutoFish.mc.thePlayer.getName()) && AutoFish.fishingMobs.stream().anyMatch(a -> v.func_95999_t().contains(a))).min(Comparator.comparing(v -> v.getDistanceToEntity((Entity)AutoFish.mc.thePlayer)));
        if (filtered.isPresent()) {
            AutoFish.curScStand = (EntityArmorStand)filtered.get();
            AutoFish.curSc = SkyblockUtils.getEntityCuttingOtherEntity((Entity)AutoFish.curScStand, null);
            AutoFish.clicksLeft = (int)Math.ceil(SkyblockUtils.getMobHp(AutoFish.curScStand) / (float)this.hypDamage.getCurrent());
            if (AutoFish.curSc != null && SkyblockUtils.getMobHp(AutoFish.curScStand) > 0) {
                AutoFish.killing = true;
                RotationUtils.setup(new Rotation(AutoFish.mc.thePlayer.field_70177_z, 90.0f), (long)this.aimSpeed.getCurrent());
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
            if (this.rodSlot.getCurrent() > 0 && this.rodSlot.getCurrent() <= 8) {
                AutoFish.mc.thePlayer.inventory.currentItem = rodSlot;
            }
        }
    }

    @SubscribeEvent
    public void onPlaySound(SoundEvent event) {
        if (event.name.equals("mob.guardian.elder.idle")) {
            if (this.debug.isEnabled()) {
                ChatUtils.send("Flash proc detected");
            }
            AutoFish.flash = true;
            AutoFish.inWaterTimer.setLastMS(System.currentTimeMillis() - 5000L);
        }
    }

    @SubscribeEvent
    public void onRenderTick(final Render3DEvent event) {
        if (AutoFish.curSc != null) {
            Render3DUtils.renderBoundingBox(AutoFish.curSc, event.partialTicks, -16776961);
        }
        if (AutoFish.mc.currentScreen != null && !(AutoFish.mc.currentScreen instanceof GuiChat)) {
            return;
        }
        if (!RotationUtils.done) {
            RotationUtils.update();
        }
        if (this.afs == AutoFishState.NAVIGATING && AutoFish.path != null) {
            switch (this.warpState) {
                case SETUP: {
                    if (AutoFish.path.size() > 0) {
                        if (AutoFish.warpTimer.hasReached(this.warpTime.getCurrent()) && !AutoFish.mc.thePlayer.func_180425_c().equals((Object)AutoFish.oldPos)) {
                            final PathPoint a = AutoFish.path.get(0);
                            AutoFish.path.remove(0);
                            RotationUtils.setup(RotationUtils.getRotation(new Vec3(a.x, a.y, a.z)), (long)this.warpLookTime.getCurrent());
                            AutoFish.oldPos = AutoFish.mc.thePlayer.func_180425_c();
                            this.warpState = WarpState.LOOK;
                            break;
                        }
                        if (AutoFish.warpTimer.hasReached(2500L)) {
                            ChatUtils.send("Got stuck while tp'ing, re-navigating");
                            AutoFish.mc.thePlayer.func_71165_d("/l");
                            AutoFish.recoverTimer.reset();
                            AutoFish.warpTimer.reset();
                            break;
                        }
                        break;
                    }
                    else {
                        if (AutoFish.warpTimer.hasReached(1000L)) {
                            if (!this.sneak.isEnabled()) {
                                KeyBinding.setKeyBindState(AutoFish.mc.gameSettings.keyBindSneak.getKeyCode(), false);
                            }
                            AutoFish.startRot = AutoFish.currentLocation.rotation;
                            AutoFish.startPos = AutoFish.mc.thePlayer.func_174791_d();
                            RotationUtils.setup(AutoFish.startRot, (long)this.recastDelay.getCurrent());
                            AutoFish.throwTimer.reset();
                            this.afs = AutoFishState.THROWING;
                            break;
                        }
                        break;
                    }
                    break;
                }
                case LOOK: {
                    if (System.currentTimeMillis() <= RotationUtils.endTime) {
                        RotationUtils.update();
                        break;
                    }
                    RotationUtils.update();
                    AutoFish.warpTimer.reset();
                    this.warpState = WarpState.WARP;
                    break;
                }
                case WARP: {
                    if (AutoFish.warpTimer.hasReached(this.warpTime.getCurrent())) {
                        AutoFish.mc.thePlayer.inventory.currentItem = this.aotvSlot.getCurrent() - 1;
                        AutoFish.mc.playerController.func_78769_a((EntityPlayer)AutoFish.mc.thePlayer, (World)AutoFish.mc.theWorld, AutoFish.mc.thePlayer.getHeldItem());
                        AutoFish.warpTimer.reset();
                        this.warpState = WarpState.SETUP;
                        break;
                    }
                    break;
                }
            }
            return;
        }
        if (AutoFish.curSc != null) {
            if (this.killMode.getCurrent().equals("Hyperion")) {
                if (AutoFish.killTimer.hasReached(125L)) {
                    if (this.whipSlot.getCurrent() > 0 && this.whipSlot.getCurrent() <= 8) {
                        AutoFish.mc.thePlayer.inventory.currentItem = this.whipSlot.getCurrent() - 1;
                    }
                    if (AutoFish.mc.thePlayer.rotationPitch > 89.0f) {
                        if (AutoFish.clicksLeft-- > 0) {
                            KeybindUtils.rightClick();
                        }
                        AutoFish.killTimer.reset();
                    }
                }
            }
            else if (AutoFish.killTimer.hasReached(125L)) {
                AutoFish.mc.thePlayer.inventory.currentItem = weaponSlot;
                final String current = this.killMode.getCurrent();
                switch (current) {
                    case "Left": {
                        KeybindUtils.leftClick();
                        break;
                    }
                    case "Right": {
                        KeybindUtils.rightClick();
                        break;
                    }
                }
                AutoFish.killTimer.reset();
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
            new Thread(() -> {
                CF4M.INSTANCE.moduleManager.toggle(this);
                ChatUtils.send("Bozo you died while AutoFishing!");
                if (this.warpOutOnDeath.isEnabled()) {
                    ChatUtils.send("I'll send you back to your island in 10 seconds... noob");
                    try {
                        Thread.sleep(10000L);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    AutoFish.mc.thePlayer.func_71165_d("/warp home");
                }
            }).start();
        }
    }

    public static void handleParticles(final S2APacketParticles packet) {
        if (packet.getParticleType() == EnumParticleTypes.WATER_WAKE || packet.getParticleType() == EnumParticleTypes.SMOKE_NORMAL) {
            AutoFish.particleList.add(new ParticleEntry(new Vec3(packet.getXCoordinate(), packet.getYCoordinate(), packet.getZCoordinate()), System.currentTimeMillis()));
        }
    }

    private boolean bobberIsNearParticles(final EntityFishHook bobber) {
        return AutoFish.particleList.stream().anyMatch(v -> VecUtils.getHorizontalDistance(bobber.func_174791_d(), v.position) < 0.2);
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
        AutoFish.fishingMobs = JsonUtils.getListFromUrl("https://gist.githubusercontent.com/Apfelmus1337/da641d3805bddf800eef170cbb0068ec/raw/sea_creatures.json", "mobs");
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
