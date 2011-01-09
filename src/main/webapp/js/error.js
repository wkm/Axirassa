
/*
 * Based on: http://jumpstart.doublenegative.com.au/jumpstart/examples/input/novalidationbubbles1
 */
Tapestry.FieldEventManager.addMethods( {
	initialize:function(field) {
		this.field = $(field);
		
		var id = this.field.id;
		this.fieldContainer = this.field.parentNode;
		Element.extend(this.fieldContainer);
		
		var msgId = id + '-msg';
		this.msg = $(msgId);
		
		if(!this.msg) {
			this.msg = new Element('span', {
				'id' : msgId,
				'class' : 'errorMsg'
			});
			this.msg.hide();
			this.fieldContainer.insert( {
				top : this.msg
			});
		}
		
		this.msgConstainer = this.msg.parentNode;
		Element.extend(this.msgContainer);
		
		this.translator = Prototype.K;
		
		document.observe(Tapestry.FOCUS_CHANGE_EVENT, function(event) {
			if(Tapestry.currentFocusField == this.field && this.field.form == event.memo.form) {
				this.validateInput();
			}
		}.bindAsEventListener(this));
	},
	
	showValidationMessage : function(message) {
		$T(this.field).validationError = true;
		$T(this.field.form).validationError = true;
		
		this.msg.update(message);
		this.msg.show();
		
		this.field.addClassName("error-field");
		this.msg.addClassName("error-msg");
	},
	
	removeDecorations: function() {
		this.msg.update(null);
		this.msg.hide();
		
		this.field.removeClassName("error-field");
		this.msg.removeClassName("error-msg");
	}
});
