package net.dolpen.lib.logic.math;

import java.util.ArrayList;
import java.util.List;

public class PrimeFactorization {
    /**
     * 素因数分解の愚直な実装
     *
     * @param n
     * @return
     */
    public static List<Integer> factrization(int n) {
        List<Integer> l = new ArrayList<Integer>();
        for (int x = 2; x * x <= n; x++) {
            while (n % x == 0) {
                n /= x;
                l.add(x);
            }
        }
        if (n > 1) l.add(n);
        return l;
    }
}
