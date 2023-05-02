package com.github.trickzstar.waypointui;

import com.github.trickzstar.waypointui.persistentdata.CustomLocationData;
import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileManager {

    private static final String playerFilePath = "plugins/waypointsui/players/";

    //TODO: Support Multiple Waypoints in UI
    public static void LoadFile(Player player, ItemStack item) {

        File file = new File(playerFilePath + player.getName() + ".yml");
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);

        ItemMeta meta = item.getItemMeta();
        if (meta != null) {

            for (String slotID : Objects.requireNonNull(cfg.getConfigurationSection("Slot")).getKeys(false)) {
                PersistentDataContainer data = meta.getPersistentDataContainer();
                NamespacedKey key = new NamespacedKey(WaypointUI.GetInstance(), "crate");

                World world = Bukkit.getWorld(Objects.requireNonNull(cfg.getString("Slot." + 0 + ".World")));
                double posX = cfg.getDouble("Slot." + slotID + ".X");
                double posY = cfg.getDouble("Slot." + slotID + ".Y");
                double posZ = cfg.getDouble("Slot." + slotID + ".Z");

                Location loc = new Location(world, posX, posY, posZ);
                data.set(key, new CustomLocationData(), loc);

                List<String> lore = new ArrayList<>();
                lore.add(ChatColor.BLUE + "World: " + ChatColor.RESET + Objects.requireNonNull(loc.getWorld()).getName());
                lore.add(ChatColor.BLUE + "X: " + ChatColor.RESET + loc.getX());
                lore.add(ChatColor.BLUE + "Y: " + ChatColor.RESET + loc.getY());
                lore.add(ChatColor.BLUE + "Z: " + ChatColor.RESET + loc.getZ());
                meta.setLore(lore);
                item.setItemMeta(meta);
                Bukkit.getConsoleSender().sendMessage("[WayUI]: Loading Inventory for Player.");
            }
        }
    }

    public static void SaveFile(Player player, Inventory inv) {
        File file = new File(playerFilePath + player.getName() + ".yml");
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);

        if (inv != null) {
            for (int i = 0; i < inv.getContents().length; i++) {
                if (inv.getContents()[i] == null) {
                    continue;
                }

                Location data = Objects.requireNonNull(inv.getContents()[i].getItemMeta()).getPersistentDataContainer()
                        .get(new NamespacedKey(WaypointUI.GetInstance(), "crate"), new CustomLocationData());

                if (data != null) {
                    cfg.set("Slot." + i + ".World", Objects.requireNonNull(data.getWorld()).getName());
                    cfg.set("Slot." + i + ".Material", Objects.requireNonNull(inv.getContents()[i].getData()).getItemType().toString());
                    cfg.set("Slot." + i + ".X", data.getX());
                    cfg.set("Slot." + i + ".Y", data.getY());
                    cfg.set("Slot." + i + ".Z", data.getZ());
                } else {
                    Bukkit.getConsoleSender().sendMessage("[ERROR]: Couldn't find LocationData!");
                }
            }
        }

        try {
            cfg.save(file);
        } catch (IOException e) {
            Bukkit.getConsoleSender().sendMessage("[ERROR]: " + e.getMessage());
        }
    }

    public static boolean CreateFile(Player player) {
        File playerFile = new File(playerFilePath + player.getName() + ".yml");
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(playerFile);

        if (!playerFile.exists()) {
            try {
                cfg.save(playerFile);
                return false;
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return true;
    }

}
