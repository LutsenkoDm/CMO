package main.functional;

public class State {
    int requestsInBuffer;
    int sourceNumber;
    int deviceNumber;
    boolean isRejected;

    public State(int requestsInBuffer, int sourceNumber, int deviceNumber, boolean isRejected) {
        this.requestsInBuffer = requestsInBuffer;
        this.sourceNumber = sourceNumber;
        this.deviceNumber = deviceNumber;
        this.isRejected = isRejected;
    }

    public int getRequestsInBuffer() {
        return requestsInBuffer;
    }

    public int getSourceNumber() {
        return sourceNumber;
    }

    public int getDeviceNumber() {
        return deviceNumber;
    }

    public boolean getIsRejected() {
        return isRejected;
    }
}
