package com.github.trickzstar.waypointui.events;

import com.github.trickzstar.waypointui.FileManager;
import com.github.trickzstar.waypointui.WaypointUI;
import com.github.trickzstar.waypointui.persistentdata.CustomLocationData;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InventoryClickListener implements Listener {

    @EventHandler
    public void OnInventoryClick(InventoryClickEvent event) {

        Player player = (Player) event.getWhoClicked();
        Inventory inv = event.getView().getTopInventory();
        InventoryView invView = event.getView();

        if(invView.getTitle().equals("Waypoint Inventory"))
        {
            if(event.getClick().isLeftClick())
            {
                ItemMeta meta = Objects.requireNonNull(event.getCurrentItem()).getItemMeta();

                if(meta != null)
                {
                    PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
                    NamespacedKey key = new NamespacedKey(WaypointUI.GetInstance(), "crate");

                    Location playerLocation = player.getLocation();
                    dataContainer.set(key, new CustomLocationData(), playerLocation);
                    List<String> lore = new ArrayList<>();
                    lore.add(ChatColor.BLUE + "World: " + ChatColor.RESET + Objects.requireNonNull(playerLocation.getWorld()).getName());
                    lore.add(ChatColor.BLUE + "X: "     + ChatColor.RESET + playerLocation.getX());
                    lore.add(ChatColor.BLUE + "Y: "     + ChatColor.RESET + playerLocation.getY());
                    lore.add(ChatColor.BLUE + "Z: "     + ChatColor.RESET + playerLocation.getZ());
                    meta.setLore(lore);
                    event.getCurrentItem().setItemMeta(meta);
                    FileManager.SaveFile(player, inv);
                }
            }
            event.setCancelled(true);
        }
    }
}
