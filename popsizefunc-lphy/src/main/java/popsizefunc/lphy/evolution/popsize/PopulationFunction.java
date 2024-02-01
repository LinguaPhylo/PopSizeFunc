package popsizefunc.lphy.evolution.popsize;

public interface PopulationFunction {

    double getTheta(double t);

    default double getIntensity(double t) {
        return 0;
    }

    default double getInverseIntensity(double x) {
        return 0;
    }

}
