// Code by Rodel77 (Modified by Choukas <juan.vlroo@gmail.com>

package me.choukas.commands;

import me.choukas.commands.utils.Tuple;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class CommandCallback {

    private static final Map<UUID, List<Tuple<Integer, Runnable>>> callbacks = new HashMap<>();

    private static final Random random = new Random();

    public CommandCallback(JavaPlugin plugin) {
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onCommand(PlayerCommandPreprocessEvent event) {
                if (event.getMessage().startsWith("/callback")) {
                    UUID player = event.getPlayer().getUniqueId();

                    if (callbacks.containsKey(player)) {
                        Tuple<Integer, Runnable> toRemove = null;
                        for (Tuple<Integer, Runnable> tuple : callbacks.get(player)) {
                            if (event.getMessage().replaceFirst("/callback", "").startsWith(String.valueOf(tuple.getKey()))) {
                                tuple.getValue().run();
                                toRemove = tuple;
                                break;
                            }
                        }
                        if (toRemove != null) {
                            callbacks.get(player).remove(toRemove);
                        }
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
        int rand = random.nextInt(9999);
        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/callback" + rand));
        callbacks.merge(uuid, new ArrayList<>(Collections.singletonList(Tuple.of(rand, callback))), (tuples, tuples2) -> {
            tuples.addAll(tuples2);
            return tuples;
        });
    }
}
