/*
 * Copyright (c) 2020 Choukas - All rights reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Choukas <juan.vlroo@gmail.com>, 22/06/2020 19:56
 */

package me.choukas.commands.api;

import org.bukkit.command.CommandSender;

import java.util.List;

public abstract class Parameter<T> extends Configurable implements TabCompleter<T> {

    private final String name;
    private final boolean extended;

    public Parameter(String name) {
        this(name, false);
    }

    public Parameter(String name, boolean extended) {
        this.name = name;
        this.extended = extended;
    }

    public abstract List<Condition<String>> getConditions(CommandSender sender);

    public abstract T get(String arg);

    public String getName() {
        return name;
    }

    public boolean isExtended() {
        return extended;
    }
}
