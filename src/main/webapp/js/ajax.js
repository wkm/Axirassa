var ajax = {
	_streamSubscription: null,
	
	_init: function() {
		
	}
};

window.onload = function(){
	dojox.cometd.configure({
		url:"http://localhost:8080/push",
		backoffIncrement: 10000,
		maxBackoff: 3600 * 1000,
		logLevel: 'debug'
	});
	dojox.cometd.handshake();
	console.log("attempting to subscribe");
	var subscribe = dojox.cometd.subscribe("/ax/timeplease", function(msg){
		console.log("received message from: /ax/timeplease");
		$("pollStatus").update("Updated: "+msg.data);
	});
	
	console.log("attempting to publish");
	dojox.cometd.publish('/ax/timeplease', 'yo dawg');
};

var x = 20;
function addDataPoint(date, responseTime, responseSize) {
	x = x + 10;	
	axplot.addDataPoint(x, responseTime);
	$("pollStatus").update("Last update: "+date);
}