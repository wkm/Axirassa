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
				
				var data = transport.responseText.evalJSON();
				
				var length = data['length'];
				var times = data['times'];
				var rawdata = data['data'];
				var datasets = data['datasets'];
				var datasz = times.length;
				var aggregatedmax = data['aggregatedMax'];
				
				var yaxislabfn = eval(data['yaxislabelfn']);
				
				if (length > 1) {
					$(id).update('Generating charts...');
					
					// create sub graph boxes
					var detailsnode = $(id + "_details_body");
					var index = 0;
					
					data['labels'].map(function(label){
						$(detailsnode).insert(new Element('div', {
							'class': 'chart axp',
							id: id + "_chart_" + index
						}));
						index++;
					});
					
					var chartdata;
					
					var visible = $(detailsnode).visible();
					$(detailsnode).show();
					
					// refactor data, create charts
					for (var i = 0; i < length; i++) {
						chartdata = new Array(datasets);
						for (var k = 0; k < datasets; k++) {
							chartdata[k] = new Array(datasz);
						}
						
						var total;
						for (var j = 0; j < datasz; j++) {
							total = 0;
							for (var k = 0; k < datasets; k++) {
								total += rawdata[i][j][k];
								chartdata[k][j] = [times[j], total];
							}
						}
						
						var xtickfn;
						if(i < (length - 2)) {
							xtickfn = function(n){
								return "";
							}
						} else {
							xtickfn = function(n) {
								return formattime(new Date(n / 1));
							}
						};
						
						Flotr.draw(
							$(id + "_chart_" + i), 
							[
								{data: chartdata[0], color: "#0117A1", lines:{show:true}}
//								{data: chartdata[1], color: "#D44917", lines:{fill:false}}
							], 
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
						for (var j = 0; j < length; j++) {
							total += rawdata[j][i][k];
						}
						chartdata[k][i] = [times[i], total];
					}
				}
				
				$(id).update('');
				$(id).removeClassName('axp_loading');
				
				Flotr.draw(
					$(id),
					[
						{data: chartdata[0], color: "#0117A1", lines:{show:true}}
//						{data: chartdata[1], color: "#D44917", lines:{fill:false}}
					],
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
