package popsizefunc.lphy.spi;

import lphy.base.spi.LPhyBaseImpl;
import lphy.core.model.BasicFunction;
import lphy.core.model.GenerativeDistribution;
import popsizefunc.lphy.evolution.coalescent.PopulationFunctionCoalescent;
import popsizefunc.lphy.evolution.func.ConstantPopSizeFunc;
import popsizefunc.lphy.evolution.func.ExponentialPopSizeFunc;
import popsizefunc.lphy.evolution.func.GompertzPopulationFunc;
import popsizefunc.lphy.evolution.func.LogisticPopSizeFunc;

import java.util.Arrays;
import java.util.List;

public class PopSizeFuncImpl extends LPhyBaseImpl {

    /**
     * Required by ServiceLoader.
     */
    public PopSizeFuncImpl() {    }

    @Override
    public List<Class<? extends GenerativeDistribution>> declareDistributions() {
        return Arrays.asList(PopulationFunctionCoalescent.class);
    }

    @Override
    public List<Class<? extends BasicFunction>> declareFunctions() {
        return Arrays.asList(ConstantPopSizeFunc.class,
                GompertzPopulationFunc.class,
                ExponentialPopSizeFunc.class,
                LogisticPopSizeFunc.class);
    }

    public String getExtensionName() {
        return "Population Size Function library";
    }
}

