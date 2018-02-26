package com.example.project.smartsilent;

import java.util.UUID;

/**
 * Created by Faojul Ahsan on 1/9/2017.
 */

public class TimeDate {
    private String id;
    private int silentHour;
    private int silentMinute;
    private int unsilentHour;
    private int unsilentMinute;

    public TimeDate() {
    }

    public TimeDate(String id, int silentHour, int silentMinute, int unsilentHour, int unsilentMinute) {

        if (id == null) {
            id = UUID.randomUUID().toString();
        }

        this.id = id;
        this.silentHour = silentHour;
        this.silentMinute = silentMinute;
        this.unsilentHour = unsilentHour;
		this.unsilentMinute = unsilentMinute;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSilentHour() {
        return silentHour;
    }

    public void setSilentHour(int silentHour) {
        this.silentHour = silentHour;
    }
    public int getSilentMinute() {
        return silentMinute;
    }

    public void setSilentMinute(int silentMinute) {
        this.silentMinute = silentMinute;
    }

    public int getUnsilentHour() {
        return unsilentHour;
    }

    public void setUnsilentHour(int unsilentHour) {
        this.unsilentHour = unsilentHour;
    }
	
	public int getUnsilentMinute() {
        return unsilentMinute;
    }

    public void setUnsilentMinute(int unsilentMinute) {
        this.unsilentMinute = unsilentMinute;
    }

    @Override
    public String toString() {
        return "TimeData{" +
                "id='" + id + '\'' +
                ", silentHour ='" + silentHour + '\'' +
                ", silentMinute=" + silentMinute +
                ", unsilentHour=" + unsilentHour +
                ", unsilentMinute=" + unsilentMinute +
                '}';
    }
}
