var ajax = {
	_streamSubscription: null,
	
	_init: function() {
		
	}
};

window.onload = function(){
	dojox.cometd.configure({
		url:"http://localhost:8080/push",
		backoffIncrement: 100,
		maxBackoff: 3600 * 1000,
		logLevel: 'debug'
	});
	dojox.cometd.handshake();
	
	var subscribe = dojox.cometd.subscribe("/ax/timeplease", function(msg){
		$("pollStatus").update("Updated: "+msg.data);
	});
	
//	dojox.cometd.publish('/ax/timeplease', 'yo dawg');
};

var x = 20;
function addDataPoint(date, responseTime, responseSize) {
	x = x + 10;	
	axplot.addDataPoint(x, responseTime);
	$("pollStatus").update("Last update: "+date);
}