package me.choukas.commands.api;

import net.md_5.bungee.api.chat.BaseComponent;

public interface Condition<T> {

    boolean check(T o);

    BaseComponent[] getMessage(T o);
}
