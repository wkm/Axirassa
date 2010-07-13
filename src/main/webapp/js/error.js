
/*
 * Based on: http://jumpstart.doublenegative.com.au/jumpstart/examples/input/novalidationbubbles1
 */
Tapestry.FieldEventManager.addMethods( {
	initialize:function(field) {
		this.field = $(field);
		
		var id = this.field.id;
		this.fieldContainer = this.field.parentNode;
		Element.extend(this.fieldContainer);
		
		this.label = $(id + '-label');
		this.labelContainer = this.label.parentNode;
		Element.extend(this.labelContainer);
		
		var msgId = id + '-msg';
		this.msg = $(msgId);
		
		if(!this.msg) {
			this.msg = new Element('strong', {
				'id' : msgId,
				'class' : 'msg'
			});
			this.fieldContainer.insert( {
				bottom : this.msg
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
		
		this.label.addClassName("error-label");
		this.labelContainer.addClassName("error-label-c");
		
		this.field.addClassName("error-field");
		this.fieldContainer.addClassName("error-field-c");
		
		this.msg.addClassName("error-msg");
		this.msgContainer.addClassName("error-msg-c");
	},
	
	removeDecorations: function() {
		this.msg.update(null);
		
		this.label.removeClassName("error-label");
		this.labelContainer.removeClassName("error-label-c");
		
		this.field.removeClassName("error-field");
		this.fieldContainer.removeClassName("error-field-c");
		
		this.msg.removeClassName("error-msg");
		this.msgContainer.removeClassName("error-msg-c");
	}
});
