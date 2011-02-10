org.cometd.HttpStreamingTransport = function() {
	var _super = new org.cometd.RequestTransport();
	var _self = org.cometd.Transport.derive(_super);
	
	// eh... maybe?
	var _supportsCrossDomain = false;
	
	_self.accept = function(version, crossDomain, url) {
		_self._debug("#### Http Stream Transport: "+version+" "+crossDomain+" "+url);
		return _supportsCrossDomain || !crossDomain;
	};
	
	_self.reset = function() {
		_super.reset();
		_supportsCrossDomain = false;
	};
	
	_self.transportSend = function(envelope, request) {
		var self = this;
		try {
			request.xhr = this.xhrSend({
				transport: this,
				url: envelope.url,
				sync: envelope.sync,
				headers: this.getConfiguration().requestHeaders,
				body: org.cometd.JSON.toJSON(envelope.messages),
				onSuccess: function(response) {
					self._debug('#### received response: ', response);
					var success = false;
					try {
						var received = self.convertToMessages(response);
						if(received.length === 0) {
							self.transportFailure(envelope, request, 'no response', null);
						} else {
							success = true;
							self.transportSuccess(envelope, request, received);
						}
					} catch (ex) {
						self._debug(ex);
						if(!success) {
							self.transportFailure(envelope, request, 'bad response', ex);
						}
					}
				},
				onError: function(response) {
					self._debug('#### received error: ', response);
				}
			});
		} catch(ex) {
			_supportsCrossDomain = false;
			this.setTimeout(function() {
				self.transportFailure();
				_self._debug("#### error: ", ex);
			}, 0);
		};
	};
	
	_self.transportFailure = function() {
		console.log("TRANSPORT FAILURE");
	};
	
	return _self;
};