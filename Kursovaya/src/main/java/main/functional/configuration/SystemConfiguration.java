package main.functional.configuration;

import org.springframework.stereotype.Component;

@Component
public class SystemConfiguration
{
  private int numberOfSources;
  private int numberOfDevices;
  private int sourceMaxGeneratedRequests;

  public SystemConfiguration()
  {
    this.numberOfSources = 1;
    this.numberOfDevices = 1;
    this.sourceMaxGeneratedRequests = 5000;
  }

  public SystemConfiguration(int numberOfSources, int numberOfDevices, int sourceMaxGeneratedRequests)
  {
    this.numberOfSources = numberOfSources;
    this.numberOfDevices = numberOfDevices;
    this.sourceMaxGeneratedRequests = sourceMaxGeneratedRequests;
  }

  public int getNumberOfSources()
  {
    return numberOfSources;
  }

  public void setNumberOfSources(int numberOfSources)
  {
    this.numberOfSources = numberOfSources;
  }

  public int getNumberOfDevices()
  {
    return numberOfDevices;
  }

  public void setNumberOfDevices(int numberOfDevices)
  {
    this.numberOfDevices = numberOfDevices;
  }

  public int getSourceMaxGeneratedRequests()
  {
    return sourceMaxGeneratedRequests;
  }

  public void setSourceMaxGeneratedRequests(int sourceMaxGeneratedRequests)
  {
    this.sourceMaxGeneratedRequests = sourceMaxGeneratedRequests;
  }

  @Override
  public String toString()
  {
    return "SystemConfiguration{" + "numberOfSources=" + numberOfSources + ", numberOfDevices=" + numberOfDevices + ", sourceMaxGeneratedRequests=" + sourceMaxGeneratedRequests + '}';
  }
}
