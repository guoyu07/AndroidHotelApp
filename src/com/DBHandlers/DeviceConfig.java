package com.DBHandlers;

public class DeviceConfig {
	private String ImgBtnId;
	private String DeviceId;
	private String HexaCode;
	private int IsConfigured;

	// Empty constructor
	public DeviceConfig() {

	}

	// constructor
	public DeviceConfig(String ImgBtnId, String DeviceId, String HexaCode,
			int IsConfigured) {
		this.ImgBtnId = ImgBtnId;
		this.DeviceId = DeviceId;
		this.HexaCode = HexaCode;
		this.IsConfigured = IsConfigured;
	}

	public String getDeviceId() {
		return this.DeviceId;
	}

	public String getHexaCode() {
		return this.HexaCode;
	}

	public String getImgBtnId() {
		return this.ImgBtnId;
	}

	public int getIsConfigured() {
		return this.IsConfigured;
	}

	public void setDeviceId(String deviceId) {
		this.DeviceId = deviceId;
	}

	public void setHexaCode(String hexaCode) {
		this.HexaCode = hexaCode;
	}

	public void setImgBtnId(String imgBtnId) {
		this.ImgBtnId = imgBtnId;
	}

	public void setIsConfigured(int isConfigured) {
		this.IsConfigured = isConfigured;
	}

}
