var connected = false;

function AxPlot(id, pingerId) {
	if(!connected) {
		connectStreaming();
	} else {
		console.log("already connected");
	}
	
	console.log("SUBSCRIBING TO: "+ pingerId);
	dojox.cometd.subscribe("/ax/pinger/"+pingerId, function(msg){
		console.log("Received data point: "+msg);
		dojo.byId(id).innerHTML = "Last data point: "+msg.data;
	});
}
function connectStreaming() {
	console.log("CONFIGURING AND HANDSHAKING");
	dojox.cometd.configure({
		url: "/push"
	});
	dojox.cometd.handshake();
}