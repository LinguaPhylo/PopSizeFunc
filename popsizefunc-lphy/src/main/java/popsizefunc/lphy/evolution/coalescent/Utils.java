package popsizefunc.lphy.evolution.coalescent;

import popsizefunc.lphy.evolution.popsize.PopulationFunction;

public class Utils {
    /**
     * 计算给定时间点和人口函数下的合并时间间隔。
     *
     * @param U 用于随机化的统一随机变量。
     * @param populationFunction 人口函数，用于计算强度和逆强度。
     * @param lineageCount 当前活跃的系谱数量。
     * @param timeOfLastCoalescent 上一个合并事件的时间。
     * @return 下一个合并事件的时间间隔。
     */
    private static double getInterval(double U, PopulationFunction populationFunction,
                                      int lineageCount, double timeOfLastCoalescent) {
        double intensity = populationFunction.getIntensity(timeOfLastCoalescent);
        double tmp = -Math.log(U) / BinomialUtils.choose2(lineageCount) + intensity;
        return populationFunction.getInverseIntensity(tmp) - timeOfLastCoalescent;
    }

    /**
     * 获取模拟的合并时间间隔。
     */
    public static double getSimulatedInterval(PopulationFunction populationFunction,
                                              int lineageCount, double timeOfLastCoalescent) {
        double U = Math.random(); // 使用Java标准库生成[0, 1)区间的随机数
        return getInterval(U, populationFunction, lineageCount, timeOfLastCoalescent);
    }

    /**
     * 获取中位数合并时间间隔，假设U=0.5。
     */
    public static double getMedianInterval(PopulationFunction populationFunction,
                                           int lineageCount, double timeOfLastCoalescent) {
        return getInterval(0.5, populationFunction, lineageCount, timeOfLastCoalescent);
    }
}

