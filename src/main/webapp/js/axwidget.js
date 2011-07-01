var connected = false;



function weightedMovingAverage(data, length) {
    if(data.length < length)
        return undefined;
    
    var averages = new Array(data.length - length + 1);

    var denominator = length * (length + 1) / 2;
    var numerator = 0, total = 0;

    // compute initial weighting
    var i;
    for(i = length - 1; i >= 0; i--) {
        numerator += (i+1) * data[i][1];
        total += data[i][1];
    }
    averages[0] = [data[i+1][0], numerator / denominator];

    // now the weighted
    var previousTotal, previousNumerator;
    for(i = 1; i < averages.length; i++) {
        previousTotal = total;
        previousNumerator = numerator;

        total = previousTotal + data[i + length - 1][1] - data[i - 1][1];
        numerator = previousNumerator + length * data[i + length - 1][1] - previousTotal;
        averages[i] = [data[i - 1][0], numerator / denominator];
    }
    
    return averages;
}

function randomData(length) {
	var data = new Array(length);
	for(var i = 0; i < length; i++)
		data[i] = 500 - 50 * Math.log(100 * Math.random());
	return data;
}

function indent(depth) {
	var tabs = "";
	for(var i = 0; i < depth; i++)
		tabs = tabs + "  ";
	return tabs;
}

function inspect(object, depth) {
	if(depth == null)
		depth = 0;
	
	if(object instanceof Object) {
		for(var key in object) {
			console.log(indent(depth) + key);
			inspect(object[key], depth+1);
		}
	} else {
		console.log(indent(depth) + object);
	}
}

function createSeries(array) {
	var maparray = [];
	for(var i = 0; i < array.length; i++) {
		maparray[i] = {x:array[i][0], y:array[i][1]};
	}
	
	return maparray;
}

function AxPlot(id) {
	new Dygraph(
		document.getElementById(id+"_plot"),
		"/monitor/data:csv/"+id,
		{
			rollPeriod: 10,
			width: 800,
			height: 300,
			colors: ["#666", "#444"],
			valueRange: [0, 5000]
		}
	);
}

function connectStreaming() {
	if(!connected) {
		dojox.cometd.configure({
			url: "/push"
		});
		dojox.cometd.handshake();
		connected=true;
	}
}