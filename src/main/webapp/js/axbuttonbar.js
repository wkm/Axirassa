dojo.ready(function() {
	var buttonbars = dojo.query(".bb");
	var i = 0;
	var inner;
	for(; i < buttonbars.length; i++) {
		console.log('button bar: ' + buttonbars[i]);
		dojo.query('label', buttonbars[i]).forEach(function(node, index, nodelist){
			console.log('node: '+node + '   ' + index + ' of '+nodelist.length);
			dojo.addClass(node, 'bbb');
			inner = node.innerHTML;
			node.innerHTML = null;
			
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
		});
		
		dojo.create("div", {className: "reset"}, buttonbars[i]);
	}

});
