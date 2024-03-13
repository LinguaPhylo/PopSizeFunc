package popsizefunc.lphy.evolution.popsize;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LogisticPopulationTest {

    private static final double DELTA = 1e-6;

    /**
     * Test the getTheta method of the LogisticPopulation class.
     * This test verifies whether the getTheta method can correctly calculate theta value based on the logistic growth model.
     */
    @Test
    // Set the parameters of the logistic growth model
    // x0 is the midpoint of the curve, L is the maximum value of the curve (carrying capacity), k is the growth rate
    public void testGetTheta() {
        double x0 = 10;
        double L = 1000;
        double k = 0.1;

        LogisticPopulation logisticPopulation = new LogisticPopulation(x0, L, k);

        // Test getTheta method
        // Choose an x value for testing. Here we choose x0.
        // It is expected that the theta value should be close to L/2 at x0 because x0 is the midpoint of the curve.
        double expectedTheta = L / 2;
        double actualTheta = logisticPopulation.getTheta(x0);

        // Assert that the expected value is equal to the actual value, allowing a certain error range
        assertEquals(expectedTheta, actualTheta, DELTA, "The theta value at x0 should be close to L/2.");
    }
    @Test
    public void testTheta() {

        double x0 = 2;
        double L = 1000;
        double k = 4;
        LogisticPopulation population = new LogisticPopulation(x0, L, k);

        double[] testTimes = {1, 3, 5};

        double[] expectedThetaValues = {
                L / (1 + Math.exp(-k * (1 - x0))), // t = 1
                L / (1 + Math.exp(-k * (3 - x0))), // t = 3
                L / (1 + Math.exp(-k * (5 - x0)))  // t = 5
        };

        for (int i = 0; i < testTimes.length; i++) {
            double actual = population.getTheta(testTimes[i]);
            System.out.printf("Testing getTheta at t = %.1f: Expected = %.3f, Actual = %.3f%n",
                    testTimes[i], expectedThetaValues[i], actual);
            assertEquals(expectedThetaValues[i], actual, 0.001,
                    "getTheta does not return expected value.");
        }
    }


    /**
     * In this example, the getIntensity method calculates the cumulative intensity at a given time point,
     * the getInverseIntensity method calculates the corresponding time given the cumulative intensity value.
     * This test case verifies that the time calculated from the accumulated intensity is close to the original given time point.
     */


        @Test
        public void testIntensityAndInverseIntensity() {
            // Parameters of the logistic growth model
            double t50 = 50;
            double nCarryingCapacity = 1000;
            double b = 0.1; // Assume growth rate b = 4 for this test

            // Initialize the LogisticPopulation with the specified parameters
            LogisticPopulation population = new LogisticPopulation(t50, nCarryingCapacity, b);

            // Print the intensity at time 0 for debugging purposes
            System.out.println("Intensity at 0: " + population.getIntensity(0.0));

            // Test a specific time point, for example, t = 2.0 (which is equal to t50 in this case)
            double t = 0.05;// 0.05;

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
    public void test2() {
        // Parameters of logistic growth model
        double L = 1000;
        double k = 5; // k = 2 passes
        double x0 = 2;

        LogisticPopulation population = new LogisticPopulation(x0, L, k);

        double t = 2;

        // Calculate cumulative intensity at a given point in time
        double intensityAtTimeT = population.getIntensity(t);

        // Use the accumulated intensity value to calculate its corresponding time
        double inverseIntensityResult = population.getInverseIntensity(intensityAtTimeT);

        System.out.println("t = " + t);
        System.out.println("intensity = " + intensityAtTimeT);
        System.out.println("inverse intensity = " + inverseIntensityResult);


        // Verify whether the inverse intensity calculation result is close to the original time point
        assertEquals(t, inverseIntensityResult, DELTA, "Inverse intensity calculation should return the original time point within an acceptable error margin.");
    }



}
