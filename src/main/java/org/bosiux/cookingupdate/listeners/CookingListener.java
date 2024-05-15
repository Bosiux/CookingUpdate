package org.bosiux.cookingupdate.listeners;

import org.bosiux.cookingupdate.Main;
import org.bosiux.cookingupdate.utils.CraftingManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CookingListener implements Listener {
    private static final Plugin plugin = Main.plugin;
    private static boolean processing = false;

    public static void openGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 45, plugin.getConfig().getString("Cooking_GUI_NameTAG"));
        player.openInventory(gui);
        startProcessing(player);
    }

    private static void startProcessing(Player player) {
        processing = true;
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            while (processing) {
                if (player.getOpenInventory().getTitle().equals(plugin.getConfig().getString("Cooking_GUI_NameTAG"))) {
                    for (int slot : new int[]{14, 15, 16}) {
                        ItemStack item = player.getOpenInventory().getItem(slot);
                        if (item != null) {
                            Material material = item.getType();
                            player.sendMessage("Material: " + material.toString() + " - Amount: " + item.getAmount());
                        }
                    }
                }

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    // 29 coal


    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getView().getTitle().equals(plugin.getConfig().getString("Cooking_GUI_NameTAG"))) {
            processing = false;
        }
    }































    //// RECIPE REGISTRATION









    public static void openRecipeGUI(Player player) {
        Inventory recipeGUI = Bukkit.createInventory(null, 9, Objects.requireNonNull(plugin.getConfig().getString("Cooking_Recipe_NameTAG")));

        String[] signNames = {"Ingredients: ", "Result: "};
        ItemStack block;
        ItemMeta meta;

        for (int i = 0; i < 7; i++) {
            if (i == 0 || i == 4 ) {
                block = new ItemStack(Material.OAK_SIGN);
                meta = block.getItemMeta();
                if (meta != null) {
                    meta.setDisplayName(signNames[i == 0 ? 0 : 1]);
                    block.setItemMeta(meta);
                }
                recipeGUI.setItem(i, block);
            }
        }

        block = new ItemStack( Material.GRAY_STAINED_GLASS );
        meta = block.getItemMeta();
        meta.setDisplayName("Empty");
        block.setItemMeta(meta);

        recipeGUI.setItem(6, block);
        recipeGUI.setItem(7, block);


        block = new ItemStack( Material.GREEN_STAINED_GLASS );
        meta = block.getItemMeta();
        meta.setDisplayName("Confirm");
        block.setItemMeta(meta);

        recipeGUI.setItem(8, block);

        player.openInventory(recipeGUI);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory inventory = event.getClickedInventory();

        if (inventory == null) return;
        if (! event.getView().getTitle().equals(plugin.getConfig().getString("Cooking_Recipe_NameTAG"))){
            return;
        }
        int slot = event.getRawSlot();
        if (slot == 0 || slot == 4 || slot == 6 || slot == 7) {
            event.setCancelled(true);
            return;
        }

        if (slot == 8) {
            event.setCancelled(true);
            player.closeInventory();
            saveAndPrint(player, inventory);
        }
    }

    private void saveAndPrint(Player player, Inventory inventory) {
        Map<Material, Integer> items = new HashMap<>();
        String[] ingredients = new String[3];
        String result = "";
        for (int i = 1; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);
            switch (i)
            {
                case 1:
                    if (item != null && item.getType() != Material.AIR) {
                        ingredients[0] = item.getType() + ":" + item.getAmount();
                    }
                    break;
                case 2:
                    if (item != null && item.getType() != Material.AIR) {
                        ingredients[1] = item.getType() + ":" + item.getAmount();
                    }
                    break;
                case 3:
                    if (item != null && item.getType() != Material.AIR) {
                        ingredients[2] = item.getType() + ":" + item.getAmount();
                    }
                    break;
                case 5:
                    if (item != null && item.getType() != Material.AIR) {
                        result = item.getType() + ":" + item.getAmount();
                    }

            }
        }






        CraftingManager.newCrafting(ingredients, result);

    }

}
