package com.project.peoplescorner.data;

public class complaint_RequestObject {
	
	private String compID;
	private String mobNo;
	private String compDesc;
	private String lat,lng;
	private String loc_Address;
	private byte[] image;
	public String getCompID() {
		return compID;
	}
	public void setCompID(String compID) {
		this.compID = compID;
	}
	public String getMobNo() {
		return mobNo;
	}
	public void setMobNo(String mobNo) {
		this.mobNo = mobNo;
	}
	public String getCompDesc() {
		return compDesc;
	}
	public void setCompDesc(String compDesc) {
		this.compDesc = compDesc;
	}
	public String getLng() {
		return lng;
	}
	public void setLng(String lng) {
		this.lng = lng;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public byte[] getImage() {
		return image;
	}
	public void setImage(byte[] image) {
		this.image = image;
	}
	public String getLoc_Address() {
		return loc_Address;
	}
	public void setLoc_Address(String loc_Address) {
		this.loc_Address = loc_Address;
	}
}
