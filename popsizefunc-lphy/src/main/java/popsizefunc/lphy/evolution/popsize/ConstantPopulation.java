package popsizefunc.lphy.evolution.popsize;

public class ConstantPopulation implements PopulationFunction{

    private double N0 = 1.0 ;

    public ConstantPopulation(double N0) {
        this.N0 = N0;
    }

    @Override
    public double getTheta(double t) {
        return this.N0;
    }

    @Override
    public double getIntensity(double t) {
        return t / this.N0;
    }

    @Override
    public double getInverseIntensity(double x) {
        return this.N0 * x;
    }

    @Override
    public boolean isAnalytical() {
        // 对于这个特定的实现，返回true或false根据实际情况
        return true; // 或者 false，取决于是否是解析的
    }
}
