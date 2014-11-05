package jp.evo.btm.fragment.department;

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

public class DeparmentEditDialog extends DialogFragment implements OnClickListener {

	private EditText edDepartmentOldName;
	private EditText edDepartmentNewName;
	private Button btnEdit;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		getDialog().setTitle(getResources().getString(R.string.dialog_department_edit));
		View view = inflater.inflate(R.layout.dialog_fragment_edit_department, container, false);
		init(view);
		return view;
	}

	public void init(View view) {

		edDepartmentOldName = (EditText) view.findViewById(R.id.edDepartmentOldName);
		edDepartmentOldName.setText(((BTMApp) getActivity().getApplication()).getCurrentDeparment().getName());
		edDepartmentNewName = (EditText) view.findViewById(R.id.edDepartmentNewName);
		btnEdit = (Button) view.findViewById(R.id.btnAdd);
		btnEdit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		if (v == btnEdit) {
			if (getActivity() != null) {
				Intent intent = new Intent(SQLLiteDBHelper.REFRESH);
				intent.putExtra("action", SQLLiteDBHelper.ACTION_EDIT);
				intent.putExtra("departmentname", edDepartmentNewName.getText().toString());
				intent.putExtra("departmentOldName", edDepartmentOldName.getText().toString());
				intent.putExtra("departmentid", ((BTMApp) getActivity().getApplication()).getCurrentDeparment().getId());
				getActivity().sendBroadcast(intent);
			}
			dismiss();

		}
	}

}
