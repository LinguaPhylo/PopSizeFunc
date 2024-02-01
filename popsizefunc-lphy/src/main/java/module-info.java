/**
 * @author Walter Xie
 */
module popsizefunc.lphy {
    requires transitive lphy.core;
    requires transitive lphy.base;

    exports popsizefunc.lphy.evolution.coalescent;
    exports popsizefunc.lphy.evolution.popsize;
    exports popsizefunc.lphy.spi;

    // declare what service interface the provider intends to use
    uses lphy.core.spi.Extension;
    provides lphy.core.spi.Extension with popsizefunc.lphy.spi.PopSizeFuncImpl;
}