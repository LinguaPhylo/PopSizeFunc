package popsizefunc.lphy.evolution.popsize;

public interface PopulationFunction {

    /**
     * @param t time
     * @return value of theta at time t
     */
    double getTheta(double t);

    /**
     * @param t time
     * @return value of demographic intensity function at time t (x = integral 1/N(s) ds from 0 to t).
     */
    default double getIntensity(double t) {
        return 0;
    }


    /**
     * @param x the coalescent intensity
     * @return value of inverse demographic intensity function
     *         (returns time, needed for simulation of coalescent intervals).
     */

    default double getInverseIntensity(double x) {
        return 0;
    }


}
