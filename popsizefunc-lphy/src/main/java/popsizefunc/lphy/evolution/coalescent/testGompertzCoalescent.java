package popsizefunc.lphy.evolution.coalescent;

import lphy.base.distribution.DistributionConstants;
import lphy.base.evolution.Taxa;
import lphy.base.evolution.tree.TaxaConditionedTreeGenerator;
import lphy.base.evolution.tree.TimeTree;
import lphy.base.evolution.tree.TimeTreeNode;
import lphy.core.model.RandomVariable;
import lphy.core.model.Value;
import lphy.core.model.annotation.ParameterInfo;
import popsizefunc.lphy.evolution.popsize.GompertzPopulation;
import popsizefunc.lphy.evolution.popsize.PopulationFunction;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class testGompertzCoalescent extends TaxaConditionedTreeGenerator {
    private Value<PopulationFunction> popFunc;
    private Value<Double> N0;
    private Value<Double> b;
    private Value<Double> NInfinity;
    public static final String PopulationFunctionName = "popFunc";




    public testGompertzCoalescent(//@ParameterInfo(name = PopulationFunctionName, description = "Initial population size.") Value<PopulationFunction> popFunc,
                                  @ParameterInfo(name = CoalescentGompertz.N0ParamName, description = "Initial population size.") Value<Double> N0,
                                  @ParameterInfo(name = CoalescentGompertz.BParamName, description = "Growth rate.") Value<Double> b,
                                  @ParameterInfo(name = CoalescentGompertz.NINFINITYParamName, description = "Carrying capacity.") Value<Double> NInfinity,
                                  @ParameterInfo(name = DistributionConstants.nParamName, description = "number of taxa.", optional = true) Value<Integer> n,
                                  @ParameterInfo(name = TaxaConditionedTreeGenerator.taxaParamName, description = "Taxa object, (e.g. Taxa or Object[])", optional = true) Value<Taxa> taxa,
                                  @ParameterInfo(name = TaxaConditionedTreeGenerator.agesParamName, description = "an array of leaf node ages.", optional = true) Value<Double[]> ages) {
        super(n, taxa, ages);

        //。。。。。。。。。。。。。。。。。。。。。。。。。
        GompertzPopulation gompertzPop = new GompertzPopulation(N0.value(), b.value(), NInfinity.value());

//        this.popFunc = new Value<>(gompertzPop);
//        String gompertzPopId = "gompertzPopulation";
//        this.popFunc = new Value<PopulationFunction>(gompertzPopId, gompertzPop);

        this.popFunc = new Value<PopulationFunction>("gompertzPopulation", gompertzPop);

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


    @Override
    public RandomVariable<TimeTree> sample() {
        TimeTree tree = new TimeTree();
        List<TimeTreeNode> activeNodes = createLeafTaxa(tree);
        double time = 0.0;

        while (activeNodes.size() > 1) {
            int lineageCount = activeNodes.size();

            // 使用 Utils.getSimulatedInterval 方法来计算下一个合并事件的时间间隔
            double interval = Utils.getSimulatedInterval(popFunc.value(), lineageCount, time);

            // 更新当前时间，加上新计算出的时间间隔
            time += interval;

            // 随机选择两个节点进行合并
            TimeTreeNode a = drawRandomNode(activeNodes);
            TimeTreeNode b = drawRandomNode(activeNodes);

            // 创建新的父节点并更新活跃节点列表
            TimeTreeNode parent = new TimeTreeNode(time, new TimeTreeNode[] {a, b});
            activeNodes.add(parent);

            // 从活跃节点列表中移除已经合并的两个节点
            activeNodes.remove(a);
            activeNodes.remove(b);
        }

        // 设置树的根节点
        tree.setRoot(activeNodes.get(0));

        return new RandomVariable<>("\u03C8", tree, this);


    }

    public Map<String, Value> getParams() {
        SortedMap<String, Value> map = new TreeMap<>();
        if (n != null) map.put(DistributionConstants.nParamName, n);
        if (taxaValue != null) map.put(TaxaConditionedTreeGenerator.taxaParamName, taxaValue);
        if (ages != null) map.put(TaxaConditionedTreeGenerator.agesParamName, ages);

        // 添加Gompertz模型参数
        if (N0 != null) map.put(CoalescentGompertz.N0ParamName, N0);
        if (b != null) map.put(CoalescentGompertz.BParamName, b);
        if (NInfinity != null) map.put(CoalescentGompertz.NINFINITYParamName, NInfinity);
        return map;
    }



    @Override
    public void setParam(String paramName, Value value) {
        switch (paramName) {
            case CoalescentGompertz.N0ParamName:
                this.N0 = (Value<Double>) value;
                break;
            case CoalescentGompertz.BParamName:
                this.b = (Value<Double>) value;
                break;
            case CoalescentGompertz.NINFINITYParamName:
                this.NInfinity = (Value<Double>) value;
                break;
            default:
                super.setParam(paramName, value);
                return;
        }

        if (N0 != null && b != null && NInfinity != null) {
            // 如果是，则创建GompertzPopulation实例并更新popFunc
            GompertzPopulation gompertzPop = new GompertzPopulation(N0.value(), b.value(), NInfinity.value());
            this.popFunc = new Value<PopulationFunction>("gompertzPopulation", gompertzPop);
        }
    }


}
