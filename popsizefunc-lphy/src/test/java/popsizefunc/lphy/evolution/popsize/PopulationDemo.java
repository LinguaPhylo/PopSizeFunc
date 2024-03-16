package popsizefunc.lphy.evolution.popsize;

import java.io.FileWriter;
import java.io.IOException;

public class PopulationDemo {
    public static void main(String[] args) throws IOException {
//        LogisticPopulation population = new LogisticPopulation(100, 1000, 0.1);
//        double tStart = 0;
//        double tEnd = 500;
//        double deltaT = 1;

        double t50 = 40;
        double b = 0.0055;
        double n = 1000;

        GompertzPopulation gPopulation = new GompertzPopulation(t50, b, n);
        writeData(gPopulation, "gompertz");

        LogisticPopulation lPopulation = new LogisticPopulation(t50,  n, b);
        writeData(lPopulation, "logistic");

    }

    private static void writeData(PopulationFunction population, String filename) throws IOException {
        double tStart = 0;
        double tEnd = 1000;
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



