package org.oceanic.magical_tech.data_structures;

public class Mutable<T> {
    private T mutable;
    public Mutable(T value) {
        mutable = value;
    }
    public T get() {
        return mutable;
    }
    public void set(T value) {
        mutable = value;
    }
}
