package me.choukas.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandDescription {

    private String name;
    private List<String> aliases;
    private String description;
    private String permission;

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

    public static class Builder {

        private String name = "";
        private List<String> aliases = new ArrayList<>();
        private String description = "";
        private String permission = "";

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

        public CommandDescription build() {
            CommandDescription commandDescription = new CommandDescription();
            commandDescription.name = name;
            commandDescription.aliases = aliases;
            commandDescription.description = description;
            commandDescription.permission = permission;

            return commandDescription;
        }
    }
}
