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
public class Controller {
    private SourcesConfiguration sourcesConfiguration;
    private BufferConfiguration bufferConfiguration;
    private DevicesConfiguration devicesConfiguration;
    private SystemConfiguration systemConfiguration;
    private final Buffer buffer = new Buffer(0);
    private final TreeSet<Request> requests = new TreeSet<>(Comparator.comparing(Request::getBeginTime));
    private final ArrayList<Device> devices = new ArrayList<>();

    public Controller(SourcesConfiguration sourcesConfiguration, BufferConfiguration bufferConfiguration,
                      DevicesConfiguration devicesConfiguration, SystemConfiguration systemConfiguration) {
        this.sourcesConfiguration = sourcesConfiguration;
        this.bufferConfiguration = bufferConfiguration;
        this.devicesConfiguration = devicesConfiguration;
        this.systemConfiguration = systemConfiguration;
    }

    public void run() {
        init();
        while (requests.first().getSource().getTotalRequests() < systemConfiguration.getSourceMaxGeneratedRequests()) {
            buffer.add(requests.first());
            Request earliestRequest = buffer.getLast();
            double earliestRequestBeginTime = earliestRequest.getBeginTime();
            requests.remove(earliestRequest);
            requests.add(earliestRequest.getSource().generateRequest());
            for (Device device : devices) {
                double deviceReleaseTime = device.getReleaseTime();
                Optional<Request> requestEarlierThenDeviceReleaseTime = buffer.findRequestEarlierThen(deviceReleaseTime);
                if (earliestRequestBeginTime >= deviceReleaseTime && requestEarlierThenDeviceReleaseTime.isEmpty()) {
                    device.addFreeTime(earliestRequestBeginTime - deviceReleaseTime);
                    device.setStartTime(earliestRequestBeginTime);
                    device.takeRequest(earliestRequest);
                    buffer.removeLast();
                    break;
                } else if (requests.first().getBeginTime() >= deviceReleaseTime && requestEarlierThenDeviceReleaseTime.isPresent()) {
                    device.setStartTime(deviceReleaseTime);
                    device.takeRequest(buffer.remove(requestEarlierThenDeviceReleaseTime.get()));
                    break;
                }
            }
        }
        takeAllRequestsFromBufferToDevices();
        Device maxReleaseTimeDevice = devices.stream().max(Comparator.comparing(Device::getReleaseTime)).get();
        for (Device device : devices) {
            device.addFreeTime(maxReleaseTimeDevice.getReleaseTime() - device.getReleaseTime());
            device.setReleaseTime(maxReleaseTimeDevice.getReleaseTime());
        }
    }

    private void takeAllRequestsFromBufferToDevices() {
        while (!buffer.isEmpty()) {
            for (Device device : devices) {
                double lastInBufferRequestBeginTime = buffer.getLast().getBeginTime();
                double deviceReleaseTime = device.getReleaseTime();
                if (lastInBufferRequestBeginTime >= device.getReleaseTime()) {
                    device.addFreeTime(lastInBufferRequestBeginTime - deviceReleaseTime);
                    device.setStartTime(lastInBufferRequestBeginTime);
                } else {
                    device.setStartTime(deviceReleaseTime);
                }
                device.takeRequest(buffer.removeLast());
                break;
            }
        }
    }

    public void init() {
        requests.clear();
        devices.clear();
        buffer.clear();
        buffer.setMaxSize(bufferConfiguration.getMaxSize());
        for (int i = 0; i < systemConfiguration.getNumberOfSources(); i++) {
            requests.add(new Source(i, sourcesConfiguration.getLambda()).generateRequest());
        }
        for (int i = 0; i < systemConfiguration.getNumberOfDevices(); i++) {
            devices.add(new Device(i, devicesConfiguration.getAlpha(), devicesConfiguration.getBeta()));
        }
    }

    public State getNextState() {
        int deviceNumber = -1;
        boolean isRejected = buffer.full();
        buffer.add(requests.first());
        Request earliestRequest = buffer.getLast();
        double earliestRequestBeginTime = earliestRequest.getBeginTime();
        requests.remove(earliestRequest);
        requests.add(earliestRequest.getSource().generateRequest());
        for (Device device : devices) {
            double deviceReleaseTime = device.getReleaseTime();
            Optional<Request> requestEarlierThenDeviceReleaseTime = buffer.findRequestEarlierThen(deviceReleaseTime);
            if (earliestRequestBeginTime >= deviceReleaseTime && requestEarlierThenDeviceReleaseTime.isEmpty()) {
                deviceNumber = device.getNumber();
                device.addFreeTime(earliestRequestBeginTime - deviceReleaseTime);
                device.setStartTime(earliestRequestBeginTime);
                device.takeRequest(earliestRequest);
                buffer.removeLast();
                break;
            } else if (requests.first().getBeginTime() >= deviceReleaseTime && requestEarlierThenDeviceReleaseTime.isPresent()) {
                deviceNumber = device.getNumber();
                device.setStartTime(deviceReleaseTime);
                device.takeRequest(buffer.remove(requestEarlierThenDeviceReleaseTime.get()));
                break;
            }
        }
        return new State(buffer.getNumberOfRequests(), earliestRequest.getSource().getNumber(), deviceNumber, isRejected);
    }

    public void printInfo() {
        requests.forEach(request -> System.out.println(
                "source" + request.getSource().getNumber() +
                        " total requests = " + request.getSource().getTotalRequests() +
                        ", accepted requests = " + request.getSource().getAcceptedRequests() +
                        ", rejected requests = " + request.getSource().getRejectedRequests() +
                        ", rejection probability = " + request.getSource().getRejectionProbability() +
                        ", avg time in buffer = " + request.getSource().getAvgTimeInBuffer() +
                        ", avg time in device = " + request.getSource().getAvgTimeInDevice() +
                        ", avg time in system = " + request.getSource().getAvgTimeInSystem()));
        devices.forEach(device -> System.out.println("device work time coefficient = " + device.getWorkTimeCoefficient()));
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

    public SourcesConfiguration getSourcesConfiguration() {
        return sourcesConfiguration;
    }

    public void setSourcesConfiguration(SourcesConfiguration sourcesConfiguration) {
        this.sourcesConfiguration = sourcesConfiguration;
    }

    public BufferConfiguration getBufferConfiguration() {
        return bufferConfiguration;
    }

    public void setBufferConfiguration(BufferConfiguration bufferConfiguration) {
        this.bufferConfiguration = bufferConfiguration;
    }

    public DevicesConfiguration getDevicesConfiguration() {
        return devicesConfiguration;
    }

    public void setDevicesConfiguration(DevicesConfiguration devicesConfiguration) {
        this.devicesConfiguration = devicesConfiguration;
    }

    public SystemConfiguration getSystemConfiguration() {
        return systemConfiguration;
    }

    public void setSystemConfiguration(SystemConfiguration systemConfiguration) {
        this.systemConfiguration = systemConfiguration;
    }
}
