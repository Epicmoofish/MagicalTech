package org.oceanic.magical_tech.util;

public class ExtraMath {
    public static long ceilDivision(long x, long y) {
        final long q = x / y;
        if ((x ^ y) >= 0 && (q * y != x)) {
            return q + 1;
        }
        return q;
    }
}
