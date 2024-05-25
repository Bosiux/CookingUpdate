package org.bosiux.cookingupdate;

import org.bosiux.cookingupdate.executors.CookingCommand;
import org.bosiux.cookingupdate.listeners.CookingListener;
import org.bosiux.cookingupdate.listeners.onJoinListener;
import org.bosiux.cookingupdate.utils.FuelManager;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.*;

import static org.bosiux.cookingupdate.utils.Variables.getCredits;
import static org.bosiux.cookingupdate.utils.Variables.getTitle;


public final class Main extends JavaPlugin {
    public static Plugin plugin;

    @Override
    public void onEnable() {
        plugin = this;

        getLogger().info(getTitle());

        createPluginFolder();

        copyResource("data.json");
        copyResource("config.yml");
        copyResource("storage.json");

        FuelManager.file = new File(getDataFolder(), "storage.json");



        getServer().getPluginManager().registerEvents(new CookingListener(), this);
        getServer().getPluginManager().registerEvents(new onJoinListener(), this);

        getCommand("cooking").setExecutor(new CookingCommand());


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }



    private void createPluginFolder() {
        File folder = getDataFolder();
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    private void copyResource(String resourceName) {
        File file = new File(getDataFolder(), resourceName);
        if (!file.exists()) {
            try (InputStream in = getResource(resourceName);
                 OutputStream out = new FileOutputStream(file)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
