package popsizefunc.lphy.spi;

import lphy.base.spi.LPhyBaseImpl;
import lphy.core.model.BasicFunction;
import lphy.core.model.GenerativeDistribution;

import java.util.Arrays;
import java.util.List;

public class PopSizeFuncImpl extends LPhyBaseImpl {

    /**
     * Required by ServiceLoader.
     */
    public PopSizeFuncImpl() {
        //TODO print package or classes info here?
    }

    @Override
    public List<Class<? extends GenerativeDistribution>> declareDistributions() {
        return Arrays.asList( );
    }

    @Override
    public List<Class<? extends BasicFunction>> declareFunctions() {
        return Arrays.asList( );
    }

    public String getExtensionName() {
        return "Population Size Function library";
    }
}

