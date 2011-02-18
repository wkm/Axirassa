window.onload = function() {
	dojox.cometd.configure({
		url: "/push"
	});
	dojox.cometd.handshake();
};

function AxPlot(id, pingerId) {
	dojox.cometd.subscribe("/ax/pinger/"+pingerId, function(msg){
		console.log("Received data point: "+msg);
		dojo.byId(id).innerHTML = "Last data point: "+msg.data;
	});
}