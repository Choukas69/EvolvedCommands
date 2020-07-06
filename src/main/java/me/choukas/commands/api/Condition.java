package me.choukas.commands.api;

public interface Condition<T> {

    boolean check(T o);

    String getMessage(T o);
}
