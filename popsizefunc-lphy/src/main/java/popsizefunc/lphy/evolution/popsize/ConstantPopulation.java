package popsizefunc.lphy.evolution.popsize;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.integration.TrapezoidIntegrator;
import org.apache.commons.math3.analysis.integration.UnivariateIntegrator;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;

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
        UnivariateFunction function = time -> 1 / getTheta(time);
        UnivariateIntegrator integrator = new TrapezoidIntegrator();
        // The number 1000 here represents a very high number of iterations for accuracy.
        return integrator.integrate(1000, function, 0, t);
    }

//    @Override
//    public double getIntensity(double t) {
//        return t / this.N0;
//    }
@Override
public double getInverseIntensity(double x) {
    UnivariateFunction function = time -> getIntensity(time) - x;
    UnivariateSolver solver = new BrentSolver();
    // The range [0, 100] might need to be adjusted depending on the growth model and expected time range.
    return solver.solve(100, function, 0, 100);
}

//    @Override
//    public double getInverseIntensity(double x) {
//        return this.N0 * x;
//    }

    @Override
    public boolean isAnalytical() {
        // 对于这个特定的实现，返回true或false根据实际情况
        return true; // 或者 false，取决于是否是解析的
    }
}
