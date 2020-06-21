package me.choukas.commands.utils;

public class Tuple<K, V> {

    private final K key;
    private final V value;

    private Tuple(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public static <K, V> Tuple<K, V> of(K key, V value) {
        return new Tuple<>(key, value);
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }
}
