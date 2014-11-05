package jp.evo.btm.fragment.travel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.evo.btm.BTMApp;
import jp.evo.btm.MainActivity;
import jp.evo.btm.R;
import jp.evo.btm.common.ConstantValues;
import jp.evo.btm.fragment.BaseFragment;
import jp.evo.btm.fragment.FragmentType;
import jp.evo.btm.model.TravelModel;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Fragment;
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

public class TravelAddNewFragment extends BaseFragment implements OnClickListener {

	private EditText edPurpose;
	private EditText edDescription;
	private EditText edPreference;
	private EditText edPhone;

	private Button btnFromtime;
	private Button btnTotime;
	private Button btnNext;

	private MenuItem menuSearch;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		setHasOptionsMenu(true);
		getActivity().setTitle(getResources().getString(R.string.screen_travel_add_new));
		View view = inflater.inflate(R.layout.fragment_travel_add_new, container, false);
		init(view);
		return view;
	}

	public void init(View view) {

		edPurpose = (EditText) view.findViewById(R.id.edPurpose);
		edDescription = (EditText) view.findViewById(R.id.edDescription);
		edPreference = (EditText) view.findViewById(R.id.edPreference);
		edPhone = (EditText) view.findViewById(R.id.edPhone);

		btnFromtime = (Button) view.findViewById(R.id.btnFromTime);
		btnTotime = (Button) view.findViewById(R.id.btnToTime);
		btnNext = (Button) view.findViewById(R.id.btnNext);

		btnFromtime.setOnClickListener(this);
		btnTotime.setOnClickListener(this);
		btnNext.setOnClickListener(this);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_fragment_travel, menu);
		this.menuSearch = menu.findItem(R.id.menuSearch);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item == menuSearch) {
			((MainActivity) getActivity()).displayView(FragmentType.TRAVEL_FILTER);
		}
		return true;
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
			if (checkConstrain()) {
				TravelModel model = new TravelModel();
				model.setFromtime(btnFromtime.getText().toString());
				model.setTotime(btnTotime.getText().toString());
				model.setPurpose(edPurpose.getText().toString());
				model.setExplanation(edDescription.getText().toString());
				model.setNote(edPreference.getText().toString());
				model.setPhone(edPhone.getText().toString());
				model.setHolder(((BTMApp) getActivity().getApplication()).getLoginUser());
				model.setStatus(ConstantValues.TRAVEL_STATION_STATUS_INIT);
				((BTMApp) getActivity().getApplication()).setTravelValues(model);
				((MainActivity) getActivity()).displayView(FragmentType.TRAVEL_DETAIL);
			} else {
				Toast.makeText(getActivity(), alert, Toast.LENGTH_SHORT).show();
			}

		}
	}

	private String alert = "";

	public boolean checkConstrain() {

		if (btnFromtime.getText().toString().equalsIgnoreCase("")) {
			alert = getActivity().getString(R.string.fromtime_empty);
			return false;
		} else if (btnTotime.getText().toString().equalsIgnoreCase("")) {
			alert = getActivity().getString(R.string.totime_empty);
			return false;
		} else if (edPurpose.getText().toString().equalsIgnoreCase("")) {
			alert = getActivity().getString(R.string.Purpose_empty);
			return false;
		} else if (edPhone.getText().toString().equalsIgnoreCase("")) {
			alert = getActivity().getString(R.string.phone_empty);
			return false;
		} else if (!compareFromTimeToTime()) {
			alert = getActivity().getString(R.string.compare_time);
			return false;
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
