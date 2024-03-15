package popsizefunc.lphy.evolution.popsize;

//import Input.Input;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.integration.IterativeLegendreGaussIntegrator;
import org.apache.commons.math3.analysis.integration.RombergIntegrator;

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

    private double resolution_magic_number = 1e4;


    private IterativeLegendreGaussIntegrator createIntegrator() {
        int numberOfPoints = 5; // Legendre-Gauss points
        double relativeAccuracy = 1.0e-10; // relative precision
        double absoluteAccuracy = 1.0e-9; // absolute accuracy
        int minimalIterationCount = 2; // Minimum number of iterations
        int maximalIterationCount = 100000; //Maximum number of iterations, adjust as needed
        return new IterativeLegendreGaussIntegrator(numberOfPoints, relativeAccuracy, absoluteAccuracy, minimalIterationCount, maximalIterationCount);
    }

    /**
     * @param t50
     * @param b
     * @param NInfinity
     */
    public GompertzPopulation(double t50, double b, double NInfinity) {
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
     *
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


    // passes unit test without magic number
// Error when running LPhy script that signs at interval endpoints are not different
//    @Override
//    public double getInverseIntensity(double x) {
////
////
////    UnivariateFunction thetaFunction = time -> getTheta(time) - NInfinity / resolution_magic_number;
////    UnivariateSolver thetaSolver = new BrentSolver();
////    double startValue = thetaFunction.value(0);
////    double endValue = thetaFunction.value(1000);
////
////    System.out.println("Function value at start of the interval (0): " + startValue);
////    System.out.println("Function value at end of the interval (1000): " + endValue);
////    double maxTime = thetaSolver.solve(100, thetaFunction, 1e-6, t50 * 10);
////    System.out.println("maxTime = " + maxTime);
////
////    UnivariateFunction function = time -> getIntensity(time) - x;
////    UnivariateSolver solver = new BrentSolver();
////    // The range [0, 100] might need to be adjusted depending on the growth model and expected time range.
//////        return solver.solve(100, function, 0, 100);
////    return solver.solve(100, function, 0.001, maxTime);
////
////}
//
//






    @Override
    public double getInverseIntensity(double x) {
        double targetIntensity = 100;

        double proportionForT1 = 0.01;
        double t1 = getTimeForGivenProportion(proportionForT1);

        double growthPhaseTime = t1 - t50;
        double deltaTime = growthPhaseTime / 10.0;

        double time = t1;
        double intensity = getIntensity(time);

        while (intensity < targetIntensity) {
            time += deltaTime;
            intensity = getIntensity(time);
        }
        return time;
    }







    //double targetIntensity = 100;
//    double t1 = getTimeForPopSize(NInfinity/100.0);
//    double growthPhaseTime = t1 - t50;
//
//    //double deltaTime = growthPhaseTime/100.0;
//    double time = t1;
//
//    double intensity = getIntensity(time);
//    while (intensity < targetIntensity) {
//        time += deltaTime;
//        intensity = getIntensity(time);
//    }
////    UnivariateFunction function = t -> getIntensity(t) - x;
////    UnivariateSolver solver = new BrentSolver();
////    return solver.solve(100, function, 1e-6, time);
//
////    UnivariateFunction thetaFunction = time -> getTheta(time) - NInfinity / resolution_magic_number;
////    UnivariateSolver thetaSolver = new BrentSolver();
////    double start = 0;
////    double startValue = thetaFunction.value(start);
////    double end = 1000; // Initial end time
////    double endValue = thetaFunction.value(end);
////    while (startValue * endValue > 0 && end < Double.MAX_VALUE / 2) {
////        end *= 2; // Expand the search interval
////        endValue = thetaFunction.value(end);
////        System.out.printf("Current interval: [%f, %f], start value: %f, end value: %f%n", start, end, startValue, endValue);
////    }
////    System.out.printf("Final interval: [%f, %f], start value: %f, end value: %f%n", start, end, startValue, endValue);
////
////    if (startValue * endValue < 0) {
////        // If a valid sign-changing interval is found
////        double maxTime = thetaSolver.solve(100, thetaFunction, start, end);
////        System.out.printf("Brent solver maxTime: %f\n", maxTime);
////        System.out.printf("x = %f\n", x);
////        System.out.printf("intensity(%f) = %f\n", maxTime, getIntensity(maxTime));
////        UnivariateFunction function = time -> getIntensity(time) - x;
////        UnivariateSolver solver = new BrentSolver();
////        return solver.solve(100, function, 0.001, maxTime);
////    } else {
////        // If no valid sign-changing interval is found
////        System.out.printf("Failed to find a valid sign-changing interval. Start value: %f, end value: %f, interval: [%f, %f]%n", startValue, endValue, start, end);
////        throw new RuntimeException("Failed to find a valid sign-changing interval, solution failed.");
////    }
//    }



    public double getTimeForGivenProportion(double k) {
        // Ensure b is not 0 to avoid division by zero
        if (b == 0) {
            throw new IllegalArgumentException("Growth rate b cannot be zero.");
        }

        double ratio = NInfinity / N0;
        double proportion = k * ratio;
        // Ensure proportion is within valid range to avoid taking log of non-positive number
        if (proportion <= 0 || proportion >= ratio) {
            throw new IllegalArgumentException("Proportion must be between 0 and " + ratio);
        }

        // Apply the formula to calculate t*
        double tStar = Math.log(1 - Math.log(proportion) / Math.log(ratio)) / b;
        return tStar;
    }


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

   //  Use the separate method to create the integrator
//    return legrandeIntegrator(function, t);
    IterativeLegendreGaussIntegrator integrator = createIntegrator();
    return integrator.integrate(Integer.MAX_VALUE, function, 0, t);
}


