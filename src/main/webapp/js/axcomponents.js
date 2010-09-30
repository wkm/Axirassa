/**
 * Axirassa Components JavaScript Library
 * Copyright 2010 - Zanoccio, LLC. All Rights Reserved.
 */

var ax = new function() {
	
	function formattime(date) {
	  var minute = date.getMinutes();
	  var hour = date.getHours();
	  
	  var ampm;
	  if(hour == 0) {
	    ampm = "am";
	    hour = 12;
	  } else if(hour >= 12) {
	    ampm = "pm";
	    hour -= 12;
	  } else {
	    ampm = "am";
	  }
	  
	  if(hour < 10) {
	    hour = "0"+hour;
	  }
	  if(minute < 10) {
	    minute = "0"+minute;
	  }
	  
	  return hour+":"+minute+ampm;
	};
	
	function percentTicks(n) {
		return n+"%";
	};
	
	this.opener_toggle = function(id) {
		var header = $(id+"_header");
		var body = $(id+"_body");
		
		if (body.visible() == false) {
			header.removeClassName("axo_closed");
			body.show();
			header.addClassName("axo_open");
		} else {
			header.removeClassName("axo_open");
			body.hide();
			header.addClassName("axo_closed");
		}
	};
	
	var gib = Math.pow(2, 30);
	var mib = Math.pow(2, 20);
	var kib = Math.pow(2, 10);
	
	function dataTicks(n, max) {
		var base;
		var unit;
		if(max > gib) {
			base = (n / gib);
			unit = "GiB";
		} else if(max > mib) {
			base = (n / mib);
			unit = "MiB";
		} else {
			base = (n / kib);
			unit = "KiB";
		}
		
		return base.toFixed(2) + unit;
	};
	
	this.axp_axislab_date = function(val, max){
		return formattime(new Date(val/1));
	}
	this.axp_axislab_percent = function(val, max){
		return percentTicks(val);
	}
	this.axp_axislab_data = dataTicks;
	
	this.agentcontrol = function(id, source) {
		// mark that we're downloading data
		$(id).addClassName('axp_loading');
		$(id).update('Downloading data...');
		
		new Ajax.Request(source, {
			method: 'get',
			onSuccess: function(transport) {
				$(id).update('Processing...');
				
				var colors = ["#D44917", "#0117A1"];
				
				var data = transport.responseText.evalJSON();
				
				var depth = data['depth'];
				var times = data['times'];
				var rawdata = data['data'];
				var datasets = data['datasets'];
				var datasz = times.length;
				var aggregatedmax = data['aggregatedMax'];
				
				var labeldatasets = data['labelDataSets'];
				
				var yaxislabfn = data['yaxislabelfn'];
				
				if (datasets > 1) {
					$(id).update('Generating charts...');
					
					// create sub graph boxes
					var detailsnode = $(id + "_details_body");
					var index = 0;
					
					data['labels'].map(function(label){
						var node = new Element('div', {
							'class': 'chart axp',
							id: id + "_chart_" + index
						});
						
						if(labeldatasets)
							node.update(label);
							
						$(detailsnode).insert(node);
						index++;
					});
					
					var chartdata;
					
					var visible = $(detailsnode).visible();
					$(detailsnode).show();
					
					// refactor data, create charts
					for (var dataset = 0; dataset < datasets; dataset++) {
						chartdata = new Array(depth);
						
						// allocate data for each depth being plotted
						for (var k = 0; k < depth; k++) {
							chartdata[k] = new Array(datasz);
						}
						
						// stack the data for each measuring point
						var total;
						for (var j = 0; j < datasz; j++) {
							total = 0;
							for (var k = 0; k < depth; k++) {
								total += rawdata[dataset][j][k];
								chartdata[k][j] = [times[j], total];
							}
						}
						
						// if we're one of the last two datasets, label the
						// x-axis ticks
						var xtickfn;
						if(dataset < (datasets - 2)) {
							xtickfn = function(n){
								return "";
							}
						} else {
							xtickfn = function(n) {
								return formattime(new Date(n / 1));
							}
						};
						
						var chartindex = 0;
						Flotr.draw(
							$(id + "_chart_" + dataset),
							chartdata.map(function(d){
								return {data: d, color: colors[chartindex++], lines:{show:true}};
							}), 
							{
								shadowSize: 0,
								xaxis: {
									tickFormatter: xtickfn
								},
								
								yaxis: {
									min: 0,
									tickFormatter: function(n){
										return yaxislabfn(n, aggregatedmax);
									}
								}
							}
						);
					}
					
					if(!visible)
						$(detailsnode).hide();
				} else {
					var detailsnode = $(id+"_details");
					detailsnode.hide();
				}
					
				$(id).update('Aggregating data...');
				chartdata = new Array(datasets);
				for(var i = 0; i < datasets; i++) {
					chartdata[i] = new Array(datasz);
				}
				
				var total;
				for(var i = 0; i < datasz; i++) {
					total = 0;
					for (var k = 0; k < datasets; k++) {
						for (var j = 0; j < depth; j++) {
							total += rawdata[j][i][k];
						}
						chartdata[k][i] = [times[i], total];
					}
				}
				
				$(id).update('');
				$(id).removeClassName('axp_loading');
				
				var chartindex = 0;
				Flotr.draw(
					$(id),
					chartdata.map(function(d){
						return {data: d, color: colors[chartindex++], lines:{show:true}};
					}),
					{
						shadowSize: 0,
						xaxis: {
							tickFormatter: function(n) {
								return ax.axp_axislab_date(n, null);
							}
						},
						yaxis: {
							min: 0,
							max: aggregatedmax,
							tickFormatter: function(n){
								return yaxislabfn(n, aggregatedmax);
							}
						}
					}
				);
			}
		});
	};
};
