package jp.evo.btm.fragment.travel;

import jp.evo.btm.BTMApp;
import jp.evo.btm.R;
import jp.evo.btm.database.APILoader;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class TravelFilterDialog extends DialogFragment implements RadioGroup.OnCheckedChangeListener {

	private RadioGroup groupFilter;
	private View viewParent;
	private boolean init = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		getDialog().setTitle(getResources().getString(R.string.dialog_travel_filter));
		View view = inflater.inflate(R.layout.dialog_travel_list_filter, container, false);
		init(view);
		return view;
	}

	public void init(View view) {
		viewParent = view;
		groupFilter = (RadioGroup) view.findViewById(R.id.groupFilter);
		groupFilter.setOnCheckedChangeListener(this);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		String currentFilter = ((BTMApp) getActivity().getApplication()).getCurrentFilter();
		if (currentFilter.equalsIgnoreCase(APILoader.FILTER_APPLY)) {
			((RadioButton) viewParent.findViewById(R.id.rbNotApply)).setChecked(true);
		} else if (currentFilter.equalsIgnoreCase(APILoader.FILTER_APPROVE)) {
			((RadioButton) viewParent.findViewById(R.id.rbNotApprove)).setChecked(true);
		} else if (currentFilter.equalsIgnoreCase(APILoader.FILTER_CANCEL)) {
			((RadioButton) viewParent.findViewById(R.id.rbCancel)).setChecked(true);
		} else if (currentFilter.equalsIgnoreCase(APILoader.FILTER_OK)) {
			((RadioButton) viewParent.findViewById(R.id.rbOK)).setChecked(true);
		} else if (currentFilter.equalsIgnoreCase(APILoader.FILTER_NONE)) {
			((RadioButton) viewParent.findViewById(R.id.rbNone)).setChecked(true);
		}
		init = true;
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		if (!init)
			return;
		Intent intent = new Intent(APILoader.REFRESH);
		switch (checkedId) {
		case R.id.rbNotApply:
			intent.putExtra("filter", APILoader.FILTER_APPLY);
			break;
		case R.id.rbNotApprove:
			intent.putExtra("filter", APILoader.FILTER_APPROVE);
			break;
		case R.id.rbCancel:
			intent.putExtra("filter", APILoader.FILTER_CANCEL);
			break;
		case R.id.rbOK:
			intent.putExtra("filter", APILoader.FILTER_OK);
			break;
		case R.id.rbNone:
			intent.putExtra("filter", APILoader.FILTER_NONE);
			break;
		default:
		}
		getActivity().sendBroadcast(intent);
		dismiss();

	}

	// @Override
	// public void onClick(View v) {
	//
	// if (v == btnAdd) {
	// if (getActivity() != null)
	// {
	// Intent intent = new Intent(SQLLiteDBHelper.REFRESH);
	// intent.putExtra("action", SQLLiteDBHelper.ACTION_CREATE);
	// intent.putExtra("departmentname", edDepartmentName.getText().toString());
	// getActivity().sendBroadcast(intent);
	// }
	// dismiss();
	//
	// }
	// }

}
