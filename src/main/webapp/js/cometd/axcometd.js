{
	org.cometd.JSON.toJSON = Object.toJSON;
	org.cometd.JSON.fromJSON = function(text) {
		return text.evalJSON();
	};

	cometd = new org.cometd.Cometd();
	
	cometd.LongPollingTransport = function ()
	{
		var _super = new org.cometd.LongPollingTransport();
		var that = org.cometd.Transport.derive(_super);
		
		that.xhrSend = function(packet) {
			return new Ajax.Request(packet.url, {
				method: 'post',
				contentType: 'application/json;chartset=UTF-8',
				postBody: packet.body,
				
				requestHeaders: _buildHeaders(packet.headers),
				
				onSuccess: packet.onSuccess,
				onFailure: function(response) {
					packet.onError(response, "");
				}
			});
		};
		
		that.reset();
	};
	
	if(window.WebSocket) {
		Tapestry.debug('have websocket');
		cometd.registerTransport('websocket', new org.cometd.WebSocketTransport());
	}
	
	cometd.registerTransport('long-polling', new cometd.LongPollingTransport());	
}