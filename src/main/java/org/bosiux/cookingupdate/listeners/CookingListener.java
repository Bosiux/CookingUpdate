package org.bosiux.cookingupdate.listeners;

import org.bosiux.cookingupdate.Main;
import org.bosiux.cookingupdate.utils.CraftingManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.bosiux.cookingupdate.utils.CraftingManager.round;
import static org.bosiux.cookingupdate.utils.FuelManager.getPlayerValue;
import static org.bosiux.cookingupdate.utils.FuelManager.setPlayerValue;

public class CookingListener implements Listener {
    private static final Plugin plugin = Main.plugin;
    private static boolean processing = false;
    private static Float Vfuel = null;

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
                    String[] list = new String[3];

                    if (player.getOpenInventory().getItem(14) != null){
                        list[0] = (player.getOpenInventory().getItem(14)).getType() + ":" + (player.getOpenInventory().getItem(14)).getAmount();
                    } else {
                        list[0] = "";
                    }
                    if (player.getOpenInventory().getItem(15) != null){
                        list[1] = (player.getOpenInventory().getItem(15)).getType() + ":" + (player.getOpenInventory().getItem(15)).getAmount();
                    } else {
                        list[1] = "";
                    }
                    if (player.getOpenInventory().getItem(16) != null){
                        list[2] = (player.getOpenInventory().getItem(16)).getType() + ":" + (player.getOpenInventory().getItem(16)).getAmount();
                    } else {
                        list[2] = "";
                    }

                    try {
                        Map<String, Float> res = CraftingManager.getRecipe(list);
                        if (res == null){
                            continue;
                        }
                        String recipeResult = res.keySet().iterator().next();
                        float fuelValue = res.get(recipeResult);
                        Vfuel = fuelValue;

                         if (recipeResult != null)
                         {


                             if (getPlayerValue(player.getName()) < fuelValue) {
                                 continue;
                             }



                             String[] parts = recipeResult.split(":");
                             if (parts.length == 2) {
                                 String material = parts[0];
                                 int amount = (int) Integer.parseInt(parts[1]);
                                 ItemStack item = new ItemStack(Objects.requireNonNull(Material.getMaterial(material)));
                                 item.setAmount(amount);
                                 player.getOpenInventory().setItem(33, item);
                             } else {
                                 System.out.println("Invalid input format");
                             }
                         }

                    } catch (IOException e) {
                        plugin.getLogger().warning("[ERROR] -> data.json not found.");
                        throw new RuntimeException(e);
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


    @EventHandler
    public void onCookingInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals(plugin.getConfig().getString("Cooking_GUI_NameTAG"))) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        int slot = event.getRawSlot();
        ItemStack air = new ItemStack(Material.AIR);


        if (slot == 33) {
            Float fuel = getPlayerValue(player.getName());
            setPlayerValue(player.getName(), fuel - Vfuel);
            player.getOpenInventory().setItem(14, air);
            player.getOpenInventory().setItem(15, air);
            player.getOpenInventory().setItem(16, air);
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


        block = new ItemStack( Material.GUNPOWDER );
        meta = block.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "1.00");
        block.setItemMeta(meta);
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


        if (slot == 0 || slot == 4 || slot == 6) {
            event.setCancelled(true);
            return;
        }

        if (slot == 7) {
            event.setCancelled(true);
            ItemStack block = inventory.getItem(7);
            ItemMeta meta;
            String name = block.getItemMeta().getDisplayName();

            String num = name.substring(2);

            float value = Float.parseFloat(num);
            value += 0.1F;
            value = round(value);

            meta = block.getItemMeta();
            meta.setDisplayName(ChatColor.GREEN + String.valueOf(value));
            block.setItemMeta(meta);
            inventory.setItem(7, block);
        }

        if (slot == 8) {
            event.setCancelled(true);
            player.closeInventory();

            ItemStack block = inventory.getItem(7);
            String name = block.getItemMeta().getDisplayName();
            String num = name.substring(2);
            float value = Float.parseFloat(num);
            saveAndPrint(inventory, round(value));
        }
    }

    private void saveAndPrint(Inventory inventory, float value) {
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


        CraftingManager.newCrafting(ingredients, result, value);

    }

}
