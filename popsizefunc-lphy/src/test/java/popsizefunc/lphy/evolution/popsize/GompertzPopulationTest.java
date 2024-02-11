package popsizefunc.lphy.evolution.popsize;

import lphy.core.model.Value;
import org.junit.jupiter.api.Test;
import popsizefunc.lphy.evolution.coalescent.GompertzPopulationCoalescent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
        // 提供非null的n值
        Value<Integer> n = new Value<>("n", 2); // 假设有2个叶节点

        // 使用初始化的参数创建GompertzPopulationCoalescent实例
        GompertzPopulationCoalescent coalescent = new GompertzPopulationCoalescent(
                null,
                N0, b, NInfinity, n, null, null);


        assertNotNull(coalescent, "GompertzPopulationCoalescent instance should not be null.");
    }


//    @Test
//    void testSampleProducesValidTree() {
//
//        Value<Double> N0 = new Value<>("N0", 100.0);
//        Value<Double> b = new Value<>("b", 0.01);
//        Value<Double> NInfinity = new Value<>("NInfinity", 500.0);
//        Value<PopulationFunction> popFunc = new Value<>("popFunc", new GompertzPopulation(N0.value(), b.value(), NInfinity.value()));
//
//        // 假设n为2
//        Value<Integer> n = new Value<>("n", 2);
//
//        GompertzPopulationCoalescent coalescent = new GompertzPopulationCoalescent(
//                popFunc, N0, b, NInfinity, n, null, null);
//
//        RandomVariable<TimeTree> result = coalescent.sample();
//
//        assertNotNull(result.value(), "The sampled TimeTree should not be null.");
//
//        assertNotNull(result.value().getRoot(), "The tree should have a root node.");
//
//        assertTrue(result.value().getNodes().size() >= 2, "The tree should have at least two leaves.");
//    }



}






