package jp.evo.btm.fragment.travel;

import java.util.ArrayList;

import jp.evo.btm.BTMApp;
import jp.evo.btm.MainActivity;
import jp.evo.btm.R;
import jp.evo.btm.database.APILoader;
import jp.evo.btm.database.SQLLiteDBHelper;
import jp.evo.btm.fragment.BaseFragment;
import jp.evo.btm.fragment.FragmentType;
import jp.evo.btm.inf.LoadingTask;
import jp.evo.btm.inf.TaskToLoad;
import jp.evo.btm.model.Result;
import jp.evo.btm.model.StaffModel;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

public class UserSearchListFragment extends BaseFragment implements SearchView.OnQueryTextListener {

	private SQLLiteDBHelper helper;
	private APILoader loader;
	private ArrayList<StaffModel> listStaff;
	private StaffListAdapter adapter;
	private ListView lvStaff;

	private MenuItem menuAdd;
	private MenuItem menuSearch;
	private SearchView mSearchView;

	private ArrayList<StaffModel> listEnroll;
	private boolean[] enrollStatus;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		setHasOptionsMenu(true);

		View rootView = inflater.inflate(R.layout.fragment_travel_detail_search_staff, container, false);
		getActivity().setTitle(getActivity().getString(R.string.screen_travel_detail_search_user));
		init(rootView);
		return rootView;
	}

	public void init(View view) {

		listStaff = new ArrayList<StaffModel>();

		lvStaff = (ListView) view.findViewById(R.id.lvStaff);

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	public void loadData() {
		helper = ((BTMApp) getActivity().getApplication()).getSQLiteDBHelper();
		loader = ((BTMApp) getActivity().getApplication()).getAPILoader();
		listEnroll = ((BTMApp) getActivity().getApplication()).getTravelValues().getListStaffEnroll();

		TaskToLoad task = new TaskToLoad() {

			@SuppressWarnings("unchecked")
			@Override
			public Result process() {
				Result result = loader.getListStaff(((BTMApp) getActivity().getApplication()).getLoginUser().getCompanyID(), ((BTMApp) getActivity().getApplication()).getLoginUser().getId());
				listStaff = (ArrayList<StaffModel>) result.getReturnObject();
				return result;
			}

			@Override
			public void callback(final Result result) {
				if (result.isSuccess()) {
					initAddRemoveButton();
					adapter = new StaffListAdapter(listStaff);
					lvStaff.setAdapter(adapter);
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

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_fragment_staff, menu);
		this.menuAdd = menu.findItem(R.id.menuAdd);
		this.menuSearch = menu.findItem(R.id.action_search);
		this.mSearchView = (SearchView) menuSearch.getActionView();
		setupSearchView(menuSearch);
		if (adapter == null)
			loadData();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item == menuAdd) {
			((MainActivity) getActivity()).displayView(FragmentType.ADD_USER);
		}
		return true;
	}

	@SuppressLint("NewApi")
	private void setupSearchView(MenuItem searchItem) {

		if (isAlwaysExpanded()) {
			mSearchView.setIconifiedByDefault(false);
		} else {
			searchItem.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
		}

		mSearchView.setOnQueryTextListener(this);
	}

	public boolean onQueryTextChange(String newText) {
		System.out.println("dangtb search " + ("Query = " + newText));
		return false;
	}

	public boolean onQueryTextSubmit(final String query) {
		System.out.println("dangtb search text submit " + (query + " : submitted"));
		TaskToLoad task = new TaskToLoad() {

			@SuppressWarnings("unchecked")
			@Override
			public Result process() {
				Result result = loader.searchStaff(query, ((BTMApp) getActivity().getApplication()).getLoginUser().getCompanyID(), ((BTMApp) getActivity().getApplication()).getLoginUser().getId());
				listStaff = (ArrayList<StaffModel>) result.getReturnObject();
				return result;
			}

			@Override
			public void callback(final Result result) {
				if (result.isSuccess()) {
					initAddRemoveButton();
					adapter = new StaffListAdapter(listStaff);
					lvStaff.setAdapter(adapter);
				} else {
					if (getActivity() != null)
						getActivity().runOnUiThread(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								Toast.makeText(getActivity(), result.getMessage(), Toast.LENGTH_SHORT).show();
							}
						});
				}
			}
		};
		LoadingTask loadingTask = new LoadingTask(task, mProgress);
		loadingTask.execute();
		return false;
	}

	public void initAddRemoveButton() {
		if (listStaff != null && listStaff.size() > 0) {
			enrollStatus = new boolean[listStaff.size()];
			for (int i = 0; i < enrollStatus.length; i++) {
				enrollStatus[i] = false;
			}
			if (listEnroll != null && listEnroll.size() > 0) {
				for (int i = 0; i < listStaff.size(); i++) {
					for (int k = 0; k < listEnroll.size(); k++) {
						if (listEnroll.get(k).getId() == listStaff.get(i).getId()) {
							enrollStatus[i] = true;
						}
					}
				}
			}
		}
	}

	public boolean onClose() {
		System.out.println("dangtb search close ");
		return false;
	}

	protected boolean isAlwaysExpanded() {
		return false;
	}

	private class StaffListAdapter extends BaseAdapter {
		ArrayList<StaffModel> _listData;

		public StaffListAdapter(ArrayList<StaffModel> listData) {
			_listData = listData;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return _listData.size();
		}

		@Override
		public Object getItem(int arg0) {
			return _listData.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup arg2) {
			ViewHolder holder;

			if (convertView == null) {

				LayoutInflater inflater = getActivity().getLayoutInflater();
				convertView = inflater.inflate(R.layout.travel_detail_user_search_text_item, null);
				holder = new ViewHolder();

				holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
				holder.tvDivision = (TextView) convertView.findViewById(R.id.tvDivision);
				holder.btnAddRemove = (Button) convertView.findViewById(R.id.btnAddRemove);

				

				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.btnAddRemove.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (enrollStatus[position]) {
						for (int k = 0; k < listEnroll.size(); k++) {
							if (listStaff.get(position).getId() == listEnroll.get(k).getId()) {
								listEnroll.remove(k);
							}
						}
					} else {
						listEnroll.add(listStaff.get(position));
					}
					enrollStatus[position] = !enrollStatus[position];
					adapter.notifyDataSetChanged();
				}
			});
//			convertView.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					((BTMApp) getActivity().getApplication()).setCurrentStaffID(listStaff.get(position).getId());
//					((MainActivity) getActivity()).displayView(FragmentType.USER_DETAIL);
//				}
//			});
			holder.tvName.setText(_listData.get(position).getUserName());
			holder.tvDivision.setText(_listData.get(position).getSubDivisionName());

			if (enrollStatus[position]) {
				holder.btnAddRemove.setText(getActivity().getString(R.string.remove));
			} else {
				holder.btnAddRemove.setText(getActivity().getString(R.string.add));
			}

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
		TextView tvDivision;
		TextView tvName;
		Button btnAddRemove;
	}

}
