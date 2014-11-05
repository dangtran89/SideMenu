package jp.evo.btm.fragment;

import jp.evo.btm.R;
import android.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class BaseFragment extends Fragment {
	protected MenuItem mProgress;

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_fragment_placeholder, menu);
		this.mProgress = menu.findItem(R.id.rotate);
		this.mProgress.setVisible(false);

	}

	public boolean onBackPressed() {
		return true;
	}

}
