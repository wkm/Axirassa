
package com.zanoccio.axirassa.domain;

import java.util.Date;

public class UserAccessEvent {
	private Long id;
	private Integer ipaddress;
	private Date date;


	public UserAccessEvent() {
	}


	public void setID(Long id) {
		this.id = id;
	}


	public Long getID() {
		return id;
	}


	public void setIPAddress(Integer ipaddress) {
		this.ipaddress = ipaddress;
	}


	public Integer getIPAddress() {
		return ipaddress;
	}


	public void setDate(Date date) {
		this.date = date;
	}


	public Date getDate() {
		return date;
	}
}
