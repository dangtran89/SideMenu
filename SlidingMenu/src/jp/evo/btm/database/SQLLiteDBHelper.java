package jp.evo.btm.database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import jp.evo.btm.R;
import jp.evo.btm.common.ConstantValues;
import jp.evo.btm.model.DepartmentModel;
import jp.evo.btm.model.Result;
import jp.evo.btm.model.StaffModel;
import jp.evo.btm.model.SubDepartmentModel;
import jp.evo.btm.model.TravelModel;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLLiteDBHelper extends SQLiteOpenHelper {

	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	public static final String DATABASE_NAME = "BTM.sqlite";
	public static final String DATABASE_PATH = "/data/data/jp.evo.btm/databases/";

	// Contacts table name
	public static final String TABLE_STAFF = "Staff";
	public static final String TABLE_DIVISION = "Division";
	public static final String TABLE_SUBVIVISION = "SubDivision";
	public static final String TABLE_TRAVEL = "Travel";
	public static final String TABLE_TRAVEL_ENROLL = "TravelEnroll";

	public static final String REFRESH = "refresh";
	public static final String ACTION_EDIT = "edit";
	public static final String ACTION_CREATE = "create";

	SQLiteDatabase myDataBase;

	private Context mContext;

	public SQLLiteDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		System.out.println("dangtb construc sql");
		mContext = context;

		try {
			createDataBase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	/**
	 * This method will create database in application package /databases
	 * directory when first time application launched
	 **/
	public void createDataBase() throws IOException {
		boolean mDataBaseExist = checkDataBase();
		if (!mDataBaseExist) {
			this.getReadableDatabase();
			try {
				copyDataBase();
			} catch (IOException mIOException) {
				mIOException.printStackTrace();
				throw new Error("Error copying database");
			} finally {
				this.close();
			}
		}
	}

	/** This method checks whether database is exists or not **/
	private boolean checkDataBase() {
		try {
			final String mPath = DATABASE_PATH + DATABASE_NAME;
			final File file = new File(mPath);
			if (file.exists())
				return true;
			else
				return false;
		} catch (SQLiteException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * This method will copy database from /assets directory to application
	 * package /databases directory
	 **/
	private void copyDataBase() throws IOException {
		try {

			InputStream mInputStream = mContext.getAssets().open(DATABASE_NAME);
			String outFileName = DATABASE_PATH + DATABASE_NAME;
			OutputStream mOutputStream = new FileOutputStream(outFileName);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = mInputStream.read(buffer)) > 0) {
				mOutputStream.write(buffer, 0, length);
			}
			mOutputStream.flush();
			mOutputStream.close();
			mInputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** This method open database for operations **/
	public boolean openDataBase() throws SQLException {
		String mPath = DATABASE_PATH + DATABASE_NAME;
		myDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.OPEN_READWRITE);
		return myDataBase.isOpen();
	}

	/** This method close database connection and released occupied memory **/
	@Override
	public synchronized void close() {
		if (myDataBase != null)
			myDataBase.close();
		SQLiteDatabase.releaseMemory();
		super.close();
	}

	/****************/
	/***** API ********/
	/****************/
	public Result Login(String username, String password) {
		Result result = new Result();
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + SQLLiteDBHelper.TABLE_STAFF + " WHERE username=?", new String[] { String.valueOf(username) });
		if (cursor != null & cursor.getCount() > 0) {
			cursor.moveToFirst();

			if (cursor.getString(cursor.getColumnIndex("password")).equalsIgnoreCase(password)) {
				StaffModel loginUser = new StaffModel();
				loginUser.setId(cursor.getInt(cursor.getColumnIndex("id")));
				loginUser.setUserName(username);
				loginUser.setRole(cursor.getInt(cursor.getColumnIndex("Role")));
				loginUser.setEmail(cursor.getString(cursor.getColumnIndex("email")));
				loginUser.setSubDivisionID(cursor.getInt(cursor.getColumnIndex("subdivisionID")));
				loginUser.setSubDivisionName(cursor.getString(cursor.getColumnIndex("subdivisionname")));
				result.setReturnObject(loginUser);
				result.setSuccess(true);
			} else {
				result.setSuccess(false);
			}
		} else {
			result.setSuccess(false);
		}
		return result;
	}

	public Result updateRole(int staffID, int role) {
		Result result = new Result();
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("Role", role);
		try {
			System.out.println("dangtb insert result " + db.update(TABLE_STAFF, values, "id=?", new String[] { String.valueOf(staffID) }));
			result.setMessage(mContext.getResources().getString(R.string.change_role_success));
			result.setSuccess(true);
		} catch (Throwable e) {
			result.setMessage(mContext.getResources().getString(R.string.change_role_fail));
			result.setSuccess(false);
		}
		return result;
	}

	/*
	 * public Result getSubDivisionNameByID(String id) { Result result = new
	 * Result(); SQLiteDatabase db = getReadableDatabase(); Cursor cursor =
	 * db.rawQuery("SELECT * FROM " + SQLLiteDBHelper.TABLE_SUBVIVISION +
	 * " WHERE id=?", new String[] { String.valueOf(id) }); if (cursor != null &
	 * cursor.getCount() > 0) { cursor.moveToFirst();
	 * 
	 * SubDepartmentModel model = new SubDepartmentModel();
	 * model.setName(cursor.getString(cursor.getColumnIndex("name")));
	 * model.setIdDepartment
	 * (cursor.getString(cursor.getColumnIndex("divisionID")));
	 * model.setId(cursor.getString(cursor.getColumnIndex("id")));
	 * result.setReturnObject(model); result.setSuccess(true); } else {
	 * result.setSuccess(false); } return result; }
	 */

	public ArrayList<DepartmentModel> getListDepartment() {
		ArrayList<DepartmentModel> listData = new ArrayList<DepartmentModel>();
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + SQLLiteDBHelper.TABLE_DIVISION, null);

		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			do {
				DepartmentModel model = new DepartmentModel();
				model.setId(cursor.getString(0));
				model.setName(cursor.getString(1));
				listData.add(model);
			} while (cursor.moveToNext());
		}
		return listData;
	}

	public ArrayList<SubDepartmentModel> getListSubDepartment(int idCurrentDepartment) {
		ArrayList<SubDepartmentModel> listData = new ArrayList<SubDepartmentModel>();
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + SQLLiteDBHelper.TABLE_SUBVIVISION + " WHERE divisionID=?", new String[] { String.valueOf(idCurrentDepartment) });

		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			do {
				SubDepartmentModel model = new SubDepartmentModel();
				model.setId(cursor.getString(0));
				model.setName(cursor.getString(1));
				listData.add(model);
			} while (cursor.moveToNext());
		}
		return listData;
	}

	public Result newDepartment(String departmentName) {
		Result result = new Result();
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("name", departmentName);
		try {
			System.out.println("dangtb insert result " + db.insert(TABLE_DIVISION, null, values));
			result.setSuccess(true);
		} catch (Throwable e) {
			result.setSuccess(false);
		}
		return result;
	}

	public Result updateDepartment(int departmentID, String departmentName) {
		Result result = new Result();
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("name", departmentName);
		try {
			System.out.println("dangtb insert result " + db.update(TABLE_DIVISION, values, "id=?", new String[] { String.valueOf(departmentID) }));
			result.setSuccess(true);
		} catch (Throwable e) {
			result.setSuccess(false);
		}
		return result;
	}

	public Result newSubDepartment(int idDepartment, String subDepartmentName) {
		Result result = new Result();
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("name", subDepartmentName);
		values.put("divisionID", idDepartment);
		try {
			System.out.println("dangtb insert result " + db.insert(TABLE_SUBVIVISION, null, values));
			result.setSuccess(true);
		} catch (Throwable e) {
			result.setSuccess(false);
		}
		return result;
	}

	public Result updateSubDepartment(int subDepartmentID, String subDepartmentName) {
		Result result = new Result();
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("name", subDepartmentName);
		try {
			System.out.println("dangtb insert result " + db.update(TABLE_SUBVIVISION, values, "id=?", new String[] { String.valueOf(subDepartmentID) }));
			result.setSuccess(true);
		} catch (Throwable e) {
			result.setSuccess(false);
		}
		return result;
	}

	public ArrayList<StaffModel> getListStaff() {
		ArrayList<StaffModel> listData = new ArrayList<StaffModel>();
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + SQLLiteDBHelper.TABLE_STAFF, null);

		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			do {
				StaffModel model = new StaffModel();
				model.setId(cursor.getInt(0));
				model.setUserName(cursor.getString(1));
				model.setSubDivisionName(cursor.getString(cursor.getColumnIndex("subdivisionname")));
				listData.add(model);
			} while (cursor.moveToNext());
		}
		return listData;
	}

	public Result getStaffDetail(int staffID) {
		Result result = new Result();
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + SQLLiteDBHelper.TABLE_STAFF + " WHERE id=?", new String[] { String.valueOf(staffID) });
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			do {
				StaffModel model = new StaffModel();
				model.setId(cursor.getInt(cursor.getColumnIndex("id")));
				model.setUserName(cursor.getString(cursor.getColumnIndex("username")));
				model.setName1(cursor.getString(cursor.getColumnIndex("name1")));
				model.setName2(cursor.getString(cursor.getColumnIndex("name2")));
				model.setName3(cursor.getString(cursor.getColumnIndex("name3")));
				model.setEmail(cursor.getString(cursor.getColumnIndex("email")));
				model.setBirthday(cursor.getString(cursor.getColumnIndex("birthday")));
				model.setGender(cursor.getInt(cursor.getColumnIndex("gender")));
				model.setAddress(cursor.getString(cursor.getColumnIndex("address")));
				model.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
				model.setANA(cursor.getString(cursor.getColumnIndex("ANA")));
				model.setJAL(cursor.getString(cursor.getColumnIndex("JAL")));
				model.setDivisionID(cursor.getInt(cursor.getColumnIndex("divisionID")));
				model.setSubDivisionID(cursor.getInt(cursor.getColumnIndex("subdivisionID")));
				model.setSubDivisionName(cursor.getString(cursor.getColumnIndex("subdivisionname")));
				model.setRole(cursor.getInt(cursor.getColumnIndex("Role")));
				result.setReturnObject(model);
				result.setSuccess(true);
			} while (cursor.moveToNext());
		} else {
			result.setSuccess(false);
		}
		return result;
	}

	public Result updateStaff(int staffID, ContentValues values) {
		Result result = new Result();
		SQLiteDatabase db = getWritableDatabase();
		try {
			System.out.println("dangtb insert result " + db.update(TABLE_STAFF, values, "id=?", new String[] { String.valueOf(staffID) }));
			result.setSuccess(true);
		} catch (Throwable e) {
			result.setSuccess(false);
		}
		return result;
	}

	public Result updateStaffPassword(int staffID, ContentValues values) {
		Result result = new Result();
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + SQLLiteDBHelper.TABLE_STAFF + " WHERE id=?", new String[] { String.valueOf(staffID) });
		if (cursor != null & cursor.getCount() > 0) {
			cursor.moveToFirst();

			if (cursor.getString(cursor.getColumnIndex("password")).equalsIgnoreCase(String.valueOf(values.get("old_pass")))) {
				values.remove("old_pass");
				db = getWritableDatabase();
				try {
					System.out.println("dangtb insert result " + db.update(TABLE_STAFF, values, "id=?", new String[] { String.valueOf(staffID) }));
					result.setSuccess(true);
					result.setMessage(mContext.getString(R.string.msg_change_pass_success));
				} catch (Throwable e) {
					result.setSuccess(false);
				}
			} else {
				result.setSuccess(false);
				result.setMessage(mContext.getString(R.string.msg_wrong_pass));
			}
		} else {
			result.setSuccess(false);
		}

		return result;
	}

	public Result insertNewStaff(ContentValues values) {
		Result result = new Result();
		SQLiteDatabase db = getWritableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + SQLLiteDBHelper.TABLE_STAFF + " WHERE username=?", new String[] { String.valueOf(values.get("username")) });
		if (cursor != null & cursor.getCount() > 0) {
			result.setSuccess(false);
			result.setMessage(mContext.getString(R.string.msg_username_duplicate));
			return result;
		}
		cursor = db.rawQuery("SELECT * FROM " + SQLLiteDBHelper.TABLE_STAFF + " WHERE email=?", new String[] { String.valueOf(values.get("email")) });
		if (cursor != null & cursor.getCount() > 0) {
			result.setSuccess(false);
			result.setMessage(mContext.getString(R.string.msg_email_duplicate));
			return result;
		}
		try {
			System.out.println("dangtb insert result " + db.insert(TABLE_STAFF, null, values));
			result.setSuccess(true);
			result.setMessage(mContext.getString(R.string.msg_create_username_success));
			return result;
		} catch (Throwable e) {
			result.setSuccess(false);
			result.setMessage(mContext.getString(R.string.msg_cannot_create_user));
			return result;
		}
	}

	public ArrayList<StaffModel> searchStaff(String searchValue) {
		ArrayList<StaffModel> listData = new ArrayList<StaffModel>();
		SQLiteDatabase db = getWritableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM Staff Where username like '%" + searchValue + "%' or email like '%" + searchValue + "%' or name1 like '%" + searchValue + "%' or name2 like '%" + searchValue + "%' or name3 like '%" + searchValue
				+ "%' or phone like '%" + searchValue + "%' ", null);

		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			do {
				StaffModel model = new StaffModel();
				model.setId(cursor.getInt(0));
				model.setUserName(cursor.getString(1));
				model.setSubDivisionName(cursor.getString(cursor.getColumnIndex("subdivisionname")));
				listData.add(model);
			} while (cursor.moveToNext());
		}
		return listData;
	}

	public Result deleteDepartment(int departmentID) {
		Result result = new Result();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + SQLLiteDBHelper.TABLE_SUBVIVISION + " WHERE divisionID=?", new String[] { String.valueOf(departmentID) });
		if (cursor != null && cursor.getCount() > 0) {
			result.setSuccess(false);
			result.setMessage(mContext.getString(R.string.delete_fail_contain_sub));
			return result;
		}
		db = this.getWritableDatabase();
		if (1 == db.delete(TABLE_DIVISION, "id=?", new String[] { String.valueOf(departmentID) })) {
			result.setSuccess(true);
			result.setMessage(mContext.getString(R.string.delete_success));
		} else {
			result.setSuccess(false);
			result.setMessage(mContext.getString(R.string.delete_fail));
		}
		return result;
	}

	public Result deleteSubDepartment(int subDepartmentID) {
		Result result = new Result();
		SQLiteDatabase db = this.getWritableDatabase();
		if (1 == db.delete(TABLE_SUBVIVISION, "id=?", new String[] { String.valueOf(subDepartmentID) })) {
			result.setSuccess(true);
			result.setMessage(mContext.getString(R.string.delete_success));
		} else {
			result.setSuccess(false);
			result.setMessage(mContext.getString(R.string.delete_fail));
		}
		return result;
	}

	public Result insertTravel(ContentValues values, ArrayList<StaffModel> listEnroll) {
		Result result = new Result();
		SQLiteDatabase db = getWritableDatabase();
		long id = -1;
		values.remove("stationstatus");
		values.put("stationstatus", ConstantValues.TRAVEL_STATION_STATUS_PENDING_APPLY);
		try {
			id = db.insert(TABLE_TRAVEL, null, values);
			if (id != -1) {
				for (int i = 0; i < listEnroll.size(); i++) {
					ContentValues valuesEnroll = new ContentValues();
					valuesEnroll.put("idTravel", id);
					valuesEnroll.put("idStaff", listEnroll.get(i).getId());
					insertTravelEnroll(valuesEnroll);
				}
			}
			result.setReturnObject(id);
			result.setSuccess(true);
			result.setMessage(mContext.getString(R.string.msg_create_travel_success));
			return result;
		} catch (Throwable e) {
			result.setSuccess(false);
			result.setMessage(mContext.getString(R.string.msg_cannot_create_travel));
			return result;
		}
	}

	public Result updateTravel(long travelID, ContentValues values, ArrayList<StaffModel> listEnroll) {
		Result result = new Result();
		SQLiteDatabase db = getWritableDatabase();
		values.remove("stationstatus");
		values.put("stationstatus", ConstantValues.TRAVEL_STATION_STATUS_PENDING_APPLY);
		try {
			db.update(TABLE_TRAVEL, values, "id=?", new String[] { String.valueOf(travelID) });
			System.out.println("dangtb delete enroll result " + db.delete(TABLE_TRAVEL_ENROLL, "idTravel=?", new String[] { String.valueOf(travelID) }));
			if (travelID != -1) {
				for (int i = 0; i < listEnroll.size(); i++) {
					ContentValues valuesEnroll = new ContentValues();
					valuesEnroll.put("idTravel", travelID);
					valuesEnroll.put("idStaff", listEnroll.get(i).getId());
					insertTravelEnroll(valuesEnroll);
				}
			}
			result.setSuccess(true);
			result.setMessage(mContext.getString(R.string.msg_create_travel_success));
			return result;
		} catch (Throwable e) {
			result.setSuccess(false);
			result.setMessage(mContext.getString(R.string.msg_cannot_create_travel));
			return result;
		}
	}

	public Result insertTravelEnroll(ContentValues values) {
		Result result = new Result();
		SQLiteDatabase db = getWritableDatabase();
		try {
			System.out.println("dangtb insert result " + db.insert(TABLE_TRAVEL_ENROLL, null, values));
			result.setSuccess(true);
			return result;
		} catch (Throwable e) {
			result.setSuccess(false);
			return result;
		}
	}

	public Result getTravelDetail(int travelID) {
		Result result = new Result();
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + SQLLiteDBHelper.TABLE_TRAVEL + " WHERE id=?", new String[] { String.valueOf(travelID) });

		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			do {
				TravelModel model = new TravelModel();
				ContentValues values = new ContentValues();

				values.put("fromtime", cursor.getString(cursor.getColumnIndex("fromtime")));
				values.put("totime", cursor.getString(cursor.getColumnIndex("totime")));
				values.put("purpose", cursor.getString(cursor.getColumnIndex("purpose")));
				values.put("description", cursor.getString(cursor.getColumnIndex("description")));
				values.put("preference", cursor.getString(cursor.getColumnIndex("preference")));
				values.put("phone", cursor.getString(cursor.getColumnIndex("phone")));
				values.put("idholder", cursor.getString(cursor.getColumnIndex("idholder")));
				values.put("stationstatus", cursor.getInt(cursor.getColumnIndex("stationstatus")));
				// model.setValues(values);
				model.setHolder((StaffModel) getStaffDetail(cursor.getInt(cursor.getColumnIndex("idholder"))).getReturnObject());
				model.setListStaffEnroll(getListStaffEnroll(travelID));
				result.setReturnObject(model);
				result.setSuccess(true);
			} while (cursor.moveToNext());
		} else {
			result.setSuccess(false);
		}
		return result;
	}

	public ArrayList<StaffModel> getListStaffEnroll(int travelID) {
		ArrayList<StaffModel> listData = new ArrayList<StaffModel>();
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + SQLLiteDBHelper.TABLE_TRAVEL_ENROLL + " WHERE idTravel=?", new String[] { String.valueOf(travelID) });

		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			do {
				listData.add((StaffModel) getStaffDetail(cursor.getInt(cursor.getColumnIndex("idStaff"))).getReturnObject());
			} while (cursor.moveToNext());
		}
		return listData;
	}

	public ArrayList<TravelModel> searchTravel(ContentValues values) {
		ArrayList<TravelModel> listData = new ArrayList<TravelModel>();
		SQLiteDatabase db = getWritableDatabase();
		String fromtime = "";
		String totime = "";

		if (values.getAsString("fromtime") != null && !values.getAsString("fromtime").equalsIgnoreCase(""))
			fromtime = "fromtime >= '" + values.getAsString("fromtime") + "' and";
		if (values.getAsString("totime") != null && !values.getAsString("totime").equalsIgnoreCase(""))
			totime = " totime <= '" + values.getAsString("totime") + "' and";

		String query = "SELECT * FROM Travel Where " + fromtime + totime + " purpose like '%" + values.getAsString("purpose") + "%' ";
		Cursor cursor = db.rawQuery(query, null);

		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			do {
				TravelModel model = new TravelModel();
				model.setId(cursor.getInt(cursor.getColumnIndex("id")));
				model.setFromtime(cursor.getString(cursor.getColumnIndex("fromtime")));
				model.setTotime(cursor.getString(cursor.getColumnIndex("totime")));
				model.setStatus(cursor.getInt(cursor.getColumnIndex("stationstatus")));
				listData.add(model);
			} while (cursor.moveToNext());
		}
		return listData;
	}
}
