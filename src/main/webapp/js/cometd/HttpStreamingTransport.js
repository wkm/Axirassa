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
	
	_self.xhrSend = function(packet) {
		var xmlhttp = new XMLHttpRequest;
		var lastindex = 0;
		
		console.log("!!!!! packet: ", packet);
		xmlhttp.open("POST", packet.url, true);
		xmlhttp.onreadystatechange = function() {
			if(xmlhttp.readyState != 4) {
				return;
			}
			
			var newindex = xmlhttp.responseText.length;
			var text = xmlhttp.responseText.substring(lastindex, newindex);
			
			
			if(text.substring(0, 11) != "<<#START#>>" ||
					text.substring(text.length-9, text.length) != "<<#END#>>") {
				packet.onError(text); 
			};
			
			text = text.substring(11, text.length-9);
			text = text.split("<<#END#>><<#START#>>");
			
			console.log("!!!! # of segments: ", text.length);
			for(var segment in text) {
				console.log("!!!!\tfor segment: ", text[segment]);
				packet.onSuccess(text[segment]);
			}
			
			lastindex = newindex;
		},
		xmlhttp.send(packet.body);
	};
	
	_self.transportFailure = function (envelope, request, msg, exception) {
		console.log("TRANSPORT FAILURE");
	};
	
	_self.transortSuccess = function (envelope, request, received){
		console.log("!!!!! TRANSPORT SUCCESS");
	};
	
	return _self;
};