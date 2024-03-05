package popsizefunc.lphy.evolution.popsize;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.integration.TrapezoidIntegrator;
import org.apache.commons.math3.analysis.integration.UnivariateIntegrator;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;


/**
 * Represents a logistic growth model.
 * This model is defined by the logistic function, which is characterized by an S-shaped growth curve.
 * It is similar to the Gompertz growth model but uses the logistic function parameters:
 * - x0 corresponds to the inflection point of the curve, similar to t50 in the Gompertz model, indicating the time at which the population reaches half of its carrying capacity.
 * - L is analogous to NInfinity in the Gompertz model, representing the carrying capacity or the maximum achievable population size.
 * - k is related to the growth rate parameter b in the Gompertz model, determining the steepness of the curve.
 */
public class LogisticPopulation implements PopulationFunction{
    private double x0;
    private double L;
    private double k;

    /**
     * Constructs a LogisticPopulation model with specified parameters.
     * @param x0 The midpoint of the logistic function, similar to t50 in the Gompertz model.
     * @param L The carrying capacity or the maximum population size, analogous to NInfinity in the Gompertz model.
     * @param k The growth rate, related to the growth rate b in the Gompertz model.
     */

    public LogisticPopulation(double x0, double L, double k) {
        this.x0 = x0;
        this.L = L;
        this.k = k;
    }

    @Override
    public double getTheta(double t) {
        return L / (1 + Math.exp(-k * (t - x0)));
    }

    @Override
    public double getIntensity(double t) {
        UnivariateFunction function = time -> 1 / getTheta(time);
        UnivariateIntegrator integrator = new TrapezoidIntegrator();
        // The number 10000 here represents a very high number of iterations for accuracy.
        return integrator.integrate(10000, function, 0, t);
    }

    @Override
    public double getInverseIntensity(double x) {
        UnivariateFunction function = time -> getIntensity(time) - x;
        UnivariateSolver solver = new BrentSolver();
        // The range [0, 100] might need to be adjusted depending on the growth model and expected time range.
//        return solver.solve(100, function, 0, 100);
        return solver.solve(100, function, 0.1, 50);
    }

    @Override
    public boolean isAnalytical() {
        return false; //use numerical method here
    }




        public static void main(String[] args) {
            double L = 1;
            double k = 1;
            double x0 = 0;
            double tStart = -10;
            double tEnd = 10;
            int nPoints = 100;

            // Logistic constructor order is (x0, L, k)
            LogisticPopulation logisticPopulation = new LogisticPopulation(x0, L, k);

            try (PrintWriter writer = new PrintWriter(new FileWriter("logistic_data.csv"))) {
                writer.println("time,theta");
                for (int i = 0; i < nPoints; i++) {
                    double t = tStart + (i / (double) (nPoints - 1)) * (tEnd - tStart);
                    double theta = logisticPopulation.getTheta(t);

                    writer.printf(Locale.US, "%.4f,%.4f%n", t, theta);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }




}
