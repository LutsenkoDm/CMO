async function run() {
    await setSourceConfigurationJson()
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

async function setSourceConfigurationJson() {
    let sourceConfigurationJson = await JSON.stringify({
        lambda: document.querySelector('#lambda').value
    });
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
    let bufferConfigurationJson = await JSON.stringify({
        maxSize: document.querySelector('#bufferSize').value
    });
    //if (bufferConfigurationJson.maxSize.value().contains('.')) alert("maxSize should be integer")
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

    let deviceConfigurationJson = await JSON.stringify({
        alpha: document.querySelector('#alpha').value,
        beta : document.querySelector('#beta').value
    });
    if (deviceConfigurationJson.beta <= deviceConfigurationJson.alpha) alert("alpha should be more than beta")
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
    let systemConfigurationJson = await JSON.stringify({
        numberOfSources           : document.querySelector('#numberOfSources').value,
        numberOfDevices           : document.querySelector('#numberOfDevices').value,
        sourceMaxGeneratedRequests: document.querySelector('#maxGeneratedRequests').value
    });
    //if (systemConfigurationJson.numberOfSources.value().contains('.')) alert("numberOfSources should be integer")
    //if (systemConfigurationJson.numberOfDevices.value().contains('.')) alert("numberOfDevices should be integer")
    //if (systemConfigurationJson.sourceMaxGeneratedRequests.value().contains('.')) alert("sourceMaxGeneratedRequests should be integer")
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
