package com.example.demo;
import java.util.*;
import java.io.*;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;
import javafx.geometry.Insets;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        //Defining the x and the y axes
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        //Setting labels for the axis
        xAxis.setLabel("Wind Speed Squared");
        yAxis.setLabel("Cumulative Probability");
        //Creating a line chart
        LineChart<Number, Number> linechart = new LineChart<Number, Number>(xAxis, yAxis);
        //Preparing the data points for the line1
        XYChart.Series series1 = new XYChart.Series();
        try{
            Scanner scnr;
            File file = new File("cumuProbability.txt");
            scnr = new Scanner(file);
            while(scnr.hasNextLine()){
                String line = scnr.nextLine();
                String[] splitApart = line.split(" ");

                if (splitApart.length == 2){
                    double x = Double.parseDouble(splitApart[0]);
                    double y = Double.parseDouble(splitApart[1]);
                    series1.getData().add(new XYChart.Data(x,y));
                }

            }
        } catch (FileNotFoundException e){
            System.err.println("Error: " + e.getMessage());
        }

        //Preparing the data points for the line2
        XYChart.Series series2 = new XYChart.Series();
        try{
            Scanner scnr;
            File file = new File("output.txt");
            scnr = new Scanner(file);
            while(scnr.hasNextLine()){
                String line = scnr.nextLine();
                String[] splitApart = line.split(" ");

                if (splitApart.length == 2){
                    double x = Double.parseDouble(splitApart[0]);
                    double y = Double.parseDouble(splitApart[1]);
                    series2.getData().add(new XYChart.Data(x,y));
                }

            }
        } catch (FileNotFoundException e){
            System.err.println("Error: " + e.getMessage());
        }

        //Setting the name to the line (series)
        series1.setName("Cumulative Probabilities per Interval");
        series2.setName("OLS");

        //Setting the data to Line chart
        linechart.getData().addAll(series1, series2); //add two lines into a single graph to compare

        //Creating a stack pane to hold the chart
        StackPane pane = new StackPane(linechart);
        pane.setPadding(new Insets(15, 15, 15, 15));
        pane.setStyle("-fx-background-color: WHITE");
        //Setting the Scene
        Scene scene = new Scene(pane, 800, 600);
        stage.setTitle("Line Chart: 2 Lines");
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {
        launch();
    }
}