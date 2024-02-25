package popsizefunc.lphy.evolution.popsize;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.integration.TrapezoidIntegrator;
import org.apache.commons.math3.analysis.integration.UnivariateIntegrator;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;

public class ExponentialGrowthModel {

    private double growthRate; // 增长率

    public ExponentialGrowthModel(double growthRate) {
        this.growthRate = growthRate;
    }

    /**
     * 计算给定时间t的增长值。
     */
    public double getTheta(double t) {
        return Math.exp(growthRate * t);
    }

    /**
     * 计算从0到t时间段内的累积强度。
     */
    public double getIntensity(double t) {
        UnivariateFunction function = time -> 1 / getTheta(time);
        UnivariateIntegrator integrator = new TrapezoidIntegrator();
        return integrator.integrate(10000, function, 0, t);
    }

    /**
     * 根据给定的强度值x找到对应的时间。
     */
    public double getInverseIntensity(double x) {
        UnivariateFunction function = time -> getIntensity(time) - x;
        UnivariateSolver solver = new BrentSolver();
        // 注意：这里的时间范围[0, 100]可能需要根据实际情况调整
        return solver.solve(100, function, 0, 100);
    }
}



