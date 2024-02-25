package popsizefunc.lphy.evolution.coalescent;

import lphy.base.distribution.DistributionConstants;
import lphy.base.evolution.Taxa;
import lphy.base.evolution.coalescent.CoalescentConstants;
import lphy.base.evolution.tree.TaxaConditionedTreeGenerator;
import lphy.base.evolution.tree.TimeTree;
import lphy.base.evolution.tree.TimeTreeNode;
import lphy.core.model.RandomVariable;
import lphy.core.model.Value;
import lphy.core.model.annotation.GeneratorCategory;
import lphy.core.model.annotation.GeneratorInfo;
import lphy.core.model.annotation.ParameterInfo;
import popsizefunc.lphy.evolution.popsize.PopulationFunction;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class PopulationFunctionCoalescent extends TaxaConditionedTreeGenerator {
    private Value<PopulationFunction> popFunc;


    public PopulationFunctionCoalescent(@ParameterInfo(name = CoalescentConstants.thetaParamName, narrativeName = "population size function.", description = "the population size.") Value<PopulationFunction> popFunc,
                                        @ParameterInfo(name = DistributionConstants.nParamName, description = "number of taxa.", optional = true) Value<Integer> n,
                                        @ParameterInfo(name = TaxaConditionedTreeGenerator.taxaParamName, description = "Taxa object, (e.g. Taxa or Object[])", optional = true) Value<Taxa> taxa,
                                        @ParameterInfo(name = TaxaConditionedTreeGenerator.agesParamName, description = "an array of leaf node ages.", optional = true) Value<Double[]> ages) {

        super(n, taxa, ages);
        this.popFunc = popFunc;
        this.ages = ages;
        super.checkTaxaParameters(true);
        checkDimensions();
    }

    private void checkDimensions() {
        boolean success = true;
        if (n != null && n.value() != n()) {
            success = false;
        }
        if (ages != null && ages.value().length != n()) {
            success = false;
        }
        if (!success) {
            throw new IllegalArgumentException("The number of theta values must be exactly one less than the number of taxa!");
        }
    }

    @GeneratorInfo(name = "Coalescent", narrativeName = "Kingman's coalescent tree prior",
            category = GeneratorCategory.COAL_TREE, examples = {"https://linguaphylo.github.io/tutorials/time-stamped-data/"},
            description = "The Kingman coalescent with serially sampled data. (Rodrigo and Felsenstein, 1999)")


    public RandomVariable<TimeTree> sample() {
        TimeTree tree = new TimeTree();

        List<TimeTreeNode> activeNodes = createLeafTaxa(tree);

        double time = 0.0;

        while (activeNodes.size() > 1) {
            int lineageCount = activeNodes.size();

            // Use the Utils.getSimulatedInterval method to calculate the time interval for the next coalescent event
//            double interval = Utils.getSimulatedInterval(popFunc.value(), lineageCount, time);

            // 假设popFunc是一个Value<PopulationFunction>类型
            PopulationFunction pf = popFunc.value(); // 获取PopulationFunction实例

            double interval = 0;
            if (pf.isAnalytical()) {

                interval = Utils.getSimulatedInterval(pf, lineageCount, time);
            } else {

                interval = Utils.getNumericalInterval(pf, lineageCount, time);
            }

            // Update the current time, plus the newly calculated time interval
            time += interval;

            // Randomly select two nodes to coalescent
            TimeTreeNode a = drawRandomNode(activeNodes);
            TimeTreeNode b = drawRandomNode(activeNodes);

            // Create a new parent node and update the list of active nodes
            TimeTreeNode parent = new TimeTreeNode(time, new TimeTreeNode[] {a, b});
            activeNodes.add(parent);

            // Remove two coalescent nodes from the active node list
            activeNodes.remove(a);
            activeNodes.remove(b);
        }

        // Set the root node of the tree
        tree.setRoot(activeNodes.get(0));

        return new RandomVariable<>("\u03C8", tree, this);


    }


//    public RandomVariable<TimeTree> sample() {
//        TimeTree tree = new TimeTree();
//
//        List<TimeTreeNode> activeNodes = createLeafTaxa(tree);
//
//        double time = 0.0;
//        double theta = popFunc.value().getTheta(time); // wrong
//
//        while (activeNodes.size() > 1) {
//            int k = activeNodes.size();
//
//            TimeTreeNode a = drawRandomNode(activeNodes);
//            TimeTreeNode b = drawRandomNode(activeNodes);
//
//            double rate = (k * (k - 1.0))/(theta * 2.0);
//
//            // random exponential variate
//            double x = - Math.log(random.nextDouble()) / rate;
//            time += x;
//
//            TimeTreeNode parent = new TimeTreeNode(time, new TimeTreeNode[] {a, b});
//            activeNodes.add(parent);
//        }
//
//        tree.setRoot(activeNodes.get(0));
//
//
//
//        return new RandomVariable<>("\u03C8", tree, this);
//    }

    @Override
    public Map<String, Value> getParams() {
        SortedMap<String, Value> map = new TreeMap<>();
        if (n != null) map.put(DistributionConstants.nParamName, n);
        if (taxaValue != null) map.put(taxaParamName, taxaValue);
        if (ages != null) map.put(agesParamName, ages);
        if (popFunc != null) map.put(agesParamName, popFunc);
        return map;
    }

    @Override
    public void setParam(String paramName, Value value) {
        if (CoalescentConstants.thetaParamName.equals(paramName)) {
            popFunc = value;
            return;
        }

        super.setParam(paramName, value);
    }


}

