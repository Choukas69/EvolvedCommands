/*
 * Copyright (c) 2020 Choukas - All rights reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Choukas <juan.vlroo@gmail.com>, 22/06/2020 19:56
 */

package me.choukas.commands.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandDescription {

    private String name;
    private List<String> aliases;
    private String description;
    private String permission;
    private boolean consoleExecutable;

    private CommandDescription() {}

    public static Builder builder() {
        return new Builder();
    }

    public String getName() {
        return name;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public String getDescription() {
        return description;
    }

    public String getPermission() {
        return permission;
    }

    public boolean isConsoleExecutable() {
        return consoleExecutable;
    }

    public static class Builder {

        private String name = "";
        private List<String> aliases = new ArrayList<>();
        private String description = "";
        private String permission = "";
        private boolean consoleExecutable = true;

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withAliases(String... aliases) {
            this.aliases = Arrays.asList(aliases);
            return this;
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder withPermission(String permission) {
            this.permission = permission;
            return this;
        }

        public Builder withConsoleExecutable(boolean consoleExecutable){
            this.consoleExecutable = consoleExecutable;
            return this;
        }

        public CommandDescription build() {
            CommandDescription commandDescription = new CommandDescription();
            commandDescription.name = name;
            commandDescription.aliases = aliases;
            commandDescription.description = description;
            commandDescription.permission = permission;
            commandDescription.consoleExecutable = consoleExecutable;

            return commandDescription;
        }
    }
}
