/*
 * Copyright (c) 2020 Choukas - All rights reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Choukas <juan.vlroo@gmail.com>, 22/06/2020 19:54
 */

package me.choukas.commands;

import me.choukas.commands.api.CommandDescription;
import me.choukas.commands.api.Condition;
import me.choukas.commands.api.Parameter;
import me.choukas.commands.utils.Tuple;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

public abstract class EvolvedCommand extends Command {

    private final CommandDescription description;

    private EvolvedCommand parent;
    private final List<EvolvedCommand> children = new ArrayList<>();

    private final List<Tuple<Parameter<?>, Boolean>> params = new ArrayList<>();
    private final List<Condition> conditions = new ArrayList<>();

    private final LinkedBlockingQueue<Tuple<?, Boolean>> args = new LinkedBlockingQueue<>();

    public EvolvedCommand(CommandDescription description) {
        super(description.getName(), "", "", description.getAliases());

        this.description = description;
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (args.length == 0) {
            if (children.isEmpty() && !params.isEmpty() && params.stream().anyMatch(Tuple::getValue)) {
                sendHelp(sender);
            } else {
                tryExecute(sender, args);
            }
        } else {
            Optional<EvolvedCommand> optional = Optional.empty();

            try {
                optional = children.stream()
                        .filter(child ->
                                child.description.getName().equalsIgnoreCase(args[0]) || child.description.getAliases().contains(args[0]))
                        .findAny();
            } catch (ArrayIndexOutOfBoundsException ignored) {

            } finally {
                if (optional.isPresent()) {
                    EvolvedCommand command = optional.get();
                    String[] newArgs = Arrays.copyOfRange(args, 1, args.length);
                    command.execute(sender, args[0], newArgs);
                } else {
                    if (children.isEmpty()) {
                        long requiredParams = params.stream().filter(Tuple::getValue).count();

                        if (args.length >= requiredParams && args.length <= params.size()) {
                            tryExecute(sender, args);
                        } else {
                            sendHelp(sender);
                        }
                    } else {
                        sendFullHelp(sender);
                    }
                }
            }
        }

        return true;
    }

    @SuppressWarnings("ReturnInsideFinallyBlock")
    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        List<String> list = new ArrayList<>();

        if (checkPreconditions(sender, false)) {
            Optional<EvolvedCommand> subCommand = Optional.empty();

            try {
                subCommand = children.stream()
                        .filter(command ->
                                command.description.getName().equalsIgnoreCase(args[0]))
                        .findFirst();
            } catch (ArrayIndexOutOfBoundsException ignored) {

            } finally {
                if (subCommand.isPresent()) {
                    EvolvedCommand command = subCommand.get();
                    String[] newArgs = Arrays.copyOfRange(args, 1, args.length);

                    return command.tabComplete(sender, args[0], newArgs);
                } else {
                    if (!children.isEmpty()) {
                        list.addAll(children.stream()
                                .filter(command ->
                                        command.description.getName().startsWith(args[0]) &&
                                                checkPreconditions(sender, false))
                                .map(command ->
                                        command.description.getName())
                                .collect(Collectors.toList()));
                    } else if (!params.isEmpty()) {
                        long requiredParams = params.stream().filter(Tuple::getValue).count();

                        if (args.length >= requiredParams && args.length <= params.size()) {
                            list.addAll(params.get(args.length - 1).getKey().tabComplete(sender).stream()
                                    .filter(o -> o.toString().startsWith(args[args.length - 1]))
                                    .map(Object::toString)
                                    .collect(Collectors.toList()));
                        }
                    }
                }
            }
        }

        Collections.sort(list); // Sort the list to get an alphabetic sorted list
        return list;
    }

    protected abstract void define();

    protected void execute(CommandSender sender) {
        sendFullHelp(sender);
    }

    protected void addSubCommand(EvolvedCommand subCommand) {
        if (params.isEmpty()) {
            subCommand.parent = this;
            children.add(subCommand);
        } else {
            throw new RuntimeException("You cannot infer params and sub commands");
        }
    }

    protected void addParam(Parameter<?> param, boolean required) {
        if (children.isEmpty()) {
            if (required) {
                if (!params.isEmpty() && !params.get(params.size() - 1).getValue()) {
                    throw new IllegalArgumentException("Vous ne pouvez pas ajouter un argument requis après un argument facultatif");
                }
            }

            params.add(Tuple.of(param, required));
        } else {
            throw new RuntimeException("You cannot infer params and sub commands");
        }
    }

    @SafeVarargs
    protected final void addCondition(Condition condition, Tuple<String, ?>... args) {
        condition.inject(args);
        conditions.add(condition);
    }

    @SuppressWarnings("unchecked")
    protected <T> T readArg() {
        if (args.isEmpty()) {
            throw new NoSuchElementException();
        } else {
            return (T) args.poll().getKey();
        }
    }

    @SuppressWarnings("unused")
    protected <T> T readArg(Class<T> clazz) {
        return readArg();
    }

    protected <T> Optional<T> readOptionalArg() {
        Optional<T> optionalArg = Optional.empty();

        if (!args.isEmpty()) {
            optionalArg = Optional.of(readArg());
        }

        return optionalArg;
    }

    @SuppressWarnings("unused")
    protected <T> Optional<T> readOptionalArg(Class<T> clazz) {
        return readOptionalArg();
    }

    protected void sendHelp(CommandSender sender) {
        EvolvedCommand parent = this;
        StringBuilder builder = new StringBuilder();

        // Slash sub commands from root
        do {
            builder.append(parent.description.getName()).append(" ");

            parent = parent.parent;
        } while (parent != null);

        List<String> help = Arrays.asList(builder.toString().split(" "));
        Collections.reverse(help);

        builder = new StringBuilder(String.join(" ", help)).append(" ");

        // Params addition
        for (Tuple<Parameter<?>, Boolean> param : params) {
            if (param.getValue()) {
                builder.append(String.format("<%s>", param.getKey().getName()));
            } else {
                builder.append(String.format("[%s]", param.getKey().getName()));
            }

            builder.append(" ");
        }

        // Slash addition
        builder.insert(0, "/");

        sender.sendMessage(builder.toString());
    }

    protected void sendFullHelp(CommandSender sender) {
        for (EvolvedCommand child : children) {
            child.sendHelp(sender);
        }
    }

    protected void sendRootHelp(CommandSender sender) {
        getRoot().sendFullHelp(sender);
    }

    private EvolvedCommand getRoot() {
        if (parent == null) {
            return this;
        } else {
            return parent.getRoot();
        }
    }

    private boolean checkPreconditions(CommandSender sender, boolean verbose) {
        for (Condition condition : conditions) {
            if (!condition.check(sender)) {
                if (verbose) {
                    sender.sendMessage(condition.getMessage());
                }

                return false;
            }
        }

        return true;
    }

    @SuppressWarnings("ReturnInsideFinallyBlock")
    private void tryExecute(CommandSender sender, String[] args) {
        if (checkPreconditions(sender, true)) {
            this.args.clear();

            for (int i = 0; i < args.length; i++) {
                Tuple<Parameter<?>, Boolean> tuple = params.get(i);
                Parameter<?> param = tuple.getKey();

                Optional<?> optional = Optional.empty();

                try {
                    optional = param.check(args[i]);
                } catch (Exception ignored) {

                } finally {
                    if (optional.isPresent()) {
                        this.args.offer(Tuple.of(optional.get(), tuple.getValue()));
                    } else {
                        sender.sendMessage(param.getMessage(args[i]));
                        return;
                    }
                }
            }

            execute(sender);
        }
    }
}