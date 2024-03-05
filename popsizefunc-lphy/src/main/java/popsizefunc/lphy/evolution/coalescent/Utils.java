package popsizefunc.lphy.evolution.coalescent;

import popsizefunc.lphy.evolution.popsize.PopulationFunction;

public class Utils {

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
        double intensity = populationFunction.getIntensity(timeOfLastCoalescent);
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

    /**
     * 获取中位数合并时间间隔，假设U=0.5。
     */
    public static double getMedianInterval(PopulationFunction populationFunction,
                                           int lineageCount, double timeOfLastCoalescent) {
        return getInterval(0.5, populationFunction, lineageCount, timeOfLastCoalescent);
    }
}

