
import java.util.*;
import java.io.*;

public class WindGen {
    public static void main(String[] args) {
        for (int l = 1500; l <= 1500; l+=250) {


            int numberOfTimeSteps = 20;
            int width = l;
            int height = l;

            FileWriter fileWriter = null;
            try {
                String fileName = "IN" + numberOfTimeSteps + "-" + width + "-" + height + ".txt";
                fileWriter = new FileWriter(fileName);
                PrintWriter printWriter = new PrintWriter(fileWriter);

                printWriter.printf(String.format("%d %d %d%n", numberOfTimeSteps, width, height));

                for (int time = 0; time < numberOfTimeSteps; time++) {
                    for (int x = 0; x < width; x++) {
                        for (int y = 0; y < height; y++) {
                            for (int i = 0; i < 3; i++) {
                                printWriter.printf((String.format("%.6f ", (-1 + Math.random() * 2))).replaceAll(",", "."));
                            }
                        }
                    }
                    printWriter.print("\n");
                }

                fileWriter.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
