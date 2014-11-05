package jp.evo.btm.fragment.staff;

import jp.evo.btm.BTMApp;
import jp.evo.btm.R;
import jp.evo.btm.database.APILoader;
import jp.evo.btm.database.SQLLiteDBHelper;
import jp.evo.btm.fragment.BaseFragment;
import jp.evo.btm.inf.LoadingTask;
import jp.evo.btm.inf.TaskToLoad;
import jp.evo.btm.model.Result;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class StaffChangePassword extends BaseFragment implements OnClickListener {

	private EditText edOldPass;
	private EditText edNewPass;
	private EditText edConfirmPass;

	private Button btnAdd;
	private SQLLiteDBHelper helper;
	private APILoader loader;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		setHasOptionsMenu(true);
		getActivity().setTitle(getActivity().getString(R.string.screen_staff_change_pass));
		View view = inflater.inflate(R.layout.fragment_user_change_pass, container, false);
		init(view);
		return view;
	}

	public void init(View view) {
		helper = ((BTMApp) getActivity().getApplication()).getSQLiteDBHelper();
		loader = ((BTMApp) getActivity().getApplication()).getAPILoader();

		edOldPass = (EditText) view.findViewById(R.id.edOldpassword);
		edNewPass = (EditText) view.findViewById(R.id.edNewpassword);
		edConfirmPass = (EditText) view.findViewById(R.id.edConfirmpassword);

		btnAdd = (Button) view.findViewById(R.id.btnAdd);
		btnAdd.setOnClickListener(this);

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public void onClick(View v) {

		if (v == btnAdd) {

			TaskToLoad task = new TaskToLoad() {

				@Override
				public Result process() {
					if (checkConstrain()) {
						// ContentValues values = new ContentValues();
						// values.put("old_pass",
						// edOldPass.getText().toString());
						// values.put("password",
						// edNewPass.getText().toString());
						// return helper.updateStaffPassword(((BTMApp)
						// getActivity().getApplication()).getCurrentStaffID(),
						// values);
						return loader.updateStaffPassword(((BTMApp) getActivity().getApplication()).getCurrentStaffID(), ((BTMApp) getActivity().getApplication()).getLoginUser().getCompanyID(), ((BTMApp) getActivity().getApplication())
								.getLoginUser().getId(), edOldPass.getText().toString(), edNewPass.getText().toString());
					} else {
						Result result = new Result();
						result.setSuccess(false);
						result.setMessage(alert);
						return result;
					}
				}

				@Override
				public void callback(final Result result) {
					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Toast.makeText(getActivity(), result.getMessage(), Toast.LENGTH_SHORT).show();

						}
					});
					if (result.isSuccess()) {
						if (getActivity() != null)
							getActivity().onBackPressed();
					}
				}
			};
			LoadingTask loadingTask = new LoadingTask(task, mProgress);
			loadingTask.execute();
		}
	}

	private String alert = "";

	public boolean checkConstrain() {
		if (edNewPass.getText().toString().equalsIgnoreCase("")) {
			alert = getActivity().getString(R.string.newpass_empty);
			return false;
		} else if (edConfirmPass.getText().toString().equalsIgnoreCase("")) {
			alert = getActivity().getString(R.string.confirm_pass_empty);
			return false;
		}
		if (!edNewPass.getText().toString().equalsIgnoreCase(edConfirmPass.getText().toString())) {
			alert = getActivity().getString(R.string.msg_password_notmatch);
			return false;
		}
		return true;
	}
}
