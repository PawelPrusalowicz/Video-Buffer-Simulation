package sample;

public class Buffer {

    private double size = 0;
    private double maxBuffer;

    Buffer(double maxBuffer){
        this.maxBuffer = maxBuffer;
    }


    public void setBufferSize( double value){

        size = value;

        if (size < 0){
            size = 0;
        }
        else if (size > maxBuffer){
            size = maxBuffer;
        }

    }

    public double getSize() {
        return size;
    }
}
