package fxControllers;

import entities.StopPoint;
import entities.Trip;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.net.URL;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class TripStatisticsController implements Initializable, Comparator<StopPoint> {
    public BarChart<String, Double> barChartStatistics;
    public CategoryAxis axisXCategory;
    public NumberAxis axisYNumbers;
    private Trip trip;
    private XYChart.Series avgSpeedSeries;
    private XYChart.Series avgFuelConsumption;

    public void setTrip(Trip trip) {
        this.trip = trip;
        List<StopPoint> stopPointList = trip.getAllRestPoints();
        stopPointList.sort(this);
        StopPoint temp = null;
        StopPoint tempFuel = null;
        for(StopPoint stopPoint : stopPointList){
            if(tempFuel == null){
                if(stopPoint.getVolumeOfDiesel()>0){
                    tempFuel=stopPoint;
                }
            }
            if(temp == null){
                temp = stopPoint;
            }else {
                if(!stopPoint.equals(tempFuel) && stopPoint.getVolumeOfDiesel()>0){
                    avgFuelConsumption.getData().add(new XYChart.Data<String, Double>(String.valueOf(stopPoint.getTimeOfArrival()),(stopPoint.getVolumeOfDiesel()/(stopPoint.getOdometer()-tempFuel.getOdometer()))*100));
                    tempFuel=stopPoint;
                }
                avgSpeedSeries.getData().add(new XYChart.Data<>(String.valueOf(stopPoint.getTimeOfArrival()), (stopPoint.getOdometer()-temp.getOdometer())/ ChronoUnit.HOURS.between(temp.getTimeOfDeparture(),stopPoint.getTimeOfArrival())));
                temp=stopPoint;
            }
        }
        barChartStatistics.getData().add(avgSpeedSeries);
        barChartStatistics.getData().add(avgFuelConsumption);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        avgSpeedSeries = new XYChart.Series<>();
        avgSpeedSeries.setName("Avg. speed of pure driving. km/h");
        avgFuelConsumption = new XYChart.Series<>();
        avgFuelConsumption.setName("Average fuel consumption l/100km");
    }

    @Override
    public int compare(StopPoint o1, StopPoint o2) {
        return o1.getTimeOfArrival().compareTo(o2.getTimeOfArrival());
    }
}
