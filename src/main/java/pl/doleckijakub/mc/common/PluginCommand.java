package pl.doleckijakub.mc.common;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import pl.doleckijakub.mc.util.ANSI;

import java.util.Arrays;
import java.util.Objects;

public abstract class PluginCommand implements CommandExecutor {

    private final CommandInfo commandInfo;

    protected PluginCommand() {
        this.commandInfo = getClass().getDeclaredAnnotation(CommandInfo.class);
        Objects.requireNonNull(commandInfo);
    }

    public final CommandInfo getCommandInfo() {
        return commandInfo;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) return false;

        if (!commandInfo.permissions().isEmpty()) if (!sender.hasPermission(commandInfo.permissions())) {
            sender.sendMessage(ChatColor.RED + "You don't have permissions to execute this command");
            return true;
        }

        try {
            execute((Player) sender, args);
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "Could not execute the command, because of an error: " + e.getMessage() + ". If the error persists, please let the server administration know.");

            Bukkit.getLogger().warning(ANSI.RED + "Error in executing command /" + s + " " + String.join(" ", args) + ": " + e.getMessage() + ": " + ANSI.RESET);
            for (StackTraceElement ste : e.getStackTrace()) {
                Bukkit.getLogger().warning("    " + ANSI.RED + ste.toString() + ANSI.RESET);
            }
        }

        return true;
    }

    public abstract void execute(Player player, String[] args);

}
