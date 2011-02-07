/*
 * Axirassa JavaScript Plotting Library 
 * Copyright (c) 2011 Zanoccio, LLC. 
 * All Rights Reserved.
 */

function AxPlot(id) {
	
	var that = this;
	
	this.id = id;
	this.plotdata = {};
	this.canvas = null;
	this.canvasContext = null;
	
	this.width = null;
	this.height = null;
	
	this._createCanvas();
	
	return this;
};

/**
 * creates a canvas inside the element with the given ID.
 */
AxPlot.prototype._createCanvas = function() {
	this.canvas = $(this.id).appendChild(new Element("canvas", {style:'background: #eee; border: 1px solid #ccc;'}));
	if(this.canvas.getContext) {
		this.canvasContext = this.canvas.getContext("2d");
		var ctx = this.canvasContext;
		
		var dimensions = this.canvas.getDimensions(); 
		
		this.width = dimensions.width;
		this.height = dimensions.height;
		
		ctx.strokeStyle = "rgb(200,0,0)";
		ctx.beginPath();
		
		ctx.moveTo(0, this.height);
		ctx.lineTo(10, this.height - 10);
		ctx.stroke();
		
		this.addDataPoint(20, 10);
	}
};

AxPlot.prototype.addDataPoint = function (x, y) {
	var ctx = this.canvasContext;
	ctx.lineTo(x, this.height - y);
	ctx.stroke();
};