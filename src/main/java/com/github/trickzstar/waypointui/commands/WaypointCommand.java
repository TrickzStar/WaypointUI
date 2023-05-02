package com.github.trickzstar.waypointui.commands;

import com.github.trickzstar.waypointui.FileManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class WaypointCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender instanceof Player) {
            Player p = (Player) commandSender;

            if (args[0].equalsIgnoreCase("pos")) {
                Location playerLoc = p.getLocation();
                p.sendMessage("Your coordinates are: [World: " + p.getWorld().getName() +
                        ", X: " + playerLoc.getX() +
                        ", Y: " + playerLoc.getY() +
                        ", Z: " + playerLoc.getZ() + "]");

                return true;
            } else if (args[0].equalsIgnoreCase("open")) {

                Inventory pInv = Bukkit.createInventory(null, 9, "Waypoint Inventory");

                if (!FileManager.CreateFile(p)) {
                    for (int i = 0; i < pInv.getSize(); i++) {
                        pInv.setItem(i, new ItemStack(Material.COBBLESTONE));
                    }
                } else {
                    for(int i = 0; i < pInv.getSize(); i++) {
                        pInv.setItem(i, new ItemStack(Material.COBBLESTONE));
                    }

                    FileManager.LoadFile(p, pInv);
                }

                p.openInventory(pInv);
                return true;
            }
            return true;
        }
        return true;
    }
}
