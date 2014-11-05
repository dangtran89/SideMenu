package jp.evo.btm.fragment.travel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import jp.evo.btm.BTMApp;
import jp.evo.btm.MainActivity;
import jp.evo.btm.R;
import jp.evo.btm.common.ConstantValues;
import jp.evo.btm.database.APILoader;
import jp.evo.btm.database.SQLLiteDBHelper;
import jp.evo.btm.fragment.BaseFragment;
import jp.evo.btm.fragment.FragmentType;
import jp.evo.btm.inf.LoadingTask;
import jp.evo.btm.inf.TaskToLoad;
import jp.evo.btm.model.Result;
import jp.evo.btm.model.TravelModel;
import android.content.ContentValues;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TravelFilterFragment extends BaseFragment implements OnClickListener {

	private EditText edPurpose;
	private EditText edTravelerName;

	private Button btnFromtime;
	private Button btnTotime;
	private Button btnNext;

	private SQLLiteDBHelper helper;
	private APILoader loader;

	private MenuItem menuItem;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		getActivity().setTitle(getResources().getString(R.string.screen_travel_filter));
		View view = inflater.inflate(R.layout.fragment_travel_filter, container, false);
		init(view);
		return view;
	}

	public void init(View view) {
		helper = ((BTMApp) getActivity().getApplication()).getSQLiteDBHelper();
		loader = ((BTMApp) getActivity().getApplication()).getAPILoader();

		edPurpose = (EditText) view.findViewById(R.id.edPurpose);
		edTravelerName = (EditText) view.findViewById(R.id.edTravelerName);

		btnFromtime = (Button) view.findViewById(R.id.btnFromTime);
		btnTotime = (Button) view.findViewById(R.id.btnToTime);
		btnNext = (Button) view.findViewById(R.id.btnNext);

		btnFromtime.setOnClickListener(this);
		btnTotime.setOnClickListener(this);
		btnNext.setOnClickListener(this);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onClick(View v) {
		if (v == btnFromtime) {
			CalendarFragment cal = new CalendarFragment();
			cal.setCallBackButton(btnFromtime);
			((MainActivity) getActivity()).showDialog(cal);
		} else if (v == btnTotime) {
			CalendarFragment cal = new CalendarFragment();
			cal.setCallBackButton(btnTotime);
			((MainActivity) getActivity()).showDialog(cal);
		} else if (v == btnNext) {
			if (getActivity() != null)
				getActivity().runOnUiThread(new Runnable() {

					@Override
					public void run() {
						TaskToLoad task = new TaskToLoad() {

							@Override
							public Result process() {
								((BTMApp) getActivity().getApplication()).setCurrentFilter(APILoader.FILTER_NONE);
								ContentValues values = new ContentValues();
								List<NameValuePair> params = new ArrayList<NameValuePair>();
								params.add(new BasicNameValuePair("trip_start", btnFromtime.getText().toString()));
								params.add(new BasicNameValuePair("trip_end", btnTotime.getText().toString()));
								params.add(new BasicNameValuePair("traveller_name", edTravelerName.getText().toString()));
								params.add(new BasicNameValuePair("trip_purpose", edPurpose.getText().toString()));

								Result result = loader.searchTravel(params, ((BTMApp) getActivity().getApplication()).getLoginUser().getCompanyID(), ((BTMApp) getActivity().getApplication()).getLoginUser().getId());

								@SuppressWarnings("unchecked")
								ArrayList<TravelModel> listData = (ArrayList<TravelModel>) result.getReturnObject();
								if (listData != null && listData.size() > 0) {
									((BTMApp) getActivity().getApplication()).setListSearchTravel(listData);
								}
								return result;
							}

							@Override
							public void callback(final Result result) {
								if (result.isSuccess()) {
									((MainActivity) getActivity()).displayView(FragmentType.TRAVEL_LIST);
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
				});

		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_travel_filter, menu);
		menuItem = (MenuItem) menu.findItem(R.id.action_clear);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (menuItem == item) {
			edPurpose.setText("");
			edTravelerName.setText("");
			btnFromtime.setText("");
			btnTotime.setText("");

		}
		return true;
	}

	public boolean compareFromTimeToTime() {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(ConstantValues.DATE_FORMAT);
			Date fromTime = sdf.parse(btnFromtime.getText().toString());
			Date toTime = sdf.parse(btnTotime.getText().toString());

			return toTime.after(fromTime);
		} catch (Throwable e) {
			e.printStackTrace();
			return false;
		}
	}
}