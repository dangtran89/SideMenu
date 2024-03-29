package jp.evo.btm.database;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jp.evo.btm.R;
import jp.evo.btm.common.ConstantValues;
import jp.evo.btm.model.DepartmentModel;
import jp.evo.btm.model.Result;
import jp.evo.btm.model.StaffModel;
import jp.evo.btm.model.SubDepartmentModel;
import jp.evo.btm.model.TravelModel;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;

public class APILoader {

	private Context mContext;
	private final String API_HEADER = "BTMTEST-API-KEY";
	private final String api_login = "/user/login";
	private final String api_insert_department = "/department/insert";
	private final String api_update_department = "/department/update";
	private final String api_department_list = "/department/list";
	private final String api_department_delete = "/department/delete";
	private final String api_staff_insert = "/user/insert";
	private final String api_staff_list = "/user/list";
	private final String api_staff_update = "/user/update";
	private final String api_staff_change_pass = "/user/change_password";
	private final String api_staff_reset_pass = "/user/reset_password";
	private final String api_staff_delete = "/user/delete";
	private final String api_staff_change_role = "/user/change_role";
	private final String api_travel_register = "/business_trip/register";
	private final String api_travel_search = "/business_trip/search";
	private final String api_travel_detail = "/business_trip/detail";
	private final String api_travel_apply = "/business_trip/apply";
	private final String api_travel_approve = "/business_trip/consent";
	private final String api_travel_reject = "/business_trip/reject";

	private final String data = "data";
	private final String status = "status";
	private final String message = "message";
	public static final String REFRESH = "refresh";
	public static final String FILTER_APPLY = "apply";
	public static final String FILTER_APPROVE = "approve";
	public static final String FILTER_CANCEL = "cancel";
	public static final String FILTER_OK = "approved";
	public static final String FILTER_NONE = "None";

	private final int DEPARTMENT_TYPE = 0;
	private final int SUB_DEPARTMENT_TYPE = 1;
	// DEBUG
	private final int SHOW_DETAIL = 1;
	private final String mresultNull = "{\"result\":{\"is_success\":\"false\",\"is_show_title\":\"true\",\"is_show_message\":\"true\",\"message\":\"Server connection error!\"},\"id\":\"getuser\",\"error\":null,\"jsonrpc\":\"2.0\"}";

	public APILoader(Context context) {
		mContext = context;
	}

	private String getJsonRespone(String api, List<NameValuePair> params) {
		String result = null;
		// connect to map web service
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(mContext.getResources().getString(R.string.api_url) + api);
		HttpResponse response;
		InputStream is = null;

		try {
			// set header
			httppost.setHeader(API_HEADER, "55587a910882016321201e6ebbc9f595");
			// put params to the post
			// Add your data
			// for (int i = 0; i < params.size(); i++) {
			// BasicNameValuePair value = (BasicNameValuePair) params.get(i);
			// value = new BasicNameValuePair(value.getName(), new
			// String(value.getValue().getBytes(), "UTF-8"));
			// params.set(i, value);
			// }
			httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			System.out.println("dangtb api request " + params + " api: " + api);

			// ... other java code to execute the apache httpclient
			response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
			StringBuilder sb = new StringBuilder();
			sb.append(reader.readLine() + "\n");
			String line = "0";
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			reader.close();
			result = sb.toString();
			System.out.println("dangtb api result " + result);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			result = mresultNull;
			e.printStackTrace();
		} finally {
			// close stream
			closeStream(is);
		}
		return result;
	}

