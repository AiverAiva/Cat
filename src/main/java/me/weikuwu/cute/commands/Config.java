package me.weikuwu.cute.commands;

import me.weikuwu.cute.CATMOD;
import me.weikuwu.cute.guis.ConfigGUI;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Config extends CommandBase {
    @Override
    public String getCommandName() {
        return "cat";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/" + getCommandName();
    }

    @Override
    public List<String> getCommandAliases() {
        return new ArrayList<>(Arrays.asList("cat", "cm"));
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        CATMOD.gui = new ConfigGUI();
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
}
