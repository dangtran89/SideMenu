package jp.evo.btm.fragment.travel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import jp.evo.btm.BTMApp;
import jp.evo.btm.MainActivity;
import jp.evo.btm.R;
import jp.evo.btm.common.ConstantValues;
import jp.evo.btm.common.Utilities;
import jp.evo.btm.database.APILoader;
import jp.evo.btm.database.SQLLiteDBHelper;
import jp.evo.btm.fragment.BaseFragment;
import jp.evo.btm.fragment.FragmentType;
import jp.evo.btm.inf.LoadingTask;
import jp.evo.btm.inf.TaskToLoad;
import jp.evo.btm.model.Result;
import jp.evo.btm.model.StaffModel;
import jp.evo.btm.model.TravelModel;
import android.content.ContentValues;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class TravelDetailFragment extends BaseFragment implements OnClickListener, OnCheckedChangeListener {

	private TextView tvFromTime;
	private TextView tvToTime;
	private TextView tvPurpose;
	private TextView tvDescription;
	private TextView tvPreference;
	private TextView tvPhone;
	private TextView tvHolderName;
	private TextView tvHolderDivision;
	private TextView tvHolderEmail;
	private TextView tvStatus;

	private CheckBox cbJoin;
	private LinearLayout llPartner;
	private LinearLayout llTravelStation;
	private LinearLayout llPackage;
	private LinearLayout llPlane;
	private LinearLayout llTrain;

	private Button btnSearch;
	private Button btnRegister;
	// private Button btnCancel;
	private Button btnTrain;
	private Button btnAircraft;
	private Button btnPackage;
	private Button btnApply;
	private Button btnApprove;
	private Button btnReject;
	private Button lbPackage;
	private Button lbPlane;
	private Button lbTrain;

	private StaffModel holder;

	private APILoader loader;
	private SQLLiteDBHelper helper;

	private ArrayList<StaffModel> listEnroll;
	private StaffModel loginUser;
	private TravelModel travelInfo;

	private LoadingTask stationTask;

	private long idTravel = -1;

	private LinearLayout.LayoutParams layoutParams;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		getActivity().setTitle(getResources().getString(R.string.screen_travel_detail));
		View view = inflater.inflate(R.layout.fragment_travel_detail, container, false);
		System.out.println("dangtb oncreate view");
		init(view);
		return view;
	}

	public void init(View view) {

		tvFromTime = (TextView) view.findViewById(R.id.tvFromTime);
		tvToTime = (TextView) view.findViewById(R.id.tvTotime);
		tvPurpose = (TextView) view.findViewById(R.id.tvPurpose);
		tvDescription = (TextView) view.findViewById(R.id.tvDescription);
		tvPreference = (TextView) view.findViewById(R.id.tvPreference);
		tvPhone = (TextView) view.findViewById(R.id.tvPhone);
		tvHolderName = (TextView) view.findViewById(R.id.tvHolderName);
		tvHolderDivision = (TextView) view.findViewById(R.id.tvHolderDivision);
		tvHolderEmail = (TextView) view.findViewById(R.id.tvHolderEmail);
		tvStatus = (TextView) view.findViewById(R.id.tvStatus);
		lbPackage = (Button) view.findViewById(R.id.lbPackage);
		lbPackage.setVisibility(View.GONE);
		lbPackage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (llPackage.getVisibility() == View.GONE)
					llPackage.setVisibility(View.VISIBLE);
				else
					llPackage.setVisibility(View.GONE);

			}
		});
		lbPlane = (Button) view.findViewById(R.id.lbPlane);
		lbPlane.setVisibility(View.GONE);
		lbPlane.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (llPlane.getVisibility() == View.GONE)
					llPlane.setVisibility(View.VISIBLE);
				else
					llPlane.setVisibility(View.GONE);

			}
		});
		lbTrain = (Button) view.findViewById(R.id.lbTrain);
		lbTrain.setVisibility(View.GONE);
		lbTrain.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (llTrain.getVisibility() == View.GONE)
					llTrain.setVisibility(View.VISIBLE);
				else
					llTrain.setVisibility(View.GONE);

			}
		});

		llPartner = (LinearLayout) view.findViewById(R.id.llPartner);
		llTravelStation = (LinearLayout) view.findViewById(R.id.llTravelStation);
		llPackage = (LinearLayout) view.findViewById(R.id.llPackage);
		llTrain = (LinearLayout) view.findViewById(R.id.llTrain);
		llPlane = (LinearLayout) view.findViewById(R.id.llPlane);

		cbJoin = (CheckBox) view.findViewById(R.id.cbJoin);
		cbJoin.setOnCheckedChangeListener(this);

		listEnroll = ((BTMApp) getActivity().getApplication()).getTravelValues().getListStaffEnroll();
		loginUser = ((BTMApp) getActivity().getApplication()).getLoginUser();

		btnSearch = (Button) view.findViewById(R.id.btnSearchPartner);
		btnRegister = (Button) view.findViewById(R.id.btnRegister);
		// btnCancel = (Button) view.findViewById(R.id.btnCancel);
		btnTrain = (Button) view.findViewById(R.id.btnTrain);
		btnAircraft = (Button) view.findViewById(R.id.btnAircraft);
		btnPackage = (Button) view.findViewById(R.id.btnPackage);
		btnApply = (Button) view.findViewById(R.id.btnApply);
		btnApprove = (Button) view.findViewById(R.id.btnApprove);
		btnReject = (Button) view.findViewById(R.id.btnReject);

		btnSearch.setOnClickListener(this);
		btnRegister.setOnClickListener(this);
		// btnCancel.setOnClickListener(this);
		btnTrain.setOnClickListener(this);
		btnAircraft.setOnClickListener(this);
		btnPackage.setOnClickListener(this);
		btnApply.setOnClickListener(this);
		btnApprove.setOnClickListener(this);
		btnReject.setOnClickListener(this);

		loadData();

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);
		mProgress.setVisible(true);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item == mProgress) {
			getTravelDetail();
		}
		return true;
	}

	public void loadData() {
		helper = ((BTMApp) getActivity().getApplication()).getSQLiteDBHelper();
		loader = ((BTMApp) getActivity().getApplication()).getAPILoader();
		travelInfo = ((BTMApp) getActivity().getApplication()).getTravelValues();
		holder = ((BTMApp) getActivity().getApplication()).getTravelValues().getHolder();

		tvFromTime.setText(travelInfo.getFromtime());
		tvToTime.setText(travelInfo.getTotime());
		tvPurpose.setText(travelInfo.getPurpose());
		tvDescription.setText(travelInfo.getExplanation());
		tvPreference.setText(travelInfo.getNote());
		tvPhone.setText(travelInfo.getPhone());

		tvHolderName.setText(holder.getUserName());

		tvHolderEmail.setText(holder.getEmail());
		tvHolderDivision.setText(holder.getSubDivisionName());

		if (travelInfo.getStatus() == ConstantValues.TRAVEL_STATION_STATUS_OK) {
			tvStatus.setText(getActivity().getString(R.string.status_ok));
		} else if (travelInfo.getStatus() == ConstantValues.TRAVEL_STATION_STATUS_PENDING_APPLY) {
			tvStatus.setText(getActivity().getString(R.string.status_pending_apply));
		} else if (travelInfo.getStatus() == ConstantValues.TRAVEL_STATION_STATUS_PENDING_APPROVE) {
			tvStatus.setText(getActivity().getString(R.string.status_pending_approve));
		} else if (travelInfo.getStatus() == ConstantValues.TRAVEL_STATION_STATUS_CANCEL) {
			tvStatus.setText(getActivity().getString(R.string.status_cancel));
		} else if (travelInfo.getStatus() == ConstantValues.TRAVEL_STATION_STATUS_PENDING_TICKET) {
			tvStatus.setText(getActivity().getString(R.string.status_pending_ticket));
		}

		if (listEnroll != null && listEnroll.size() > 0) {
			for (int i = 0; i < listEnroll.size(); i++) {
				System.out.println("dangtb checkbox " + listEnroll.get(i).getId() + "===" + loginUser.getId());
				if (listEnroll.get(i).getId() == loginUser.getId()) {
					cbJoin.setChecked(true);
				}
			}
		}

		addListEnroll();

		if (travelInfo.getTravelPackage() != null) {
			lbPackage.setVisibility(View.VISIBLE);
			llPackage.removeAllViews();
			for (int i = 0; i < travelInfo.getTravelPackage().size(); i++) {
				LinearLayout layoutParent = new LinearLayout(getActivity());
				layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				layoutParams.setMargins((int) getActivity().getResources().getDimension(R.dimen.travel_detail_margin_detail), 0, (int) getActivity().getResources().getDimension(R.dimen.travel_detail_margin_detail), (int) getActivity().getResources()
						.getDimension(R.dimen.travel_detail_margin_detail) * 2);
				layoutParent.setBackgroundResource(R.drawable.border);
				layoutParent.setLayoutParams(layoutParams);
				layoutParent.setOrientation(LinearLayout.VERTICAL);
				llPackage.addView(layoutParent);

				addDepartureInfo(layoutParent, travelInfo.getTravelPackage().get(i), false);
			}

		}
		if (travelInfo.getTravelPlane() != null) {
			lbPlane.setVisibility(View.VISIBLE);
			llPlane.removeAllViews();
			for (int i = 0; i < travelInfo.getTravelPlane().size(); i++) {
				LinearLayout layoutParent = new LinearLayout(getActivity());
				layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				layoutParams.setMargins((int) getActivity().getResources().getDimension(R.dimen.travel_detail_margin_detail), 0, (int) getActivity().getResources().getDimension(R.dimen.travel_detail_margin_detail), (int) getActivity().getResources()
						.getDimension(R.dimen.travel_detail_margin_detail) * 2);
				layoutParent.setBackgroundResource(R.drawable.border);
				layoutParent.setLayoutParams(layoutParams);
				layoutParent.setOrientation(LinearLayout.VERTICAL);
				llPlane.addView(layoutParent);

				addDepartureInfo(layoutParent, travelInfo.getTravelPlane().get(i), false);
			}
		}
		if (travelInfo.getTravelShinkansen() != null) {
			lbTrain.setVisibility(View.VISIBLE);
			llTrain.removeAllViews();
			for (int i = 0; i < travelInfo.getTravelShinkansen().size(); i++) {
				LinearLayout layoutParent = new LinearLayout(getActivity());
				layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				layoutParams.setMargins((int) getActivity().getResources().getDimension(R.dimen.travel_detail_margin_detail), 0, (int) getActivity().getResources().getDimension(R.dimen.travel_detail_margin_detail), (int) getActivity().getResources()
						.getDimension(R.dimen.travel_detail_margin_detail) * 2);
				layoutParent.setBackgroundResource(R.drawable.border);
				layoutParent.setLayoutParams(layoutParams);
				layoutParent.setOrientation(LinearLayout.VERTICAL);
				llTrain.addView(layoutParent);

				addDepartureInfo(layoutParent, travelInfo.getTravelShinkansen().get(i), false);
			}
		}

		initButton();
	}

	public void initButton() {
		if (travelInfo.getStatus() == ConstantValues.TRAVEL_STATION_STATUS_INIT) {
			// btnCancel.setVisibility(View.GONE);
			btnRegister.setVisibility(View.VISIBLE);
			llTravelStation.setVisibility(View.GONE);
			btnSearch.setVisibility(View.VISIBLE);
			cbJoin.setEnabled(true);
		} else if (travelInfo.getStatus() == ConstantValues.TRAVEL_STATION_STATUS_PENDING_TICKET) {
			llTravelStation.setVisibility(View.VISIBLE);
			if (isEnablePackage()) {
				btnPackage.setVisibility(View.VISIBLE);
			}
			btnRegister.setVisibility(View.GONE);
			// btnCancel.setVisibility(View.VISIBLE);
			btnSearch.setVisibility(View.GONE);
			cbJoin.setEnabled(false);
		} else if (travelInfo.getStatus() == ConstantValues.TRAVEL_STATION_STATUS_PENDING_APPLY && loginUser.getRole() == ConstantValues.ROLE_ADMIN) {
			llTravelStation.setVisibility(View.GONE);
			btnRegister.setVisibility(View.GONE);
			// btnCancel.setVisibility(View.GONE);
			btnSearch.setVisibility(View.GONE);
			cbJoin.setEnabled(false);
			btnApply.setVisibility(View.VISIBLE);
			btnApprove.setVisibility(View.GONE);
			btnReject.setVisibility(View.GONE);
		} else if (travelInfo.getStatus() == ConstantValues.TRAVEL_STATION_STATUS_PENDING_APPROVE && loginUser.getRole() == ConstantValues.ROLE_ADMIN) {
			llTravelStation.setVisibility(View.GONE);
			btnRegister.setVisibility(View.GONE);
			// btnCancel.setVisibility(View.GONE);
			btnSearch.setVisibility(View.GONE);
			cbJoin.setEnabled(false);
			btnApply.setVisibility(View.GONE);
			btnApprove.setVisibility(View.VISIBLE);
			btnReject.setVisibility(View.VISIBLE);
		} else if (travelInfo.getStatus() == ConstantValues.TRAVEL_STATION_STATUS_OK || travelInfo.getStatus() == ConstantValues.TRAVEL_STATION_STATUS_CANCEL) {
			llTravelStation.setVisibility(View.GONE);
			btnRegister.setVisibility(View.GONE);
			// btnCancel.setVisibility(View.GONE);
			btnSearch.setVisibility(View.GONE);
			cbJoin.setEnabled(false);
			btnApply.setVisibility(View.GONE);
			btnApprove.setVisibility(View.GONE);
			btnReject.setVisibility(View.GONE);
		}
	}

	public void addListEnroll() {
		if (listEnroll != null && listEnroll.size() > 0) {
			llPartner.setVisibility(View.VISIBLE);
			llPartner.removeAllViews();
			for (int i = 0; i < listEnroll.size(); i++) {
				LinearLayout llLayout = new LinearLayout(getActivity());
				llLayout.setOrientation(LinearLayout.HORIZONTAL);
				LinearLayout.LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				layoutParams.setMargins((int) getActivity().getResources().getDimension(R.dimen.travel_detail_textview_margin_left), 0, 0, 0);
				llLayout.setLayoutParams(layoutParams);

				if (i != 0) {
					ImageView divider = new ImageView(getActivity());
					divider.setBackgroundColor(getActivity().getResources().getColor(R.color.black));
					LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, (int) getActivity().getResources().getDimension(R.dimen.divider_height));
					divider.setLayoutParams(dividerParams);
					llPartner.addView(divider);
				}

				TextView tvDivision = new TextView(getActivity());
				tvDivision.setText(listEnroll.get(i).getSubDivisionName());
				LinearLayout.LayoutParams paramsDivision = new LayoutParams((int) getActivity().getResources().getDimension(R.dimen.travel_detail_textview_item_label_width), LayoutParams.WRAP_CONTENT);
				tvDivision.setLayoutParams(paramsDivision);

				TextView tvEnrollName = new TextView(getActivity());
				tvEnrollName.setText(listEnroll.get(i).getUserName());
				LinearLayout.LayoutParams paramsEnrollName = new LayoutParams((int) getActivity().getResources().getDimension(R.dimen.travel_detail_textview_item_value_width), LayoutParams.WRAP_CONTENT);
				tvEnrollName.setLayoutParams(paramsEnrollName);
				llLayout.addView(tvDivision);
				llLayout.addView(tvEnrollName);
				llPartner.addView(llLayout);

			}
		} else {
			llPartner.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {

		if (v == btnSearch) {
			((BTMApp) getActivity().getApplication()).getTravelValues().setListStaffEnroll(listEnroll);
			((MainActivity) getActivity()).displayView(FragmentType.SEARCH_USER);
		} else if (v == btnRegister) {
			btnSearch.setVisibility(View.GONE);

			TaskToLoad task = new TaskToLoad() {

				@Override
				public Result process() {
					if (travelInfo.getId() != -1) {
						System.out.println("dangtb update");
						// return helper.updateTravel(idTravel, values,
						// listEnroll);
						return loader.updateTravel(travelInfo, listEnroll, ((BTMApp) getActivity().getApplication()).getLoginUser().getCompanyID(), ((BTMApp) getActivity().getApplication()).getLoginUser().getId());
					} else {
						System.out.println("dangtb insert");
						// return helper.insertTravel(travelInfo, listEnroll);
						return loader.insertTravel(travelInfo, listEnroll, ((BTMApp) getActivity().getApplication()).getLoginUser().getCompanyID(), ((BTMApp) getActivity().getApplication()).getLoginUser().getId());
					}
				}

				@Override
				public void callback(final Result result) {
					if (getActivity() != null)
						getActivity().runOnUiThread(new Runnable() {

							@Override
							public void run() {
								Toast.makeText(getActivity(), result.getMessage(), Toast.LENGTH_SHORT).show();
							}
						});
					if (result.isSuccess()) {
						travelInfo = (TravelModel) result.getReturnObject();

						if (getActivity() != null)
							getActivity().runOnUiThread(new Runnable() {

								@Override
								public void run() {
									getTravelDetail();
								}
							});

					}
				}
			};
			LoadingTask loadingTask = new LoadingTask(task, mProgress);
			loadingTask.execute();

		}
		// else if (v == btnCancel) {
		// travelInfo.setStatus(ConstantValues.TRAVEL_STATION_STATUS_INIT);
		// initButton();
		// }
		else if (v == btnTrain) {
			((BTMApp) getActivity().getApplication()).setTypeOfTicket(ConstantValues.TICKET_TYPE_SHINKANSEN);
			((MainActivity) getActivity()).displayView(FragmentType.WEBVIEW);
		} else if (v == btnAircraft) {
			((BTMApp) getActivity().getApplication()).setTypeOfTicket(ConstantValues.TICKET_TYPE_PLANE);
			((MainActivity) getActivity()).displayView(FragmentType.WEBVIEW);
		} else if (v == btnPackage) {
			((BTMApp) getActivity().getApplication()).setTypeOfTicket(ConstantValues.TICKET_TYPE_PACKAGE);
			((MainActivity) getActivity()).displayView(FragmentType.WEBVIEW);
		} else if (v == btnApply) {
			TaskToLoad task = new TaskToLoad() {

				@Override
				public Result process() {
					return loader.travelApply(travelInfo.getId(), ((BTMApp) getActivity().getApplication()).getLoginUser().getCompanyID(), ((BTMApp) getActivity().getApplication()).getLoginUser().getId());
				}

				@Override
				public void callback(final Result result) {
					if (getActivity() != null)
						getActivity().runOnUiThread(new Runnable() {

							@Override
							public void run() {
								Toast.makeText(getActivity(), result.getMessage(), Toast.LENGTH_SHORT).show();
							}
						});
					if (result.isSuccess()) {
						if (getActivity() != null)
							getActivity().runOnUiThread(new Runnable() {

								@Override
								public void run() {
									getTravelDetail();
								}
							});

					}
				}
			};
			LoadingTask loadingTask = new LoadingTask(task, mProgress);
			loadingTask.execute();
		} else if (v == btnApprove) {
			TaskToLoad task = new TaskToLoad() {

				@Override
				public Result process() {
					return loader.travelApprove(travelInfo.getId(), ((BTMApp) getActivity().getApplication()).getLoginUser().getCompanyID(), ((BTMApp) getActivity().getApplication()).getLoginUser().getId());
				}

				@Override
				public void callback(final Result result) {
					if (getActivity() != null)
						getActivity().runOnUiThread(new Runnable() {

							@Override
							public void run() {
								Toast.makeText(getActivity(), result.getMessage(), Toast.LENGTH_SHORT).show();
							}
						});
					if (result.isSuccess()) {
						if (getActivity() != null)
							getActivity().runOnUiThread(new Runnable() {

								@Override
								public void run() {
									getTravelDetail();
								}
							});

					}
				}
			};
			LoadingTask loadingTask = new LoadingTask(task, mProgress);
			loadingTask.execute();

		} else if (v == btnReject) {
			TaskToLoad task = new TaskToLoad() {

				@Override
				public Result process() {
					return loader.travelReject(travelInfo.getId(), ((BTMApp) getActivity().getApplication()).getLoginUser().getCompanyID(), ((BTMApp) getActivity().getApplication()).getLoginUser().getId());
				}

				@Override
				public void callback(final Result result) {
					if (getActivity() != null)
						getActivity().runOnUiThread(new Runnable() {

							@Override
							public void run() {
								Toast.makeText(getActivity(), result.getMessage(), Toast.LENGTH_SHORT).show();
							}
						});
					if (result.isSuccess()) {
						if (getActivity() != null)
							getActivity().runOnUiThread(new Runnable() {

								@Override
								public void run() {
									getTravelDetail();
								}
							});

					}
				}
			};
			LoadingTask loadingTask = new LoadingTask(task, mProgress);
			loadingTask.execute();
		}
	}

	public void getTravelDetail() {
		TaskToLoad station = new TaskToLoad() {

			@Override
			public Result process() {
				// TODO Auto-generated method stub
				return loader.getTravelDetail(travelInfo.getId(), ((BTMApp) getActivity().getApplication()).getLoginUser().getCompanyID(), ((BTMApp) getActivity().getApplication()).getLoginUser().getId());
			}

			@Override
			public void callback(final Result result) {
				if (result.isSuccess()) {
					travelInfo = (TravelModel) result.getReturnObject();
					((BTMApp) getActivity().getApplication()).setTravelValues(travelInfo);
					System.out.println("dangtb travel detail result " + travelInfo);
					loadData();
					llTravelStation.setVisibility(View.VISIBLE);
					initButton();
				} else {
					btnSearch.setVisibility(View.VISIBLE);
					if (getActivity() != null)
						getActivity().runOnUiThread(new Runnable() {

							@Override
							public void run() {
								Toast.makeText(getActivity(), result.getMessage(), Toast.LENGTH_SHORT).show();
							}
						});
				}
				mProgress.setVisible(true);
			}
		};
		stationTask = new LoadingTask(station, mProgress);
		stationTask.execute();
	}

	public boolean isEnablePackage() {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(ConstantValues.DATE_FORMAT);
			Calendar c = Calendar.getInstance();
			String time = c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DAY_OF_MONTH);
			Date fromTime = sdf.parse(time);
			Date toTime = sdf.parse(travelInfo.getFromtime());
			if (ConstantValues.RANGE_TO_DISPLAY_PACKAGE <= Utilities.subtractTime(fromTime, toTime) / ConstantValues.SECOND_OF_A_DAY)
				return true;
			else
				return false;
		} catch (Throwable e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		boolean loginUserEnroll = false;
		int position = -1;
		for (int i = 0; i < listEnroll.size(); i++) {
			if (listEnroll.get(i).getId() == loginUser.getId()) {
				loginUserEnroll = true;
				position = i;
			}
		}
		if (isChecked) {

			if (!loginUserEnroll) {
				listEnroll.add(loginUser);
			}
		} else {
			if (loginUserEnroll && position != -1)
				listEnroll.remove(position);
		}

		addListEnroll();
	}

	public void addDepartureInfo(LinearLayout parent, ContentValues values, boolean hasTotal) {

		addLayoutFieldTextView(parent, getString(R.string.label_departure), getString(R.string.label_destination), "", false);
		addLayoutFieldTextView(parent, getValue(values, "dealings_id"), "", getString(R.string.transaction_id), true);
		addLayoutFieldTextView(parent, getValue(values, "ticket_date1_from"), getValue(values, "ticket_date2_from"), getString(R.string.departure_date), true);
		addLayoutFieldTextView(parent, getValue(values, "ticket_kukan1_from") + "⇒" + getValue(values, "ticket_kukan1_to"), getValue(values, "ticket_kukan2_from") + "⇒" + getValue(values, "ticket_kukan2_to"), getString(R.string.kukan), true);
		addLayoutFieldTextView(parent, getValue(values, "ticket_name1"), getValue(values, "ticket_name2"), getString(R.string.flight_name), true);
		addLayoutFieldTextView(parent, getValue(values, "ticket_time1_from"), getValue(values, "ticket_time2_from"), getString(R.string.time_start), true);
		addLayoutFieldTextView(parent, getValue(values, "ticket_time1_to"), getValue(values, "ticket_time2_to"), getString(R.string.time_end), true);
		addLayoutFieldTextView(parent, getValue(values, "ticket_type1"), getValue(values, "ticket_type2"), getString(R.string.ticket_type), true);
		addLayoutFieldTextView(parent, getValue(values, "ticket_price1"), getValue(values, "ticket_price2"), getString(R.string.money), true);
		addLayoutFieldTextView(parent, getValue(values, "ticket_count1"), getValue(values, "ticket_count2"), getString(R.string.ticket_count), true);
		String total = "";
		try {
			if (!getValue(values, "ticket_count2").equalsIgnoreCase("null") && !getValue(values, "ticket_count2").equalsIgnoreCase("") && !getValue(values, "ticket_price2").equalsIgnoreCase("null")
					&& !getValue(values, "ticket_price2").equalsIgnoreCase(""))
				total = String.valueOf(Integer.parseInt(getValue(values, "ticket_count1")) * Integer.parseInt(getValue(values, "ticket_price1")) + Integer.parseInt(getValue(values, "ticket_count2"))
						* Integer.parseInt(getValue(values, "ticket_price2")));
			else
				total = String.valueOf(Integer.parseInt(getValue(values, "ticket_count1")) * Integer.parseInt(getValue(values, "ticket_price1")));

		} catch (Throwable e) {
			e.printStackTrace();
		}
		addLayoutFieldTextView(parent, total + " 円", "", getString(R.string.total), true);

	}

	// public void addDestinationInfo(LinearLayout parent, ContentValues values,
	// boolean hasTotal) {
	//
	// addLayoutFieldTextView(parent, getValue(values, "dealings_id"),
	// "TransactionID:", true);
	// addLayoutFieldTextView(parent, getValue(values, "deal_date"),
	// "Departure Date:", true);
	// addLayoutFieldTextView(parent, getValue(values, "kuhan"), "Kukan:",
	// true);
	// addLayoutFieldTextView(parent, getValue(values, "ticket_name2"),
	// "Flight Name:", true);
	// addLayoutFieldTextView(parent, getValue(values, "ticket_date2_from"),
	// "Time Start:", true);
	// addLayoutFieldTextView(parent, getValue(values, "ticket_date2_to"),
	// "Time End:", true);
	// addLayoutFieldTextView(parent, getValue(values, "ticket_type2"),
	// "Ticket Type:", true);
	// addLayoutFieldTextView(parent, getValue(values, "ticket_price2"),
	// "Money:", true);
	// addLayoutFieldTextView(parent, getValue(values, "ticket_count2"),
	// "Ticket Count:", true);
	// if (hasTotal) {
	// String total = "";
	// try {
	// if (!getValue(values, "ticket_count2").equalsIgnoreCase("null") &&
	// !getValue(values, "ticket_count2").equalsIgnoreCase("") &&
	// !getValue(values, "ticket_price2").equalsIgnoreCase("null")
	// && !getValue(values, "ticket_price2").equalsIgnoreCase(""))
	// total = String.valueOf(Integer.parseInt(getValue(values,
	// "ticket_count1")) * Integer.parseInt(getValue(values, "ticket_price1")) +
	// Integer.parseInt(getValue(values, "ticket_count2"))
	// * Integer.parseInt(getValue(values, "ticket_price2")));
	// else
	// total = String.valueOf(Integer.parseInt(getValue(values,
	// "ticket_count1")) * Integer.parseInt(getValue(values, "ticket_price1")));
	//
	// } catch (Throwable e) {
	// e.printStackTrace();
	// }
	// addLayoutFieldTextView(parent, total + " 円", "Total:", true);
	// }
	// }

	public String getValue(ContentValues content, String name) {
		String returnValue = "";
		if (content.containsKey(name)) {
			returnValue = content.getAsString(name);
		}
		return returnValue;
	}

	public void addLayoutFieldTextView(LinearLayout parentView, String valueDeparture, String valueDestination, String label, boolean isDivider) {
		if (valueDeparture.equalsIgnoreCase("⇒")) {
			valueDeparture = "";
		}
		if (valueDestination.equalsIgnoreCase("⇒")) {
			valueDestination = "";
		}
		if (valueDeparture.equalsIgnoreCase(""))
			return;

		LinearLayout llLayout = new LinearLayout(getActivity());
		llLayout.setPadding((int) getActivity().getResources().getDimension(R.dimen.travel_detail_textview_margin_left), 0, 0, 0);
		llLayout.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout.LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		llLayout.setLayoutParams(layoutParams);
		if (isDivider) {
			ImageView divider = new ImageView(getActivity());
			divider.setBackgroundColor(getActivity().getResources().getColor(R.color.black));
			LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, (int) getActivity().getResources().getDimension(R.dimen.divider_height));
			divider.setLayoutParams(dividerParams);
			parentView.addView(divider);
		} else {
			llLayout.setBackgroundColor(getResources().getColor(R.color.bronw));
		}

		TextView tvLabel = new TextView(getActivity());
		tvLabel.setText(label);
		tvLabel.setTextColor(getResources().getColor(R.color.black));
		LinearLayout.LayoutParams paramsDivision = new LayoutParams((int) getActivity().getResources().getDimension(R.dimen.travel_detail_textview_item_label_width), LayoutParams.WRAP_CONTENT);
		tvLabel.setLayoutParams(paramsDivision);

		TextView tvValuesDeparture = new TextView(getActivity());
		tvValuesDeparture.setText(valueDeparture);
		tvValuesDeparture.setTextColor(getResources().getColor(R.color.black));
		LinearLayout.LayoutParams paramsEnrollNameDeparture = new LayoutParams((int) getActivity().getResources().getDimension(R.dimen.travel_detail_textview_item_value_width), LayoutParams.WRAP_CONTENT);
		tvValuesDeparture.setLayoutParams(paramsEnrollNameDeparture);

		TextView tvValuesDestination = new TextView(getActivity());
		tvValuesDestination.setText(valueDestination);
		tvValuesDestination.setTextColor(getResources().getColor(R.color.black));
		LinearLayout.LayoutParams paramsEnrollNameDestination = new LayoutParams((int) getActivity().getResources().getDimension(R.dimen.travel_detail_textview_item_value_width), LayoutParams.WRAP_CONTENT);
		tvValuesDestination.setLayoutParams(paramsEnrollNameDestination);

		llLayout.addView(tvLabel);
		llLayout.addView(tvValuesDeparture);
		llLayout.addView(tvValuesDestination);

		parentView.addView(llLayout);

	}
}
