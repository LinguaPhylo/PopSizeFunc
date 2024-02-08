package popsizefunc.lphy.evolution.popsize;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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


}
