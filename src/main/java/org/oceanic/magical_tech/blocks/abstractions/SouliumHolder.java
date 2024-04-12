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
    long removeSoulium(long amount);
    default long addSoulium(long amount) {
        return removeSoulium(-amount);
    }
}