package jp.evo.btm.inf;

import jp.evo.btm.R;
import jp.evo.btm.model.Result;
import android.os.AsyncTask;
import android.support.v4.view.MenuItemCompat;
import android.view.MenuItem;

/**
 * worker loading task
 * 
 * @author dangtb
 * 
 */
public class LoadingTask extends AsyncTask<Void, Result, Result> {

	private TaskToLoad _task;
	private MenuItem _progress;

	public LoadingTask(TaskToLoad task, MenuItem progress) {
		_task = task;
		System.out.println("dangtb process " + (progress == null));
		if (progress != null)
			_progress = progress;
	}

	@Override
	protected Result doInBackground(Void... params) {
		// simulate doing some time-consuming stuff:
		// try {
		// Thread.sleep(2000);
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		return _task.process();
	}

	@Override
	protected void onPostExecute(Result result) {
		super.onPostExecute(result);
		if (_progress != null) {
			System.out.println("dangtb process disable ");
			_progress.setVisible(false);
			MenuItemCompat.setActionView(_progress, null);
		}
		_task.callback(result);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (_progress != null) {
			System.out.println("dangtb process show ");
			_progress.setVisible(true);
			MenuItemCompat.setActionView(_progress, R.layout.actionview_progress);
		}
	}

}
