dojox.cometd.HttpStreamingTransport = function() {
	var _super = new org.cometd.HttpStreamingTransport();
	var that = org.cometd.Transport.derive(_super);
	
	that.xhrSend = function(packet) {
		var deferred = dojo.rawXhrPost({
			url: packet.url,
			sync: packet.sync === true,
			contentType: 'application/json;charset=UTF-8',
			headers: packet.headers,
			postData: packet.body,
			handleAs: 'json',
			load: packet.onSuccess,
			error: function(error) {
				packet.onError(error.message, deferred ? deferred.ioArgs.error : error);
			}
		});
		
		return deferred.ioArgs.xhr;
	};
	
	return that;
};

dojox.cometd.registerTransport("http-streaming", new dojox.cometd.HttpStreamingTransport(), 0);