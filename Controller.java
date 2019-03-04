package sample;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;

public class Controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label drawGraphLabel;

    @FXML
    private Canvas drawGraphCanvas;





    @FXML
    private AreaChart<Double, Double> areaChartPlayer;

    @FXML
    private NumberAxis areaChartPlayerXAxis;

    @FXML
    private AreaChart<Double, Double> areaChartBandwidth;

    @FXML
    private NumberAxis areaChartBandwidthXAxis;

    @FXML
    private AreaChart<Double, Double> areaChartBuffer;

    @FXML
    private NumberAxis areaChartBufferXAxis;



    @FXML
    void initialize() throws IOException {


        Simulation simulation = new Simulation();

        double simulationLength = simulation.getSimulationLength();

        areaChartBandwidth.getData().addAll(simulation.getBandwidthSeries());
        areaChartBuffer.getData().addAll(simulation.getBufferSeries());


        for( int i = 0; i < simulation.getPackagesSeries().getData().size() ; i++){
            XYChart.Series <Double,Double> tempPackage = new XYChart.Series<>();

            ObservableList<XYChart.Data<Double,Double>> listOfData = tempPackage.getData();




            tempPackage.getData().addAll(new XYChart.Data<Double, Double>( simulation.getPackagesSeries().getData().get(i).getXValue(),
                                         simulation.getPackagesSeries().getData().get(i).getYValue()),
                    new XYChart.Data<Double, Double>( simulation.getPackagesSeries().getData().get(i).getXValue() + simulation.getFragmentLength(),
                             simulation.getPackagesSeries().getData().get(i).getYValue())


                    );


            areaChartPlayer.getData().add(tempPackage);
        }

        areaChartBufferXAxis.setAutoRanging(false);
        areaChartBufferXAxis.setUpperBound(simulationLength);

        areaChartBandwidthXAxis.setAutoRanging(false);
        areaChartBandwidthXAxis.setUpperBound(simulationLength);

        areaChartPlayerXAxis.setAutoRanging(false);
        areaChartPlayerXAxis.setUpperBound(simulationLength);


    }


}

