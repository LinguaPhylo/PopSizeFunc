package popsizefunc.lphy.evolution.popsize;


import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.integration.RombergIntegrator;
import org.apache.commons.math3.analysis.integration.SimpsonIntegrator;
import org.apache.commons.math3.analysis.integration.UnivariateIntegrator;

public class SpecialFunctions {
    public static double exponentialIntegralEi(double x) {
        if (x <= 0) {
            throw new IllegalArgumentException("Ei(x) is undefined for x <= 0.");
        }
//        double relativeAccuracy = 1.0e-12;
//        double absoluteAccuracy = 1.0e-12;
//        int minimalIterationCount = 10;
//        int maximalIterationCount = 60;

        UnivariateFunction function = t -> Math.exp(t) / t;

        UnivariateIntegrator integrator = new RombergIntegrator();
        double startIntegrationFrom = 1e-10;
        double result = integrator.integrate(10000, function, startIntegrationFrom, x);

        return result;
    }
    }

