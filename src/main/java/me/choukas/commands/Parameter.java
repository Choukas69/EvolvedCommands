/*
 * Copyright (c) 2020 Choukas - All rights reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Choukas <juan.vlroo@gmail.com>, 06/06/2020 20:40
 */

package me.choukas.commands;

import java.util.Optional;

public abstract class Parameter<T> implements TabCompleter<T> {

    private final String name;

    public Parameter(String name) {
        this.name = name;
    }

    public final String getName() {
        return name;
    }

    public abstract Optional<T> check(String arg);

    public abstract String getMessage(String arg);
}
