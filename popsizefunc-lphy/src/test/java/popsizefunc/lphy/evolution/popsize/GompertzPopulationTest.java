package popsizefunc.lphy.evolution.popsize;

import lphy.core.model.Value;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.integration.RombergIntegrator;
import org.apache.commons.math3.analysis.integration.UnivariateIntegrator;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.junit.jupiter.api.Test;
import popsizefunc.lphy.evolution.coalescent.Utils;

import static org.junit.jupiter.api.Assertions.*;


class GompertzPopulationTest {

    private static final double DELTA = 1e-6;

    @Test
    void getTheta() {
        double N0 = 1.0;
        double Ninf = 1000.0;
        double b = 0.5;
        double t = 0.5;
        GompertzPopulation gompertz = new GompertzPopulation(N0, b, Ninf);
        double theta = gompertz.getTheta(t);
        double expectedTheta = 4.608903879;
        double delta = 0.0000001;
        assertEquals(expectedTheta, theta, delta);
    }

    @Test
    void getTheta2() {
        double N0 = 2.0;
        double Ninf = 1000.0;
        double b = 0.5;
        double t = 2.0;
        GompertzPopulation gompertz = new GompertzPopulation(N0, b, Ninf);
        double theta = gompertz.getTheta(t);
        double expectedTheta = 101.6493072;
        double delta = 0.000001;
        assertEquals(expectedTheta, theta, delta);


    }

    @Test
    public void testIntensityAndInverseIntensity() {

        double t50 = 100;
        double b = 0.1;
        double NInfinity = 1000;
        GompertzPopulation population = new GompertzPopulation(t50, b, NInfinity);

        double t = 130; // given time point

        System.out.println("Intensity at 0: " + population.getIntensity(0.0));

        UnivariateFunction function = time -> population.getTheta(time) - NInfinity/1e3;
        UnivariateSolver solver = new BrentSolver();
        // The range [0, 100] might need to be adjusted depending on the growth model and expected time range.
//        return solver.solve(100, function, 0, 100);
        double maxTime = solver.solve(100, function, 1e-6, t50*10);

        System.out.println("Time of theta = NInfinity/1e3 is " + maxTime);

        System.out.println("Theta at time " + t + " is " + population.getTheta(t));
        System.out.println("Theta at time " + t50 + " is " + population.getTheta(t50));

        // Calculate the cumulative intensity at a given time point
        double intensityAtTimeT = population.getIntensity(t);

        System.out.println("Passed intensity test! Intensity at time " + t + " is " + intensityAtTimeT);

        // Use the accumulated intensity value to calculate its corresponding time
        double inverseIntensityResult = population.getInverseIntensity(intensityAtTimeT);

        // Verify whether the inverse intensity calculation result is close to the original time point
        //Allow a certain error range
        assertEquals(t, inverseIntensityResult, DELTA, "Inverse intensity calculation should return the original time point within an acceptable error margin.");
    }



    @Test
    public void testTheta() {
        double N0 = 100;
        double b = 0.1;
        double NInfinity = 1000;
        GompertzPopulation population = new GompertzPopulation(N0, b, NInfinity);

        double t = 5;
        double expectedTheta = N0 * Math.exp(Math.log(NInfinity / N0) * (1 - Math.exp(-b * t)));
        double actualTheta = population.getTheta(t);
        assertEquals(expectedTheta, actualTheta, "The theta calculation does not match the expected result.");
    }

    @Test
    public void testGetIntensityAtZero() {

        double N0 = 100;
        double b = 0.1;
        double NInfinity = 1000;
        PopulationFunction model = new GompertzPopulation(N0, b, NInfinity);
        double intensityAtZero = model.getIntensity(0);
        assertEquals(100, intensityAtZero, 1e-5, "Intensity at t=0 should be N0.");
    }


    @Test
    void testGetThetaAtT50() {
        double t50 = 10;
        double b = 0.1;
        double NInfinity = 1000;

        GompertzPopulation gompertz = new GompertzPopulation(t50, b, NInfinity);

        // Calculate theta value at t50
        double thetaAtT50 = gompertz.getTheta(t50);

        // Verify whether the theta value is close to NInfinity / 2 at time t50
        assertEquals(NInfinity / 2, thetaAtT50, NInfinity * 0.05, "Theta at t50 should be approximately half of NInfinity");
    }

    @Test
    void testGetThetaAtDifferentTimes() {
        double t50 = 10;
        double b = 0.1;
        double NInfinity = 1000;
        GompertzPopulation gompertz = new GompertzPopulation(t50, b, NInfinity);
        assertAll(
                () -> assertTrue(gompertz.getTheta(1) > NInfinity / 2, "Theta at time 0 should be less than NInfinity / 2"),
                () -> assertTrue(gompertz.getTheta(t50) > 0, "Theta at t50 should be greater than 0 and close to NInfinity / 2"),
                () -> assertTrue(gompertz.getTheta(t50) == gompertz.getTheta(t50), "Theta should increase over time")
        );
    }



}





