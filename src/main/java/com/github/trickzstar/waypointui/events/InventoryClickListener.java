package com.github.trickzstar.waypointui.events;

import com.github.trickzstar.waypointui.FileManager;
import com.github.trickzstar.waypointui.WaypointUI;
import com.github.trickzstar.waypointui.persistentdata.CustomLocationData;
import org.bukkit.*;
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

    private int slotID = 0;
    private Inventory previousInventory = null;

    @EventHandler
    public void OnInventoryClick(InventoryClickEvent event) {

        Player player = (Player) event.getWhoClicked();
        Inventory inv = event.getView().getTopInventory();
        InventoryView invView = event.getView();

        if (invView.getTitle().equals("Waypoint Inventory")) {
            if (event.getClick().isLeftClick() && event.getClick().isShiftClick()) {
                if (event.getCurrentItem() != null) {
                    previousInventory = inv;
                    slotID = event.getSlot();
                    Inventory newInv = Bukkit.createInventory(null, 9, "Choose Block");
                    newInv.addItem(new ItemStack(Material.DIAMOND_BLOCK));
                    newInv.addItem(new ItemStack(Material.GOLD_BLOCK));
                    newInv.addItem(new ItemStack(Material.IRON_BLOCK));
                    newInv.addItem(new ItemStack(Material.OAK_WOOD));
                    player.openInventory(newInv);
                }
            } else if (event.getClick().isLeftClick()) {
                if (event.getCurrentItem() != null) {
                    ItemMeta meta = event.getCurrentItem().getItemMeta();
                    if (meta != null) {
                        PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
                        NamespacedKey key = new NamespacedKey(WaypointUI.GetInstance(), "crate");

                        Location playerLocation = player.getLocation();
                        dataContainer.set(key, new CustomLocationData(), playerLocation);
                        List<String> lore = new ArrayList<>();
                        lore.add(ChatColor.BLUE + "World: " + ChatColor.RESET + Objects.requireNonNull(playerLocation.getWorld()).getName());
                        lore.add(ChatColor.BLUE + "X: " + ChatColor.RESET + playerLocation.getX());
                        lore.add(ChatColor.BLUE + "Y: " + ChatColor.RESET + playerLocation.getY());
                        lore.add(ChatColor.BLUE + "Z: " + ChatColor.RESET + playerLocation.getZ());
                        meta.setLore(lore);
                        event.getCurrentItem().setItemMeta(meta);
                        FileManager.SaveFile(player, inv);
                    }
                }
            } else if (event.isRightClick()) {
                if (event.getCurrentItem() != null) {
                    ItemMeta meta = Objects.requireNonNull(event.getCurrentItem()).getItemMeta();
                    if (meta != null) {
                        Location locData = meta.getPersistentDataContainer().get(new NamespacedKey(WaypointUI.GetInstance(), "crate"),
                                new CustomLocationData());
                        player.sendMessage("Location: " + locData);
                        if (locData != null) {
                            player.teleport(locData);
                        }
                    }
                }
            }
            event.setCancelled(true);
        } else if (invView.getTitle().equals("Choose Block")) {
            if (event.getClick().isLeftClick()) {
                ItemStack currentItem = event.getCurrentItem();
                if (currentItem != null && currentItem.getData() != null) {
                    ItemStack changeStack = new ItemStack(Objects.requireNonNull(event.getCurrentItem().getData()).getItemType());
                    ItemMeta itemMeta = Objects.requireNonNull(previousInventory.getItem(slotID)).getItemMeta();
                    changeStack.setItemMeta(itemMeta);
                    FileManager.SaveSlot(player, changeStack, slotID);
                    previousInventory.setItem(slotID, changeStack);
                    player.openInventory(previousInventory);
                }
            }
            event.setCancelled(true);
        }
    }
}
