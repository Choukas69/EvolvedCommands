// Code by Rodel77 (Modified by Choukas <juan.vlroo@gmail.com>

package me.choukas.commands;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CommandCallback {

    private static final Map<UUID, Runnable> callbacks = new HashMap<>();

    public CommandCallback(JavaPlugin plugin) {
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onCommand(PlayerCommandPreprocessEvent event) {
                if (event.getMessage().startsWith("/callback")) {
                    UUID player = event.getPlayer().getUniqueId();

                    if (callbacks.containsKey(player)) {
                        callbacks.get(player).run();
                    }
                }
            }
        }, plugin);
    }

    /**
     * Add a callback when clicking a message
     *
     * @param message  message to add the click event to
     * @param uuid     uuid of the command sender
     * @param callback callback to run
     */
    public static void createCommand(TextComponent message, UUID uuid, Runnable callback) {
        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/callback"));
        callbacks.put(uuid, callback);
    }
}
