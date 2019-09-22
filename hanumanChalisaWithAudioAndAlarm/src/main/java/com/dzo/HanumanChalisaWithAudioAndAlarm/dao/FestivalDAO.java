package com.dzo.HanumanChalisaWithAudioAndAlarm.dao;

import android.os.Parcel;
import android.os.Parcelable;

public class FestivalDAO implements Parcelable {
	private int id;
	private String festival_name;
	private String festival_date;
	private String festival_desc;
	private String festival_image;

	public FestivalDAO() {
		super();
	}

	public FestivalDAO(int id, String festival_name, String festival_date,
			String festival_desc, String festival_image) {
		super();
		this.id = id;
		this.festival_name = festival_name;
		this.festival_date = festival_date;
		this.festival_desc = festival_desc;
		this.festival_image = festival_image;
	}

	public FestivalDAO(Parcel in) {
		id = in.readInt();
		festival_name = in.readString();
		festival_date = in.readString();
		festival_desc = in.readString();
		festival_image = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(festival_name);
		dest.writeString(festival_date);
		dest.writeString(festival_desc);
		dest.writeString(festival_image);
	}

	public static final Parcelable.Creator<FestivalDAO> CREATOR = new Parcelable.Creator<FestivalDAO>() {
		public FestivalDAO createFromParcel(Parcel in) {
			return new FestivalDAO(in);
		}

		public FestivalDAO[] newArray(int size) {
			return new FestivalDAO[size];
		}
	};

	public int getFestivalID() {
		return id;
	}

	public void setFestivalID(int id) {
		this.id = id;
	}

	public String getFestivalName() {
		return festival_name;
	}

	public void setFestivalName(String festival_name) {
		this.festival_name = festival_name;
	}

	public String getFestivalDate() {
		return festival_date;
	}

	public void setFestivalDate(String festival_date) {
		this.festival_date = festival_date;
	}

	public String getFestivalDesc() {
		return festival_desc;
	}

	public void setFestivalDesc(String festival_desc) {
		this.festival_desc = festival_desc;
	}

	public String getFestivalImg() {
		return festival_image;
	}

	public void setFestivalImg(String festival_image) {
		this.festival_image = festival_image;
	}
}
