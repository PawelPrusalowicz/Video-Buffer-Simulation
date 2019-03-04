package sample;

import javafx.scene.chart.XYChart;

import java.util.ArrayList;
import java.util.Random;

public class Simulation {

    XYChart.Series <Double,Double> bandwidthSeries = new XYChart.Series<>();
    XYChart.Series <Double,Double> bufferSeries = new XYChart.Series<>();
    XYChart.Series <Double,Double> packagesSeries = new XYChart.Series<>();

    ArrayList<Package> previousPackages;

    double fragmentLength;

    double simulationLength;

    Simulation() {

        double currentTime = 0;
        simulationLength = 150;
        double timeToBandwidthChange = generateBandwidthChangeTime();
        double maxBuffer = 30;
        double bufferMinSize = maxBuffer/2;

        Buffer buffer = new Buffer(maxBuffer);
        Band band = new Band();

        previousPackages = new ArrayList<Package>(3);


        bandwidthSeries.getData().add(new XYChart.Data<Double, Double>( currentTime, band.getWidth()));
        bufferSeries.getData().add( new XYChart.Data<Double, Double>(currentTime,  buffer.getSize()));


        Package currentPackage = new Package();
        previousPackages.add(currentPackage);
        changeQuality(buffer,band, band.getWidth(), currentPackage, bufferMinSize);

        fragmentLength = currentPackage.getFragmentLength();


        double downloadedSize = 0;
        double previousPackageDownloadTime = 0;

        while (currentTime < simulationLength) {

            double currentPackageDownloadTime;
            double maximalBandwidth;

            // if buffer is reached, minimize the bandwidth to the playback speed
            if( buffer.getSize() >= maxBuffer){
                maximalBandwidth = Math.min(currentPackage.getPlaybackSpeed(), band.getWidth());
                band. setWidth(maximalBandwidth);
            }
            else{
                maximalBandwidth = band.getWidth();
            }

            currentPackageDownloadTime = currentPackage.getLeftToDownload() / maximalBandwidth;


            double timeToNextEvent = Math.min(currentPackageDownloadTime, timeToBandwidthChange);


            if (timeToNextEvent == timeToBandwidthChange) {

                downloadedSize = timeToNextEvent * band.getWidth();
                currentPackage.setLeftToDownload(currentPackage.getLeftToDownload() - downloadedSize);

                band.changeBandWidth();
                timeToBandwidthChange = generateBandwidthChangeTime();

                double partDownloaded = downloadedSize/ currentPackage.getFragmentSize();

                buffer.setBufferSize( buffer.getSize() + currentPackage.getFragmentLength() * partDownloaded - timeToNextEvent);

            }
            else {

                double lastStreamTime;

                downloadedSize = timeToNextEvent * maximalBandwidth;
                double partDownloaded = downloadedSize/ currentPackage.getFragmentSize();

                // packagesSeries - for the stream graph
                if (packagesSeries.getData().size() > 0){
                lastStreamTime = packagesSeries.getData().get(packagesSeries.getData().size()-1).getXValue();
                }
                else{
                    lastStreamTime = 0;
                }



                if (packagesSeries.getData().size() == 0 ){
                    packagesSeries.getData().add(new XYChart.Data<Double, Double>( currentTime + currentPackageDownloadTime, currentPackage.getFragmentSize()));
                }
                else if ( lastStreamTime - previousPackageDownloadTime < currentTime - buffer.getSize()){
                    packagesSeries.getData().add(new XYChart.Data<Double, Double>( currentTime  + currentPackageDownloadTime, currentPackage.getFragmentSize()));

                }
                else{
                    packagesSeries.getData().add(new XYChart.Data<Double, Double>( lastStreamTime + currentPackage.getFragmentLength(), currentPackage.getFragmentSize()));
                }

                previousPackageDownloadTime = currentPackageDownloadTime;

                currentPackage = new Package();



                double previousQuality = previousPackages.get(0).getFragmentSize();

                boolean changeQuality = true;

                for (Package package1: previousPackages){
                    if (package1.getFragmentSize() != previousQuality){
                        changeQuality = false;
                    }
                }

                if(buffer.getSize() <= maxBuffer/4){
                    changeQuality = true;
                }


                if (changeQuality) {
                    changeQuality(buffer, band, maximalBandwidth, currentPackage, bufferMinSize);
                }
                else {
                    currentPackage.setQuality(previousPackages.get(previousPackages.size()-1).getQuality());
                }


                previousPackages.add(currentPackage);

                if (previousPackages.size() > 3){
                    previousPackages.remove(0);
                }

//                currentTime += timeToNextEvent;

                //buffer.setBufferSize( buffer.getSize() + currentPackage.getFragmentLength() - timeToNextEvent);

                buffer.setBufferSize( buffer.getSize() + currentPackage.getFragmentLength()*partDownloaded - timeToNextEvent);

                timeToBandwidthChange -= timeToNextEvent;

            }

            currentTime += timeToNextEvent;


            bandwidthSeries.getData().add ( new XYChart.Data<Double, Double>( currentTime, band.getWidth()));
            bufferSeries.getData().add (new XYChart.Data<Double, Double>( currentTime, buffer.getSize()));

        }


        simulationLength = Math.ceil( packagesSeries.getData().get(packagesSeries.getData().size() - 1).getXValue() + 4 );
        System.out.println(packagesSeries.getData().size() * currentPackage.getFragmentLength());

    }

    private void changeQuality(Buffer buffer, Band band, double speed, Package currentPackage, double bufferMinSize) {
        double bufferSize = buffer.getSize(); // + currentPackage.getFragmentLeftToDownload();


//        if (bufferSize - currentPackage.getQualityFragmentSize(Quality.HIGH_FullHD) / speed > bufferMinSize){
//            currentPackage.setQuality(Quality.HIGH_FullHD);
//        }
//
//        else if (bufferSize -  currentPackage.getQualityFragmentSize(Quality.MIDDLE_HD) / speed > bufferMinSize*2/3) {
//            currentPackage.setQuality(Quality.MIDDLE_HD);
//        }
//        else {
//            currentPackage.setQuality(Quality.LOW_480p);
//        }


//        if (bufferSize  > bufferMinSize && band.getWidth() >= 2/3*currentPackage.getPlaybackSpeed()){
//            currentPackage.setQuality(Quality.HIGH_FullHD);
//        }
//
//        else if (bufferSize  > (bufferMinSize*2/3) && band.getWidth() >= 2/3*currentPackage.getPlaybackSpeed()) {
//            currentPackage.setQuality(Quality.MIDDLE_HD);
//        }
//        else {
//            currentPackage.setQuality(Quality.LOW_480p);
//        }

        if (bufferSize  > bufferMinSize){
            currentPackage.setQuality(Quality.HIGH_FullHD);
        }

        else if (bufferSize  > (bufferMinSize*2/3) ) {
            currentPackage.setQuality(Quality.MIDDLE_HD);
        }
        else {
            currentPackage.setQuality(Quality.LOW_480p);
        }

    }


    private double generateBandwidthChangeTime() {

        Random generator = new Random();
        double lambda = 0.5;

        return (-1/lambda) * Math.log(generator.nextDouble()%1);
    }


    public XYChart.Series getBandwidthSeries() {
        return bandwidthSeries;
    }

    public XYChart.Series getBufferSeries() {
        return bufferSeries;
    }

    public XYChart.Series<Double, Double> getPackagesSeries() {
        return packagesSeries;
    }

    public double getFragmentLength() {
        return fragmentLength;
    }

    public double getSimulationLength() {
        return simulationLength;
    }
}
