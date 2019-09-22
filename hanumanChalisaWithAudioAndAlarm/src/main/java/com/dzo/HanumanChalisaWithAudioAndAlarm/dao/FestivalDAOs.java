package com.dzo.HanumanChalisaWithAudioAndAlarm.dao;

import java.util.List;

public class FestivalDAOs {
	private List<FestivalDAO> Result;

	public List<FestivalDAO> getFestivalsList() {
		return Result;
	}

	public void setFestivalsList(List<FestivalDAO> Result) {
		this.Result = Result;
	}
}