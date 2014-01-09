package net.dolpen.lib.logic.math;

import java.util.ArrayList;
import java.util.List;

/**
 * 主に割り算系
 */
public class Dividions {

    /**
     * 約数の列挙(未整列、nが小さければ使える) O(sqrt(n))
     */
    public static List<Integer> divisor(int n) {
        List<Integer> ans = new ArrayList<Integer>();
        for (int i = 1; i * i <= n; i++) {
            if (n % i != 0) continue;
            ans.add(i);
            if (i * i != n) ans.add(n / i);
        }
        return ans;
    }

}
