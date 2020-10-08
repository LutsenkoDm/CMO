package main.functional.configuration;

import org.springframework.stereotype.Component;

@Component
public class DevicesConfiguration
{
  private double alpha;
  private double beta;

  public DevicesConfiguration()
  {
    alpha = 1.0;
    beta  = 2.0;
  }

  public DevicesConfiguration(double alpha, double beta)
  {
    this.alpha = alpha;
    this.beta = beta;
  }

  public double getAlpha()
  {
    return alpha;
  }

  public void setAlpha(double alpha)
  {
    this.alpha = alpha;
  }

  public double getBeta()
  {
    return beta;
  }

  public void setBeta(double beta)
  {
    this.beta = beta;
  }

  @Override
  public String toString()
  {
    return "DevicesConfiguration{" + "alpha=" + alpha + ", beta=" + beta + '}';
  }
}
