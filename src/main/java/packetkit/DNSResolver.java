
package packetkit;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

public class DNSResolver {

	public enum DNSRecordName {
		SOA,
		A,
		NS,
		MX,
		CNAME;
	}


	public static List<String> lookup(String hostname, DNSRecordName record) {
		return lookup(hostname, record.toString());
	}


	public static List<String> lookup(String hostname, String record) {
		List<String> result = new ArrayList<String>();
		try {
			Hashtable<String, String> env = new Hashtable<String, String>();
			env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
			DirContext ictx = new InitialDirContext(env);
			Attributes attrs = ictx.getAttributes(hostname, new String[] { record });
			Attribute attr = attrs.get(record);

			NamingEnumeration attrenum = attr.getAll();
			while (attrenum.hasMoreElements())
				result.add((String) attrenum.next());

		} catch (NamingException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		return result;
	}
}
