package popsizefunc.lphy.evolution.func;


import lphy.core.model.DeterministicFunction;
import lphy.core.model.Value;
import lphy.core.model.annotation.GeneratorCategory;
import lphy.core.model.annotation.GeneratorInfo;
import lphy.core.model.annotation.ParameterInfo;
import popsizefunc.lphy.evolution.popsize.GompertzPopulation;
import popsizefunc.lphy.evolution.popsize.PopulationFunction;

import static popsizefunc.lphy.evolution.popsize.GompertzPopulation.*;


public class GompertzPopulationFunc extends DeterministicFunction<PopulationFunction> {
    public GompertzPopulationFunc(@ParameterInfo(name = N0ParamName, description = "Initial population size.") Value<Double> N0,
                                      @ParameterInfo(name = BParamName, description = "Initial growth rate of tumor growth.") Value<Double> b,
                                      @ParameterInfo(name = NINFINITYParamName, description = "Limiting population size (carrying capacity).") Value<Double> NInfinity) {
        setParam(N0ParamName, N0);
        setParam(BParamName, b);
        setParam(NINFINITYParamName, NInfinity);
    }

    @GeneratorInfo(name="gompertz", narrativeName = "Gompertz growth function",
            category = GeneratorCategory.COAL_TREE, examples = {" .lphy" },
            description = "Models population growth using the Gompertz growth function.")

    @Override
    public Value<PopulationFunction> apply() {

        double N0 = ((Number) getParams().get(N0ParamName).value()).doubleValue();
        double b = ((Number) getParams().get(BParamName).value()).doubleValue();
        double NInfinity = ((Number) getParams().get(NINFINITYParamName).value()).doubleValue();

        PopulationFunction gompertzPopulation = new GompertzPopulation(N0, b, NInfinity);

        return new Value<>( gompertzPopulation, this);
    }


}
