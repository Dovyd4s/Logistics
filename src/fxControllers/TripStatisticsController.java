package fxControllers;

import entities.StopPoint;
import entities.Trip;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.text.Text;

import java.net.URL;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class TripStatisticsController implements Initializable, Comparator<StopPoint> {
    public BarChart<String, Double> barChartStatistics;
    public CategoryAxis axisXCategory;
    public NumberAxis axisYNumbers;
    public Text textSummary;
    private Trip trip;
    private XYChart.Series avgSpeedSeries;
    private XYChart.Series avgFuelConsumption;
    private double avgDrivingSpeed;
    private long drivingTime;
    double refueledDuringTrip = 0;
    private int distance;
    private int distanceWithKnownFuelConsumption;


    public void setTrip(Trip trip) {
        this.trip = trip;
        List<StopPoint> stopPointList = trip.getAllRestPoints();
        stopPointList.sort(this);
        StopPoint temp = null;
        StopPoint tempFuel = null;
        int starOdo = 0;
        int destOdo = 0;
        int firstOdoRefuel = 0;
        int lastOdoRefuel = 0;
        for(StopPoint stopPoint : stopPointList){
            if(tempFuel == null){
                if(stopPoint.getVolumeOfDiesel()>0){
                    tempFuel=stopPoint;
                    firstOdoRefuel = tempFuel.getOdometer();
                }
            }
            if(temp == null){
                temp = stopPoint;
                starOdo = temp.getOdometer();
            }else {
                if(!stopPoint.equals(tempFuel) && stopPoint.getVolumeOfDiesel()>0){
                    avgFuelConsumption.getData().add(new XYChart.Data<String, Double>(String.valueOf(stopPoint.getTimeOfArrival()),(stopPoint.getVolumeOfDiesel()/(stopPoint.getOdometer()-tempFuel.getOdometer()))*100));
                    tempFuel=stopPoint;
                    lastOdoRefuel = stopPoint.getOdometer();
                    refueledDuringTrip += stopPoint.getVolumeOfDiesel();
                }
                avgSpeedSeries.getData().add(new XYChart.Data<>(String.valueOf(stopPoint.getTimeOfArrival()), (stopPoint.getOdometer()-temp.getOdometer())/ ChronoUnit.HOURS.between(temp.getTimeOfDeparture(),stopPoint.getTimeOfArrival())));
                drivingTime += ChronoUnit.HOURS.between(temp.getTimeOfDeparture(),stopPoint.getTimeOfArrival());
                temp=stopPoint;
                destOdo=stopPoint.getOdometer();
                distanceWithKnownFuelConsumption = lastOdoRefuel-firstOdoRefuel;
                if(distanceWithKnownFuelConsumption <0){
                    distanceWithKnownFuelConsumption = 0;
                }
            }
        }
        barChartStatistics.getData().add(avgSpeedSeries);
        barChartStatistics.getData().add(avgFuelConsumption);
        distance=destOdo-starOdo;
        fillText();
    }
    private void fillText (){
        String avgFuelCons;
        String totalFuel;
        String avgSpeed = "N/A";
        if(distanceWithKnownFuelConsumption == 0 || refueledDuringTrip == 0){
            avgFuelCons = "N/A";
            totalFuel = "N/A";
        }else{
            avgFuelCons = (refueledDuringTrip/distanceWithKnownFuelConsumption*100) + " l/100km";
            totalFuel = String.valueOf(refueledDuringTrip/distanceWithKnownFuelConsumption*distance)+" l";
        }
        if(drivingTime>0){
            avgSpeed = distance/drivingTime + " km/h";
        }

        textSummary.setText("Distance: " + distance + " km" +
                "\nTotal stops: " + trip.getAllRestPoints().size() +
                "\nAvg. fuel consumption: " +  avgFuelCons +
                "\nEst. Total fuel consumed: " + totalFuel +
                "\nTotal time driving: " + drivingTime + " hours" +
                "\nAvg. driving speed: " + avgSpeed);
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
