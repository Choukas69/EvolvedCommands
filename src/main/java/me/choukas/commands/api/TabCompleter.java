/*
 * Copyright (c) 2020 Choukas - All rights reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Choukas <juan.vlroo@gmail.com>, 22/06/2020 19:56
 */

package me.choukas.commands.api;

import org.bukkit.command.CommandSender;

import java.util.Collection;
import java.util.Collections;

public interface TabCompleter<T> {

    default Collection<T> tabComplete(CommandSender sender) {
        return Collections.emptyList();
    }
}
