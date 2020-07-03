/*
 * Copyright (c) 2020 Choukas - All rights reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Choukas <juan.vlroo@gmail.com>, 22/06/2020 19:56
 */

package me.choukas.commands.api;

import me.choukas.commands.utils.Tuple;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public abstract class Condition {

    private final List<Tuple<String, ?>> args = new ArrayList<>();

    public abstract boolean check(CommandSender sender);

    public abstract String getMessage();

    @SafeVarargs
    public final void inject(Tuple<String, ?>... args) {
        this.args.addAll(Arrays.asList(args));
    }

    @SuppressWarnings("unchecked")
    public <T> T readArg(String name) {
        Optional<Tuple<String, ?>> optional = args.stream().filter(arg -> arg.getKey().equals(name)).findFirst();

        if (optional.isPresent()) {
            return (T) optional.get().getValue();
        } else {
            throw new IllegalArgumentException();
        }
    }
}
