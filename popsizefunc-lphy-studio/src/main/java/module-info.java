module popsizefunc.lphy.studio {

    requires transitive popsizefunc.lphy;
    requires transitive lphystudio;

    // LPhy extensions
    uses lphy.core.spi.Extension;
    // declare what service interface the provider intends to use
    provides lphy.core.spi.Extension with popsizefunc.lphystudio.spi.PopSizeFuncViewerImpl;

}