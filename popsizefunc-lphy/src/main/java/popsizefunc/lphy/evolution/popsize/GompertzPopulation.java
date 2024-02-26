package popsizefunc.lphy.evolution.popsize;

//import Input.Input;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.integration.RombergIntegrator;
import org.apache.commons.math3.analysis.integration.TrapezoidIntegrator;
import org.apache.commons.math3.analysis.integration.UnivariateIntegrator;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;


public class GompertzPopulation implements PopulationFunction{

//    final public Input<Double> N0Input = new Input<>("N0", "Initial population size", 1.0);
//    final public Input<Double> bInput = new Input<>("b", "Initial growth rate of tumor growth", 0.0);
//    final public Input<Double> NInfinityInput = new Input<>("NInfinity", "Carrying capacity", 1000.0);


    private double N0;  // Initial population size
    private double b;   // Initial growth rate of tumor growth
    private double NInfinity; // Carrying capacity
    private double k;


    public GompertzPopulation(double N0, double b, double NInfinity){
        this.N0 = N0;
        this.b = b;
        this.NInfinity = NInfinity;
        this.k = Math.log(NInfinity / N0);
    }

    /**
     * Implement the Gompertz function to calculate theta at time t
     * Assuming theta is proportional to population size for simplicity
     * @param t time
     * @return N0 * Math.exp(Math.log(NInfinity / N0) * (1 - Math.exp(-b * t)))
     */




//        Implement the Gompertz function to calculate theta at time t
//        Assuming theta is proportional to population size for simplicity
//        return N0 * Math.exp(k * (1 - Math.exp(-b * t)));

    //    public double getIntensity(double t) {
//        double exponent = -b * t;
//        double eiArgument = Math.exp(exponent) * this.k;
//        double ei = SpecialFunctions.exponentialIntegralEi(eiArgument);
//        double intensity = -Math.exp(-this.k) * ei / (b * N0);
//        return intensity;
//
//    }

//    public double getInverseIntensity(double x) {
//        UnivariateFunction function = new UnivariateFunction() {
//            @Override
//            public double value(double time) {
//                return getIntensity(time) - x;
//            }
//        };
//
//        UnivariateSolver solver = new BrentSolver();
//        return solver.solve(100, function, 0, 100); // Adjust the range [0, 100] as necessary
//    }
    @Override
    public double getTheta(double t) {
        return N0 * Math.exp(Math.log(NInfinity / N0) * (1 - Math.exp(-b * t)));
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
        return solver.solve(100, function, 0, 100);
    }

    /**
    use numerical method here, return false
     */
    @Override
    public boolean isAnalytical() {
        return false; //use numerical method here
    }


        public static void main(String[] args) {
            double N0 = 100;
            double b = 0.1;
            double NInfinity = 100000;
            double tStart = 0;
            double tEnd = 50;
            int nPoints = 100;

            GompertzPopulation gompertzPopulation = new GompertzPopulation(N0, b, NInfinity);

            try (PrintWriter writer = new PrintWriter(new FileWriter("gompertzpop_data.csv"))) {
                writer.println("time,theta");
                for (int i = 0; i < nPoints; i++) {
                    double t = tStart + (i / (double)(nPoints - 1)) * (tEnd - tStart);
                    double x = tEnd - t;
                    double theta = gompertzPopulation.getTheta(t);

                    writer.printf(Locale.US, "%.4f,%.4f%n", x, theta);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }







