package jp.evo.btm.fragment.department;

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

public class DeparmentAddDialog extends DialogFragment implements OnClickListener {

	private EditText edDepartmentName;
	private Button btnAdd;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		getDialog().setTitle(getResources().getString(R.string.dialog_department_add));
		View view = inflater.inflate(R.layout.dialog_fragment_add_department, container, false);
		init(view);
		return view;
	}

	public void init(View view) {

		edDepartmentName = (EditText) view.findViewById(R.id.edDepartmentName);
		btnAdd = (Button) view.findViewById(R.id.btnAdd);
		btnAdd.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		if (v == btnAdd) {
			if (getActivity() != null)
			{
				Intent intent = new  Intent(SQLLiteDBHelper.REFRESH);
				intent.putExtra("action", SQLLiteDBHelper.ACTION_CREATE);
				intent.putExtra("departmentname", edDepartmentName.getText().toString());
				getActivity().sendBroadcast(intent);
			}
			dismiss();
			
		}
	}

}
