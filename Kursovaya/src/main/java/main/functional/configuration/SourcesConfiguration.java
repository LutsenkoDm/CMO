package main.functional.configuration;

import org.springframework.stereotype.Component;

@Component
public class SourcesConfiguration
{
  private double lambda;

  public SourcesConfiguration()
  {
    lambda = 1.0;
  }

  public SourcesConfiguration(double lambda)
  {
    this.lambda = lambda;
  }

  public double getLambda()
  {
    return lambda;
  }

  public void setLambda(double lambda)
  {
    this.lambda = lambda;
  }

  @Override
  public String toString()
  {
    return "SourcesConfiguration{" + "lambda=" + lambda + '}';
  }
}
