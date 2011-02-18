window.onload = function() {	
	dojox.cometd.configure({
		url: "/push"
	});
	dojox.cometd.handshake();
	
	var subscribe = dojox.cometd.subscribe("/ax/timeplease", function(msg){
		dojo.byId("pollStatus").innerHTML = "Updated: " + msg.data;
	});
};
