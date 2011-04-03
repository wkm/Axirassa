var connected = false;

dojo.require("dojox.charting.widget.Chart2D");
dojo.require("dojox.charting.Theme");
dojo.require("dojox.charting.themes.Tom");

var colorTheme = new dojox.charting.Theme({
	chart: {
		fill: 'inherit'
	},
	plotarea: {
		fill: 'rgba(255,255,255,0)'
	},
	axis: {
		stroke: {
			color: '#aaa'
		},
		tick: {
			color: '#333',
			position: 'center',
			fontColor: '#333'
		}
	},
	seriesThemes: [
	    {stroke: {color: '#eee'}},
	    {stroke: {color: 'rgb(174,3,0)'}},
	    {stroke: {color: '#fafafa'}}
	]
});


function weightedMovingAverage(data, length) {
    if(data.length < length)
        return undefined;

    var averages = new Array(data.length - length + 1);

    var denominator = length * (length + 1) / 2;
    var numerator = 0, total = 0;

    // compute initial weighting
    for(var i = length - 1; i >= 0; i--) {
        numerator += (i+1) * data[i];
        total += data[i];

        averages[0] = numerator / denominator;
    }

    // now the weighted
    var previousTotal, previousNumerator;
    for(i = 1; i < averages.length; i++) {
        previousTotal = total;
        previousNumerator = numerator;

        total = previousTotal + data[i + length - 1] - data[i - 1];
        numerator = previousNumerator + length * data[i + length - 1] - previousTotal;
        averages[i] = numerator / denominator;
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

function AxPlot(id, pingerId) {
    var data = randomData(200);
    var smoothed = weightedMovingAverage(data, 10);
    data = data.slice(10);

	var json = {
		responseTime: smoothed,
		responseSize: data
	};
	
	var chart = new dojox.charting.Chart2D(id + "_plot");
	chart.setTheme(colorTheme);
	chart.addPlot('smooth', {
		type: 'Lines',
		lines: true,
        tension: 2
	});
    chart.addPlot('raw', {type: 'Lines', lines: true});
	
	chart.addSeries('Response Time', json['responseTime'], {plot:'smooth'});
	chart.addSeries('Response Size', json['responseSize'], {plot:'raw'});
	
	chart.render();
	
	
	if(!connected) {
		connectStreaming();
	} else {
		console.log("already connected");
	}
	
	console.log("SUBSCRIBING TO: "+ pingerId);
	dojox.cometd.subscribe("/ax/pinger/"+pingerId, function(msg){
		console.log("Received data point: for ["+pingerId+"] " + msg.data);
		dojo.byId(id + "_time").innerHTML = msg.data;
		dojo.byId(id + "_sz").innerHTML = msg.data['responseSize'] + "<span class='u'>b</span>";
		dojo.byId(id + "_t").innerHtml = msg.data['responseTime'] + "<span class='u'>ms</span>";
	});
	dojox.cometd.subscribe("/ax/timeplease", function(msg) {
		dojo.byId(id + "_timestamp").innerHTML = msg.data;
	});
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