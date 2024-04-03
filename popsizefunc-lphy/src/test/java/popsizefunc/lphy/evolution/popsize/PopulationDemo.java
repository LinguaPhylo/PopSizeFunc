package popsizefunc.lphy.evolution.popsize;

import java.io.FileWriter;
import java.io.IOException;

public class PopulationDemo {
    public static void main(String[] args) throws IOException {
//        LogisticPopulation population = new LogisticPopulation(100, 1000, 0.1);
//        double tStart = 0;
//        double tEnd = 500;
//        double deltaT = 1;

        double t50 = 2;
        double f0 = 0.5;
        double b = 0.1;
        double n = 1000;
        //double n0 = n * 0.9; // pick randomly between 0.75 and 0.99?

        //double t50 = GompertzPopulation.computeT50(n, n0, b);

        //GompertzPopulation gPopulation = new GompertzPopulation(t50, b, n);
        GompertzPopulation gPopulation = new GompertzPopulation(f0, b, n);
        double t1 = gPopulation.getTimeForGivenProportion(0.01);
        //double n0 = gPopulation.getNO();

        System.out.println("t1 = " + t1);
        //System.out.println("n0 = " + n0);
        System.out.println("t at n50 = " + gPopulation.getTimeForGivenProportion(0.5));

        writeData(gPopulation, "gompertz");

//        LogisticPopulation lPopulation = new LogisticPopulation(t50,  n, b);
//        writeData(lPopulation, "logistic");

    }

    private static void writeData(PopulationFunction population, String filename) throws IOException {
        double tStart = 0;
        double tEnd = 12;
        double deltaT = 1;

        FileWriter writer = new FileWriter(filename + "_theta_intensity_data.csv");
        try {
            writer.write("Time,Theta,Intensity\n");

            for (double t = tStart; t <= tEnd; t += deltaT) {
                double theta = population.getTheta(t);
                double intensity = population.getIntensity(t);
                writer.write(String.format("%f,%f,%f\n", t, theta, intensity));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writer.close();
        }

    }
}



