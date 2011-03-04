window.onload = function() {	
	dojox.cometd.configure({
		url: "/push"
	});
	dojox.cometd.handshake();
	
	var subscribe = dojox.cometd.subscribe("/ax/timeplease", function(msg){
		dojo.byId("pollStatus1").innerHTML = "Updated: " + msg.data;
	});
	
	var subscribe2 = dojox.cometd.subscribe("/ax/valueplease", function(msg){
		dojo.byId("pollStatus2").innerHTML = "Updated: " + msg.data;
	});
};
