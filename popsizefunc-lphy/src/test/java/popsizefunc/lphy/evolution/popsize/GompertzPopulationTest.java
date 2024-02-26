package popsizefunc.lphy.evolution.popsize;

import lphy.core.model.Value;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.integration.RombergIntegrator;
import org.apache.commons.math3.analysis.integration.UnivariateIntegrator;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.junit.jupiter.api.Test;
import popsizefunc.lphy.evolution.coalescent.GompertzPopulationCoalescent;
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


    @Test
    public void testTrapezoidalRule() {
        // 模拟PopulationFunction接口
        PopulationFunction constantPopulation = new ConstantPopulation(1.0);

        // 使用梯形法进行数值积分
        double result = Utils.trapezoidalRule(constantPopulation, 0, 10, 1000);

        // 对于上述情况，期望的积分结果是10（因为强度函数是t/N0，积分区间是0到10，N0是1）
        assertEquals(50.0, result, 0.01); // 允许一定的误差
    }



    //In R :
    // > my_function <- function(x){exp(-x^2)}
    //> integration_result <- integrate(my_function, lower = -Inf, upper = Inf)
    //> expected_value <- integration_result$value
    //> print(expected_value)
    //[1] 1.772453851
    @Test
    public void testTrapezoidalRuleAgainstRExpectedValue() {

        double expectedValue = 1.772453851;
        PopulationFunction myFunction = new PopulationFunction() {
            @Override
            public double getTheta(double t) {
                return 0;
            }

            @Override
            public double getIntensity(double t) {
                return Math.exp(-Math.pow(t, 2));
            }

            @Override
            public boolean isAnalytical() {
                return false;
            }
        };
        double result = Utils.trapezoidalRule(myFunction, -10, 10, 10000); // 注意选择适当的积分区间和分割数

        assertEquals(expectedValue, result, 0.01);
    }

//    @Test
//    public void testExponentialIntegralEiPositive() {
//        double x = 1.0;
//        double expectedResult = 1.895117;
//        double result = SpecialFunctions.exponentialIntegralEi(x);
//        assertEquals( expectedResult, result, 0.0001);
//    }

    @Test
    public void testExponentialIntegralEiNegative() {

        double x = -1.0;
        assertThrows(IllegalArgumentException.class, () -> {
            SpecialFunctions.exponentialIntegralEi(x);
        }, "Ei function should throw IllegalArgumentException for negative input");
    }
    @Test
    public void testExponentialIntegralEiZero() {
        double x = 0;
        assertThrows(IllegalArgumentException.class, () -> {
            SpecialFunctions.exponentialIntegralEi(x);
        }, "Ei(x) is undefined for x <= 0.");
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
        // 初始化 GompertzPopulation 实例
        double N0 = 100; // 初始人口大小
        double b = 0.1; // 增长率
        double NInfinity = 1000; // 最大承载量
        GompertzPopulation population = new GompertzPopulation(N0, b, NInfinity);

        double t = 5; // 给定的时间点

        // 计算给定时间点的累积强度
        double intensityAtTimeT = population.getIntensity(t);

        // 使用累积强度值计算其对应的时间
        double inverseIntensityResult = population.getInverseIntensity(intensityAtTimeT);

        // 验证逆强度计算结果是否接近原始时间点
        // 允许一定的误差范围
        assertEquals(t, inverseIntensityResult, 0.1, "Inverse intensity calculation should return the original time point within an acceptable error margin.");
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
    public void testGetTheta() {
        ExponentialGrowthModel model = new ExponentialGrowthModel(0.1);
        assertEquals(Math.exp(0.1 * 5), model.getTheta(5), 1e-5);
    }

    @Test
    void testGetIntensity() {
        ExponentialGrowthModel model = new ExponentialGrowthModel(0.1);
        double expectedIntensity = 3.9346934028736658;
        double actualIntensity = model.getIntensity(5);
        assertEquals(expectedIntensity, actualIntensity, 1e-5);

    }
    @Test
    void testGetInverseIntensity() {
        ExponentialGrowthModel model = new ExponentialGrowthModel(0.1);
        // Directly use known strength values for testing
        double expectedIntensity = 3.9346934028736658;
        double actualTime = model.getInverseIntensity(expectedIntensity);
        // Verify whether getInverseIntensity can accurately derive the original time point. 0.1 is used as the error range here.
        assertEquals(5, actualTime, 0.1, "The inverse intensity calculation should accurately return the original time point within an acceptable error margin.");
    }





}


//1.89511781636




