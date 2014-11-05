package jp.evo.btm.fragment.staff;

import java.util.ArrayList;
import java.util.List;

import jp.evo.btm.BTMApp;
import jp.evo.btm.MainActivity;
import jp.evo.btm.R;
import jp.evo.btm.common.ConstantValues;
import jp.evo.btm.database.APILoader;
import jp.evo.btm.database.SQLLiteDBHelper;
import jp.evo.btm.fragment.BaseFragment;
import jp.evo.btm.fragment.DatePickerFragment;
import jp.evo.btm.inf.LoadingTask;
import jp.evo.btm.inf.TaskToLoad;
import jp.evo.btm.model.DepartmentModel;
import jp.evo.btm.model.Result;
import jp.evo.btm.model.SubDepartmentModel;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

public class StaffAddNewFragment extends BaseFragment implements OnClickListener, OnItemSelectedListener, OnCheckedChangeListener {

	private SQLLiteDBHelper helper;
	private APILoader loader;

	private EditText edFirstName1;
	private EditText edLastName1;
	private EditText edFirstName2;
	private EditText edLastName2;
	private EditText edFirstName3;
	private EditText edLastName3;
	// private EditText edUsername;
	private EditText edEmail;
	private EditText edPassword;
	private EditText edAddress;
	private EditText edPhone;
	private EditText edANA;
	private EditText edJAL;

	private Button btnBirthday;
	private Button btnRegister;
	private Button btnCancel;
	private RadioButton rdMale;
	private RadioButton rdFemale;

	private Spinner spnDivision;
	private Spinner spnSubDivision;

	private ArrayList<DepartmentModel> listDepartment;
	private ArrayList<SubDepartmentModel> listSubDepartment;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		setHasOptionsMenu(true);

