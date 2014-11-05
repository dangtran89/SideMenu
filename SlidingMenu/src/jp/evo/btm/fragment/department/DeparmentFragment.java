package jp.evo.btm.fragment.department;

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
import jp.evo.btm.model.DepartmentModel;
import jp.evo.btm.model.Result;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DeparmentFragment extends BaseFragment implements OnItemClickListener {

	private SQLLiteDBHelper helper;
	private APILoader loader;
	private ArrayList<DepartmentModel> listDepartment;
	private DepartmentListAdapter adapter;
	private ListView lvDeparment;

	private MenuItem menuAdd;
	private MenuItem menuDelete;
	private MenuItem menuEdit;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		setHasOptionsMenu(true);

		View rootView = inflater.inflate(R.layout.fragment_deparment, container, false);
		getActivity().setTitle(getActivity().getString(R.string.screen_department));
		init(rootView);
		return rootView;
	}

	public void init(View view) {
		System.out.println("dangtb department init");

		listDepartment = new ArrayList<DepartmentModel>();

		lvDeparment = (ListView) view.findViewById(R.id.lvDepartment);
		lvDeparment.setOnItemClickListener(this);

	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	public void loadData() {
		helper = ((BTMApp) getActivity().getApplication()).getSQLiteDBHelper();
		loader = ((BTMApp) getActivity().getApplication()).getAPILoader();
		System.out.println("dangtb loaddata");
		TaskToLoad task = new TaskToLoad() {

			@SuppressWarnings("unchecked")
			@Override
			public Result process() {

				// listDepartment = helper.getListDepartment();
				Result result = loader.getListDepartment(((BTMApp) getActivity().getApplication()).getLoginUser().getCompanyID(), ((BTMApp) getActivity().getApplication()).getLoginUser().getId());
				listDepartment = (ArrayList<DepartmentModel>) result.getReturnObject();
				return result;
			}

			@Override
			public void callback(final Result result) {
				if (result.isSuccess()) {
					adapter = new DepartmentListAdapter(listDepartment);
					if (getActivity() != null)
						((BTMApp) getActivity().getApplication()).setListDepartment(listDepartment);
					lvDeparment.setAdapter(adapter);
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
		inflater.inflate(R.menu.menu_fragment_department_add, menu);
		this.menuAdd = menu.findItem(R.id.menuAdd);
		this.menuDelete = menu.findItem(R.id.menuDelete);
		this.menuEdit = menu.findItem(R.id.menuEdit);
		// this.mProgress = menu.findItem(R.id.rotate);

		if (adapter == null)
			loadData();

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item == menuAdd) {
			IntentFilter intentRefresh = new IntentFilter(SQLLiteDBHelper.REFRESH);
			getActivity().registerReceiver(refreshReceiver, intentRefresh);
			((MainActivity) getActivity()).showDialog(new DeparmentAddDialog());
		} else if (item == menuDelete && adapter != null) {
			if (adapter.enableButtonDelete()) {
				menuDelete.setTitle(getActivity().getString(R.string.menu_done));
				menuEdit.setTitle(getActivity().getString(R.string.edit));
			} else {
				menuDelete.setTitle(getActivity().getString(R.string.delete));
				menuEdit.setTitle(getActivity().getString(R.string.edit));
			}
			adapter.notifyDataSetChanged();
		} else if (item == menuEdit && adapter != null) {
			if (adapter.enableButtonEdit()) {
				menuEdit.setTitle(getActivity().getString(R.string.menu_done));
				menuDelete.setTitle(getActivity().getString(R.string.delete));
			} else {
				menuEdit.setTitle(getActivity().getString(R.string.edit));
				menuDelete.setTitle(getActivity().getString(R.string.delete));
			}
			adapter.notifyDataSetChanged();
		} else if (item == mProgress) {
			loadData();
		}
		return true;
	}

	BroadcastReceiver refreshReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, final Intent intent) {
			System.out.println("dangtb refresh");
			if (intent.getAction().equalsIgnoreCase(SQLLiteDBHelper.REFRESH)) {
				if (intent.getExtras().getString("action").equalsIgnoreCase(SQLLiteDBHelper.ACTION_CREATE)) {
					TaskToLoad task = new TaskToLoad() {

						@Override
						public Result process() {
							// return
							// helper.newDepartment(intent.getExtras().getString("departmentname"));
							return loader.newDepartment(intent.getExtras().getString("departmentname"), ((BTMApp) getActivity().getApplication()).getLoginUser().getCompanyID(), ((BTMApp) getActivity().getApplication()).getLoginUser().getId());
						}

						@Override
						public void callback(final Result result) {
							if (result.isSuccess()) {
								loadData();
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

				} else if (intent.getExtras().getString("action").equalsIgnoreCase(SQLLiteDBHelper.ACTION_EDIT)) {
					TaskToLoad task = new TaskToLoad() {

						@Override
						public Result process() {
							// return
							// helper.updateDepartment(Integer.parseInt(intent.getExtras().getString("departmentid")),
							// intent.getExtras().getString("departmentname"));
							return loader.updateDepartment(Integer.parseInt(intent.getExtras().getString("departmentid")), intent.getExtras().getString("departmentOldName"), intent.getExtras().getString("departmentname"), ((BTMApp) getActivity()
									.getApplication()).getLoginUser().getCompanyID(), ((BTMApp) getActivity().getApplication()).getLoginUser().getId());
						}

						@Override
						public void callback(final Result result) {
							if (result.isSuccess())
								loadData();
							else {
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
				try {
					getActivity().unregisterReceiver(this);
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		}
	};

	@Override
	public void onDestroy() {
		super.onDestroy();
		try {
			getActivity().unregisterReceiver(refreshReceiver);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	};

	private class DepartmentListAdapter extends BaseAdapter {
		ArrayList<DepartmentModel> _listData;
		private boolean isViewButtonDelete = false;
		private boolean isViewButtonEdit = false;

		public DepartmentListAdapter(ArrayList<DepartmentModel> listData) {
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

		public boolean enableButtonDelete() {
			isViewButtonDelete = !isViewButtonDelete;
			if (isViewButtonEdit)
				isViewButtonEdit = false;
			return isViewButtonDelete;
		}

		public boolean enableButtonEdit() {
			isViewButtonEdit = !isViewButtonEdit;
			if (isViewButtonDelete)
				isViewButtonDelete = false;
			return isViewButtonEdit;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup arg2) {
			ViewHolder holder;

			if (convertView == null) {

				LayoutInflater inflater = getActivity().getLayoutInflater();
				convertView = inflater.inflate(R.layout.department_listview_text_item, null);
				holder = new ViewHolder();

				holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
				holder.btnDelete = (Button) convertView.findViewById(R.id.btnDelete);
				holder.btnDelete.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						AlertDialog.Builder builderDialog = new AlertDialog.Builder(getActivity());
						builderDialog.setTitle(getActivity().getString(R.string.warning));
						builderDialog.setMessage(getActivity().getString(R.string.confirm_delete) + " " + _listData.get(position).getName());
						builderDialog.setCancelable(true);
						builderDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								deleteDeparment(position);
							}
						});
						builderDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});

						AlertDialog alert11 = builderDialog.create();
						alert11.show();

					}
				});

				holder.btnEdit = (Button) convertView.findViewById(R.id.btnEdit);
				holder.btnEdit.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						IntentFilter intentRefresh = new IntentFilter(SQLLiteDBHelper.REFRESH);
						getActivity().registerReceiver(refreshReceiver, intentRefresh);
						((BTMApp) getActivity().getApplication()).setCurrentDeparment(_listData.get(position));
						((MainActivity) getActivity()).showDialog(new DeparmentEditDialog());
					}
				});
				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tvName.setText(_listData.get(position).getName());
			if (isViewButtonDelete)
				holder.btnDelete.setVisibility(View.VISIBLE);
			else
				holder.btnDelete.setVisibility(View.GONE);

			if (isViewButtonEdit)
				holder.btnEdit.setVisibility(View.VISIBLE);
			else
				holder.btnEdit.setVisibility(View.GONE);

			return convertView;
		}

		public void deleteDeparment(final int departmentPosition) {
			TaskToLoad task = new TaskToLoad() {

				@Override
				public Result process() {
					// return
					// helper.deleteDepartment(Integer.parseInt(_listData.get(departmentPosition).getId()));
					return loader
							.deleteDepartment(Integer.parseInt(_listData.get(departmentPosition).getId()), ((BTMApp) getActivity().getApplication()).getLoginUser().getCompanyID(), ((BTMApp) getActivity().getApplication()).getLoginUser().getId());
				}

				@Override
				public void callback(final Result result) {
					if (result.isSuccess()) {
						listDepartment.remove(departmentPosition);
					}
					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Toast.makeText(getActivity(), result.getMessage(), Toast.LENGTH_SHORT).show();
							enableButtonDelete();
							adapter.notifyDataSetChanged();
						}
					});

				}
			};
			LoadingTask loadingTask = new LoadingTask(task, mProgress);
			loadingTask.execute();
		}

	}

	/**
	 * holder of a view
	 * 
	 * @author dangtb
	 * 
	 */
	private static class ViewHolder {
		TextView tvName;
		Button btnDelete;
		Button btnEdit;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

		if (arg0 == lvDeparment) {
			((BTMApp) getActivity().getApplication()).setCurrentDeparment(listDepartment.get(position));
			((MainActivity) getActivity()).displayView(FragmentType.SUB_DEPARMENT);
		}

	}
}
