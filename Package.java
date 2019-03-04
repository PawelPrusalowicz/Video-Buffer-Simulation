package sample;

enum Quality {
    LOW_480p, MIDDLE_HD, HIGH_FullHD
}


public class Package {


    double FRAGMENTSIZE_480p = 4;
    double FRAGMENTSIZE_HD = 8;
    double FRAGMENTSIZE_FullHD = 12;

    Quality quality = Quality.MIDDLE_HD;

    private double fragmentSize = 3; // Mb
    private double leftToDownload;
    private double fragmentLength = 2; //play seconds
    private double playbackSpeed;


    Package(){

        setQuality(quality);
    }

    public double getFragmentSize() {
        return fragmentSize;
    }

    public double getFragmentLength() { return fragmentLength; }


    public void setLeftToDownload(double leffToDownload) {
        this.leftToDownload = leffToDownload;
    }

    public double getLeftToDownload() {
        return leftToDownload;
    }

    public double getPlaybackSpeed() {
        return playbackSpeed;
    }

    public void setQuality(Quality quality) {
        this.quality = quality;

        switch(quality){
            case LOW_480p:
                fragmentSize = FRAGMENTSIZE_480p;
                break;
            case MIDDLE_HD:
                fragmentSize = FRAGMENTSIZE_HD;
                break;
            case HIGH_FullHD:
                fragmentSize = FRAGMENTSIZE_FullHD;
                break;
        }

        leftToDownload = fragmentSize;
        playbackSpeed = fragmentSize / fragmentLength; // Mb /s
    }

    public Quality getQuality() {
        return quality;
    }

    public double getQualityFragmentSize(Quality quality1) {

        double size = 0;
        switch(quality1) {
            case LOW_480p:
                size = FRAGMENTSIZE_480p;
                break;
            case MIDDLE_HD:
                size = FRAGMENTSIZE_HD;
                break;
            case HIGH_FullHD:
                size = FRAGMENTSIZE_FullHD;
                break;
        }

        return size;

    }

}
