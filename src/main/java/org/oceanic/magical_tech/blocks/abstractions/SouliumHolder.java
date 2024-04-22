package org.oceanic.magical_tech.blocks.abstractions;

public interface SouliumHolder {
    long getSoulium();
    long getMaxSoulium();
    default long getExporting() {
        return getSoulium();
    }
    default long getImporting() {
        return getMaxSoulium() - getSoulium();
    }
    void removeSoulium(long amount);
    default void addSoulium(long amount) {
        removeSoulium(-amount);
    }
}