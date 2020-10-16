async function stepMode() {
    await setSourceConfiguration()
    await setBufferConfiguration()
    await setDeviceConfiguration()
    await setSystemConfiguration()
    const initResponse = await fetch('/CMO/init', {
        method: 'POST'
    });
    if (initResponse.ok) {
        console.log("Init successfully")
    } else {
        alert("Init failed")
        console.log("Init failed")
    }
    document.getElementById("sources2D").innerHTML = ''
    document.getElementById("buffer").innerHTML = ''
    document.getElementById("devices2D").innerHTML = ''

    let numberOfSources = document.querySelector('#numberOfSources').value
    let numberOfDevices = document.querySelector('#numberOfDevices').value

    let sources2D = d3.select("#sources2D")
    let buffer = d3.select("#buffer")
    let devices2D = d3.select("#devices2D")

    let sourceSvgHeight
    if (numberOfSources > 15) {
        sourceSvgHeight = 450 / 15;
    } else {
        sourceSvgHeight = 450 / numberOfSources;
    }
    let sourceSvgWidth = 100;
    for (let i = 0; i < numberOfSources; i++) {
        let g = sources2D
            .append("svg")
            .attr("width", sourceSvgWidth)
            .attr("height", sourceSvgHeight)
            .append("g")
        g.append("rect")
            .attr("id", "source" + i)
            .attr("width", sourceSvgWidth - 25)
            .attr("height", sourceSvgHeight - 10)
            .style("fill", "blue")
            .style("stroke", "black")
            .style("stroke-width", "3")
            .style("opacity", "0.5")
        g.append("text")
            .attr("x", "37%")
            .attr("y", "32%")
            .attr("text-anchor", "middle")
            .attr("dominant-baseline", "middle")
            .attr("font-size", 10)
            .attr("fill", "black")
            .attr("font-family", "Verdana")
            .text("source" + i);
    }


    let bufferG = buffer
        .append("svg")
        .attr("width", 125)
        .attr("height", 45)
        .attr("class", "position-fixed")
        .style("top", "40%")
        .style("right", "46%")
        .append("g")
    bufferG.append("rect")
        .attr("id", "buffer")
        .attr("width", 100)
        .attr("height", 30)
        .style("fill", "#7FFFD4")
        .style("stroke", "black")
        .style("stroke-width", "3")
        .style("opacity", "0.5")
    bufferG.append("text")
        .attr("id", "bufferText")
        .attr("x", "41%")
        .attr("y", "32%")
        .attr("text-anchor", "middle")
        .attr("dominant-baseline", "middle")
        .attr("font-size", 12)
        .attr("fill", "black")
        .attr("font-family", "Verdana")
        .text("0");

    let rejectionG = buffer
        .append("svg")
        .attr("width", 125)
        .attr("height", 45)
        .attr("class", "position-fixed")
        .style("top", "47%")
        .style("right", "46%")
        .append("g")
    rejectionG.append("rect")
        .attr("id", "rejection")
        .attr("width", 100)
        .attr("height", 30)
        .style("fill", "#9966CC")
        .style("stroke", "black")
        .style("stroke-width", "3")
        .style("opacity", "0.5")
    rejectionG.append("text")
        .attr("x", "40%")
        .attr("y", "32%")
        .attr("text-anchor", "middle")
        .attr("dominant-baseline", "middle")
        .attr("font-size", 12)
        .attr("fill", "black")
        .attr("font-family", "Verdana")
        .text("Rejected");

    let deviceSvgHeight;
    if (numberOfDevices > 15) {
        deviceSvgHeight = 450 / 15;
    } else {
        deviceSvgHeight = 450 / numberOfDevices;
    }
    let deviceSvgWidth = 100;
    for (let i = 0; i < numberOfDevices; i++) {
        let g = devices2D
            .append("svg")
            .attr("width", deviceSvgWidth)
            .attr("height", deviceSvgHeight)
            .append("g")
        g.append("rect")
            .attr("id", "device" + i)
            .attr("width", deviceSvgWidth - 25)
            .attr("height", deviceSvgHeight - 10)
            .style("fill", "yellow")
            .style("stroke", "black")
            .style("stroke-width", "3")
            .style("opacity", "0.5")
        g.append("text")
            .attr("x", "38%")
            .attr("y", "32%")
            .attr("text-anchor", "middle")
            .attr("dominant-baseline", "middle")
            .attr("font-size", 10)
            .attr("fill", "black")
            .attr("font-family", "Verdana")
            .text("device" + i);
    }
}

async function run() {
    await setSourceConfiguration()
    await setBufferConfiguration()
    await setDeviceConfiguration()
    await setSystemConfiguration()
    const runResponse = await fetch('/CMO/run', {
        method: 'POST'
    });
    if (runResponse.ok) {
        location.reload()
        console.log("Run successfully")
    } else {
        alert("Run failed")
        console.log("Run failed")
    }
}

