package main.functional.components;

public class Request
{
  private Source source;
  private double beginTime;

  public Request(Source source, double beginTime)
  {
    this.source = source;
    this.beginTime = beginTime;
  }

  public Source getSource()
  {
    return source;
  }

  public double getBeginTime()
  {
    return beginTime;
  }
}
