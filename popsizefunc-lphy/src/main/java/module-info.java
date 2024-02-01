/**
 * @author Walter Xie
 */
module popsizefunc.lphy {
    requires transitive lphy.core;
    requires transitive lphy.base;

//    exports popsizefunc.lphy.evolution.coalescent;


    // declare what service interface the provider intends to use
    uses lphy.core.spi.Extension;
    provides lphy.core.spi.Extension with popsizefunc.lphy.spi.PopSizeFuncImpl;
}