package jp.evo.btm.fragment;

import jp.evo.btm.fragment.department.DeparmentFragment;
import jp.evo.btm.fragment.staff.StaffAddNewFragment;
import jp.evo.btm.fragment.staff.StaffChangePassword;
import jp.evo.btm.fragment.staff.StaffEditFragment;
import jp.evo.btm.fragment.staff.StaffListFragment;
import jp.evo.btm.fragment.subdepartment.SubDeparmentFragment;
import jp.evo.btm.fragment.travel.TravelAddNewFragment;
import jp.evo.btm.fragment.travel.TravelDetailFragment;
import jp.evo.btm.fragment.travel.TravelFilterFragment;
import jp.evo.btm.fragment.travel.TravelListFragment;
import jp.evo.btm.fragment.travel.UserSearchListFragment;
import jp.evo.btm.fragment.travel.WebviewFragment;
import android.app.Fragment;

public class FragmentFactory {

	public static Fragment getFragment(FragmentType type) {
		switch (type) {
		case DEPARMENT:
			return new DeparmentFragment();
		case SUB_DEPARMENT:
			return new SubDeparmentFragment();
		case USER:
			return new StaffListFragment();
		case ADD_USER:
			return new StaffAddNewFragment();
		case USER_DETAIL:
			return new StaffEditFragment();
		case USER_CHANGE_PASSWORD:
			return new StaffChangePassword();
		case TRAVEL:
			return new TravelAddNewFragment();
		case TRAVEL_DETAIL:
			return new TravelDetailFragment();
		case SEARCH_USER:
			return new UserSearchListFragment();
		case TRAVEL_FILTER:
			return new TravelFilterFragment();
		case TRAVEL_LIST:
			return new TravelListFragment();
		case WEBVIEW:
			return new WebviewFragment();

		default:
			return null;
		}
	}

}
