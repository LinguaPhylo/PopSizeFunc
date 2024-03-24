package popsizefunc.lphy.evolution.func;

import lphy.core.model.DeterministicFunction;
import lphy.core.model.Value;
import lphy.core.model.annotation.GeneratorCategory;
import lphy.core.model.annotation.GeneratorInfo;
import lphy.core.model.annotation.ParameterInfo;
import popsizefunc.lphy.evolution.popsize.ExponentialPopulation;
import popsizefunc.lphy.evolution.popsize.PopulationFunction;

public class ExponentialPopSizeFunc extends DeterministicFunction<PopulationFunction> {

    public static final String GROWTH_RATE_PARAM_NAME = "GrowthRate";
    public static final String N0_PARAM_NAME = "N0";
    public ExponentialPopSizeFunc(@ParameterInfo(name = GROWTH_RATE_PARAM_NAME, description = "The growth rate of the population.") Value<Double> GrowthRate,
                                  @ParameterInfo(name = N0_PARAM_NAME, description = "The initial population size.") Value<Double> N0) {
        setParam(GROWTH_RATE_PARAM_NAME, GrowthRate);
        setParam(N0_PARAM_NAME, N0);
    }

    @GeneratorInfo(name="exponential1", narrativeName = "Exponential growth function",
            category = GeneratorCategory.COAL_TREE, examples = {" .lphy" },
            description = "Models population growth using an exponential growth function.")

    @Override
    public Value<PopulationFunction> apply() {
        double GrowthRate = ((Number) getParams().get(GROWTH_RATE_PARAM_NAME).value()).doubleValue();
        double N0 = ((Number) getParams().get(N0_PARAM_NAME).value()).doubleValue();


        PopulationFunction exponentialPopulation = new ExponentialPopulation(GrowthRate, N0);

        return new Value<>(exponentialPopulation, this);
    }


}
