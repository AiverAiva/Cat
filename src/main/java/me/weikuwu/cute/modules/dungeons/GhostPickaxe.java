package me.weikuwu.cute.modules.dungeons;

import me.weikuwu.cute.config.Config;
import me.weikuwu.cute.utils.skyblock.Location;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.Arrays;

public class GhostPickaxe {
    private static final Minecraft mc = Minecraft.getMinecraft();
    private final ArrayList<Block> interactables;

    public GhostPickaxe() {
        this.interactables = new ArrayList(Arrays.asList(Blocks.acacia_door, Blocks.anvil, Blocks.beacon, Blocks.bed, Blocks.birch_door, Blocks.brewing_stand, Blocks.command_block, Blocks.crafting_table, Blocks.chest, Blocks.dark_oak_door, Blocks.daylight_detector, Blocks.daylight_detector_inverted, Blocks.dispenser, Blocks.dropper, Blocks.enchanting_table, Blocks.ender_chest, Blocks.furnace, Blocks.hopper, Blocks.jungle_door, Blocks.lever, Blocks.noteblock, Blocks.powered_comparator, Blocks.unpowered_comparator, Blocks.powered_repeater, Blocks.unpowered_repeater, Blocks.standing_sign, Blocks.wall_sign, Blocks.trapdoor, Blocks.trapped_chest, Blocks.wooden_button, Blocks.stone_button, Blocks.oak_door, Blocks.skull));
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (!Config.ghostPickaxe) return;

        if (mc.thePlayer == null ||
            mc.theWorld == null ||
            mc.objectMouseOver == null ||
            mc.objectMouseOver.getBlockPos() == null ||
            mc.thePlayer.inventory.getCurrentItem() == null
            ) return;

        if (!mc.gameSettings.keyBindAttack.isKeyDown()) return;
        if (Config.ghostPickaxeOnShift && !mc.thePlayer.isSneaking()) return;
        if (Config.ghostPickaxeOnlyInDungeon && !Location.isInDungeon()) return;

        if (mc.thePlayer.inventory.getCurrentItem().getDisplayName().contains("Stonk") || mc.thePlayer.inventory.getCurrentItem().getDisplayName().contains("Pickaxe")) {
            final Block block = Minecraft.getMinecraft().theWorld.getBlockState(mc.objectMouseOver.getBlockPos()).getBlock();
            if (!this.interactables.contains(block)) {
                mc.theWorld.setBlockToAir(mc.objectMouseOver.getBlockPos());
            }

        }
    }
}






