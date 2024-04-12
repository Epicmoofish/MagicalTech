package org.oceanic.magical_tech.transferrable.filter;

public class CompositeFilter<T> extends Filter<T> {
    public CompositeFilter(Filter<T> filter1, Filter<T> filter2, CompositeType type) {
        this.filter1 = filter1;
        this.filter2 = filter2;
        this.type = type;
    }
    enum CompositeType {
        AND,
        OR
    }
    private final Filter<T> filter1;
    private final Filter<T> filter2;
    private final CompositeType type;
    @Override
    boolean contains(T value) {
        if (type == CompositeType.AND) {
            return filter1.contains(value) && filter2.contains(value);
        }
        if (type == CompositeType.OR) {
            return filter1.contains(value) || filter2.contains(value);
        }
        return false;
    }
}
