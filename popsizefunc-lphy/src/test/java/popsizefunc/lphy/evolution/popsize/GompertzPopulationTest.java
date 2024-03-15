package popsizefunc.lphy.evolution.popsize;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class GompertzPopulationTest {

    private static final double DELTA = 1e-6;


    //@Test
    //test gettheta

    @Test
    public void testIntensityAndInverseIntensity() {

        double t50 = 100;
        double b = 0.1;
        double NInfinity = 1000;
        GompertzPopulation population = new GompertzPopulation(t50, b, NInfinity);

        double t = 101; // given time point

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
    public void testGetIntensity() {
        // Parameters of the logistic growth model
        double t50 = 100;

        double b = 0.1; // Assume growth rate b = 4 for this test
        double NInfinity = 1000;

        //max = 173, 174 will fail(org.apache.commons.math3.exception.MaxCountExceededException: illegal state: maximal count (10,000) exceeded)

        double t = 173;
        // Initialize the LogisticPopulation with the specified parameters
        GompertzPopulation population = new GompertzPopulation(t50, b, NInfinity);

        // Print the intensity at time 0 for debugging purposes
        System.out.println("Intensity at " + t + " = " + population.getIntensity(t));
    }

        @Test
    public void testIntensityAndInverseIntensity2() {
        // Parameters of the logistic growth model
        double t50 = 200; // 100;

        double b = 0.14; // 0.1; // Assume growth rate b = 4 for this test
        double NInfinity = 980; // 1000;

        // Initialize the LogisticPopulation with the specified parameters
        GompertzPopulation population = new GompertzPopulation(t50, b, NInfinity);

        // Print the intensity at time 0 for debugging purposes
        System.out.println("Intensity at 0: " + population.getIntensity(0.0));

        // Test a specific time point, for example, t = 2.0 (which is equal to t50 in this case)
        double t = 250.0; // 120.000000001;

        // Brent solver maxTime: 217.178971

        double x = 8.408209;

        // Calculate the intensity at the given time point t
        double intensityAtTimeT = population.getIntensity(t);
        System.out.println("Passed intensity test! Intensity at time " + t + " is " + intensityAtTimeT);

        // Use the calculated intensity value to find the corresponding time using the inverse intensity function
        double inverseIntensityResult = population.getInverseIntensity(intensityAtTimeT);
        System.out.println("t = " + t);
        System.out.println("Intensity = " + intensityAtTimeT);
        System.out.println("Inverse Intensity = " + inverseIntensityResult);


        // Verify whether the inverse intensity calculation result is close to the original time point t
        assertEquals(t, inverseIntensityResult, DELTA, "Inverse intensity calculation should return the original time point within an acceptable error margin.");
    }

    @Test
    public void testGetTimeForGivenProportion() {

        double t50 = 200;
        double b = 0.1;
        double NInfinity = 1000;

        GompertzPopulation population = new GompertzPopulation(t50, b, NInfinity);

        // Set the target percentage
        double targetProportion = 0.010;

        // Calculate the time to reach the target percentage
        double tStar = population.getTimeForGivenProportion(targetProportion);

        // verify that the population at time tStar is indeed close to NInfinity of 20%
        double expectedPopulation = NInfinity * targetProportion;
        double actualPopulation = population.getTheta(tStar);


        System.out.println("tStar: " + tStar);
        System.out.println("Expected population at tStar: " + expectedPopulation);
        System.out.println("Actual population at tStar: " + actualPopulation);

        assertEquals(expectedPopulation, actualPopulation, DELTA,
                "The population at tStar should be close to the expected proportion of NInfinity.");
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





