package org.bosiux.cookingupdate.executors;

import org.bosiux.cookingupdate.listeners.CookingListener;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CookingCommand implements CommandExecutor, TabCompleter {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be executed by a player.");
            return false;
        }

        Player player = (Player) sender;

        if (command.getName().equalsIgnoreCase("cooking")) {
            if (args.length > 0) {

                if (args[0].equalsIgnoreCase("open") && args.length > 1 && args[1].equalsIgnoreCase("pot")) {
                    CookingListener.openGUI(player);
                    return true;

                } else if (args[0].equalsIgnoreCase("recipe") && args.length > 1 && args[1].equalsIgnoreCase("add")) {
                    if (!sender.hasPermission("cookingupdate.command.recipe.add")){
                        return true;
                    }

                    CookingListener.openRecipeGUI(player);
                    return true;

                } else if (args[0].equalsIgnoreCase("recipe") && args.length > 1 && args[1].equalsIgnoreCase("list")) {
                if (!sender.hasPermission("cookingupdate.command.recipe.list")){
                    return true;
                }

                //TODO LIST COMMAND
                return true;

            }
            }
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            String[] subCommands = {"recipe", "open"};
            for (String subCommand : subCommands) {
                if (subCommand.startsWith(args[0])) {
                    completions.add(subCommand);
                }
            }
        } else if (args.length == 2 && args[0].equalsIgnoreCase("recipe")) {
            String[] recipeSubCommands = {"add", "list"};
            for (String subCommand : recipeSubCommands) {
                if (subCommand.startsWith(args[1])) {
                    completions.add(subCommand);
                }
            }
        }  else if (args.length == 2 && args[0].equalsIgnoreCase("open")) {
            String[] recipeSubCommands = {"pot"};
            for (String subCommand : recipeSubCommands) {
                if (subCommand.startsWith(args[1])) {
                    completions.add(subCommand);
                }
            }
        }

        return completions;
    }
}

