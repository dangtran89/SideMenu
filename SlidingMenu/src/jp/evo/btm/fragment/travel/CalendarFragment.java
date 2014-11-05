package jp.evo.btm.fragment.travel;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import jp.evo.btm.R;
import jp.evo.btm.common.ConstantValues;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;

public class CalendarFragment extends DialogFragment implements OnDateChangeListener{

	private Button btnDate;
	private CalendarView calView;

	public void setCallBackButton(Button btnDate) {
		this.btnDate = btnDate;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		getDialog().setTitle(getResources().getString(R.string.dialog_calendar));
		View view = inflater.inflate(R.layout.dialog_fragment_calendar, container, false);
		init(view);
		return view;
	}
	
	public void init(View view)
	{
		calView = (CalendarView)view.findViewById(R.id.calendar);
		calView.setOnDateChangeListener(this);
	}

	@Override
	public void onSelectedDayChange(CalendarView arg0, int year, int month, int dayOfMonth) {
		Calendar c = Calendar.getInstance();
		c.set(year, month, dayOfMonth);

		SimpleDateFormat sdf = new SimpleDateFormat(ConstantValues.DATE_FORMAT);
		String formattedDate = sdf.format(c.getTime());
		btnDate.setText(formattedDate);
		dismiss();
	}
}