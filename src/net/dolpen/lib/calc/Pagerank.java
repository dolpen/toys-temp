package net.dolpen.lib.calc;

/**
 * ページランク
 */
public class Pagerank {

    // ダンピング・ファクター。(<1=)通常0.85に設定されるが
    // 作為的にページランクを上げようとする者に対しては、より小さい値に設定される。
    public static final double d = 0.85;

    /**
     * ページランクの計算
     *
     * @param w     隣接行列(リンクの有向グラフ)
     * @param limit 計算反復回数
     * @return 得点の配列
     */
    public static double[] calc(double[][] w, int limit) {
        double[][] transposed = transpose(w);
        double[] pagerank = initialPagerank(w);
        for (int i = 0; i < limit; i++)
            pagerank = computeNewPagerank(pagerank, transposed);
        return pagerank;
    }

    /**
     * 転置行列を得る
     * リンクを示す有向グラフの隣接行列の転置とは、
     * 被リンクの隣接行列を得ることに他ならない
     */
    private static double[][] transpose(double[][] w) {
        double[][] r = new double[w[0].length][w.length];
        for (int i = 0; i < w.length; i++)
            for (int j = 0; j < w[0].length; j++)
                r[j][i] = w[i][j];
        return r;
    }

    /**
     * ページランクの初期値を得る
     */
    private static double[] initialPagerank(double[][] w) {
        double[] r = new double[w.length];
        for (int i = 0; i < w.length; i++) r[i] = 1.00;
        return r;
    }

    /**
     * 反復計算
     *
     * @param p 途中ページランク
     * @param w 被リンク行列
     * @return 更新ページランク
     */
    private static double[] computeNewPagerank(double[] p, double[][] w) {
        double[] r = new double[p.length];
        for (int i = 0; i < p.length; i++) {
            double pr = 0.0;
            for (int j = 0; j < w[i].length; j++)
                if (w[i][j] > 0)
                    pr += p[i] * w[i][j];
            r[i] = (1.0 - d) + d * pr;
        }
        return r;
    }
}
