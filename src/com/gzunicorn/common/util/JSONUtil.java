package com.gzunicorn.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class JSONUtil {
	
	public static List<Map<String,Object>> jsonToList(String jsonStr,String key){
		List list = null;
		try {	
			JSONObject jsonObj = new JSONObject(jsonStr);
			JSONArray jArray = jsonObj.getJSONArray(key);
			list = new ArrayList();

			for(int i=0;i<jArray.length();i++){
				JSONObject item = jArray.getJSONObject(i);
				String[] names = JSONObject.getNames(item);
				Map map = new HashMap();
				for (String name : names) {
					map.put(name, item.get(name));
				}
				list.add(map);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
		
	}

	public static List<Map<String,Object>> jsonToList(JSONObject jsonObj,String key){
		List list = null;
		try {	
			JSONArray jArray = jsonObj.getJSONArray(key);
			list = new ArrayList();

			for(int i=0;i<jArray.length();i++){
				JSONObject item = jArray.getJSONObject(i);
				String[] names = JSONObject.getNames(item);
				Map map = new HashMap();
				for (String name : names) {
					map.put(name, item.get(name));
				}
				list.add(map);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
		
	}

}
