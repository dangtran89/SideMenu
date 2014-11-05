package jp.evo.btm.fragment.subdepartment;

import java.util.ArrayList;

import jp.evo.btm.BTMApp;
import jp.evo.btm.MainActivity;
import jp.evo.btm.R;
import jp.evo.btm.database.APILoader;
import jp.evo.btm.database.SQLLiteDBHelper;
import jp.evo.btm.fragment.BaseFragment;
import jp.evo.btm.inf.LoadingTask;
import jp.evo.btm.inf.TaskToLoad;
import jp.evo.btm.model.Result;
import jp.evo.btm.model.SubDepartmentModel;
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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SubDeparmentFragment extends BaseFragment {

	private SQLLiteDBHelper helper;
	private APILoader loader;
	private ArrayList<SubDepartmentModel> listDepartment;
	private DepartmentListAdapter adapter;
	private ListView lvSubDeparment;

	private MenuItem menuAdd;
	private MenuItem menuDelete;
	private MenuItem menuEdit;

	private int idCurrentDepartment=-1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		setHasOptionsMenu(true);

		View rootView = inflater.inflate(R.layout.fragment_sub_deparment, container, false);
		getActivity().setTitle(getActivity().getString(R.string.screen_sub_department));
		idCurrentDepartment = Integer.parseInt(((BTMApp) getActivity().getApplication()).getCurrentDeparment().getId());
		System.out.println("dangtb subde id " + idCurrentDepartment);
		init(rootView);
		return rootView;
	}

	public void init(View view) {
		System.out.println("dangtb department init");

		listDepartment = new ArrayList<SubDepartmentModel>();

		lvSubDeparment = (ListView) view.findViewById(R.id.lvSubDepartment);

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	public void loadData() {
		helper = ((BTMApp) getActivity().getApplication()).getSQLiteDBHelper();
		loader = ((BTMApp) getActivity().getApplication()).getAPILoader();
		TaskToLoad task = new TaskToLoad() {

			@SuppressWarnings("unchecked")
			@Override
			public Result process() {
				// Result result =
				// helper.getListSubDepartment(idCurrentDepartment);
				Result result = loader.getListSubDepartment(idCurrentDepartment, ((BTMApp) getActivity().getApplication()).getLoginUser().getCompanyID(), ((BTMApp) getActivity().getApplication()).getLoginUser().getId());
				listDepartment = (ArrayList<SubDepartmentModel>) result.getReturnObject();
				return result;
			}

			@Override
			public void callback(Result result) {
				if (result.isSuccess()) {
					adapter = new DepartmentListAdapter(listDepartment);
					lvSubDeparment.setAdapter(adapter);
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
		if (idCurrentDepartment != -1 && adapter == null)
			loadData();

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item == menuAdd) {
			IntentFilter intentRefresh = new IntentFilter(SQLLiteDBHelper.REFRESH);
			getActivity().registerReceiver(refreshReceiver, intentRefresh);
			((MainActivity) getActivity()).showDialog(new SubDeparmentAddDialog());
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
							// helper.newSubDepartment(intent.getExtras().getInt("departmentid"),
							// intent.getExtras().getString("subdepartmentname"));
							return loader.newSubDepartment(intent.getExtras().getInt("departmentid"), intent.getExtras().getString("subdepartmentname"), ((BTMApp) getActivity().getApplication()).getLoginUser().getCompanyID(), ((BTMApp) getActivity()
									.getApplication()).getLoginUser().getId());
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
							// helper.updateSubDepartment(intent.getExtras().getInt("subdepartmentid"),
							// intent.getExtras().getString("subdepartmentname"));
							return loader.updateSubDepartment(intent.getExtras().getInt("subdepartmentid"), intent.getExtras().getString("subdepartmentOldName"), intent.getExtras().getString("subdepartmentname"), ((BTMApp) getActivity()
									.getApplication()).getLoginUser().getCompanyID(), ((BTMApp) getActivity().getApplication()).getLoginUser().getId());
						}

						@Override
						public void callback(Result result) {
							loadData();
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
		ArrayList<SubDepartmentModel> _listData;
		private boolean isViewButtonDelete = false;
		private boolean isViewButtonEdit = false;

		public DepartmentListAdapter(ArrayList<SubDepartmentModel> listData) {
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
				convertView = inflater.inflate(R.layout.sub_department_listview_text_item, null);
				holder = new ViewHolder();

				holder.tvSubDepartmentName = (TextView) convertView.findViewById(R.id.tvSubDepartmentName);
				holder.btnSubDepartmentDelete = (Button) convertView.findViewById(R.id.btnSubDepartmentDelete);
				holder.btnSubDepartmentDelete.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						AlertDialog.Builder builderDialog = new AlertDialog.Builder(getActivity());
						builderDialog.setTitle(getActivity().getString(R.string.warning));
						builderDialog.setMessage(getActivity().getString(R.string.confirm_delete) + " " + _listData.get(position).getName());
						builderDialog.setCancelable(true);
						builderDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								deleteSubDepartment(position);
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

				holder.btnSubDepartmentEdit = (Button) convertView.findViewById(R.id.btnSubDepartmentEdit);
				holder.btnSubDepartmentEdit.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						IntentFilter intentRefresh = new IntentFilter(SQLLiteDBHelper.REFRESH);
						getActivity().registerReceiver(refreshReceiver, intentRefresh);
						((BTMApp) getActivity().getApplication()).setCurrentSubDeparment(_listData.get(position));
						((MainActivity) getActivity()).showDialog(new SubDeparmentEditDialog());
					}
				});

				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tvSubDepartmentName.setText(_listData.get(position).getName());
//			holder.tvName.setText(_listData.get(position).getName());
			if (isViewButtonDelete)
				holder.btnSubDepartmentDelete.setVisibility(View.VISIBLE);
			else
				holder.btnSubDepartmentDelete.setVisibility(View.GONE);

			if (isViewButtonEdit)
				holder.btnSubDepartmentEdit.setVisibility(View.VISIBLE);
			else
				holder.btnSubDepartmentEdit.setVisibility(View.GONE);

			return convertView;
		}

		public void deleteSubDepartment(final int subDepartmentPosition) {
			TaskToLoad task = new TaskToLoad() {

				@Override
				public Result process() {
					// return
					// helper.deleteSubDepartment(Integer.parseInt(_listData.get(subDepartmentPosition).getId()));
					return loader.deleteSubDepartment(Integer.parseInt(_listData.get(subDepartmentPosition).getId()), ((BTMApp) getActivity().getApplication()).getLoginUser().getCompanyID(), ((BTMApp) getActivity().getApplication()).getLoginUser()
							.getId());
				}

				@Override
				public void callback(final Result result) {
					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {
							if (result.isSuccess()) {
								listDepartment.remove(subDepartmentPosition);
							}
							Toast.makeText(getActivity(), result.getMessage(), Toast.LENGTH_SHORT).show();
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
		TextView tvSubDepartmentName;
		Button btnSubDepartmentDelete;
		Button btnSubDepartmentEdit;
	}
}
