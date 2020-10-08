package main.functional;

import main.functional.components.Buffer;
import main.functional.components.Device;
import main.functional.components.Request;
import main.functional.components.Source;
import main.functional.configuration.BufferConfiguration;
import main.functional.configuration.DevicesConfiguration;
import main.functional.configuration.SourcesConfiguration;
import main.functional.configuration.SystemConfiguration;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import java.util.TreeSet;

@Component
public class Controller
{
  private SourcesConfiguration sourcesConfiguration;
  private BufferConfiguration bufferConfiguration;
  private DevicesConfiguration devicesConfiguration;
  private SystemConfiguration systemConfiguration;
  private Buffer buffer = new Buffer(0);
  private TreeSet<Request> requests = new TreeSet<>(Comparator.comparing(Request::getBeginTime));
  private ArrayList<Device> devices = new ArrayList<>();

  public Controller(SourcesConfiguration sourcesConfiguration, BufferConfiguration bufferConfiguration,
                    DevicesConfiguration devicesConfiguration, SystemConfiguration systemConfiguration) {
    this.sourcesConfiguration = sourcesConfiguration;
    this.bufferConfiguration  = bufferConfiguration;
    this.devicesConfiguration = devicesConfiguration;
    this.systemConfiguration  = systemConfiguration;
  }

  public void run()
  {
    requests.clear();
    devices.clear();
    buffer.setMaxSize(bufferConfiguration.getMaxSize());
    for (int i = 0; i < systemConfiguration.getNumberOfSources(); i++)
    {
      requests.add(new Source(sourcesConfiguration.getLambda(), i + 1).generateRequest());
    }
    for (int i = 0; i < systemConfiguration.getNumberOfDevices(); i++)
    {
      devices.add(new Device(i + 1, devicesConfiguration.getAlpha(), devicesConfiguration.getBeta()));
    }

    while (requests.first().getSource().getTotalRequests() < systemConfiguration.getSourceMaxGeneratedRequests())
    {
      buffer.add(requests.first());
      Request earliestRequest = buffer.getLast();
      double earliestRequestBeginTime = earliestRequest.getBeginTime();
      requests.remove(earliestRequest);
      requests.add(earliestRequest.getSource().generateRequest());
      for (Device device : devices)
      {
        double deviceReleaseTime = device.getReleaseTime();
        Optional<Request> requestEarlierThenDeviceReleaseTime = buffer.findRequestEarlierThen(deviceReleaseTime);
        if (earliestRequestBeginTime >= deviceReleaseTime && requestEarlierThenDeviceReleaseTime.isEmpty())
        {
          device.addFreeTime(earliestRequestBeginTime - deviceReleaseTime);
          device.setStartTime(earliestRequestBeginTime);
          device.takeRequest(earliestRequest);
          buffer.removeLast();
          break;
        }
        else if (requests.first().getBeginTime() >= deviceReleaseTime && requestEarlierThenDeviceReleaseTime.isPresent())
        {
          device.setStartTime(deviceReleaseTime);
          device.takeRequest(buffer.remove(requestEarlierThenDeviceReleaseTime.get()));
          break;
        }
      }
    }
    Device maxReleaseTimeDevice = devices.stream().max(Comparator.comparing(Device::getReleaseTime)).get();
    for (Device device : devices) {
      device.addFreeTime(maxReleaseTimeDevice.getReleaseTime() - device.getReleaseTime());
      device.setReleaseTime(maxReleaseTimeDevice.getReleaseTime());
    }
  }

  public void printInfo() {
    requests.forEach(request -> System.out.println(
        "source"                     + request.getSource().getNumber() +
        " total requests = "         + request.getSource().getTotalRequests() +
        ", accepted requests = "     + request.getSource().getAcceptedRequests() +
        ", rejected requests = "     + request.getSource().getRejectedRequests() +
        ", rejection probability = " + request.getSource().getRejectionProbability() +
        ", avg time in buffer = "    + request.getSource().getAvgTimeInBuffer() +
        ", avg time in device = "    + request.getSource().getAvgTimeInDevice() +
        ", avg time in system = "    + request.getSource().getAvgTimeInSystem() ));
    devices.forEach(device -> System.out.println("device work time coefficient = " + device.getWorkTimeCoefficient() ));
  }

  public ArrayList<Source> getSources() {
    ArrayList<Source> results = new ArrayList<>();
    requests.forEach(request -> results.add(request.getSource()));
    results.sort(Comparator.comparing(Source::getNumber));
    return results;
  }

  public ArrayList<Device> getDevices() {
        return devices;
    }

  public void printCheckedInfo()
  {
    int i = 0;
    for (Request request : requests)
    {
      if (request.getSource().getRejectionProbability() <= 0.1)
      {
        ++i;
      }
    }
    double sum = 0;
    for (Device device : devices)
    {
      sum += device.getWorkTimeCoefficient();
    }
    if (i == requests.size() && (sum / devices.size() >= 0.9)) {
      System.out.println(getSourcesConfiguration());
      System.out.println(getBufferConfiguration());
      System.out.println(getDevicesConfiguration());
      System.out.println(getSystemConfiguration());
      printInfo();
      System.out.println("-------------------------------------------");
    }
  }


  public SourcesConfiguration getSourcesConfiguration()
  {
    return sourcesConfiguration;
  }

  public void setSourcesConfiguration(SourcesConfiguration sourcesConfiguration)
  {
    this.sourcesConfiguration = sourcesConfiguration;
  }

  public BufferConfiguration getBufferConfiguration()
  {
    return bufferConfiguration;
  }

  public void setBufferConfiguration(BufferConfiguration bufferConfiguration)
  {
    this.bufferConfiguration = bufferConfiguration;
  }

  public DevicesConfiguration getDevicesConfiguration()
  {
    return devicesConfiguration;
  }

  public void setDevicesConfiguration(DevicesConfiguration devicesConfiguration)
  {
    this.devicesConfiguration = devicesConfiguration;
  }

  public SystemConfiguration getSystemConfiguration()
  {
    return systemConfiguration;
  }

  public void setSystemConfiguration(SystemConfiguration systemConfiguration)
  {
    this.systemConfiguration = systemConfiguration;
  }

  public void findOptimalConfiguration() {
    for (int beta = 2; beta < 30; beta++)
    {
      for (int a = 1; a < 30; a++)
      {
        for (int b = 1; b < 20; b++)
        {
          for (int i = 1; i < 25; i++)
          {
            for (int j = 2; j < 25; j++)
            {
              for (int k = 1; k < 30; k++)
              {
                SourcesConfiguration sourcesConfiguration = new SourcesConfiguration(0.1 * k);
                BufferConfiguration bufferConfiguration = new BufferConfiguration(b);
                DevicesConfiguration devicesConfiguration = new DevicesConfiguration(0.1 * a, 0.1 * beta);
                SystemConfiguration systemConfiguration = new SystemConfiguration(i, j, 3000);
                Controller controller = new Controller(sourcesConfiguration, bufferConfiguration, devicesConfiguration, systemConfiguration);
                controller.run();
                controller.printCheckedInfo();
              }
            }
          }
        }
      }
    }
  }
}
