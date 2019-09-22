package com.dzo.HanumanChalisaWithAudioAndAlarm.reminder;

import java.io.Serializable;

public class CalendarEventsDAO implements Serializable {

	private static final long serialVersionUID = 5241440058993453074L;

	private String eventID;
	private String reminderID;
	private String eventTitle;
	private String eventDate;
	private String eventTime;

	public String getEventID() {
		return eventID;
	}

	public void setEventID(String eventID) {
		this.eventID = eventID;
	}

	public String getReminderID() {
		return reminderID;
	}

	public void setReminderID(String reminderID) {
		this.reminderID = reminderID;
	}

	public String getEventTitle() {
		return eventTitle;
	}

	public void setEventTitle(String eventTitle) {
		this.eventTitle = eventTitle;
	}

	public String getEventDate() {
		return eventDate;
	}

	public void setEventDate(String eventDate) {
		this.eventDate = eventDate;
	}

	public String getEventTime() {
		return eventTime;
	}

	public void setEventTime(String eventTime) {
		this.eventTime = eventTime;
	}
}