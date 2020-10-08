package main.functional.components;

public class Device
{
  private int    number;
  private double alpha;
  private double beta;
  private double startTime;
  private double releaseTime;
  private double freeTime;

  public Device(int number, double alpha, double beta)
  {
    this.alpha  = alpha;
    this.beta   = beta;
    this.number = number;
  }

  public void takeRequest(Request request) {
    releaseTime = startTime + Math.random() * (beta - alpha) + alpha;
    Source source = request.getSource();
    source.addTimeInBuffer(startTime - request.getBeginTime());
    source.addTimeInDevice(releaseTime - startTime);
    source.addTimeInSystem(releaseTime - request.getBeginTime());
    source.incAcceptedRequests();
  }

  public int getNumber() {
    return number;
  }

  public double getAlpha() {
    return alpha;
  }

  public double getBeta() {
    return beta;
  }

  public double getReleaseTime()
  {
    return releaseTime;
  }

  public void setStartTime(double startTime)
  {
    this.startTime = startTime;
  }

  public void setReleaseTime(double releaseTime) {
    this.releaseTime = releaseTime;
  }

  public void addFreeTime(double freeTime)
  {
    this.freeTime += freeTime;
  }

  public double getWorkTimeCoefficient()
  {
    return 1.0 - (freeTime / releaseTime);
  }
}
