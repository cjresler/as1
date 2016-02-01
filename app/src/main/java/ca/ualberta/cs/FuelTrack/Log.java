package ca.ualberta.cs.FuelTrack;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by resler on 1/14/16.
 */
public class Log {
    private Date date;
    private String station;
    private double odometer;
    private String fuel_grade;
    private double amount;
    private double unit_cost;
    private double cost;

    //Initialise all values
    public Log() {
        this.station = "";
        this.date = new Date(System.currentTimeMillis());
        odometer = 0;
        fuel_grade = "";
        amount = 0;
        unit_cost = 0;
        cost = 0;
    }

    public Date getDate() {
        return date;
    }

    //Return string of date
    public String dateToString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    //Given a string, set date to this string formatted properly
    public void setDate(String string) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
        try {
            date = format.parse(string);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public double getOdometer() {
        return odometer;
    }

    public void setOdometer(double odometer) {
        this.odometer = odometer;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getUnit_cost() {
        return unit_cost;
    }

    public void setUnit_cost(double unit_cost) {
        this.unit_cost = unit_cost;
    }

    public String getFuel_grade() {
        return fuel_grade;
    }

    public void setFuel_grade(String fuel_grade) {
        this.fuel_grade = fuel_grade;
    }

    public double getCost() {
        return cost;
    }

    public void updateCost() {
        this.cost = amount * (unit_cost / 100);
    }

    //Takes a Log as a parameter, and sets current Log's information to be equal to that of
    //given Log
    public void updateLog(Log log){
        this.date = log.getDate();
        this.station = log.getStation();
        this.amount = log.getAmount();
        this.odometer = log.getOdometer();
        this.unit_cost = log.getUnit_cost();
        updateCost();
    }

    //Convert Log to string, format display properly
    @Override
    public String toString(){
        updateCost();
        return "Date: " + dateToString() + " | Station: " + station + "\nOdometer: " + String.format("%.1f", odometer) +
                "km | Fuel Grade: " + fuel_grade + "\nFuel Amount: " + String.format("%.3f", amount) + "L | Unit Cost:" +
                String.format("%.1f", unit_cost) + "cents/L | Cost: $" + String.format("%.2f", cost);
    }
}
