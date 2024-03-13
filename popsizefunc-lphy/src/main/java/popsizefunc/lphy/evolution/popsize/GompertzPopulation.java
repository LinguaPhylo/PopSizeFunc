package popsizefunc.lphy.evolution.popsize;

//import Input.Input;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.integration.IterativeLegendreGaussIntegrator;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;


public class GompertzPopulation implements PopulationFunction {


    public static final String BParamName = "b";
    public static final String NINFINITYParamName = "NInfinity";
    public static final String T50ParamName = "t50";

    private double N0;  // Initial population size
    private double b;   // Initial growth rate of tumor growth
    private double NInfinity; // Carrying capacity
    private double t50; // time when population is half of carrying capacity

    private double resolution_magic_number = 1e5;

    private IterativeLegendreGaussIntegrator createIntegrator() {
        int numberOfPoints = 5; // Legendre-Gauss points
        double relativeAccuracy = 1.0e-10; // relative precision
        double absoluteAccuracy = 1.0e-9; // absolute accuracy
        int minimalIterationCount = 2; // Minimum number of iterations
        int maximalIterationCount = 10000; //Maximum number of iterations, adjust as needed
        return new IterativeLegendreGaussIntegrator(numberOfPoints, relativeAccuracy, absoluteAccuracy, minimalIterationCount, maximalIterationCount);
    }

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

//    @Override
//    public double getIntensity(double t) {
//
//        if (t == 0) return 0;
//
//        if (getTheta(t) < NInfinity/resolution_magic_number) {
//            throw new RuntimeException("Theta too small to calculate intensity!");
//        }
//
//        UnivariateFunction function = time -> 1.0 / getTheta(time);
//        UnivariateIntegrator integrator = new TrapezoidIntegrator();
//        // The number 10000 here represents a very high number of iterations for accuracy.
//        return integrator.integrate(100000, function, 0, t);
//    }
//
//@Override
//public double getInverseIntensity(double x) {
//
//    UnivariateFunction thetaFunction = time -> getTheta(time) - NInfinity / resolution_magic_number;
//    UnivariateSolver thetaSolver = new BrentSolver();
//    double startValue = thetaFunction.value(0);
//    double endValue = thetaFunction.value(1000);
//
//    System.out.println("Function value at start of the interval (0): " + startValue);
//    System.out.println("Function value at end of the interval (1000): " + endValue);
//    double maxTime = thetaSolver.solve(100, thetaFunction, 1e-6, t50 * 10);
//    System.out.println("maxTime = " + maxTime);
//
//    UnivariateFunction function = time -> getIntensity(time) - x;
//    UnivariateSolver solver = new BrentSolver();
//    // The range [0, 100] might need to be adjusted depending on the growth model and expected time range.
////        return solver.solve(100, function, 0, 100);
//    return solver.solve(100, function, 0.001, maxTime);
//
//}


@Override
public double getIntensity(double t) {
    if (t == 0) return 0;

//    double thetaAtT = getTheta(t);
//
//    if (getTheta(t) < NInfinity / resolution_magic_number) {
//
//        System.out.println("Theta at time " + t + " is too small: " + thetaAtT);
//        throw new RuntimeException("Theta too small to calculate intensity!");
//
//    }
    UnivariateFunction function = time -> 1 / getTheta(time);

    // Use the separate method to create the integrator
    IterativeLegendreGaussIntegrator integrator = createIntegrator();
    return integrator.integrate(Integer.MAX_VALUE, function, 0, t);
}



    @Override
    public double getInverseIntensity(double x) {
        UnivariateFunction function = time -> getIntensity(time) - x;
        UnivariateSolver solver = new BrentSolver();
        double tMin, tMax;

        // Calculate the cumulative intensity at t50 to determine which part of the curve x belongs to
        double intensityAtT50 = getIntensity(t50);

        if (x <= intensityAtT50) {
            // If the given x is less than or equal to the cumulative intensity at t50, the search interval is from 0 to t50
            tMin = 0;
            tMax = t50;
        } else {
            // If the given x is greater than the cumulative intensity at t50, start the search interval from t50 and extend further
            tMin = t50;
            tMax = 2 * t50; // Initially assume the maximum time as 2*t50

            if (tMax <= 0) {
                throw new IllegalArgumentException("Calculated tMax is non-positive, which is invalid for the given context.");
            }


            // Dynamically adjust tMax until finding a sufficiently large value such that getIntensity(tMax) <= x
            while (getIntensity(tMax) < x) {
                if (tMax < Double.MAX_VALUE / 2) {
                    tMax *= 2; // Gradually increase the upper limit of the interval
                } else {
                    tMax = Double.MAX_VALUE;
                    System.out.println("Reached Double.MAX_VALUE when finding tMax"); // This print statement is just for testing purposes
                    break; // Prevent infinite loop
                }
            }
        }

        // Attempt to solve using the adjusted interval
        try {
            return solver.solve(100, function, tMin, tMax);
        } catch (Exception e) {
            throw new RuntimeException("Failed to find a valid time for given intensity: " + x, e);
        }
    }



















    @Override
    public boolean isAnalytical() {
        return false; //use numerical method here
    }




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







