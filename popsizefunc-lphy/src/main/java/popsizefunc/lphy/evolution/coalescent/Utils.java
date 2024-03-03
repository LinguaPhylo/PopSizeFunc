package popsizefunc.lphy.evolution.coalescent;

import popsizefunc.lphy.evolution.popsize.PopulationFunction;

public class Utils {
    /**
     * Numerical integration was performed using the trapezoidal method.
     * @param populationFunction 人口函数，其getIntensity方法提供了需要积分的函数。
     * @param t0 积分的起始时间。
     * @param t1 积分的结束时间。
     * @param n 积分区间的划分数量，更多的划分可以提供更精确的结果，但计算成本更高。
     * @return 在[t0, t1]区间上人口函数的积分。
     */
    public static double trapezoidalRule(PopulationFunction populationFunction, double t0, double t1, int n) {
        double delta = (t1 - t0) / n;
        double sum = 0.5 * (populationFunction.getIntensity(t0) + populationFunction.getIntensity(t1));
        for (int i = 1; i < n; i++) {
            double ti = t0 + i * delta;
            sum += populationFunction.getIntensity(ti);
        }
        return sum * delta;
    }


    /**
     * 计算给定时间点和人口函数下的合并时间间隔。
     *
     * @param U Uniform random variable used for randomization.
     * @param populationFunction Population function used to calculate intensity and inverse intensity.
     * @param lineageCount The number of currently active pedigrees.
     * @param timeOfLastCoalescent The time of the last merge event.
     * @return The time interval for the next merge event.
     */
    private static double getInterval(double U, PopulationFunction populationFunction,
                                      int lineageCount, double timeOfLastCoalescent) {
        double intensity;
        // 直接使用 populationFunction 的 isAnalytical 方法来决定计算方式
//        if (populationFunction.isAnalytical()) {
//            intensity = populationFunction.getIntensity(timeOfLastCoalescent);
//        } else {
            intensity = trapezoidalRule(populationFunction, 0, timeOfLastCoalescent, 100); // 使用数值积分
//        }
        double tmp = -Math.log(U) / BinomialUtils.choose2(lineageCount) + intensity;
        return populationFunction.getInverseIntensity(tmp) - timeOfLastCoalescent;
    }

    /**
     * Gets the simulated merge interval.
     */
    public static double getSimulatedInterval(PopulationFunction populationFunction,
                                              int lineageCount, double timeOfLastCoalescent) {
        double U = Math.random(); // Use the Java standard library to generate random numbers in the interval [0, 1)
        return getInterval(U, populationFunction, lineageCount, timeOfLastCoalescent);
    }

    public static double getNumericalInterval(PopulationFunction populationFunction, int lineageCount, double time) {
        double U = Math.random();
        // 使用数值积分计算强度
        double intensity = trapezoidalRule(populationFunction, 0, time, 100);
        double tmp = -Math.log(U) / BinomialUtils.choose2(lineageCount) + intensity;
        return populationFunction.getInverseIntensity(tmp) - time;
    }

    /**
     * 获取中位数合并时间间隔，假设U=0.5。
     */
    public static double getMedianInterval(PopulationFunction populationFunction,
                                           int lineageCount, double timeOfLastCoalescent) {
        return getInterval(0.5, populationFunction, lineageCount, timeOfLastCoalescent);
    }
}

