package main.functional.components;

public class Source {
    private final int number;
    private final double lambda;
    private double timeInBuffer;
    private double timeInDevice;
    private double timeInSystem;
    private int totalRequests;
    private int acceptedRequests;
    private int rejectedRequests;
    private double nextRequestBeginTime;

    public Source(int number, double lambda) {
        totalRequests = -1;
        this.number = number;
        this.lambda = lambda;
    }

    public Request generateRequest() {
        ++totalRequests;
        nextRequestBeginTime += (-1 / lambda) * Math.log(Math.random());
        return new Request(this, nextRequestBeginTime);
    }

    public double getLambda() {
        return lambda;
    }

    public int getNumber() {
        return number;
    }

    public double getAvgTimeInBuffer() {
        return timeInBuffer / totalRequests;
    }

    public double getAvgTimeInDevice() {
        return timeInDevice / totalRequests;
    }

    public double getAvgTimeInSystem() {
        return timeInSystem / totalRequests;
    }

    public int getTotalRequests() {
        return totalRequests;
    }

    public int getAcceptedRequests() {
        return acceptedRequests;
    }

    public int getRejectedRequests() {
        return rejectedRequests;
    }

    public double getRejectionProbability() {
        return (double) rejectedRequests / totalRequests;
    }

    public void incAcceptedRequests() {
        ++acceptedRequests;
    }

    public void incRejectedRequests() {
        ++rejectedRequests;
    }

    public void addTimeInBuffer(double timeInBuffer) {
        this.timeInBuffer += timeInBuffer;
    }

    public void addTimeInDevice(double timeInDevice) {
        this.timeInDevice += timeInDevice;
    }

    public void addTimeInSystem(double timeInSystem) {
        this.timeInSystem += timeInSystem;
    }
}
