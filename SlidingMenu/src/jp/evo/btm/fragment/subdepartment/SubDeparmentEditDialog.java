package jp.evo.btm.fragment.subdepartment;

import jp.evo.btm.BTMApp;
import jp.evo.btm.R;
import jp.evo.btm.database.SQLLiteDBHelper;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class SubDeparmentEditDialog extends DialogFragment implements OnClickListener {

	private EditText edSubDepartmentOldName;
	private EditText edSubDepartmentNewName;
	private Button btnEdit;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		getDialog().setTitle(getResources().getString(R.string.dialog_sub_department_edit));
		View view = inflater.inflate(R.layout.dialog_fragment_edit_sub_department, container, false);
		init(view);
		return view;
	}

	public void init(View view) {

		edSubDepartmentOldName = (EditText) view.findViewById(R.id.edSubDepartmentOldName);
		edSubDepartmentOldName.setText(((BTMApp)getActivity().getApplication()).getCurrentSubDeparment().getName());
		edSubDepartmentNewName = (EditText) view.findViewById(R.id.edSubDepartmentNewName);
		btnEdit = (Button) view.findViewById(R.id.btnAdd);
		btnEdit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		
		if (v == btnEdit) {
			
			if (getActivity() != null)
			{
				Intent intent = new  Intent(SQLLiteDBHelper.REFRESH);
				intent.putExtra("action", SQLLiteDBHelper.ACTION_EDIT);
				intent.putExtra("subdepartmentname",edSubDepartmentNewName.getText().toString());
				intent.putExtra("subdepartmentOldName",edSubDepartmentOldName.getText().toString());
				intent.putExtra("subdepartmentid", Integer.parseInt(((BTMApp)getActivity().getApplication()).getCurrentSubDeparment().getId()));
				getActivity().sendBroadcast(intent);
			}
			dismiss();
		}
	}

}
