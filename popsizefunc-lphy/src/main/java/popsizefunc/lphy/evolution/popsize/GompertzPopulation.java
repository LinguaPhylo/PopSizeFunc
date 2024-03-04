package popsizefunc.lphy.evolution.popsize;

//import Input.Input;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.integration.RombergIntegrator;
import org.apache.commons.math3.analysis.integration.TrapezoidIntegrator;
import org.apache.commons.math3.analysis.integration.UnivariateIntegrator;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;


public class GompertzPopulation implements PopulationFunction{

    public static final String N0ParamName = "N0";
    public static final String BParamName = "b";
    public static final String NINFINITYParamName = "NInfinity";

    private double N0;  // Initial population size
    private double b;   // Initial growth rate of tumor growth
    private double NInfinity; // Carrying capacity
    private double t50; // time when population is half of carrying capacity

    /**
     *
     * @param t50
     * @param b
     * @param NInfinity
     */
    public GompertzPopulation(double t50, double b, double NInfinity){
        this.b = b;
        this.t50 = t50;
        this.NInfinity = NInfinity;
        // Calculate N0 based on t50, b, and NInfinity
        // N(t50) = NInfinity / 2
        // t50 is a time location given by the user, t50 < 0 means it is in the early exponential phase
        this.N0 = NInfinity * Math.pow(2, -Math.exp(-b * this.t50));
    }


    /**
     * Implement the Gompertz function to calculate theta at time t
     * Assuming theta is proportional to population size for simplicity
     * @param t time, where t > 0 is time in the past
     * @return N0 * Math.exp(Math.log(NInfinity / N0) * (1 - Math.exp(b * t)))
     */
    @Override
    public double getTheta(double t) {
        // the sign of b * t is such that t = 0 is present time and t > 0 is time in the past
        return N0 * Math.exp(Math.log(NInfinity / N0) * (1 - Math.exp(b * t)));
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
        return solver.solve(100, function, 0.001, 100);
    }

    /**
    use numerical method here, return false
     */



//        public static void main(String[] args) {
//            double N0 = 100;
//            double b = 0.1;
//            double NInfinity = 100000;
//            double tStart = 0;
//            double tEnd = 50;
//            int nPoints = 100;
//
//            GompertzPopulation gompertzPopulation = new GompertzPopulation(N0, b, NInfinity);
//
//            try (PrintWriter writer = new PrintWriter(new FileWriter("gompertzpop_data.csv"))) {
//                writer.println("time,theta");
//                for (int i = 0; i < nPoints; i++) {
//                    double t = tStart + (i / (double)(nPoints - 1)) * (tEnd - tStart);
//                    double x = tEnd - t;
//                    double theta = gompertzPopulation.getTheta(t);
//
//                    writer.printf(Locale.US, "%.4f,%.4f%n", x, theta);
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }


    public static void main(String[] args) {
        double t50 = 10;
        double b = 0.1;
        double NInfinity = 1000;
        double tStart = 0;
        double tEnd = 50;
        int nPoints = 100;


        GompertzPopulation gompertzPopulation = new GompertzPopulation(t50, b, NInfinity);

        try (PrintWriter writer = new PrintWriter(new FileWriter("gompertzpopt50_data.csv"))) {
            writer.println("time,theta");
            for (int i = 0; i < nPoints; i++) {
                double t = tStart + (i / (double)(nPoints - 1)) * (tEnd - tStart);
                double theta = gompertzPopulation.getTheta(t);

                writer.printf(Locale.US, "%.4f,%.4f%n", t, theta);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }






    }







