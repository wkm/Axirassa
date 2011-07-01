
var $buttonbar = {};

dojo.ready(function() {
	var buttonbars = dojo.query(".bb");
	var inner;
	dojo.query('.bb').forEach(function(buttonbar, i, nodelist) {
		dojo.query('label', buttonbars[i]).forEach(function(node, index, nodelist){
			dojo.addClass(node, 'bbb');
			inner = node.innerHTML;
			node.innerHTML = '';
			
			dojo.create('div', {
				innerHTML: inner,
				className: 'bbbi'
			}, node);
			
			if(index == 0) {
				dojo.addClass(node, 'bbbl');
			}
			
			if(index == nodelist.length - 1) {
				dojo.addClass(node, 'bbbr');
			}
			
			$buttonbar[buttonbars[i]] = {};

			var input = dojo.byId(dojo.attr(node, 'for'));
			if(input != null && dojo.attr(input, 'checked') == true) {
				buttonbar["active"] = node;
				dojo.addClass(node, 'active');
			}
			
			dojo.connect(node, "onclick", function(evt){
				var activenode = buttonbar["active"];
				if(activenode != null) {
					dojo.removeClass(activenode, 'active');
				}
				
				buttonbar["active"] = node;
				dojo.addClass(node, 'active');
			});
		});
		
		dojo.create("div", {className: "reset"}, buttonbars[i]);
	});

});
