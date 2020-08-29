/*
 * Copyright (c) 2020 Choukas - All rights reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Choukas <juan.vlroo@gmail.com>, 22/06/2020 19:56
 */

package me.choukas.commands.api;

import java.util.List;

public abstract class Parameter<T> implements TabCompleter<T> {

    private final String name;

    public Parameter(String name) {
        this.name = name;
    }

    public abstract List<Condition<String>> getConditions();

    public abstract T get(String arg);

    public String getName() {
        return name;
    }
}
