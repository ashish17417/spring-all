package com.ashish.lucene.index;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

class MapComparator implements Comparator<Object> {

	Map<String, Double> map;

	public MapComparator(Map<String, Double> map) {
		this.map = map;
	}

	public int compare(Object o1, Object o2) {

		if (map.get(o2) == map.get(o1))
			return 1;
		else
			return ((Double) map.get(o2)).compareTo((Double)     
					map.get(o1));

	}
	
	public static void main(String[] args) {
		Map<String, Double> lMap = new HashMap<String, Double>();
	    lMap.put("A", 35.90);
	    lMap.put("B", 75.25);
	    lMap.put("C", 50.11);
	    lMap.put("D", 50.10);

	    MapComparator comparator = new MapComparator(lMap);

	    Map<String, Double> newMap = new TreeMap<String, Double>(comparator);
	    newMap.putAll(lMap);
	    System.out.println(newMap);
	}
}