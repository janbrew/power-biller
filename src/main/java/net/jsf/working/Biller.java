package net.jsf.working;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

public class Biller {
    ArrayList<String[]> billData;

    int meterNumber, presentMeterReading;

    public Biller(int meterNumber, int meterReading) {
        this.meterNumber = meterNumber;
        this.presentMeterReading = meterReading;
    }

    public String[] get() {
        billData = new Reader().read();

        Optional<String[]> foundData = billData.stream().filter(x -> Integer.parseInt(x[0]) == this.meterNumber).findFirst();

        if (foundData.isEmpty()) {
            String[] customerTypes = {"residential", "commercial"};
            String customerType = customerTypes[new Random().nextInt(2)];

            String[] newData = {String.valueOf(this.meterNumber), "00000", customerType};
            
            Reader.append(newData);
            
            return newData;
        }
        return foundData.get();
    }

    public int calculateKilowatt(String[] data) {
        if (this.presentMeterReading < Integer.parseInt(data[1])) {
            return (100000 + this.presentMeterReading) - Integer.parseInt(data[1]);
        }
        return this.presentMeterReading - Integer.parseInt(data[1]);
    }

    public double calculatePayAmount(int kilowatt, String[] data) {
        if (data[2].equals("residential")) {    
            if (kilowatt <= 100) {
                return kilowatt * 1.50;
            }
            else if (kilowatt > 100 && kilowatt <= 200) {
                return kilowatt * 2.00;
            }
            return kilowatt * 2.50;
        }
        else {
            if (kilowatt <= 100) {
                return kilowatt * 3.00;
            }
            else if (kilowatt > 100 && kilowatt <= 200) {
                return kilowatt * 4.00;
            }
            return kilowatt * 5.00;
        }
    }
}