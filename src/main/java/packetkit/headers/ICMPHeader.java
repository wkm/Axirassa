
package packetkit.headers;

import packetkit.headers.annotations.Checksum;
import packetkit.headers.annotations.Data;
import packetkit.headers.annotations.StaticFragment;

/**
 * Internet Control Message Protocol (ICMP) Header
 * http://www.networksorcery.com/enp/protocol/icmp.htm
 * 
 * @author wiktor
 */
public class ICMPHeader extends AbstractPacketHeader {
	@StaticFragment(slot = 0)
	ICMPType type;

	@StaticFragment(slot = 1, size = 1, fixed = true)
	int code;

	@StaticFragment(slot = 2, size = 2, fixed = true)
	@Checksum(type = ChecksumMethod.ONESCOMPLEMENT)
	byte[] checksum;

	@StaticFragment(slot = 3, size = 2, fixed = true)
	int identifier;

	@StaticFragment(slot = 4, size = 2, fixed = true)
	int sequencenumber;

	@StaticFragment(slot = 5)
	@Data
	byte[] data;


	//
	// Accessors
	//

	public void setType(ICMPType type) {
		this.type = type;
	}


	public ICMPType getType() {
		return type;
	}


	public void setCode(int code) {
		this.code = code;
	}


	public int getCode() {
		return code;
	}


	public void setChecksum(byte[] checksum) {
		this.checksum = checksum;
	}


	public byte[] getChecksum() {
		return checksum;
	}


	public void setIdentifier(int identifier) {
		this.identifier = identifier;
	}


	public int getIdentifier() {
		return identifier;
	}


	public void setSequenceNumber(int sequencenumber) {
		this.sequencenumber = sequencenumber;
	}


	public int getSequenceNumber() {
		return sequencenumber;
	}


	public void setData(byte[] data) {
		this.data = data;
	}


	public byte[] getData() {
		return data;
	}
}
