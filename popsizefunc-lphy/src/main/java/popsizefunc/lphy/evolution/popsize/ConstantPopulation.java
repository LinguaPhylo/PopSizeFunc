package popsizefunc.lphy.evolution.popsize;

public class ConstantPopulation implements PopulationFunction{

    private double N0 = 1.0;

    public ConstantPopulation(double N0) {
        this.N0 = N0;
    }

    @Override
    public double getTheta(double t) {
        return this.N0;
    }

    @Override
    public double getIntensity(double t) {
        throw new UnsupportedOperationException();
    }

    @Override
    public double getInverseIntensity(double x) {
        throw new UnsupportedOperationException();
    }
}