async function setSourceConfiguration() {
    let lambda = document.querySelector('#lambda').value
    let sourceConfigurationJson = await JSON.stringify({
        lambda: lambda
    });
    if (lambda <= 0) {
        alert("Lambda should be more than 0. Last worked or default source configuration will be set")
        return
    }
    const sourceConfigurationResponse = await fetch('/CMO/setSourcesConfiguration', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: sourceConfigurationJson
    });
    if (sourceConfigurationResponse.ok) {
        console.log("Source configuration successfully")
    } else {
        alert("Source configuration failed")
        console.log("Source configuration failed")
    }
}

async function setBufferConfiguration() {
    let maxSize = document.querySelector('#bufferSize').value;
    if (maxSize.indexOf('.') > -1 || maxSize < 1) {
        alert("Buffer max size should be integer. Last worked or default buffer configuration will be set")
        return
    }
    let bufferConfigurationJson = await JSON.stringify({
        maxSize: maxSize
    });
    const bufferConfigurationResponse = await fetch('/CMO/setBufferConfiguration', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: bufferConfigurationJson
    });
    if (bufferConfigurationResponse.ok) {
        console.log("Buffer configuration successfully")
    } else {
        alert("Buffer configuration failed")
        console.log("Buffer configuration failed")
    }
}

async function setDeviceConfiguration() {
    let alpha = document.querySelector('#alpha').value
    let beta = document.querySelector('#beta').value

    if (beta <= alpha) {
        alert("Beta should be more than alpha. Last worked or default device configuration will be set")
        return
    }

    if (alpha <= 0) {
        alert("Alpha should be more than zero. Last worked or default device configuration will be set")
        return
    }

    if (beta <= 0) {
        alert("Beta should be more than zero. Last worked or default device configuration will be set")
        return
    }

    let deviceConfigurationJson = await JSON.stringify({
        alpha: alpha,
        beta: beta
    });
    const deviceConfigurationResponse = await fetch('/CMO/setDevicesConfiguration', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: deviceConfigurationJson
    });
    if (deviceConfigurationResponse.ok) {
        console.log("Device configuration successfully")
    } else {
        alert("Device configuration failed")
        console.log("Device configuration failed")
    }
}

async function setSystemConfiguration() {
    let numberOfSources = document.querySelector('#numberOfSources').value
    let numberOfDevices = document.querySelector('#numberOfDevices').value
    let sourceMaxGeneratedRequests = document.querySelector('#maxGeneratedRequests').value

    if (numberOfSources.indexOf(".") > -1 || numberOfSources < 1) {
        alert("Number of sources should be integer. Last worked or default system configuration will be set")
        return
    }
    if (numberOfDevices.indexOf(".") > -1 || numberOfDevices < 1) {
        alert("Number of devices should be integer Last worked or default system configuration will be set")
        return
    }
    if (sourceMaxGeneratedRequests.indexOf(".") > -1 || sourceMaxGeneratedRequests < 1) {
        alert("Source max generated requests should be integer Last worked or default system configuration will be set")
        return
    }

    let systemConfigurationJson = await JSON.stringify({
        numberOfSources: numberOfSources,
        numberOfDevices: numberOfDevices,
        sourceMaxGeneratedRequests: sourceMaxGeneratedRequests
    });
    const systemConfigurationResponse = await fetch('/CMO/setSystemConfiguration', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: systemConfigurationJson
    });
    if (systemConfigurationResponse.ok) {
        console.log("System Configuration successfully")
    } else {
        alert("System configuration failed")
        console.log("System Configuration failed")
    }
}

let prevSourceNumber = 0;
let prevDeviceNumber = 0;

async function nextState() {
    document.getElementById("source" + prevSourceNumber).style.fill = "blue"
    if (prevDeviceNumber !== -1) {
        document.getElementById("device" + prevDeviceNumber).style.fill = "yellow"
    }
    document.getElementById("rejection").style.fill = "#293133"
    const nextStateResponse = await fetch('/CMO/getNextState', {
        method: 'GET'
    });
    if (nextStateResponse.ok) {
        let nextState = await nextStateResponse.json()
        prevSourceNumber = nextState.sourceNumber
        prevDeviceNumber = nextState.deviceNumber
        document.getElementById("source" + nextState.sourceNumber).style.fill = "green"
        document.getElementById("bufferText").textContent = nextState.requestsInBuffer
        if (nextState.deviceNumber !== -1) {
            document.getElementById("device" + nextState.deviceNumber).style.fill = "green"
        }
        if (nextState.isRejected) {
            document.getElementById("rejection").style.fill = "red"
        }
        console.log("Getting next state successfully")
    } else {
        alert("Getting next state failed")
        console.log("Getting next state failed")
    }
}