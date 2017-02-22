package cn.apisium.util;

import java.util.HashMap;

public class DefaultMap<K, V> extends HashMap<K, V> {
	protected V defaultValue;

	public DefaultMap(V defaultValue) {
		this.defaultValue = defaultValue;
	}

	@Override
	public V get(Object k) {
		return containsKey(k) ? super.get(k) : defaultValue;
	}
}
