/*
 * Axirassa JavaScript Plotting Library 
 * Copyright (c) 2011 Zanoccio, LLC. 
 * All Rights Reserved.
 */

function AxPlot(id, plotdata) {
	
	var that = this;
	
	this.id = id;
	this.plotData = plotdata;
	this.plotoptions = {};
	
	this.drawPlot();
	
	return this;
};

AxPlot.prototype.addDataPoint = function (x, y) {	
	this.plotData.push([x,y]);
	this.drawPlot();
	
	$(this.id + "_val").update(y+"<span class='u'>ms</span>");
};

AxPlot.prototype.drawPlot = function() {
	Flotr.draw(
			$(this.id),
			[{
				data: this.plotData,
				lines: { show:true },
				color: "#8e2800"
			}],
			{
				shadowSize: 0,
				grid: {
					outlineWidth: 0
				},
				yaxis: {
					min: 0
				}
			}
	);
};