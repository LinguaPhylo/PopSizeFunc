package popsizefunc.lphy.evolution.coalescent;

public class BinomialUtils {
    /**
     * 计算从n个元素中选择两个的方式的数量。
     *
     * @param n 元素总数
     * @return 从n个元素中选择两个的方式的数量
     */
    public static double choose2(final int n) {
        return n * (n - 1) / 2.0;
    }
}
