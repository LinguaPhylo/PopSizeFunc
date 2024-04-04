package popsizefunc.lphy.evolution.func;

import lphy.base.evolution.coalescent.CoalParamNames;
import lphy.core.model.DeterministicFunction;
import lphy.core.model.Value;
import lphy.core.model.annotation.GeneratorCategory;
import lphy.core.model.annotation.GeneratorInfo;
import lphy.core.model.annotation.ParameterInfo;
import popsizefunc.lphy.evolution.popsize.ConstantPopulation;
import popsizefunc.lphy.evolution.popsize.PopulationFunction;

public class ConstantPopSizeFunc extends DeterministicFunction<PopulationFunction> {


    public ConstantPopSizeFunc(@ParameterInfo(name = CoalParamNames.thetaParamName,
            narrativeName = "coalescent parameter",
            description = "effective population size, possibly scaled to mutations or calendar units.") Value<Number> theta) {
        setParam(CoalParamNames.thetaParamName, theta);
    }

    @GeneratorInfo(name="constant", verbClause = "is assumed to come from", narrativeName = "",
            category = GeneratorCategory.COAL_TREE, examples = {" .lphy" },
            description = " ")
    public Value<PopulationFunction> apply() {

        double theta = ((Number) getParams().get(CoalParamNames.thetaParamName).value()).doubleValue();

        PopulationFunction pf = new ConstantPopulation(theta);

        return new Value<>(null, pf, this);
    }
}
