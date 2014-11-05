package jp.evo.btm;

import jp.evo.btm.common.AppPreferencesLib;
import jp.evo.btm.common.ConstantValues;
import jp.evo.btm.database.APILoader;
import jp.evo.btm.database.SQLLiteDBHelper;
import jp.evo.btm.inf.LoadingTask;
import jp.evo.btm.inf.TaskToLoad;
import jp.evo.btm.model.Result;
import jp.evo.btm.model.StaffModel;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener {

	private EditText edUsername;
	private EditText edPassword;
	private EditText edCompanyName;
	private Button btnLogin;
	// private TextView tvForgotPass;

	private SQLLiteDBHelper helper;
	private APILoader loader;

	ProgressBar mProcess;

	private AppPreferencesLib pref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_act);

		pref = new AppPreferencesLib(LoginActivity.this);
		helper = ((BTMApp) getApplication()).getSQLiteDBHelper();
		loader = ((BTMApp) getApplication()).getAPILoader();

		initView();
		initListener();

	}

	public void initView() {
		edUsername = (EditText) findViewById(R.id.edUsername);
		edPassword = (EditText) findViewById(R.id.edPassword);
		edCompanyName = (EditText) findViewById(R.id.edCompanyName);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		// tvForgotPass = (TextView) findViewById(R.id.tvForgotpass);

		mProcess = (ProgressBar) findViewById(R.id.progress);
	}

	public void initListener() {
		btnLogin.setOnClickListener(this);
		if (pref.isLogin()) {
			System.out.println("dangtb login info " + pref.getLoginUserName() + " " + pref.getLoginPassword());
			login(pref.getLoginUserName(), pref.getLoginPassword(), pref.getCompanyName());
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == btnLogin) {
			login(edUsername.getText().toString(), edPassword.getText().toString(), edCompanyName.getText().toString());
		}
	}

	public void login(final String username, final String password, final String companyName) {
		TaskToLoad task = new TaskToLoad() {

			@Override
			public Result process() {
				// TODO Auto-generated method stub
				// return helper.Login(username, password);
				return loader.Login(username, password, companyName);
			}

			@Override
			public void callback(final Result result) {
				// TODO Auto-generated method stub
				if (result.isSuccess()) {
					((BTMApp) getApplication()).setLoginUser((StaffModel) result.getReturnObject());
					ConstantValues.COMPANY_NAME = companyName;
					pref.saveLoginInfo(username, password, companyName);
					pref.setLogin(true);
					Intent intentMainAct = new Intent(LoginActivity.this, MainActivity.class);
					startActivity(intentMainAct);
					finish();
				} else {
					runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(LoginActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
						}
					});

				}
				mProcess.setVisibility(View.GONE);
			}
		};
		mProcess.setVisibility(View.VISIBLE);
		LoadingTask loadingTask = new LoadingTask(task, null);
		loadingTask.execute();
	}
}
