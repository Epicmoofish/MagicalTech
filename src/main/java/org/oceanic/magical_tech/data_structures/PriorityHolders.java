package org.oceanic.magical_tech.data_structures;

public class PriorityHolders<T> {
    public int priority;
    public T holder;
    public PriorityHolders(T holder, int priority) {
        this.priority = priority;
        this.holder = holder;
    }
}
