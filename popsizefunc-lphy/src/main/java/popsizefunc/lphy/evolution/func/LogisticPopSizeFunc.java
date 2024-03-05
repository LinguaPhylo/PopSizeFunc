package popsizefunc.lphy.evolution.func;


import lphy.core.model.DeterministicFunction;
import lphy.core.model.Value;
import lphy.core.model.annotation.GeneratorCategory;
import lphy.core.model.annotation.GeneratorInfo;
import lphy.core.model.annotation.ParameterInfo;
import popsizefunc.lphy.evolution.popsize.LogisticPopulation;
import popsizefunc.lphy.evolution.popsize.PopulationFunction;

public class LogisticPopSizeFunc extends DeterministicFunction<PopulationFunction> {
    // 参数名称定义
    public static final String X0_PARAM_NAME = "x0";
    public static final String L_PARAM_NAME = "L";
    public static final String K_PARAM_NAME = "k";

    public LogisticPopSizeFunc(@ParameterInfo(name = X0_PARAM_NAME, description = "The midpoint of the logistic function.") Value<Double> x0,
                                  @ParameterInfo(name = L_PARAM_NAME, description = "The carrying capacity or the maximum population size.") Value<Double> L,
                                  @ParameterInfo(name = K_PARAM_NAME, description = "The logistic growth rate.") Value<Double> k) {
        setParam(X0_PARAM_NAME, x0);
        setParam(L_PARAM_NAME, L);
        setParam(K_PARAM_NAME, k);
    }

    @GeneratorInfo(name="logistic", narrativeName = "Logistic growth function",
            category = GeneratorCategory.COAL_TREE, examples = {" .lphy" },
            description = "Models population growth using the logistic growth function.")

    @Override
    public Value<PopulationFunction> apply() {
        double x0 = ((Number) getParams().get(X0_PARAM_NAME).value()).doubleValue();
        double L = ((Number) getParams().get(L_PARAM_NAME).value()).doubleValue();
        double k = ((Number) getParams().get(K_PARAM_NAME).value()).doubleValue();

        PopulationFunction logisticPopulation = new LogisticPopulation(x0, L, k);

        return new Value<>(logisticPopulation, this);
    }
}
