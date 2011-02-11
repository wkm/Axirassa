dojox.cometd.HttpStreamingTransport = function() {
	var _super = new org.cometd.HttpStreamingTransport();
	var that = org.cometd.Transport.derive(_super);
	return that;
};

dojox.cometd.registerTransport("http-streaming", new dojox.cometd.HttpStreamingTransport(), 0);