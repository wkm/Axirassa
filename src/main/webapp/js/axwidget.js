var connected = false;

dojo.require("dojox.charting.widget.Chart2D");
dojo.require("dojox.charting.Theme");
dojo.require("dojox.charting.action2d.Tooltip");
dojo.require("dojox.charting.action2d.Magnify");

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
			fontColor: '#333',
			font: 'normal normal normal 7pt Helvetica, Arial, sans-serif'
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
	// pull the original data
	dojo.xhrGet({
			url: '/monitorconsole.axmonitorwidget/'+id,
			handleAs: 'json',
			load: function(data) {
				if(data.length < 5) {
					dojo.destroy(id+"_spin");
					dojo.create(
							'div', 
							{className: 'status', innerHTML: 'not enough data'}, 
							dojo.getById(id+"_plot")
					);
					return;
				}
				
				var chart = new dojox.charting.Chart2D(id + "_plot");
				
				var i;
				var responseSize = [];
				for(i = 0; i < data.length; i++) {
					responseSize[i] = [data[i][0], data[i][1]];
				}
				
				var smoother = weightedMovingAverage(responseSize, 5);
				responseSize = responseSize.slice(5);
				
				chart.setTheme(colorTheme);
				chart.addPlot('smooth', {
					type: 'Lines',
					lines: true,
			        tension: 5,
			        shadows: 0
				});
			    chart.addPlot('raw', {type: 'Lines', lines: true, vAxis: 'y'});
				
			    chart.addAxis("y", {
			    	vertical: true, 
			    	includeZero: true, 
			    	max: 5000,
			    	minorTicks: false,
			    	majorTicks: [0, 1000],
			    	labels: [{value: 0, text:'0s'}, {value: 1000, text:'1s'}, {value: 2000, text:'2s'}, {value: 3000, text:'3s'}, {value: 4000, text:'4s'}, {value: 5000}]
			    });
			    
			    chart.addAxis("x", {
			    	
			    });
			    
			    chart.addSeries('time.smooth', createSeries(smoother), {plot: 'smooth'});
			    chart.addSeries('time.raw', createSeries(responseSize), {plot: 'raw'});
				
			    new dojox.charting.action2d.Tooltip(chart, "smooth", {scale: 3});
			    
				chart.render();
				
				// remove spinner
				dojo.destroy(id+"_spin");
			}
	});

	
//	if(!connected) {
//		connectStreaming();
//	} else {
//		console.log("already connected");
//	}
//	
//	console.log("SUBSCRIBING TO: "+ pingerId);
//	dojox.cometd.subscribe("/ax/pinger/"+pingerId, function(msg){
//		console.log("Received data point: for ["+pingerId+"] " + msg.data);
//		dojo.byId(id + "_time").innerHTML = msg.data;
//		dojo.byId(id + "_sz").innerHTML = msg.data['responseSize'] + "<span class='u'>b</span>";
//		dojo.byId(id + "_t").innerHtml = msg.data['responseTime'] + "<span class='u'>ms</span>";
//	});
//	dojox.cometd.subscribe("/ax/timeplease", function(msg) {
//		dojo.byId(id + "_timestamp").innerHTML = msg.data;
//	});
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