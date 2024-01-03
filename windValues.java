/* This particular program will read the windSpeed from the chart of the data
* then use it to calculate the probabilities of the wind with
* respect to their maximum entropy
*/

import java.util.*;
import java.io.*;
public class windValues {
    private List<Double> windSpeed;
    private Bin[] histogram;
    private int interval;

    public windValues(int userDefinedInterval) {
        windSpeed = new ArrayList<Double>();
        this.interval = userDefinedInterval;
        this.histogram = new Bin[1000 / userDefinedInterval];
        for (int i = 0; i < histogram.length; i++) {
            histogram[i] = new Bin((double) (i * userDefinedInterval), 0, 0.0);
        }
    }
    /* the readFile method below will be able to read any file that has the same format
     with the Augspurger_2017_10.csv file with column 6 represent the windSpeed.
     */
    public void readFile(Scanner inputFile) {
        while (inputFile.hasNextLine()) {
            String[] line = inputFile.nextLine().split(",");
            if (line.length > 7) {
                try {
                    windSpeed.add(Double.parseDouble(line[5]));
                    System.out.println("Value: " + Double.parseDouble(line[5]));
                } catch (Exception e) {
                    System.out.println(line[5] + "is not a double");
                }
            }
        }
        inputFile.close();
    }
    /* the method below is crucial since it will calculate the cumulative
    probability of the wind speed by the interval that user would like to define.
     */
    public void makeHistogram() {
        for (int i = 0; i < windSpeed.size(); i++) {
            for (int j = 0; j < histogram.length - 1; j++) {
                double squareWindSpeed = windSpeed.get(i) * windSpeed.get(i);
                if ((squareWindSpeed >= histogram[j].interval) &&
                        (squareWindSpeed < histogram[j + 1].interval)) {
                    histogram[j].count += 1;
                    break;
                }
            }
        }
        double Cumulative_P = 1.0;
        for (int j = 0; j < histogram.length - 1; j++) {
            Cumulative_P = Cumulative_P - ((double) histogram[j].count / windSpeed.size());
            if (Cumulative_P < 0.0) {
                break;
            }
            histogram[j].cumProbability = Cumulative_P;
        }
        try{
            FileWriter fileWriter = new FileWriter("cumuProbability.txt");
            for(int j = 0; j < histogram.length; j++){
                fileWriter.write((j * 1000/ histogram.length) + " " + histogram[j].cumProbability + "\n");
            }
            fileWriter.close();
        } catch(Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }
    /* this is another method that is RegressionAnalysis to compare with the other method
    so that we have another way to compare the probability of distribution.
     */
    public double RegressionAnalysis() {
        double Num = 0.0;
        double Den = 0.0;
        for (int j = 0; j < histogram.length - 1; j++) {
            if (histogram[j].cumProbability <= 0.01) {
                break;
            }
            Num -= Math.log(histogram[j].cumProbability);
            Den += histogram[j + 1].interval;
        }
        double K = Num / Den;
        return K;

    }

    public String toString() {
        String ret = "";
        for (Bin b : histogram) {
            ret += b.interval + " " + b.cumProbability + "\n";
        }
        return ret;
    }

    public static void main(String[] args) {
        Scanner scnr = new Scanner(System.in);
        int interval = -1;

        while (true) {
            System.out.println("Please choose an interval from 50 to 100: ");
            String input = scnr.nextLine();
            try {
                interval = Integer.parseInt(input);
                if (interval < 50 || interval > 100) {
                    System.out.println("Invalid input. Please choose an interval from 50 to 100:  ");
                    continue;

                } else {
                    break;
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please choose an interval from 50 to 100:");
            }
        }
        windValues windChart = new windValues(interval);

        System.out.println("Please enter the name of the file:");
        String file = scnr.nextLine();
        Scanner scanFile = null;
        while (scanFile == null) {
            try {
                scanFile = new Scanner(new File(file));
            } catch (Exception e) {
                System.out.println("Invalid file: " + e.getMessage());
                System.out.println("Please enter the name of the file: ");
                file = scnr.nextLine();
                scanFile = null;
            }
        }
        windChart.readFile(scanFile);
        windChart.makeHistogram();
        double k = windChart.RegressionAnalysis();

        String input = "";
        while (!input.equals("q")) {
            System.out.println("Enter 'less', 'greaterEq', or 'q' to quit");
            input = scnr.nextLine().toLowerCase();
            double velocity = -1;
            switch (input) {
                case "less": {
                    while (velocity < 0) {
                        System.out.println("Enter wind speed: ");
                        try {
                            velocity = Double.parseDouble(scnr.nextLine());
                        } catch (Exception e) {
                            System.out.println("Invalid wind speed: " + e.getMessage());
                            velocity = -1;
                        }
                    }
                    System.out.println("Probability wind speed < " + velocity + " is " + (1.0 - Math.exp(-k * Math.pow(velocity, 2))));
                    break;
                }
                case "greatereq": {
                    while (velocity < 0) {
                        System.out.println("Enter wind speed: ");
                        try {
                            velocity = Double.parseDouble(scnr.nextLine());
                        } catch (Exception e) {
                            System.out.println();
                            System.out.println("Invalid wind speed: " + e.getMessage());
                            velocity = -1;
                        }
                    }
                    System.out.println("Probability wind speed >= " + velocity + " is " + Math.exp(-k * Math.pow(velocity, 2)));
                    break;
                }
                case "q": {
                    break;
                }
                default: {
                    System.out.println("Invalid: " + input);
                }
            }
            try {
                FileWriter fileW = new FileWriter("output.txt");
                fileW.write(windChart + "\n ");
                fileW.close();

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }


        }
    }
}