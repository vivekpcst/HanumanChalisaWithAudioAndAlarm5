package com.dzo.HanumanChalisaWithAudioAndAlarm.reminder;

import java.io.Serializable;

public class CalendarListDAO implements Serializable {

	private static final long serialVersionUID = 3046839624985490268L;

	private String calendarId;
	private String accountName;
	private String calendarName;

	public String getCalendarId() {
		return calendarId;
	}

	public void setCalendarId(String calendarId) {
		this.calendarId = calendarId;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getCalendarName() {
		return calendarName;
	}

	public void setCalendarName(String calendarName) {
		this.calendarName = calendarName;
	}
}
