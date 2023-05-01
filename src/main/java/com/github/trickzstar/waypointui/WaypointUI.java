package com.github.trickzstar.waypointui;

import com.github.trickzstar.waypointui.commands.WaypointCommand;
import com.github.trickzstar.waypointui.events.InventoryClickListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class WaypointUI extends JavaPlugin {

    private static WaypointUI INSTANCE;

    @Override
    public void onEnable() {

        if(INSTANCE != null)
        {
            Bukkit.getConsoleSender().sendMessage("There is already a instance of: WaypointUI.");
        }
        else
        {
            INSTANCE = this;
        }

        Objects.requireNonNull(getCommand("wp")).setExecutor(new WaypointCommand());

        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new InventoryClickListener(), this);
    }

    @Override
    public void onDisable() {
    }

    public static WaypointUI GetInstance() {
        return INSTANCE;
    }

}
