package jp.evo.btm.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

/**
 * App Preferences for store some thing
 * 
 * 
 */
public class AppPreferencesLib {
	private SharedPreferences mAppSharedPrefs;
	private Editor mPrefsEditor;

	/**
	 * Contructor get function return mAppSharedPrefs.getString("key", "");
	 * mPrefsEditor.putString("key", text); mPrefsEditor.commit();
	 * 
	 * @param context
	 */
	@SuppressLint("CommitPrefEdits")
	public AppPreferencesLib(Context context) {
		this.mAppSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		this.mPrefsEditor = mAppSharedPrefs.edit();
	}

	public void saveLoginInfo(String username, String password, String companyName) {
		mPrefsEditor.putString("username", username);
		mPrefsEditor.putString("password", password);
		mPrefsEditor.putString("company", companyName);
		mPrefsEditor.commit();
	}

	public void setLogin(boolean isLogin) {
		mPrefsEditor.putBoolean("isLogin", isLogin);
		mPrefsEditor.commit();
	}
	
	public String getCompanyName() {
		return mAppSharedPrefs.getString("company", "");
	}

	public String getLoginUserName() {
		return mAppSharedPrefs.getString("username", "");
	}

	public String getLoginPassword() {
		return mAppSharedPrefs.getString("password", "");
	}

	public boolean isLogin() {
		return mAppSharedPrefs.getBoolean("isLogin", false);
	}

}
