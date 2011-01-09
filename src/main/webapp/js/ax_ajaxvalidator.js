function addRequestParameter(name, value, url) {
	if(url.indexOf('?') < 0)
		url += '?';
	else
		url += '&';
	
	value = escape(value);
	url += name += '=' + value;
	return url;
}

var AjaxValidator = Class.create({
		initialize : function(spec) {
			this.field = $(spec.elementId);
			this.listenerURI = spec.uri;
			
			document.observe(Tapestry.FOCUS_CHANGE_EVENT, function(event) {
					if(Tapestry.currentFocusField == this.field &&
							this.field.form == event.memo.form)
						this.asyncValidateInServer();
				}.bindAsEventListener(this)
			);
		},
		
		asyncValidateInServer : function() {
			var value = this.field.value;
			var listenerURIWithParam = this.listenerURI;
			
			if(value) {
				listenerURIWithParam = addRequestParameter('param', value, this.listenerURI);
				
				new Ajax.Request(listenerURIWithParam, {
					method: 'get',
					onFailure: function(t) {
						alert('Error communicating with server: '+t.responseText.stripTags());
					},
					onException: function(t, x) {
						alert('Error communicating with server: '+x.message);
					},
					onSuccess: function(t) {
						if(t.responseJSON.error) {
							this.field.showValidationMessage(t.responseJSON.error);
						}
					}.bind(this)
				});
			}
		}
})