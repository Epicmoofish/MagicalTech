package org.oceanic.magical_tech.transferrable.filter;

public class NullFilter<T> extends Filter<T> {
    private NullFilter() {}
    public static NullFilter<?> get() {
        return new NullFilter<>();
    }
    @Override
    boolean contains(T value) {
        return true;
    }
}
