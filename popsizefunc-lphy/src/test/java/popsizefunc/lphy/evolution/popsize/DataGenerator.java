package popsizefunc.lphy.evolution.popsize;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.integration.TrapezoidIntegrator;
import org.apache.commons.math3.analysis.integration.UnivariateIntegrator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class DataGenerator {
    private static double nCarryingCapacity = 1000;
    private static double b = 0.1;
    private static double t50 = 50;
    private static double resolution_magic_number = 100;

    public static void main(String[] args) {
        String filePath = "intensity_data.csv";
        List<String> lines = new ArrayList<>();
        lines.add("Time,Intensity");

        for (double t = 0; t <= 100; t += 1) {
            double intensity = getIntensity(t);
            lines.add(t + "," + intensity);
        }

        try {

            Files.write(Paths.get(filePath), lines, StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static double getTheta(double t) {
        return nCarryingCapacity / (1 + Math.exp(-b * (t - t50)));
    }

    public static double getIntensity(double t) {
        if (t == 0) return 0;

        if (getTheta(t) < nCarryingCapacity / resolution_magic_number) {
            System.out.println("Theta too small to calculate intensity for t= " + t);
            return Double.NaN;
        }

        UnivariateFunction function = time -> 1 / getTheta(time);
        UnivariateIntegrator integrator = new TrapezoidIntegrator();
        return integrator.integrate(10000, function, 0, t);
    }
}







