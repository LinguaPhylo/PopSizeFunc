package popsizefunc.lphy.evolution.popsize;

public class GompertzPopulation implements PopulationFunction{
    private double N0;  // Initial population size
    private double b;   // Initial growth rate of tumor growth
    private double NInfinity; // Carrying capacity


    public GompertzPopulation(double N0, double b, double NInfinity) {
        this.N0 = N0;
        this.b = b;
        this.NInfinity = NInfinity;
    }

    @Override
    public double getTheta(double t) {
        // Implement the Gompertz function to calculate theta at time t
        // Assuming theta is proportional to population size for simplicity
        return N0 * Math.exp(Math.log(NInfinity / N0) * (1 - Math.exp(-b * t)));
    }

    @Override
    public double getIntensity(double t) {
        // Intensity function specific to Gompertz growth might require numerical integration
        throw new UnsupportedOperationException("Intensity calculation for Gompertz growth is not implemented.");
    }

    @Override
    public double getInverseIntensity(double x) {
        // The inverse intensity function might be complex and require specific mathematical treatment
        throw new UnsupportedOperationException("Inverse intensity calculation for Gompertz growth is not implemented.");
    }
}





