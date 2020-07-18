package me.choukas.commands.api;

import org.bukkit.command.CommandSender;

import java.util.function.Function;

public class Arg<T> {

    private final String name;
    private final Function<CommandSender, T> function;

    private T value;

    public Arg(String name, Function<CommandSender, T> function) {
        this.name = name;
        this.function = function;
    }

    public void apply(CommandSender sender) {
        this.value = function.apply(sender);
    }

    public String getName() {
        return name;
    }

    public T getValue() {
        return value;
    }
}
