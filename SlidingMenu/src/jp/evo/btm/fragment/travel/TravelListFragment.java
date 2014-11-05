package jp.evo.btm.fragment.travel;

import java.util.ArrayList;

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
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TravelListFragment extends BaseFragment implements OnItemClickListener {

	private SQLLiteDBHelper helper;
	private APILoader loader;

	private ArrayList<TravelModel> listTravel;
	private TravelListAdapter adapter;
	private ListView lvTravel;

	private MenuItem menuUpdate;
	private MenuItem menuDelete;
	private MenuItem menuFilter;

	ArrayList<TravelModel> listFilter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		setHasOptionsMenu(true);

		View rootView = inflater.inflate(R.layout.fragment_travel_list, container, false);
		getActivity().setTitle(getResources().getString(R.string.screen_travel_list));
		init(rootView);
		return rootView;
	}

	public void init(View view) {

		listTravel = new ArrayList<TravelModel>();

		lvTravel = (ListView) view.findViewById(R.id.lvTravel);
		LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View headerView = layoutInflater.inflate(R.layout.travel_list_text_item, null);
		lvTravel.addHeaderView(headerView);
		lvTravel.setOnItemClickListener(this);

		if (adapter == null)
			loadData();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	public void loadData() {
		helper = ((BTMApp) getActivity().getApplication()).getSQLiteDBHelper();
		loader = ((BTMApp) getActivity().getApplication()).getAPILoader();
		listTravel = ((BTMApp) getActivity().getApplication()).getListSearchTravel();
		if (listTravel != null && listTravel.size() > 0) {
			// ((BTMApp)
			// getActivity().getApplication()).setCurrentFilter(APILoader.FILTER_NONE);
			// adapter = new TravelListAdapter(listTravel);
			// lvTravel.setAdapter(adapter);
			filter(((BTMApp) getActivity().getApplication()).getCurrentFilter());
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_fragment_travel_list, menu);
		this.menuUpdate = menu.findItem(R.id.menuUpdate);
		this.menuDelete = menu.findItem(R.id.menuDelete);
		this.menuFilter = menu.findItem(R.id.menuFilter);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item == menuUpdate) {
		} else if (item == menuDelete) {

		} else if (item == menuFilter) {
			IntentFilter intentFilter = new IntentFilter(APILoader.REFRESH);
			getActivity().registerReceiver(receiverFilter, intentFilter);
			((MainActivity) getActivity()).showDialog(new TravelFilterDialog());
		}
		return true;
	}

	BroadcastReceiver receiverFilter = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			filter(intent.getExtras().getString("filter"));

		}
	};

	public void filter(String filter) {
		int statusFilter = -1;
		if (filter.equalsIgnoreCase(APILoader.FILTER_APPLY)) {
			statusFilter = ConstantValues.TRAVEL_STATION_STATUS_PENDING_APPLY;
		} else if (filter.equalsIgnoreCase(APILoader.FILTER_APPROVE)) {
			statusFilter = ConstantValues.TRAVEL_STATION_STATUS_PENDING_APPROVE;
		} else if (filter.equalsIgnoreCase(APILoader.FILTER_CANCEL)) {
			statusFilter = ConstantValues.TRAVEL_STATION_STATUS_CANCEL;
		} else if (filter.equalsIgnoreCase(APILoader.FILTER_OK)) {
			statusFilter = ConstantValues.TRAVEL_STATION_STATUS_OK;
		} else if (filter.equalsIgnoreCase(APILoader.FILTER_NONE)) {
			statusFilter = ConstantValues.TRAVEL_STATION_STATUS_INIT;
		}
		((BTMApp) getActivity().getApplication()).setCurrentFilter(filter);
		if (statusFilter != ConstantValues.TRAVEL_STATION_STATUS_INIT) {
			listFilter = new ArrayList<TravelModel>();
			if (listTravel != null && listTravel.size() > 0) {
				for (int i = 0; i < listTravel.size(); i++) {
					if (listTravel.get(i).getStatus() == statusFilter) {
						listFilter.add(listTravel.get(i));
					}
				}
			}
		} else {
			listFilter = listTravel;
		}
		adapter = new TravelListAdapter(listFilter);
		lvTravel.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		try {
			getActivity().unregisterReceiver(receiverFilter);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	};

	private class TravelListAdapter extends BaseAdapter {
		ArrayList<TravelModel> _listData;

		public TravelListAdapter(ArrayList<TravelModel> listData) {
			_listData = listData;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return _listData.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return _listData.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			ViewHolder holder;

			if (convertView == null) {

				LayoutInflater inflater = getActivity().getLayoutInflater();
				convertView = inflater.inflate(R.layout.travel_list_text_item, null);
				holder = new ViewHolder();

				holder.tvID = (TextView) convertView.findViewById(R.id.tvIdTravel);
				holder.tvStatus = (TextView) convertView.findViewById(R.id.tvStatus);
				holder.tvFromTime = (TextView) convertView.findViewById(R.id.tvFromTime);
				holder.tvTotome = (TextView) convertView.findViewById(R.id.tvToTime);

				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.tvID.setText(String.valueOf(_listData.get(position).getDealCode()));
			holder.tvStatus.setText(String.valueOf(_listData.get(position).getPurpose()));
			System.out.println("dangtb status filter " + _listData.get(position).getStatus());
			if (_listData.get(position).getStatus() == ConstantValues.TRAVEL_STATION_STATUS_OK) {
				convertView.setBackgroundColor(Color.parseColor("#19BFE8"));
				// holder.tvStatus.setText(getActivity().getString(R.string.status_ok));
			} else if (_listData.get(position).getStatus() == ConstantValues.TRAVEL_STATION_STATUS_PENDING_APPLY) {
				convertView.setBackgroundColor(Color.parseColor("#C2FF85"));
				// holder.tvStatus.setText(getActivity().getString(R.string.status_pending_apply));
			} else if (_listData.get(position).getStatus() == ConstantValues.TRAVEL_STATION_STATUS_PENDING_APPROVE) {
				convertView.setBackgroundColor(Color.parseColor("#FFFF00"));
				// holder.tvStatus.setText(getActivity().getString(R.string.status_pending_apply));
			} else if (_listData.get(position).getStatus() == ConstantValues.TRAVEL_STATION_STATUS_CANCEL) {
				convertView.setBackgroundColor(Color.parseColor("#C2C2CE"));
				// holder.tvStatus.setText(getActivity().getString(R.string.status_cancel));
			}
			holder.tvFromTime.setText(_listData.get(position).getFromtime());
			holder.tvTotome.setText(_listData.get(position).getTotime());

			return convertView;
		}

	}

	/**
	 * holder of a view
	 * 
	 * @author dangtb
	 * 
	 */
	private static class ViewHolder {
		TextView tvID;
		TextView tvStatus;
		TextView tvFromTime;
		TextView tvTotome;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, final int position, long arg3) {
		if (arg0 == lvTravel) {
			if (position == 0)
				return;
			((BTMApp) getActivity().getApplication()).setCurrentTravelID(listFilter.get(position - 1).getId());
			TaskToLoad task = new TaskToLoad() {

				@Override
				public Result process() {
					Result result = loader.getTravelDetail(listFilter.get(position - 1).getId(), ((BTMApp) getActivity().getApplication()).getLoginUser().getCompanyID(), ((BTMApp) getActivity().getApplication()).getLoginUser().getId());
					((BTMApp) getActivity().getApplication()).setTravelValues((TravelModel) result.getReturnObject());
					return result;
				}

				@Override
				public void callback(final Result result) {
					if (result.isSuccess()) {
						if (getActivity() != null)
							getActivity().runOnUiThread(new Runnable() {

								@Override
								public void run() {
									((MainActivity) getActivity()).displayView(FragmentType.TRAVEL_DETAIL);

								}
							});
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

	}
}