		View rootView = inflater.inflate(R.layout.fragment_staff_add_new, container, false);
		getActivity().setTitle(getActivity().getString(R.string.screen_staff_add_new));
		init(rootView);
		return rootView;
	}

	public void init(View view) {

		edFirstName1 = (EditText) view.findViewById(R.id.edFirstName1);
		edFirstName2 = (EditText) view.findViewById(R.id.edFirstName2);
		edFirstName3 = (EditText) view.findViewById(R.id.edFirstName3);
		edLastName1 = (EditText) view.findViewById(R.id.edLastName1);
		edLastName2 = (EditText) view.findViewById(R.id.edLastName2);
		edLastName3 = (EditText) view.findViewById(R.id.edLastName3);
		// edUsername = (EditText) view.findViewById(R.id.edUsername);
		edEmail = (EditText) view.findViewById(R.id.edEmail);
		edPassword = (EditText) view.findViewById(R.id.edPassword);
		edAddress = (EditText) view.findViewById(R.id.edAddress);
		edPhone = (EditText) view.findViewById(R.id.edPhone);
		edANA = (EditText) view.findViewById(R.id.edANA);
		edJAL = (EditText) view.findViewById(R.id.edJAL);

		btnBirthday = (Button) view.findViewById(R.id.btnBirthday);
		btnRegister = (Button) view.findViewById(R.id.btnRegister);
		btnCancel = (Button) view.findViewById(R.id.btnCancel);
		btnBirthday.setOnClickListener(this);
		btnRegister.setOnClickListener(this);
		btnCancel.setOnClickListener(this);

		rdMale = (RadioButton) view.findViewById(R.id.rbMale);
		rdFemale = (RadioButton) view.findViewById(R.id.rbFemale);
		rdMale.setOnCheckedChangeListener(this);
		rdFemale.setOnCheckedChangeListener(this);

		spnDivision = (Spinner) view.findViewById(R.id.spDepartment);
		spnSubDivision = (Spinner) view.findViewById(R.id.spSubDepartment);
		spnDivision.setOnItemSelectedListener(this);

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		if (listDepartment == null)
			loadData();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	public void loadData() {
		helper = ((BTMApp) getActivity().getApplication()).getSQLiteDBHelper();
		loader = ((BTMApp) getActivity().getApplication()).getAPILoader();
		listDepartment = ((BTMApp) getActivity().getApplication()).getListDepartment();
		TaskToLoad task = new TaskToLoad() {

			@SuppressWarnings("unchecked")
			@Override
			public Result process() {
				Result result;
				if (listDepartment == null || listDepartment.size() == 0) {
					// listDepartment = helper.getListDepartment();
					result = loader.getListDepartment(((BTMApp) getActivity().getApplication()).getLoginUser().getCompanyID(), ((BTMApp) getActivity().getApplication()).getLoginUser().getId());
					listDepartment = (ArrayList<DepartmentModel>) result.getReturnObject();
				} else {
					result = new Result();
					result.setSuccess(true);
				}
				return result;
			}

			@Override
			public void callback(final Result result) {
				if (result.isSuccess()) {
					String[] data = new String[listDepartment.size()];
					for (int i = 0; i < listDepartment.size(); i++) {
						data[i] = listDepartment.get(i).getName();
					}
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, data);
					spnDivision.setAdapter(adapter);
					spnDivision.setSelection(0);
				} else {
					if (getActivity() != null)
						getActivity().runOnUiThread(new Runnable() {

							@Override
							public void run() {
								Toast.makeText(getActivity(), result.getMessage(), Toast.LENGTH_SHORT).show();

							}
						});
				}
			}
		};
		LoadingTask loadingTask = new LoadingTask(task, mProgress);
		loadingTask.execute();
	}

	public void loadSubDepartment(final int position) {
		TaskToLoad task = new TaskToLoad() {
			@SuppressWarnings("unchecked")
			@Override
			public Result process() {
				Result result = new Result();
				result = loader.getListSubDepartment(Integer.parseInt(listDepartment.get(position).getId()), ((BTMApp) getActivity().getApplication()).getLoginUser().getCompanyID(), ((BTMApp) getActivity().getApplication()).getLoginUser().getId());
				// listSubDepartment =
				// helper.getListSubDepartment(Integer.parseInt(listDepartment.get(position).getId()));
				listSubDepartment = (ArrayList<SubDepartmentModel>) result.getReturnObject();
				return result;
			}

			@Override
			public void callback(final Result result) {
				if (result.isSuccess()) {
					String[] data = new String[listSubDepartment.size()];
					for (int i = 0; i < listSubDepartment.size(); i++) {
						data[i] = listSubDepartment.get(i).getName();
					}
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, data);
					spnSubDivision.setAdapter(adapter);
					spnSubDivision.setVisibility(View.VISIBLE);
				} else {
					if (getActivity() != null)
						getActivity().runOnUiThread(new Runnable() {

							@Override
							public void run() {
								Toast.makeText(getActivity(), result.getMessage(), Toast.LENGTH_SHORT).show();

							}
						});
					spnSubDivision.setVisibility(View.GONE);
				}
			}
		};
		LoadingTask loadingTask = new LoadingTask(task, mProgress);
		loadingTask.execute();
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
		if (arg0 == spnDivision) {
			spnSubDivision.setVisibility(View.GONE);
			loadSubDepartment(position);
		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		if (v == btnBirthday) {

			DatePickerFragment picker = new DatePickerFragment();
			picker.setCallBackButton(btnBirthday);
			((MainActivity) getActivity()).showDialog(picker);

		} else if (v == btnRegister) {
			addNewUser();

		} else if (v == btnCancel) {
			getActivity().onBackPressed();
		}

	}

	public void addNewUser() {
		TaskToLoad task = new TaskToLoad() {
			@Override
			public Result process() {
				Result result = new Result();
				if (!checkConstraint()) {
					result.setMessage(alertConstrain);
					result.setSuccess(false);
					return result;
				} else {
					// ContentValues values = new ContentValues();
					List<NameValuePair> values = new ArrayList<NameValuePair>();
					values.add(new BasicNameValuePair("staff_passwd", edPassword.getText().toString()));
					values.add(new BasicNameValuePair("staff_name_sei_kanji", edFirstName1.getText().toString()));
					values.add(new BasicNameValuePair("staff_name_mei_kanji", edLastName1.getText().toString()));
					values.add(new BasicNameValuePair("staff_name_sei_furi", edFirstName2.getText().toString()));
					values.add(new BasicNameValuePair("staff_name_mei_furi", edLastName2.getText().toString()));
					values.add(new BasicNameValuePair("staff_name_sei_roma", edFirstName3.getText().toString()));
					values.add(new BasicNameValuePair("staff_name_mei_roma", edLastName3.getText().toString()));
					values.add(new BasicNameValuePair("staff_email", edEmail.getText().toString()));
					String[] date = btnBirthday.getText().toString().split(ConstantValues.DATE_SPLITER);
					values.add(new BasicNameValuePair("birth_y", date[0]));
					values.add(new BasicNameValuePair("birth_m", date[1]));
					values.add(new BasicNameValuePair("birth_d", date[2]));
					if (rdMale.isChecked() && !rdFemale.isChecked()) {
						values.add(new BasicNameValuePair("staff_sex", String.valueOf(ConstantValues.MALE)));
					} else {
						values.add(new BasicNameValuePair("staff_sex", String.valueOf(ConstantValues.FEMALE)));
					}
					values.add(new BasicNameValuePair("address", edAddress.getText().toString()));
					values.add(new BasicNameValuePair("tel1", edPhone.getText().toString()));
					values.add(new BasicNameValuePair("staff_mileage_ana", edANA.getText().toString()));
					values.add(new BasicNameValuePair("staff_mileage_jal", edJAL.getText().toString()));
					// values.add(new BasicNameValuePair("Role",
					// String.valueOf(ConstantValues.ROLE_NORMAL)));
					values.add(new BasicNameValuePair("branch_id", listDepartment.get(spnDivision.getSelectedItemPosition()).getId()));
					values.add(new BasicNameValuePair("section_id", listSubDepartment.get(spnSubDivision.getSelectedItemPosition()).getId()));
					// values.add(new BasicNameValuePair("subdivisionname",
					// listSubDepartment.get(spnSubDivision.getSelectedItemPosition()).getName()));
					// return helper.insertNewStaff(values);
					return loader.insertNewStaff(values, ((BTMApp) getActivity().getApplication()).getLoginUser().getCompanyID(), ((BTMApp) getActivity().getApplication()).getLoginUser().getId());
				}
			}

			@Override
			public void callback(final Result result) {
				if (getActivity() != null)
					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Toast.makeText(getActivity(), result.getMessage(), Toast.LENGTH_SHORT).show();
						}
					});
				if (result.isSuccess()) {
					getActivity().onBackPressed();
				}
			}
		};
		LoadingTask loadingTask = new LoadingTask(task, mProgress);
		loadingTask.execute();
	}

	private String alertConstrain = "";

	public boolean checkConstraint() {
		// if (edUsername.getText().toString().equalsIgnoreCase("")) {
		// alertConstrain = "Username cannot be empty";
		// return false;
		// } else
		if (edPassword.getText().toString().equalsIgnoreCase("")) {
			alertConstrain = getActivity().getString(R.string.psw_empty);
			return false;
		} else if ((edFirstName1.getText().toString() + edLastName1.getText().toString()).equalsIgnoreCase("")) {
			alertConstrain = getActivity().getString(R.string.nam1_empty);
			return false;
		} else if ((edFirstName2.getText().toString() + edLastName2.getText().toString()).equalsIgnoreCase("")) {
			alertConstrain = getActivity().getString(R.string.nam2_empty);
			return false;
		} else if ((edFirstName3.getText().toString() + edLastName3.getText().toString()).equalsIgnoreCase("")) {
			alertConstrain = getActivity().getString(R.string.nam3_empty);
			return false;
		} else if (edEmail.getText().toString().equalsIgnoreCase("")) {
			alertConstrain = getActivity().getString(R.string.email_empty);
			return false;
		} else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(edEmail.getText().toString()).matches()) {
			alertConstrain = getActivity().getString(R.string.email_format_fail);
			return false;
		} else if (btnBirthday.getText().toString().equalsIgnoreCase("")) {
			alertConstrain = getActivity().getString(R.string.birthday_empty);
			return false;
		} else if (!rdMale.isChecked() && !rdFemale.isChecked()) {
			alertConstrain = getActivity().getString(R.string.gender_empty);
			return false;
		} else if (edAddress.getText().toString().equalsIgnoreCase("")) {
			alertConstrain = getActivity().getString(R.string.address_empty);
			return false;
		} else if (spnSubDivision.getVisibility() == View.GONE) {
			alertConstrain = getActivity().getString(R.string.subdepartment_empty);
			return false;
		}
		return true;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		if (buttonView == rdMale) {
			rdMale.setChecked(isChecked);
			rdFemale.setChecked(!isChecked);

		} else if (buttonView == rdFemale) {
			rdMale.setChecked(!isChecked);
			rdFemale.setChecked(isChecked);
		}
	}

}
