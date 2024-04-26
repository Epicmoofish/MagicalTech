package org.oceanic.magical_tech.util;

public class ExtraMath {
    public static long ceilDivision(long x, long y) {
        final long q = x / y;
        if ((x ^ y) >= 0 && (q * y != x)) {
            return q + 1;
        }
        return q;
    }
    public static int[] longSwapper(long val) {
        int a = (int)(val >> 32);
        int b = (int)val;
        return new int[]{a, b};
    }
    public static long longSwapper(int a, int b) {
        return (long)a << 32 | b & 0xFFFFFFFFL;
    }
}
