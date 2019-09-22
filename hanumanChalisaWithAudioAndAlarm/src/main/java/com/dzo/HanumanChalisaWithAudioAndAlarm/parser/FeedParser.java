package com.dzo.HanumanChalisaWithAudioAndAlarm.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import android.util.Log;

import com.dzo.HanumanChalisaWithAudioAndAlarm.dao.FestivalDAOs;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

public class FeedParser {
	private static final String TAG = "FeedParser";

	public FestivalDAOs getData(InputStream stream) throws JsonSyntaxException,
			IOException {
		Log.e(TAG, "getData");
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		Reader reader = new BufferedReader(new InputStreamReader(stream));
		return gson.fromJson(reader, FestivalDAOs.class);
	}
}