//    return rombergIntegrator(function, t);
//}

private double legrandeIntegrator(UnivariateFunction function, double t) {
    IterativeLegendreGaussIntegrator integrator = createIntegrator();
    return integrator.integrate(Integer.MAX_VALUE, function, 0, t);
}

private double rombergIntegrator(UnivariateFunction function, double t) {

            int maxBound = Integer.MAX_VALUE;
//    int maxBound = 100000;

    RombergIntegrator integrator = new RombergIntegrator();
    double absoluteAccuracy = 1e-6;
    integrator = new RombergIntegrator(RombergIntegrator.DEFAULT_RELATIVE_ACCURACY,
            absoluteAccuracy, RombergIntegrator.DEFAULT_MIN_ITERATIONS_COUNT,
            RombergIntegrator.ROMBERG_MAX_ITERATIONS_COUNT);

//    try {

//        System.out.println("Absolute accuracy = " + integrator.getAbsoluteAccuracy());
//        System.out.println("Relative accuracy = " + integrator.getRelativeAccuracy());
        return integrator.integrate(maxBound, function, 0, t);
//    }
//    catch (Exception e) {
//        System.out.println("Exception during integration: " + e.getMessage());
//        System.out.println("getEvaluations() " + integrator.getEvaluations());
//        return Double.NaN;
//    }
}



//    @Override
//    public double getInverseIntensity(double x) {
//        UnivariateFunction function = time -> getIntensity(time) - x;
//        UnivariateSolver solver = new BrentSolver();
//        double tMin, tMax;
//
//        // Calculate the cumulative intensity at t50 to determine which part of the curve x belongs to
//        double intensityAtT50 = getIntensity(t50);
//
//        if (x <= intensityAtT50) {
//            // If the given x is less than or equal to the cumulative intensity at t50, the search interval is from 0 to t50
//            tMin = 0;
//            tMax = t50;
//        } else {
//            // If the given x is greater than the cumulative intensity at t50, start the search interval from t50 and extend further
//            tMin = t50;
//            tMax = 1.1 * t50; // Initially assume the maximum time as 2*t50
//
//            if (tMax <= 0) {
//                throw new IllegalArgumentException("Calculated tMax is non-positive, which is invalid for the given context.");
//            }
//
//
//            // Dynamically adjust tMax until finding a sufficiently large value such that getIntensity(tMax) <= x
//            System.out.println("initial tMax for getIntensity() is " + tMax);
//            double intensityTMax = getIntensity(tMax);
//            System.out.println("initial getIntensity(" + tMax + ") = " + intensityTMax);
//            System.out.println("x = " + x);
//            while (getIntensity(tMax) < x) {
//                if (tMax < Double.MAX_VALUE / 2) {
//                    tMax += t50/1000.0; // Gradually increase the upper limit of the interval
//                } else {
//                    tMax = Double.MAX_VALUE;
//                    System.out.println("Reached Double.MAX_VALUE when finding tMax"); // This print statement is just for testing purposes
//                    break; // Prevent infinite loop
//                }
//            }
//        }
//
//        // Attempt to solve using the adjusted interval
//        try {
//            System.out.println("tMax at brent solver.solve = " + tMax);
//            return solver.solve(100, function, tMin, tMax);
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to find a valid time for given intensity: " + x, e);
//        }
//    }

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








