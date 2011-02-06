// Axirassa JavaScript Plotting Library Copyright (c) 2011 Zanoccio, LLC. All Rights Reserved.
var AxPlot = {
		version: '0.1',
		website: 'http://zanoccio.com',
		
		plot: function(id) {
			var canvas = $(id);
			if(canvas.getContext) {
				var ctx = canvas.getContext("2d");
				
				ctx.strokeStyle = "rgb(200,0,0)";
				
				ctx.beginPath();
				ctx.moveTo(75,25);
				ctx.quadraticCurveTo(25,25,25,62.5);
				ctx.stroke();
			}
		}		
};