//package popsizefunc.lphybeast.spi;
//
//import beast.base.evolution.datatype.DataType;
//import lphy.core.model.Generator;
//import lphy.core.model.Value;
//import lphybeast.GeneratorToBEAST;
//import lphybeast.ValueToBEAST;
//import lphybeast.spi.LPhyBEASTExt;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
///**
// * The "Container" provider class of SPI
// * which include a list of {@link lphybeast.ValueToBEAST},
// * {@link lphybeast.GeneratorToBEAST}, and {@link DataType}
// * to extend.
// * @author Walter Xie
// */
//public class LBPopSizeFunc implements LPhyBEASTExt {
//
//    @Override
//    public List<Class<? extends ValueToBEAST>> getValuesToBEASTs() {
//        return new ArrayList<>();
//    }
//
//    @Override
//    public List<Class<? extends GeneratorToBEAST>> getGeneratorToBEASTs() {
//        return Arrays.asList(   );
//    }
//
//    @Override
//    public List<Class<? extends Generator>> getExcludedGenerator() {
//        return Arrays.asList( );
//    }
//
//    @Override
//    public List<Class<? extends Value>> getExcludedValue() {
//        return new ArrayList<>();
//    }
//
//
//}
