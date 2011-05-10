
package axirassa.util.test;

import java.util.LinkedHashMap;

import lombok.Getter;

public class MapUtility {
	@Getter
	private final LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();


	public MapUtility p(String key, String value) {
		valueMap.put(key, value);
		return this;
	}
}
