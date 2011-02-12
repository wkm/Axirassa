window.onload = function() {	
	dojox.cometd.configure({
		url: "/push",
		logLevel: 'debug',
		advice: {
			timeout: 60000,
			reconnect: 'retry'
		}
	});
	dojox.cometd.handshake();
	
	var subscribe = dojox.cometd.subscribe("/ax/timeplease", function(msg){
		dojo.byId("pollStatus").innerHTML = "Updated: " + msg.data;
	});
};
