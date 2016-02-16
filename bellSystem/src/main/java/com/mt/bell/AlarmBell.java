package com.mt.bell;

public class AlarmBell {

	private String serialNo;
	private String alarmLabel;
	private String alarmTime;
	private String bellTime;

	public String getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	public String getAlarmTime() {
		return alarmTime;
	}
	public void setAlarmTime(String alarmTime) {
		this.alarmTime = alarmTime;
	}
	public String getAlarmLabel() {
		return alarmLabel;
	}
	public void setAlarmLabel(String alarmLabel) {
		this.alarmLabel = alarmLabel;
	}

	public String getBellTime() {
		return bellTime;
	}

	public void setBellTime(String bellTime) {
		this.bellTime = bellTime;
	}
}
