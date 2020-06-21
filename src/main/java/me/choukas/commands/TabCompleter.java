package me.choukas.commands;

import org.bukkit.command.CommandSender;

import java.util.Collection;
import java.util.Collections;

public interface TabCompleter<T> {

    default Collection<T> tabComplete(CommandSender sender) {
        return Collections.emptyList();
    }
}
