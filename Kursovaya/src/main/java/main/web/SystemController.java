package main.web;

import main.functional.Controller;
import main.functional.State;
import main.functional.components.Device;
import main.functional.components.Source;
import main.functional.configuration.BufferConfiguration;
import main.functional.configuration.DevicesConfiguration;
import main.functional.configuration.SourcesConfiguration;
import main.functional.configuration.SystemConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/CMO")
public class SystemController {

    private final SourcesConfiguration sourcesConfiguration;
    private final BufferConfiguration bufferConfiguration;
    private final DevicesConfiguration devicesConfiguration;
    private final SystemConfiguration systemConfiguration;
    private final Controller controller;

    public SystemController(SourcesConfiguration sourcesConfiguration, BufferConfiguration bufferConfiguration,
                            DevicesConfiguration devicesConfiguration, SystemConfiguration systemConfiguration, Controller controller) {
        this.sourcesConfiguration = sourcesConfiguration;
        this.bufferConfiguration = bufferConfiguration;
        this.devicesConfiguration = devicesConfiguration;
        this.systemConfiguration = systemConfiguration;
        this.controller = controller;
    }

    @PostMapping(value = "/setSourcesConfiguration", consumes = "application/json")
    public void setSourcesConfiguration(@RequestBody SourcesConfiguration newSourcesConfiguration) {
        sourcesConfiguration.setLambda(newSourcesConfiguration.getLambda());
    }

    @PostMapping(value = "/setBufferConfiguration", consumes = "application/json")
    public void setBufferConfiguration(@RequestBody BufferConfiguration newBufferConfiguration) {
        bufferConfiguration.setMaxSize(newBufferConfiguration.getMaxSize());
    }

    @PostMapping(value = "/setDevicesConfiguration", consumes = "application/json")
    public void setDevicesConfiguration(@RequestBody DevicesConfiguration newDevicesConfiguration) {
        devicesConfiguration.setAlpha(newDevicesConfiguration.getAlpha());
        devicesConfiguration.setBeta(newDevicesConfiguration.getBeta());
    }

    @PostMapping(value = "/setSystemConfiguration", consumes = "application/json")
    public void setSystemConfiguration(@RequestBody SystemConfiguration newSystemConfiguration) {
        systemConfiguration.setNumberOfSources(newSystemConfiguration.getNumberOfSources());
        systemConfiguration.setNumberOfDevices(newSystemConfiguration.getNumberOfDevices());
        systemConfiguration.setSourceMaxGeneratedRequests(newSystemConfiguration.getSourceMaxGeneratedRequests());
    }

    @PostMapping(value = "/init")
    public void init() {
        controller.init();
    }

    @PostMapping(value = "/run")
    public void run() {
        controller.setSourcesConfiguration(sourcesConfiguration);
        controller.setBufferConfiguration(bufferConfiguration);
        controller.setDevicesConfiguration(devicesConfiguration);
        controller.setSystemConfiguration(systemConfiguration);
        controller.run();
    }

    @GetMapping(value = "/getSources")
    public ResponseEntity<List<Source>> getSources() {
        return new ResponseEntity<>(controller.getSources(), HttpStatus.OK);
    }

    @GetMapping(value = "/getDevices")
    public ResponseEntity<List<Device>> getDevices() {
        return new ResponseEntity<>(controller.getDevices(), HttpStatus.OK);
    }

    @GetMapping(value = "/getNextState")
    public ResponseEntity<State> getNextState() {
        return new ResponseEntity<>(controller.getNextState(), HttpStatus.OK);
    }
}
