/**
 * Axirassa Components JavaScript Library
 * Copyright 2010 - Zanoccio, LLC. All Rights Reserved.
 */

var ax = new function() {
	
	this.opener_toggle = function(id) {
		var obj = $($(id+"_body"));
		if(obj.visible() == false)
			obj.show();
		else
			obj.hide();
	};
};
