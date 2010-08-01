
package com.zanoccio.packetkit.headers;

import com.zanoccio.packetkit.headers.annotations.Checksum;
import com.zanoccio.packetkit.headers.annotations.Data;
import com.zanoccio.packetkit.headers.annotations.StaticFragment;

/**
 * Internet Control Message Protocol (ICMP) Header
 * http://www.networksorcery.com/enp/protocol/icmp.htm
 * 
 * @author wiktor
 */
public class ICMPHeader {
	@StaticFragment(slot = 0)
	ICMPType type;

	@StaticFragment(slot = 1, size = 1, fixed = true)
	int code;

	@StaticFragment(slot = 2, size = 2, fixed = true)
	@Checksum(type = ChecksumMethod.ONESCOMPLEMENT)
	int checksum;

	@StaticFragment(slot = 3, size = 2, fixed = true)
	int identifier;

	@StaticFragment(slot = 4, size = 2, fixed = true)
	int sequencenumber;

	@Data
	byte[] data;
}
