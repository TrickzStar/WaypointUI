package com.github.trickzstar.waypointui;

import com.github.trickzstar.waypointui.persistentdata.CustomLocationData;
import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
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
    public static void LoadFile(Player player, ItemMeta meta) {

        File file = new File(playerFilePath + player.getName() + ".yml");
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);

        PersistentDataContainer data = meta.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(WaypointUI.GetInstance(), "crate");

        World world = Bukkit.getWorld(Objects.requireNonNull(cfg.getString("World")));
        double posX = cfg.getDouble("X");
        double posY = cfg.getDouble("Y");
        double posZ = cfg.getDouble("Z");

        Location loc = new Location(world, posX, posY, posZ);
        data.set(key, new CustomLocationData(), loc);

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.BLUE + "World: " + ChatColor.RESET + Objects.requireNonNull(loc.getWorld()).getName());
        lore.add(ChatColor.BLUE + "X: " + ChatColor.RESET + loc.getX());
        lore.add(ChatColor.BLUE + "Y: " + ChatColor.RESET + loc.getY());
        lore.add(ChatColor.BLUE + "Z: " + ChatColor.RESET + loc.getZ());
        meta.setLore(lore);

        try {
            cfg.save(file);
        } catch (IOException e) {
            Bukkit.getConsoleSender().sendMessage("[ERROR]: " + e.getMessage());
        }
    }


    public static void SaveFile(Player player, ItemMeta meta) {
        File file = new File(playerFilePath + player.getName() + ".yml");
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);

        Location data = meta.getPersistentDataContainer().get(new NamespacedKey(WaypointUI.GetInstance(), "crate"), new CustomLocationData());


        if (data != null) {
            cfg.set("World", Objects.requireNonNull(data.getWorld()).getName());
            cfg.set("X", data.getX());
            cfg.set("Y", data.getY());
            cfg.set("Z", data.getZ());
        } else {
            Bukkit.getConsoleSender().sendMessage("[ERROR]: Couldn't find LocationData!");
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
