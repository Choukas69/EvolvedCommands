package me.choukas.commands.api;

import org.bukkit.command.CommandSender;

import java.util.*;
import java.util.stream.Collectors;

public abstract class Configurable {

    private final Map<String, Arg<?>> args = new HashMap<>();

    public void apply(CommandSender sender) {
        for (Arg<?> arg : args.values()) {
            arg.apply(sender);
        }
    }

    public final void inject(Arg<?>... args) {
        this.args.putAll(Arrays
                .stream(args)
                .collect(Collectors.toMap(Arg::getName, arg -> arg)));
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> readArg(String name) {
        return Optional.ofNullable((T) args.get(name));
    }
}
