package jp.evo.btm.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;

import android.content.ContentValues;

public class TravelModel {

	private String fromtime;
	private String totime;
	private String registerTime;
	private String purpose;
	private String phone;
	private String explanation;
	private String note;
	private String dealCode;
	StaffModel holder;

	private int id = -1;
	private int status;

	ArrayList<StaffModel> listStaffEnroll;

	private ArrayList<ContentValues> travelPackage;
	private ArrayList<ContentValues> travelPlane;
	private ArrayList<ContentValues> travelShinkansen;
	private List<NameValuePair> travelParams;
	private String urlPackage;
	private String urlPlane;
	private String urlShinkansen;
	private String urlHotel;
	private String urlCar;

	public ArrayList<StaffModel> getListStaffEnroll() {
		if (listStaffEnroll == null)
			listStaffEnroll = new ArrayList<StaffModel>();
		return listStaffEnroll;
	}

	public void setListStaffEnroll(ArrayList<StaffModel> listStaffEnroll) {
		this.listStaffEnroll = listStaffEnroll;
	}

	public StaffModel getHolder() {
		return holder;
	}

	public void setHolder(StaffModel holder) {
		this.holder = holder;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getFromtime() {
		return fromtime;
	}

	public void setFromtime(String fromtime) {
		this.fromtime = fromtime;
	}

	public String getTotime() {
		return totime;
	}

	public void setTotime(String totime) {
		this.totime = totime;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getExplanation() {
		return explanation;
	}

	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public ArrayList<ContentValues> getTravelPackage() {
		return travelPackage;
	}

	public void setTravelPackage(ArrayList<ContentValues> travelPackage) {
		this.travelPackage = travelPackage;
	}

	public ArrayList<ContentValues> getTravelPlane() {
		return travelPlane;
	}

	public void setTravelPlane(ArrayList<ContentValues> travelPlane) {
		this.travelPlane = travelPlane;
	}

	public ArrayList<ContentValues> getTravelShinkansen() {
		return travelShinkansen;
	}

	public void setTravelShinkansen(ArrayList<ContentValues> travelShinkansen) {
		this.travelShinkansen = travelShinkansen;
	}

	public List<NameValuePair> getTravelParams() {
		return travelParams;
	}

	public void setTravelParams(List<NameValuePair> travelParams) {
		this.travelParams = travelParams;
	}

	public String getUrlPackage() {
		return urlPackage;
	}

	public void setUrlPackage(String urlPackage) {
		this.urlPackage = urlPackage;
	}

	public String getUrlPlane() {
		return urlPlane;
	}

	public void setUrlPlane(String urlPlane) {
		this.urlPlane = urlPlane;
	}

	public String getUrlShinkansen() {
		return urlShinkansen;
	}

	public void setUrlShinkansen(String urlShinkansen) {
		this.urlShinkansen = urlShinkansen;
	}

	public String getUrlHotel() {
		return urlHotel;
	}

	public void setUrlHotel(String urlHotel) {
		this.urlHotel = urlHotel;
	}

	public String getUrlCar() {
		return urlCar;
	}

	public void setUrlCar(String urlCar) {
		this.urlCar = urlCar;
	}

	public String getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(String registerTime) {
		this.registerTime = registerTime;
	}

	public String getDealCode() {
		return dealCode;
	}

	public void setDealCode(String dealCode) {
		this.dealCode = dealCode;
	}

}
