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
    void testSettingAndGettingParams() {
        Value<Double> N0 = new Value<>("N0", 100.0);
        Value<Double> b = new Value<>("b", 0.01);
        Value<Double> NInfinity = new Value<>("NInfinity", 500.0);
        // Provide a non-null n value
        Value<Integer> n = new Value<>("n", 2); // Assume there are 2 leaf nodes

        //Create a GompertzPopulationCoalescent instance using initialized parameters
        GompertzPopulationCoalescent coalescent = new GompertzPopulationCoalescent(
                null,
                N0, b, NInfinity, n, null, null);


        assertNotNull(coalescent, "GompertzPopulationCoalescent instance should not be null.");
    }


    @Test
    public void testTrapezoidalRule() {

        PopulationFunction constantPopulation = new ConstantPopulation(1.0);

        // Use the trapezoidal method for numerical integration
        double result = Utils.trapezoidalRule(constantPopulation, 0, 10, 1000);


        assertEquals(50.0, result, 0.01);
    }


    private static final double DELTA = 1e-6;

    @Test
    public void testNaturalLogFunction() {
        UnivariateFunction lnFunction = Math::log;
        assertEquals(1, lnFunction.value(Math.E), DELTA);
    }
    @Test
    public void testDerivativeOfNaturalLogFunction() {
        UnivariateFunction derivativeFunction = x -> 1 / x;
        assertEquals(1 / Math.E, derivativeFunction.value(Math.E), DELTA);
    }

    @Test
    public void testIntegralOfNaturalLogFunction() {
        UnivariateFunction exponentialFunction = Math::exp;
        UnivariateIntegrator integrator = new RombergIntegrator();

        double integralResult = integrator.integrate(10000, exponentialFunction, 0, 1);
        assertEquals(Math.E - 1, integralResult, 0.01);
    }

    @Test
    public void testInverseFunction() {
        UnivariateFunction function = x -> Math.exp(x) - 5;
        UnivariateSolver solver = new BrentSolver();

        double lowerBound = 1;
        double upperBound = 3;
        double expectedSolution = Math.log(5);

        // Solve for f(x) = 0, the expected solution is ln(5)
        double solution = solver.solve(100, function, lowerBound, upperBound);
        assertEquals(expectedSolution, solution, 0.01);
    }


    @Test
    public void testIntensityAndInverseIntensity() {

        double N0 = 100;
        double b = 0.1;
        double NInfinity = 1000;
        GompertzPopulation population = new GompertzPopulation(N0, b, NInfinity);

        double t = 5; // given time point

        // Calculate the cumulative intensity at a given time point
        double intensityAtTimeT = population.getIntensity(t);

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





