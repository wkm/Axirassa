window.onload = function() {
	var xmlhttp = new XMLHttpRequest;
	var lastindex = 0;
	/*
	xmlhttp.open("GET", "http://localhost:8080/stream/", true);
	xmlhttp.onreadystatechange = function() {
		console.log("stage changed ---> ", xmlhttp.readyState);
		var newindex = xmlhttp.responseText.length;
		var text = xmlhttp.responseText.substr(lastindex, newindex);
		console.log("text: ", text);
		console.log("total bytes: ", newindex);
		lastindex = newindex;
		
		alert(text);
	};
	xmlhttp.send(null); */
	
	dojox.cometd.configure({
		url: "http://localhost:8080/push",
//		logLevel: 'debug',
		advice: {
			timeout: 60000,
			interval: 10000,
			reconnect: 'retry'
		}
	});
	dojox.cometd.handshake();
	
	console.log("attempting to subscribe");
	var subscribe = dojox.cometd.subscribe("/ax/timeplease", function(msg){
		console.error("$$$$$ MESSAGE RECEIVED BY CALLBACK UPDATING CONTENT");
		dojo.byId("pollStatus").innerHTML = "Updated: " + msg.data;
	});
};
