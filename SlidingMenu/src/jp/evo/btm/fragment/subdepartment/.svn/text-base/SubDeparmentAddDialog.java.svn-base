package jp.evo.btm.fragment.subdepartment;

import java.util.ArrayList;

import jp.evo.btm.BTMApp;
import jp.evo.btm.R;
import jp.evo.btm.database.SQLLiteDBHelper;
import jp.evo.btm.model.DepartmentModel;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class SubDeparmentAddDialog extends DialogFragment implements OnClickListener {

	private EditText edDepartmentName;
	private Button btnAdd;
//	private Spinner spnListDepartment;
	private ArrayList<DepartmentModel> listDepartment;
	private int currentDepartmentID;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
	
		View view = inflater.inflate(R.layout.dialog_fragment_add_sub_department, container, false);
		init(view);
		return view;
	}

	public void init(View view) {

		edDepartmentName = (EditText) view.findViewById(R.id.edDepartmentName);
//		spnListDepartment = (Spinner) view.findViewById(R.id.spnDepartment);
		listDepartment = ((BTMApp) getActivity().getApplication()).getListDepartment();
//		spnListDepartment.setAdapter(new DepartmentSpinnerListAdapter(listDepartment));
		btnAdd = (Button) view.findViewById(R.id.btnAdd);
		btnAdd.setOnClickListener(this);
		
		String departmentName = "";
		currentDepartmentID = Integer.parseInt(((BTMApp) getActivity().getApplication()).getCurrentDeparment().getId());
		for (int i = 0; i < listDepartment.size(); i++) {
			if (currentDepartmentID == Integer.parseInt(listDepartment.get(i).getId()))
				departmentName = listDepartment.get(i).getName();
		}
		getDialog().setTitle(departmentName);
	}

	@Override
	public void onClick(View v) {

		if (v == btnAdd) {
			if (getActivity() != null)
			{
				Intent intent = new  Intent(SQLLiteDBHelper.REFRESH);
				intent.putExtra("action", SQLLiteDBHelper.ACTION_CREATE);
				intent.putExtra("subdepartmentname", edDepartmentName.getText().toString());
				intent.putExtra("departmentid", currentDepartmentID);
				getActivity().sendBroadcast(intent);
			}
			dismiss();
			
		}
	}

//	private class DepartmentSpinnerListAdapter extends BaseAdapter {
//		ArrayList<DepartmentModel> _listData;
//
//		public DepartmentSpinnerListAdapter(ArrayList<DepartmentModel> listData) {
//			_listData = listData;
//		}
//
//		@Override
//		public int getCount() {
//			// TODO Auto-generated method stub
//			return _listData.size();
//		}
//
//		@Override
//		public Object getItem(int arg0) {
//			// TODO Auto-generated method stub
//			return _listData.get(arg0);
//		}
//
//		@Override
//		public long getItemId(int arg0) {
//			return arg0;
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup arg2) {
//			ViewHolder holder;
//
//			if (convertView == null) {
//
//				LayoutInflater inflater = getActivity().getLayoutInflater();
//				convertView = inflater.inflate(R.layout.spinner_text_item, null);
//				holder = new ViewHolder();
//
//				holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
//
//				convertView.setTag(holder);
//
//			} else {
//				holder = (ViewHolder) convertView.getTag();
//			}
//			holder.tvName.setText(_listData.get(position).getName());
//
//			return convertView;
//		}
//
//	}
//
//	/**
//	 * holder of a view
//	 * 
//	 * @author dangtb
//	 * 
//	 */
//	private static class ViewHolder {
//		TextView tvName;
//	}

}
