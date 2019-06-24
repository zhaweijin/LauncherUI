package com.mirageTeam.jsonHelper;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class JSONBuilder<T>{

	public abstract T builder(JSONObject jsonObject) throws JSONException;
}
