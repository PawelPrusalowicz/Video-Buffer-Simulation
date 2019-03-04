package sample;

import java.util.Random;

public class Band {


    private final double MIN_BANDWIDTH = 0.01;
    private final double MAX_BANDWDIDTH = 12;

    private Random randomGenerator;

    private double width; // Mb/ s

    Band() {

        randomGenerator = new Random();

        changeBandWidth();

    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) { this.width = width; }

    void changeBandWidth(){

        width = MIN_BANDWIDTH + randomGenerator.nextDouble() * (MAX_BANDWDIDTH - MIN_BANDWIDTH);

    }

}
