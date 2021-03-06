package com.snx.snxservice.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snx.snxservice.dto.ActivityDataDto;

/**
 * @author Rohit Lakshykar
 */
public class HourlyActivityThread implements Callable<List<Object>> {

	static final Logger logger = LoggerFactory.getLogger(HourlyActivityThread.class);

	Map<Integer, Map<String, Integer>> hourlyActivities;

	public HourlyActivityThread(Map<Integer, Map<String, Integer>> hourlyActivities) {
		super();
		this.hourlyActivities = hourlyActivities;
	}

	@Override
	public List<Object> call() throws Exception {
		logger.info("Activity Size : {}", hourlyActivities.size());

		JSONArray hourlyActivityList = new JSONArray();

		for (Entry<Integer, Map<String, Integer>> hour : hourlyActivities.entrySet()) {

			JSONObject hourObj = new JSONObject();
			hourObj.put("hour", String.valueOf(hour.getKey()));
			List<ActivityDataDto> activityList = new ArrayList<>();

			for (Entry<String, Integer> activity : hour.getValue().entrySet()) {
				activityList.add(new ActivityDataDto(activity.getKey(), activity.getValue()));
			}

			Collections.sort(activityList,
					(o1, o2) -> o1.getHits() < o2.getHits() ? 1 : o1.getHits() > o2.getHits() ? -1 : 0);

			hourObj.put("activities", activityList);
			hourlyActivityList.put(hourObj);
		}

		return hourlyActivityList.toList();
	}
}
