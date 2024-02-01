package popsizefunc.lphy.evolution.coalescent;

import lphy.core.model.DeterministicFunction;
import lphy.core.model.Value;
import lphy.core.model.annotation.GeneratorCategory;
import lphy.core.model.annotation.GeneratorInfo;
import popsizefunc.lphy.evolution.popsize.ConstantPopulation;
import popsizefunc.lphy.evolution.popsize.PopulationFunction;

public class ConstantPopSizeFunc extends DeterministicFunction<PopulationFunction> {


    //TODO

    @GeneratorInfo(name="constant", verbClause = "is assumed to come from", narrativeName = "",
            category = GeneratorCategory.COAL_TREE, examples = {" .lphy" },
            description = " ")
    public Value<PopulationFunction> apply() {

        PopulationFunction pf = new ConstantPopulation();

        return new Value<>(null, pf, this);
    }
}
