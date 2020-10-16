package main.functional.components;

public class Request {
    private final Source source;
    private final double beginTime;

    public Request(Source source, double beginTime) {
        this.source = source;
        this.beginTime = beginTime;
    }

    public Source getSource() {
        return source;
    }

    public double getBeginTime() {
        return beginTime;
    }
}
