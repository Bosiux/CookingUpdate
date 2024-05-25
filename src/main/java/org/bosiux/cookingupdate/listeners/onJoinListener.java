package org.bosiux.cookingupdate.listeners;

import org.bosiux.cookingupdate.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static org.bosiux.cookingupdate.utils.FuelManager.*;

import org.bukkit.plugin.Plugin;


public class onJoinListener implements Listener {
    private static final Plugin plugin = Main.plugin;


    @EventHandler
    void JoinListener(PlayerJoinEvent playerJoinEvent){

        Player player = playerJoinEvent.getPlayer();
        String username = player.getName();
        if( ! playerExists( username ) ){

                float fuel = (float) plugin.getConfig().getDouble("DEFAULT_FUEL");
                createPlayer(username, fuel);
        }

    }
}
