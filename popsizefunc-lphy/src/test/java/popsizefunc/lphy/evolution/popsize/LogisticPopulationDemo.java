package popsizefunc.lphy.evolution.popsize;

import java.io.FileWriter;
import java.io.IOException;

public class LogisticPopulationDemo {
    public static void main(String[] args) {
//        LogisticPopulation population = new LogisticPopulation(100, 1000, 0.1);
//        double tStart = 0;
//        double tEnd = 500;
//        double deltaT = 1;

        GompertzPopulation population = new GompertzPopulation(100, 0.1, 1000);
        double tStart = 0;
        double tEnd = 500;
        double deltaT = 1;

        try (FileWriter writer = new FileWriter("population_data.csv for Gomp")) {
            writer.write("Time,Theta,Intensity\n");

            for (double t = tStart; t <= tEnd; t += deltaT) {
                double theta = population.getTheta(t);
                double intensity = population.getIntensity(t);
                writer.write(String.format("%f,%f,%f\n", t, theta, intensity));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



