package popsizefunc.lphy.evolution.popsize;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.integration.TrapezoidIntegrator;
import org.apache.commons.math3.analysis.integration.UnivariateIntegrator;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ConstantPopulation implements PopulationFunction{

    private double N0 = 60.0 ;

    public ConstantPopulation(double N0) {
        this.N0 = N0;
    }

    @Override
    public double getTheta(double t) {
        return this.N0;
    }
    public String startValue;
    public String endValue;


@Override
public double getIntensity(double t) {
    if (t == 0) {
        return 0;
    }
    UnivariateFunction function = time -> 1 / getTheta(time);
    UnivariateIntegrator integrator = new TrapezoidIntegrator();
    return integrator.integrate(10000, function, 0, t);
}

    @Override
    public double getInverseIntensity(double x) {
        UnivariateFunction function = time -> getIntensity(time) - x;
        UnivariateSolver solver = new BrentSolver();
        double startValue = function.value(0);
        double endValue = function.value(100);
        System.out.println("Function value at start of the interval (0): " + startValue);
        System.out.println("Function value at end of the interval (100): " + endValue);

        // Check whether the function values at both ends of the interval have changed signs
        if (startValue * endValue > 0) {
            // If there is no sign change, try adjusting the interval or use an alternative method
            System.out.println("No sign change detected, adjusting the interval or using alternative methods.");


            // For example, gradually expand the interval and try to find the point where the sign changes
            double adjustedEnd = 100;
            while (adjustedEnd <= 1000 && function.value(adjustedEnd) * startValue > 0) {
                adjustedEnd *= 2; // Gradually increase the upper bound of the interval
            }
            // If adjustedEnd exceeds a certain threshold, other strategies may need to be considered
            if (adjustedEnd > 1000) {
                throw new RuntimeException("Failed to find a valid interval with a sign change.");
            }
            // Try the solution again using the adjusted interval
            return solver.solve(100, function, 0, adjustedEnd);
        } else {
            // If the function values at both ends of the interval change signs, solve directly in the original interval
            return solver.solve(100, function, 0, 100);
        }

    }

    @Override
    public boolean isAnalytical() {
        return false; //use numerical method here
    }

    public static void main(String[] args) {
        ConstantPopulation population = new ConstantPopulation(50.0);

        try (PrintWriter out = new PrintWriter(new FileWriter("results.csv"))) {

            out.println("time,theta,intensity");

            for (double t = 0; t <= 10; t += 0.1) {
                double theta = population.getTheta(t);
                double intensity = population.getIntensity(t);

                out.printf("%.2f,%.2f,%.2f%n", t, theta, intensity);
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }


//    @Override
//    public double getIntensity(double t) {
//        return t / this.N0;
//    }
//
//
//    @Override
//    public double getInverseIntensity(double x) {
//        return this.N0 * x;
//    }


}
