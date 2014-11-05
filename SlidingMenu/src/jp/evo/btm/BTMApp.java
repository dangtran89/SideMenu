package jp.evo.btm;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jp.evo.btm.common.ConstantValues;
import jp.evo.btm.database.APILoader;
import jp.evo.btm.database.SQLLiteDBHelper;
import jp.evo.btm.model.DepartmentModel;
import jp.evo.btm.model.StaffModel;
import jp.evo.btm.model.SubDepartmentModel;
import jp.evo.btm.model.TravelModel;

import android.app.Application;
import android.content.ContentValues;

public class BTMApp extends Application {

	// private ExecutorService appExecutorService;
	private SQLLiteDBHelper helper;
	private ArrayList<DepartmentModel> listDepartment;

	private int currentStaffID = -1;

	private DepartmentModel currentDeparment;
	private SubDepartmentModel currentSubDeparment;

	private StaffModel loginUser;

	private TravelModel travelValues;

	private ArrayList<TravelModel> listSearchTravel;

	private int currentTravelID;

	private APILoader loader;

	private int typeOfTicket = -1;

	private String currentFilter = APILoader.FILTER_NONE;

	// public ExecutorService getAppExecutor() {
	// if (appExecutorService == null)
	// appExecutorService = Executors.newFixedThreadPool(5);
	// return appExecutorService;
	// }

	public SQLLiteDBHelper getSQLiteDBHelper() {
		if (helper == null)
			helper = new SQLLiteDBHelper(getApplicationContext());
		return helper;
	}

	public APILoader getAPILoader() {
		if (loader == null)
			loader = new APILoader(getApplicationContext());
		return loader;
	}

	public ArrayList<DepartmentModel> getListDepartment() {
		return listDepartment;
	}

	public void setListDepartment(ArrayList<DepartmentModel> listDepartment) {
		this.listDepartment = listDepartment;
	}

	public int getCurrentStaffID() {
		return currentStaffID;
	}

	public void setCurrentStaffID(int currentStaffID) {
		this.currentStaffID = currentStaffID;
	}

	public DepartmentModel getCurrentDeparment() {
		return currentDeparment;
	}

	public void setCurrentDeparment(DepartmentModel currentDeparment) {
		this.currentDeparment = currentDeparment;
	}

	public SubDepartmentModel getCurrentSubDeparment() {
		return currentSubDeparment;
	}

	public void setCurrentSubDeparment(SubDepartmentModel currentSubDeparment) {
		this.currentSubDeparment = currentSubDeparment;
	}

	public StaffModel getLoginUser() {
		return loginUser;
	}

	public void setLoginUser(StaffModel loginUser) {
		this.loginUser = loginUser;
	}

	public TravelModel getTravelValues() {
		return travelValues;
	}

	public void setTravelValues(TravelModel travelValues) {
		this.travelValues = travelValues;
	}

	public ArrayList<TravelModel> getListSearchTravel() {
		return listSearchTravel;
	}

	public void setListSearchTravel(ArrayList<TravelModel> listSearchTravel) {
		this.listSearchTravel = listSearchTravel;
	}

	public int getCurrentTravelID() {
		return currentTravelID;
	}

	public void setCurrentTravelID(int currentTravelID) {
		this.currentTravelID = currentTravelID;
	}

	public int getTypeOfTicket() {
		return typeOfTicket;
	}

	public void setTypeOfTicket(int typeOfTicket) {
		this.typeOfTicket = typeOfTicket;
	}

	public String getCurrentFilter() {
		return currentFilter;
	}

	public void setCurrentFilter(String currentFilter) {
		this.currentFilter = currentFilter;
	}

}
