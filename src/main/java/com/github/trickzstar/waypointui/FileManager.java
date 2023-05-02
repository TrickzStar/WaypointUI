package com.github.trickzstar.waypointui;

import com.github.trickzstar.waypointui.persistentdata.CustomLocationData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class FileManager {

    private static final String playerFilePath = "plugins/waypointsui/players/";

    public static void LoadFile(Player player, Inventory inv) {

        File file = new File(playerFilePath + player.getName() + ".yml");
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        Bukkit.getConsoleSender().sendMessage("[WayUI]: Loading Inventory for Player.");

        for(String s : Objects.requireNonNull(cfg.getConfigurationSection("Slot")).getKeys(false)) {

            ItemStack item = new ItemStack(Objects.requireNonNull(cfg.getItemStack("Slot." + s + ".ItemStack")));
//            ItemMeta meta = item.getItemMeta();
//
//            if (meta != null) {
//                PersistentDataContainer data = meta.getPersistentDataContainer();
//                NamespacedKey key = new NamespacedKey(WaypointUI.GetInstance(), "crate");
//
//                World world = Bukkit.getWorld(Objects.requireNonNull(cfg.getString("Slot." + s + ".World")));
//                double posX = cfg.getDouble("Slot." + s + ".X");
//                double posY = cfg.getDouble("Slot." + s + ".Y");
//                double posZ = cfg.getDouble("Slot." + s + ".Z");
//
//                Location loc = new Location(world, posX, posY, posZ);
//                data.set(key, new CustomLocationData(), loc);
//
//                List<String> lore = new ArrayList<>();
//                lore.add(ChatColor.BLUE + "World: " + ChatColor.RESET + Objects.requireNonNull(loc.getWorld()).getName());
//                lore.add(ChatColor.BLUE + "X: " + ChatColor.RESET + loc.getX());
//                lore.add(ChatColor.BLUE + "Y: " + ChatColor.RESET + loc.getY());
//                lore.add(ChatColor.BLUE + "Z: " + ChatColor.RESET + loc.getZ());
//                meta.setLore(lore);
//                item.setItemMeta(meta);
//
//            }

            inv.setItem(Integer.parseInt(s), item);
        }
    }

    public static void SaveFile(Player player, Inventory inv) {
        File file = new File(playerFilePath + player.getName() + ".yml");
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);

        if (inv != null) {
            for (int i = 0; i < inv.getContents().length; i++) {
//                if (inv.getContents()[i] == null) {
//                    continue;
//                }

                Location data = Objects.requireNonNull(inv.getContents()[i].getItemMeta()).getPersistentDataContainer()
                        .get(new NamespacedKey(WaypointUI.GetInstance(), "crate"), new CustomLocationData());

                if (data != null) {
//                    cfg.set("Slot." + i + ".World", Objects.requireNonNull(data.getWorld()).getName());
//                    cfg.set("Slot." + i + ".Material", Objects.requireNonNull(inv.getContents()[i].getData()).getItemType().toString());
//                    cfg.set("Slot." + i + ".X", data.getX());
//                    cfg.set("Slot." + i + ".Y", data.getY());
//                    cfg.set("Slot." + i + ".Z", data.getZ());
                    cfg.set("Slot." + i + ".ItemStack", inv.getContents()[i]);
                }
            }
        }

        try {
            cfg.save(file);
        } catch (IOException e) {
            Bukkit.getConsoleSender().sendMessage("[ERROR]: " + e.getMessage());
        }
    }

    public static void SaveSlot(Player player, ItemStack item, int slotID) {
        File file = new File(playerFilePath + player.getName() + ".yml");
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);

        if(item.getItemMeta() != null) {
            cfg.set("Slot." + slotID + ".ItemStack", item);
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
