var connected = false;

dojo.require("dojox.charting.widget.Chart2D");
dojo.require("dojox.charting.Theme");
dojo.require("dojox.charting.themes.Tom");

var colorTheme = new dojox.charting.Theme({
	chart: {
		fill: 'inherit'
	},
	plotarea: {
		fill: 'rgba(36,43,51,0)'
	},
	axis: {
		stroke: {
			color: 'rgb(18, 21, 26)'
		},
		tick: {
			color: 'rgb(18, 21, 26)',
			position: 'center',
			fontColor: '#aaa'
		}
	},
	seriesThemes: [
	    {stroke: {color: 'rgb(255,178,0)'}},
	    {stroke: {color: 'rgb(48,56,68)'}},
	    {stroke: {color: 'rgb(18,21,26)'}}
	]
});

function randomData(length) {
	var data = new Array(length);

	for(var i = 0; i < length; i++)
		data[i] = Math.floor(Math.random()*1000);
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
	var json = {
		responseTime: randomData(60),
		responseSize: randomData(60)
	};
	
	var chart = new dojox.charting.Chart2D(id);
	chart.setTheme(colorTheme);
	chart.addPlot('default', {
		type: 'Lines',
		lines: true,
		labelOffset: -30
	});
	
	chart.addAxis('x');
	chart.addAxis('y', {vertical:true});
	
	chart.addSeries('Response Time', json['responseTime']);
	chart.addSeries('Response Size', json['responseSize']);
	
	chart.render();
	
	
	/*if(!connected) {
		connectStreaming();
	} else {
		console.log("already connected");
	}
	
	console.log("SUBSCRIBING TO: "+ pingerId);
	dojox.cometd.subscribe("/ax/pinger/"+pingerId, function(msg){
		console.log("Received data point: "+msg);
		dojo.byId(id).innerHTML = "Last data point: "+msg.data;
	});*/
}
function connectStreaming() {
	/*console.log("CONFIGURING AND HANDSHAKING");
	dojox.cometd.configure({
		url: "/push"
	});
	dojox.cometd.handshake();*/
}