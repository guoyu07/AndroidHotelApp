package com.DBHandlers;

public class UsersInfo {
	private String UN;
	private String PWD;

	public UsersInfo() {

	}

	public UsersInfo(String UN, String PWD) {
		this.UN = UN;
		this.PWD = PWD;
	}

	public String getUN() {
		return this.UN;
	}

	public void setUN(String uN) {
		this.UN = uN;
	}

	public String getPWD() {
		return this.PWD;
	}

	public void setPWD(String pWD) {
		this.PWD = pWD;
	}

}