	private String getWebView(String api, List<NameValuePair> params) {
		String result = null;
		// connect to map web service
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(api);
		HttpResponse response;
		InputStream is = null;

		try {
			// set header
			httppost.setHeader(API_HEADER, "55587a910882016321201e6ebbc9f595");
			// put params to the post
			// Add your data
			// for (int i = 0; i < params.size(); i++) {
			// BasicNameValuePair value = (BasicNameValuePair) params.get(i);
			// value = new BasicNameValuePair(value.getName(), new
			// String(value.getValue().getBytes(), "UTF-8"));
			// params.set(i, value);
			// }
			httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			System.out.println("dangtb api request " + params + " api: " + api);

			// ... other java code to execute the apache httpclient
			response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "EUC-JP"), 8);
			StringBuilder sb = new StringBuilder();
			sb.append(reader.readLine() + "\n");
			String line = "0";
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			reader.close();
			result = sb.toString();
			System.out.println("dangtb api result " + result);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			result = mresultNull;
			e.printStackTrace();
		} finally {
			// close stream
			closeStream(is);
		}
		return result;
	}

	void closeStream(Closeable stream) {
		try {
			stream.close();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	/****************/
	/***** API ********/
	/****************/
	public Result Login(String username, String password , String companyName) {
		Result result = new Result();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("username", username));
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("company_name", companyName));
		try {
			JSONObject jsonResult = new JSONObject(getJsonRespone(api_login, params));
			if (jsonResult.getInt(status) == ConstantValues.INT_VALUE_TRUE) {
				JSONObject object = jsonResult.getJSONArray(data).getJSONObject(0);
				StaffModel loginUser = new StaffModel();
				loginUser.setId(object.getInt("id"));
				loginUser.setUserName(object.getString("staff_name_sei_kanji") + " " + object.getString("staff_name_mei_kanji"));
				loginUser.setRole(object.getInt("role_id"));
				loginUser.setEmail(object.getString("email"));
				loginUser.setSubDivisionID(object.getInt("section_id"));
				loginUser.setSubDivisionName(object.getString("section_name"));
				loginUser.setDivisionID(object.getInt("branch_id"));
				loginUser.setDivisionName(object.getString("branch_name"));
				loginUser.setCompanyID(object.getInt("company_id"));
				result.setReturnObject(loginUser);
				result.setSuccess(true);
			} else {
				result.setSuccess(false);
			}
			result.setMessage(jsonResult.getString(message));
		} catch (Throwable e) {
			e.printStackTrace();
			result.setMessage(mContext.getResources().getString(R.string.error_network));
			result.setSuccess(false);
		}
		return result;
	}

	public Result newDepartment(String departmentName, int companyID, int userID) {
		Result result = new Result();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("company_id", String.valueOf(companyID)));
		params.add(new BasicNameValuePair("user_id", String.valueOf(userID)));
		params.add(new BasicNameValuePair("name", departmentName));
		params.add(new BasicNameValuePair("ptn", String.valueOf(DEPARTMENT_TYPE)));
		try {
			JSONObject jsonResult = new JSONObject(getJsonRespone(api_insert_department, params));
			if (jsonResult.getInt(status) == ConstantValues.INT_VALUE_TRUE)
				result.setSuccess(true);
			else
				result.setSuccess(false);
			result.setMessage(jsonResult.getString(message));
		} catch (Throwable e) {
			e.printStackTrace();
			result.setMessage(mContext.getResources().getString(R.string.error_network));
			result.setSuccess(false);
		}
		return result;
	}

	public Result updateDepartment(int departmentID, String departmentOldName, String departmentNewName, int companyID, int userID) {
		Result result = new Result();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("id", String.valueOf(departmentID)));
		params.add(new BasicNameValuePair("company_id", String.valueOf(companyID)));
		params.add(new BasicNameValuePair("user_id", String.valueOf(userID)));
		params.add(new BasicNameValuePair("current_name", departmentOldName));
		params.add(new BasicNameValuePair("new_name", departmentNewName));
		try {
			JSONObject jsonResult = new JSONObject(getJsonRespone(api_update_department, params));
			if (jsonResult.getInt(status) == ConstantValues.INT_VALUE_TRUE)
				result.setSuccess(true);
			else
				result.setSuccess(false);
			result.setMessage(jsonResult.getString(message));
		} catch (Throwable e) {
			e.printStackTrace();
			result.setMessage(mContext.getResources().getString(R.string.error_network));
			result.setSuccess(false);
		}
		return result;
	}

	public Result newSubDepartment(int departmentID, String departmentName, int companyID, int userID) {
		Result result = new Result();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("company_id", String.valueOf(companyID)));
		params.add(new BasicNameValuePair("user_id", String.valueOf(userID)));
		params.add(new BasicNameValuePair("branch_id", String.valueOf(departmentID)));
		params.add(new BasicNameValuePair("name", departmentName));
		params.add(new BasicNameValuePair("ptn", String.valueOf(SUB_DEPARTMENT_TYPE)));
		try {
			JSONObject jsonResult = new JSONObject(getJsonRespone(api_insert_department, params));
			if (jsonResult.getInt(status) == ConstantValues.INT_VALUE_TRUE)
				result.setSuccess(true);
			else
				result.setSuccess(false);
			result.setMessage(jsonResult.getString(message));
		} catch (Throwable e) {
			e.printStackTrace();
			result.setMessage(mContext.getResources().getString(R.string.error_network));
			result.setSuccess(false);
		}
		return result;
	}

	public Result updateSubDepartment(int departmentID, String departmentOldName, String departmentNewName, int companyID, int userID) {
		Result result = new Result();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("id", String.valueOf(departmentID)));
		params.add(new BasicNameValuePair("company_id", String.valueOf(companyID)));
		params.add(new BasicNameValuePair("user_id", String.valueOf(userID)));
		params.add(new BasicNameValuePair("current_name", departmentOldName));
		params.add(new BasicNameValuePair("new_name", departmentNewName));
		try {
			JSONObject jsonResult = new JSONObject(getJsonRespone(api_update_department, params));
			if (jsonResult.getInt(status) == ConstantValues.INT_VALUE_TRUE)
				result.setSuccess(true);
			else
				result.setSuccess(false);
			result.setMessage(jsonResult.getString(message));
		} catch (Throwable e) {
			e.printStackTrace();
			result.setMessage(mContext.getResources().getString(R.string.error_network));
			result.setSuccess(false);
		}
		return result;
	}

	public Result getListDepartment(int companyID, int userID) {
		Result result = new Result();
		ArrayList<DepartmentModel> listData = new ArrayList<DepartmentModel>();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("company_id", String.valueOf(companyID)));
		params.add(new BasicNameValuePair("user_id", String.valueOf(userID)));
		params.add(new BasicNameValuePair("ptn", String.valueOf(DEPARTMENT_TYPE)));

		try {
			JSONObject jsonResult = new JSONObject(getJsonRespone(api_department_list, params));
			if (jsonResult.getInt(status) == ConstantValues.INT_VALUE_TRUE) {
				JSONArray arrayData = jsonResult.getJSONArray(data);
				for (int i = 0; i < arrayData.length(); i++) {
					JSONObject object = arrayData.getJSONObject(i);
					DepartmentModel model = new DepartmentModel();
					model.setId(object.getString("id"));
					model.setName(object.getString("name"));
					listData.add(model);
				}
				result.setReturnObject(listData);
				result.setSuccess(true);
			} else {
				result.setSuccess(false);
			}
			result.setMessage(jsonResult.getString(message));
		} catch (Throwable e) {
			e.printStackTrace();
			result.setMessage(mContext.getResources().getString(R.string.error_network));
			result.setSuccess(false);
		}
		return result;
	}

	public Result getListSubDepartment(int idCurrentDepartment, int companyID, int userID) {
		Result result = new Result();
		ArrayList<SubDepartmentModel> listData = new ArrayList<SubDepartmentModel>();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("company_id", String.valueOf(companyID)));
		params.add(new BasicNameValuePair("user_id", String.valueOf(userID)));
		params.add(new BasicNameValuePair("branch_id", String.valueOf(idCurrentDepartment)));
		params.add(new BasicNameValuePair("ptn", String.valueOf(SUB_DEPARTMENT_TYPE)));
		try {
			JSONObject jsonResult = new JSONObject(getJsonRespone(api_department_list, params));
			if (jsonResult.getInt(status) == ConstantValues.INT_VALUE_TRUE) {
				JSONArray arrayData = jsonResult.getJSONArray(data);
				for (int i = 0; i < arrayData.length(); i++) {
					JSONObject object = arrayData.getJSONObject(i);
					SubDepartmentModel model = new SubDepartmentModel();
					model.setId(object.getString("id"));
					model.setName(object.getString("name"));
					listData.add(model);
				}
				result.setReturnObject(listData);
				result.setSuccess(true);
			} else {
				result.setSuccess(false);
			}
			result.setMessage(jsonResult.getString(message));
		} catch (Throwable e) {
			e.printStackTrace();
			result.setMessage(mContext.getResources().getString(R.string.error_network));
			result.setSuccess(false);
		}
		return result;
	}

	public Result deleteDepartment(int departmentID, int companyID, int userID) {
		Result result = new Result();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("company_id", String.valueOf(companyID)));
		params.add(new BasicNameValuePair("user_id", String.valueOf(userID)));
		params.add(new BasicNameValuePair("id", String.valueOf(departmentID)));
		try {
			JSONObject jsonResult = new JSONObject(getJsonRespone(api_department_delete, params));
			if (jsonResult.getInt(status) == ConstantValues.INT_VALUE_TRUE) {
				result.setSuccess(true);
			} else {
				result.setSuccess(false);
			}
			result.setMessage(jsonResult.getString(message));
		} catch (Throwable e) {
			e.printStackTrace();
			result.setMessage(mContext.getResources().getString(R.string.error_network));
			result.setSuccess(false);
		}
		return result;
	}

	public Result deleteSubDepartment(int departmentID, int companyID, int userID) {
		Result result = new Result();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("company_id", String.valueOf(companyID)));
		params.add(new BasicNameValuePair("user_id", String.valueOf(userID)));
		params.add(new BasicNameValuePair("id", String.valueOf(departmentID)));
		try {
			JSONObject jsonResult = new JSONObject(getJsonRespone(api_department_delete, params));
			if (jsonResult.getInt(status) == ConstantValues.INT_VALUE_TRUE) {
				result.setSuccess(true);
			} else {
				result.setSuccess(false);
			}
			result.setMessage(jsonResult.getString(message));
		} catch (Throwable e) {
			e.printStackTrace();
			result.setMessage(mContext.getResources().getString(R.string.error_network));
			result.setSuccess(false);
		}
		return result;
	}

	public Result insertNewStaff(List<NameValuePair> values, int companyID, int userID) {
		Result result = new Result();
		values.add(new BasicNameValuePair("company_id", String.valueOf(companyID)));
		values.add(new BasicNameValuePair("user_id", String.valueOf(userID)));
		values.add(new BasicNameValuePair("show_detail", String.valueOf(SHOW_DETAIL)));

		try {
			JSONObject jsonResult = new JSONObject(getJsonRespone(api_staff_insert, values));
			if (jsonResult.getInt(status) == ConstantValues.INT_VALUE_TRUE) {
				result.setSuccess(true);
			} else {
				result.setSuccess(false);
			}
			result.setMessage(jsonResult.getString(message));
		} catch (Throwable e) {
			e.printStackTrace();
			result.setMessage(mContext.getResources().getString(R.string.error_network));
			result.setSuccess(false);
		}
		return result;
	}

	public Result getListStaff(int companyID, int userID) {
		Result result = new Result();
		ArrayList<StaffModel> listData = new ArrayList<StaffModel>();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("company_id", String.valueOf(companyID)));
		params.add(new BasicNameValuePair("user_id", String.valueOf(userID)));

		try {
			JSONObject jsonResult = new JSONObject(getJsonRespone(api_staff_list, params));
			if (jsonResult.getInt(status) == ConstantValues.INT_VALUE_TRUE) {
				JSONArray arrayData = jsonResult.getJSONArray(data);
				for (int i = 0; i < arrayData.length(); i++) {
					JSONObject object = arrayData.getJSONObject(i);
					StaffModel model = new StaffModel();
					model.setId(object.getInt("id"));
					model.setUserName(object.getString("staff_name_sei_kanji") + " " + object.getString("staff_name_mei_kanji"));
					model.setSubDivisionName(object.getString("section_name"));
					listData.add(model);
				}
				result.setReturnObject(listData);
				result.setSuccess(true);
			} else {
				result.setSuccess(false);
			}
			result.setMessage(jsonResult.getString(message));
		} catch (Throwable e) {
			e.printStackTrace();
			result.setMessage(mContext.getResources().getString(R.string.error_network));
			result.setSuccess(false);
		}
		return result;
	}

	public Result getStaffDetail(int staffID, int companyID, int userID) {
		Result result = new Result();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("company_id", String.valueOf(companyID)));
		params.add(new BasicNameValuePair("user_id", String.valueOf(userID)));
		params.add(new BasicNameValuePair("staff_id", String.valueOf(staffID)));
		try {
			JSONObject jsonResult = new JSONObject(getJsonRespone(api_staff_list, params));
			if (jsonResult.getInt(status) == ConstantValues.INT_VALUE_TRUE) {
				JSONObject object = jsonResult.getJSONArray(data).getJSONObject(0);
				StaffModel model = new StaffModel();
				model.setId(object.getInt("id"));
				model.setUserName(object.getString("staff_name_sei_kanji") + " " + object.getString("staff_name_mei_kanji"));
				model.setName1(object.getString("staff_name_sei_kanji") + " " + object.getString("staff_name_mei_kanji"));
				model.setName2(object.getString("staff_name_sei_furi") + " " + object.getString("staff_name_mei_furi"));
				model.setName3(object.getString("staff_name_sei_roma") + " " + object.getString("staff_name_mei_roma"));
				model.setEmail(object.getString("staff_email"));
				model.setBirthday(object.getString("birth_ymd"));
				model.setGender(object.getInt("staff_sex"));
				model.setAddress(object.getString("address"));
				model.setPhone(object.getString("tel1"));
				model.setANA(object.getString("staff_mileage_ana"));
				model.setJAL(object.getString("staff_mileage_jal"));
				model.setDivisionID(object.getInt("branch_id"));
				model.setSubDivisionID(object.getInt("section_id"));
				model.setSubDivisionName(object.getString("section_name"));
				model.setRole(object.getInt("role_id"));
				model.setCompanyID(object.getInt("company_id"));
				result.setReturnObject(model);
				result.setSuccess(true);
			} else {
				result.setSuccess(false);
			}
			result.setMessage(jsonResult.getString(message));
		} catch (Throwable e) {
			e.printStackTrace();
			result.setMessage(mContext.getResources().getString(R.string.error_network));
			result.setSuccess(false);
		}
		return result;
	}

	public Result searchStaff(String searchValue, int companyID, int userID) {
		Result result = new Result();
		ArrayList<StaffModel> listData = new ArrayList<StaffModel>();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("company_id", String.valueOf(companyID)));
		params.add(new BasicNameValuePair("user_id", String.valueOf(userID)));
		params.add(new BasicNameValuePair("search_string", searchValue));

		try {
			JSONObject jsonResult = new JSONObject(getJsonRespone(api_staff_list, params));
			if (jsonResult.getInt(status) == ConstantValues.INT_VALUE_TRUE) {
				JSONArray arrayData = jsonResult.getJSONArray(data);
				for (int i = 0; i < arrayData.length(); i++) {
					JSONObject object = arrayData.getJSONObject(i);
					StaffModel model = new StaffModel();
					model.setId(object.getInt("id"));
					model.setUserName(object.getString("staff_name_sei_kanji") + " " + object.getString("staff_name_mei_kanji"));
					model.setSubDivisionName(object.getString("section_name"));
					listData.add(model);
				}
				result.setReturnObject(listData);
				result.setSuccess(true);
			} else {
				result.setSuccess(false);
			}
			result.setMessage(jsonResult.getString(message));
		} catch (Throwable e) {
			e.printStackTrace();
			result.setMessage(mContext.getResources().getString(R.string.error_network));
			result.setSuccess(false);
		}
		return result;
	}

	public Result updateStaff(int staffID, List<NameValuePair> values, int companyID, int userID) {
		Result result = new Result();
		values.add(new BasicNameValuePair("company_id", String.valueOf(companyID)));
		values.add(new BasicNameValuePair("user_id", String.valueOf(userID)));
		try {
			JSONObject jsonResult = new JSONObject(getJsonRespone(api_staff_update, values));
			if (jsonResult.getInt(status) == ConstantValues.INT_VALUE_TRUE) {
				result.setSuccess(true);
			} else {
				result.setSuccess(false);
			}
			result.setMessage(jsonResult.getString(message));
		} catch (Throwable e) {
			e.printStackTrace();
			result.setMessage(mContext.getResources().getString(R.string.error_network));
			result.setSuccess(false);
		}
		return result;
	}

	public Result updateStaffPassword(int staffID, int companyID, int userID, String oldPass, String newpass) {
		Result result = new Result();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("company_id", String.valueOf(companyID)));
		params.add(new BasicNameValuePair("user_id", String.valueOf(userID)));
		params.add(new BasicNameValuePair("cur_password", oldPass));
		params.add(new BasicNameValuePair("new_password", newpass));
		try {
			JSONObject jsonResult = new JSONObject(getJsonRespone(api_staff_change_pass, params));
			if (jsonResult.getInt(status) == ConstantValues.INT_VALUE_TRUE) {
				result.setSuccess(true);
			} else {
				result.setSuccess(false);
			}
			result.setMessage(jsonResult.getString(message));
		} catch (Throwable e) {
			e.printStackTrace();
			result.setMessage(mContext.getResources().getString(R.string.error_network));
			result.setSuccess(false);
		}
		return result;
	}

	public Result updateRole(int staffID, int role, int companyID, int userID) {
		Result result = new Result();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("company_id", String.valueOf(companyID)));
		params.add(new BasicNameValuePair("user_id", String.valueOf(userID)));
		params.add(new BasicNameValuePair("staff_id", String.valueOf(staffID)));
		params.add(new BasicNameValuePair("role_id", String.valueOf(role)));
		try {
			JSONObject jsonResult = new JSONObject(getJsonRespone(api_staff_change_role, params));
			if (jsonResult.getInt(status) == ConstantValues.INT_VALUE_TRUE) {
				result.setSuccess(true);
			} else {
				result.setSuccess(false);
			}
			result.setMessage(jsonResult.getString(message));
		} catch (Throwable e) {
			e.printStackTrace();
			result.setMessage(mContext.getResources().getString(R.string.error_network));
			result.setSuccess(false);
		}
		return result;
	}

	public Result insertTravel(TravelModel values, ArrayList<StaffModel> listEnroll, int companyID, int userID) {
		Result result = new Result();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("company_id", String.valueOf(companyID)));
		params.add(new BasicNameValuePair("user_id", String.valueOf(userID)));
		params.add(new BasicNameValuePair("trip_start", values.getFromtime()));
		params.add(new BasicNameValuePair("trip_end", values.getTotime()));
		params.add(new BasicNameValuePair("purpose", values.getPurpose()));
		params.add(new BasicNameValuePair("explanation", values.getExplanation()));
		params.add(new BasicNameValuePair("note", values.getNote()));
		params.add(new BasicNameValuePair("tel", values.getPhone()));
		params.add(new BasicNameValuePair("show_detail", String.valueOf(SHOW_DETAIL)));
		String travellers = "";
		for (int i = 0; i < listEnroll.size(); i++) {
			if (i == 0)
				travellers = travellers + listEnroll.get(i).getId();
			else
				travellers = travellers + "," + listEnroll.get(i).getId();
		}
		params.add(new BasicNameValuePair("traveller_ids", String.valueOf(travellers)));

		try {
			JSONObject jsonResult = new JSONObject(getJsonRespone(api_travel_register, params));
			if (jsonResult.getInt(status) == ConstantValues.INT_VALUE_TRUE) {
				JSONObject object = jsonResult.getJSONObject(data);
				values.setId(object.getInt("business_trip_id"));
				result.setReturnObject(values);
				result.setSuccess(true);
			} else {
				result.setSuccess(false);
			}
			result.setMessage(jsonResult.getString(message));
		} catch (Throwable e) {
			e.printStackTrace();
			result.setMessage(mContext.getResources().getString(R.string.error_network));
			result.setSuccess(false);
		}
		return result;
	}

	public Result updateTravel(TravelModel values, ArrayList<StaffModel> listEnroll, int companyID, int userID) {
		Result result = new Result();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("trip_id", "-1"));
		params.add(new BasicNameValuePair("company_id", String.valueOf(companyID)));
		params.add(new BasicNameValuePair("user_id", String.valueOf(userID)));
		params.add(new BasicNameValuePair("trip_start", values.getFromtime()));
		params.add(new BasicNameValuePair("trip_end", values.getTotime()));
		params.add(new BasicNameValuePair("purpose", values.getPurpose()));
		params.add(new BasicNameValuePair("explanation", values.getExplanation()));
		params.add(new BasicNameValuePair("note", values.getNote()));
		params.add(new BasicNameValuePair("tel", values.getPhone()));
		String travellers = "";
		for (int i = 0; i < listEnroll.size(); i++) {
			if (i == 0)
				travellers = travellers + listEnroll.get(i).getId();
			else
				travellers = travellers + "," + listEnroll.get(i).getId();
		}
		params.add(new BasicNameValuePair("traveller_ids", String.valueOf(travellers)));

		try {
			JSONObject jsonResult = new JSONObject(getJsonRespone(api_travel_register, params));
			if (jsonResult.getInt(status) == ConstantValues.INT_VALUE_TRUE) {
				JSONObject object = jsonResult.getJSONArray(data).getJSONObject(0);
				values.setId(object.getInt("business_trip_id"));
				result.setReturnObject(values);
				result.setSuccess(true);
			} else {
				result.setSuccess(false);
			}
			result.setMessage(jsonResult.getString(message));
		} catch (Throwable e) {
			e.printStackTrace();
			result.setMessage(mContext.getResources().getString(R.string.error_network));
			result.setSuccess(false);
		}
		return result;
	}

	public Result getTravelDetail(int travelID, int companyID, int userID) {
		Result result = new Result();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("business_trip_id", String.valueOf(travelID)));
		params.add(new BasicNameValuePair("company_id", String.valueOf(companyID)));
		params.add(new BasicNameValuePair("user_id", String.valueOf(userID)));
		TravelModel model = new TravelModel();
		try {
			JSONObject jsonResult = new JSONObject(getJsonRespone(api_travel_detail, params));
			if (jsonResult.getInt(status) == ConstantValues.INT_VALUE_TRUE) {
				JSONObject objectDetail = jsonResult.getJSONObject(data).getJSONObject("detail");

				model.setId(objectDetail.getInt("id"));
				model.setFromtime(objectDetail.getString("trip_start"));
				model.setTotime(objectDetail.getString("trip_end"));
				model.setPurpose(objectDetail.getString("purpose"));
				model.setExplanation(objectDetail.getString("explanation"));
				model.setNote(objectDetail.getString("note"));
				model.setPhone(objectDetail.getString("tel"));
				model.setStatus(objectDetail.getInt("trip_wf"));

				Result resultDetail = getStaffDetail(objectDetail.getInt("registrant_id"), companyID, userID);
				if (resultDetail.isSuccess())
					model.setHolder((StaffModel) getStaffDetail(objectDetail.getInt("registrant_id"), companyID, userID).getReturnObject());
				else {
					result.setSuccess(false);
					result.setMessage(resultDetail.getMessage());
					return result;
				}

				JSONArray objectTraveler = jsonResult.getJSONObject(data).getJSONArray("travellers");
				ArrayList<StaffModel> listEnroll = new ArrayList<StaffModel>();

				for (int i = 0; i < objectTraveler.length(); i++) {
					JSONObject staffInstance = objectTraveler.getJSONObject(i);
					StaffModel staff = new StaffModel();
					staff.setId(staffInstance.getInt("user_id"));
					staff.setDivisionID(staffInstance.getInt("branch_id"));
					staff.setDivisionName(staffInstance.getString("branch_name"));
					staff.setSubDivisionID(staffInstance.getInt("section_id"));
					staff.setSubDivisionName(staffInstance.getString("section_name"));
					staff.setUserName(staffInstance.getString("last_name") + " " + staffInstance.getString("first_name"));
					staff.setEmail(staffInstance.getString("email"));
					listEnroll.add(staff);
				}

				model.setListStaffEnroll(listEnroll);
				model.setUrlPackage(jsonResult.getJSONObject(data).getString("url_package"));
				model.setUrlPlane(jsonResult.getJSONObject(data).getString("url_plane"));
				model.setUrlShinkansen(jsonResult.getJSONObject(data).getString("url_shinkansen"));
				model.setUrlHotel(jsonResult.getJSONObject(data).getString("url_hotel"));
				model.setUrlCar(jsonResult.getJSONObject(data).getString("url_car"));

				boolean isPendingTicket = true;
				JSONArray arrayInfo = null;
				arrayInfo = jsonResult.getJSONObject(data).getJSONArray("package");
				if (arrayInfo != null && arrayInfo.length() > 0) {
					ArrayList<ContentValues> listValues = new ArrayList<ContentValues>();
					for (int i = 0; i < arrayInfo.length(); i++) {
						JSONObject instance = arrayInfo.getJSONObject(i);
						ContentValues packageParams = new ContentValues();
						Iterator<?> iter = instance.keys();
						while (iter.hasNext()) {
							String key = (String) iter.next();
							try {
								packageParams.put(key, instance.getString(key));
							} catch (Throwable e) {
								e.printStackTrace();
							}
						}
						listValues.add(packageParams);
					}
					isPendingTicket = false;
					model.setTravelPackage(listValues);
				}

				arrayInfo = null;
				arrayInfo = jsonResult.getJSONObject(data).getJSONArray("plane");
				if (arrayInfo != null && arrayInfo.length() > 0) {
					ArrayList<ContentValues> listValues = new ArrayList<ContentValues>();
					for (int i = 0; i < arrayInfo.length(); i++) {
						JSONObject instance = arrayInfo.getJSONObject(i);
						ContentValues planeParams = new ContentValues();
						Iterator<?> iter = instance.keys();
						while (iter.hasNext()) {
							String key = (String) iter.next();
							try {
								planeParams.put(key, instance.getString(key));
							} catch (Throwable e) {
								e.printStackTrace();
							}
						}
						listValues.add(planeParams);
					}
					isPendingTicket = false;
					model.setTravelPlane(listValues);
				}

				arrayInfo = null;
				arrayInfo = jsonResult.getJSONObject(data).getJSONArray("shinkansen");
				if (arrayInfo != null && arrayInfo.length() > 0) {
					ArrayList<ContentValues> listValues = new ArrayList<ContentValues>();
					for (int i = 0; i < arrayInfo.length(); i++) {
						JSONObject instance = arrayInfo.getJSONObject(i);
						ContentValues shinkansenParams = new ContentValues();
						Iterator<?> iter = instance.keys();
						while (iter.hasNext()) {
							String key = (String) iter.next();
							try {
								shinkansenParams.put(key, instance.getString(key));
							} catch (Throwable e) {
								e.printStackTrace();
							}
						}
						listValues.add(shinkansenParams);
					}
					isPendingTicket = false;
					model.setTravelShinkansen(listValues);
				}

				if (isPendingTicket) {
					model.setStatus(ConstantValues.TRAVEL_STATION_STATUS_PENDING_TICKET);
				}

				JSONObject objectParams = jsonResult.getJSONObject(data).getJSONObject("params");
				if (objectParams != null) {
					List<NameValuePair> stationParams = new ArrayList<NameValuePair>();
					Iterator<?> iter = objectParams.keys();
					while (iter.hasNext()) {
						String key = (String) iter.next();
						try {
							stationParams.add(new BasicNameValuePair(key, objectParams.getString(key)));
						} catch (Throwable e) {
							e.printStackTrace();
						}
					}
					model.setTravelParams(stationParams);
				}

				result.setReturnObject(model);
				result.setSuccess(true);
			} else {
				result.setSuccess(false);
			}
			result.setMessage(jsonResult.getString(message));
		} catch (Throwable e) {
			e.printStackTrace();
			result.setMessage(mContext.getResources().getString(R.string.error_network));
			result.setSuccess(false);
		}
		return result;
	}

	public Result searchTravel(List<NameValuePair> params, int companyID, int userID) {
		Result result = new Result();
		params.add(new BasicNameValuePair("company_id", String.valueOf(companyID)));
		params.add(new BasicNameValuePair("user_id", String.valueOf(userID)));
		ArrayList<TravelModel> listTravel = new ArrayList<TravelModel>();
		try {
			JSONObject jsonResult = new JSONObject(getJsonRespone(api_travel_search, params));
			if (jsonResult.getInt(status) == ConstantValues.INT_VALUE_TRUE) {
				JSONArray arrayData = jsonResult.getJSONArray(data);
				for (int i = 0; i < arrayData.length(); i++) {
					JSONObject objectData = arrayData.getJSONObject(i);
					TravelModel model = new TravelModel();
					model.setId(objectData.getInt("id"));
					model.setStatus(objectData.getInt("trip_wf"));
					model.setDealCode(objectData.getString("deal_codes"));
					model.setFromtime(objectData.getString("trip_start"));
					model.setTotime(objectData.getString("trip_end"));
					model.setPurpose(objectData.getString("purpose"));
					listTravel.add(model);
				}
				result.setReturnObject(listTravel);
				result.setSuccess(true);
			} else {
				result.setSuccess(false);
			}
			result.setMessage(jsonResult.getString(message));
		} catch (Throwable e) {
			e.printStackTrace();
			result.setMessage(mContext.getResources().getString(R.string.error_network));
			result.setSuccess(false);
		}
		return result;
	}

	public Result travelApply(int travelID, int companyID, int userID) {
		Result result = new Result();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("company_id", String.valueOf(companyID)));
		params.add(new BasicNameValuePair("user_id", String.valueOf(userID)));
		params.add(new BasicNameValuePair("business_trip_id", String.valueOf(travelID)));
		params.add(new BasicNameValuePair("company_name", String.valueOf(ConstantValues.COMPANY_NAME)));
		try {
			JSONObject jsonResult = new JSONObject(getJsonRespone(api_travel_apply, params));
			if (jsonResult.getInt(status) == ConstantValues.INT_VALUE_TRUE) {
				result.setSuccess(true);
			} else {
				result.setSuccess(false);
			}
			result.setMessage(jsonResult.getString(message));
		} catch (Throwable e) {
			e.printStackTrace();
			result.setMessage(mContext.getResources().getString(R.string.error_network));
			result.setSuccess(false);
		}
		return result;
	}

	public Result travelApprove(int travelID, int companyID, int userID) {
		Result result = new Result();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("company_id", String.valueOf(companyID)));
		params.add(new BasicNameValuePair("user_id", String.valueOf(userID)));
		params.add(new BasicNameValuePair("business_trip_id", String.valueOf(travelID)));
		params.add(new BasicNameValuePair("company_name", String.valueOf(ConstantValues.COMPANY_NAME)));
		try {
			JSONObject jsonResult = new JSONObject(getJsonRespone(api_travel_approve, params));
			if (jsonResult.getInt(status) == ConstantValues.INT_VALUE_TRUE) {
				result.setSuccess(true);
			} else {
				result.setSuccess(false);
			}
			result.setMessage(jsonResult.getString(message));
		} catch (Throwable e) {
			e.printStackTrace();
			result.setMessage(mContext.getResources().getString(R.string.error_network));
			result.setSuccess(false);
		}
		return result;
	}

	public Result travelReject(int travelID, int companyID, int userID) {
		Result result = new Result();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("company_id", String.valueOf(companyID)));
		params.add(new BasicNameValuePair("user_id", String.valueOf(userID)));
		params.add(new BasicNameValuePair("business_trip_id", String.valueOf(travelID)));
		params.add(new BasicNameValuePair("company_name", String.valueOf(ConstantValues.COMPANY_NAME)));
		try {
			JSONObject jsonResult = new JSONObject(getJsonRespone(api_travel_reject, params));
			if (jsonResult.getInt(status) == ConstantValues.INT_VALUE_TRUE) {
				result.setSuccess(true);
			} else {
				result.setSuccess(false);
			}
			result.setMessage(jsonResult.getString(message));
		} catch (Throwable e) {
			e.printStackTrace();
			result.setMessage(mContext.getResources().getString(R.string.error_network));
			result.setSuccess(false);
		}
		return result;
	}

	public Result loadUrl(List<NameValuePair> params, String url) {
		Result result = new Result();
		result.setReturnObject(getWebView(url, params));
		return result;

	}

}
