package jp.evo.btm.fragment.travel;

import java.util.List;

import jp.evo.btm.BTMApp;
import jp.evo.btm.R;
import jp.evo.btm.common.ConstantValues;
import jp.evo.btm.fragment.BaseFragment;

import org.apache.http.NameValuePair;
import org.apache.http.util.EncodingUtils;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebviewFragment extends BaseFragment {

	private WebView webview;
	private int typeOfTicket;

	// private APILoader loader;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		setHasOptionsMenu(true);
		typeOfTicket = ((BTMApp) getActivity().getApplication()).getTypeOfTicket();
		switch (typeOfTicket) {
		case ConstantValues.TICKET_TYPE_PACKAGE:
			getActivity().setTitle(getResources().getString(R.string.screen_travel_webview_package));
			break;
		case ConstantValues.TICKET_TYPE_PLANE:
			getActivity().setTitle(getResources().getString(R.string.screen_travel_webview_plane));
			break;
		case ConstantValues.TICKET_TYPE_SHINKANSEN:
			getActivity().setTitle(getResources().getString(R.string.screen_travel_webview_train));
			break;

		default:
			break;
		}
	
		View view = inflater.inflate(R.layout.webview_fragment, container, false);
		init(view);
		return view;
	}

	public void init(View view) {
		webview = (WebView) view.findViewById(R.id.webview);
		settingWebView();
	}

	@SuppressLint("SetJavaScriptEnabled")
	public void settingWebView() {
		webview.getSettings().setJavaScriptEnabled(true);
		webview.getSettings().setBuiltInZoomControls(true);
		webview.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		loadData();
	}

	public void loadData() {
		// loader = ((BTMApp) getActivity().getApplication()).getAPILoader();
		
		List<NameValuePair> values = ((BTMApp) getActivity().getApplication()).getTravelValues().getTravelParams();
		String postData = "";
		for (int i = 0; i < values.size(); i++) {
			if (i == 0) {
				postData = postData + values.get(i).getName() + "=" + values.get(i).getValue();
			} else {
				postData = postData + "&" + values.get(i).getName() + "=" + values.get(i).getValue();
			}
		}
		System.out.println("dangtb postdata " + postData);

		switch (typeOfTicket) {
		case ConstantValues.TICKET_TYPE_PACKAGE:
			webview.postUrl(((BTMApp) getActivity().getApplication()).getTravelValues().getUrlPackage(), EncodingUtils.getBytes(postData, "BASE64"));
			break;
		case ConstantValues.TICKET_TYPE_PLANE:
			webview.postUrl(((BTMApp) getActivity().getApplication()).getTravelValues().getUrlPlane(), EncodingUtils.getBytes(postData, "BASE64"));
			break;
		case ConstantValues.TICKET_TYPE_SHINKANSEN:
			webview.postUrl(((BTMApp) getActivity().getApplication()).getTravelValues().getUrlShinkansen(), EncodingUtils.getBytes(postData, "BASE64"));
			break;

		default:
			break;
		}
		// TaskToLoad task = new TaskToLoad() {
		//
		// @Override
		// public Result process() {
		// TravelModel model = ((BTMApp)
		// getActivity().getApplication()).getTravelValues();
		// Result result = new Result();
		// switch (typeOfTicket) {
		// case ConstantValues.TICKET_TYPE_PACKAGE:
		// result = loader.loadUrl(model.getTravelParams(),
		// model.getUrlPackage());
		// break;
		// case ConstantValues.TICKET_TYPE_PLANE:
		// result = loader.loadUrl(model.getTravelParams(),
		// model.getUrlPlane());
		// break;
		// case ConstantValues.TICKET_TYPE_SHINKANSEN:
		// result = loader.loadUrl(model.getTravelParams(),
		// model.getUrlShinkansen());
		// break;
		//
		// default:
		// break;
		// }
		// return result;
		// }
		//
		// @Override
		// public void callback(Result result) {
		// // TODO Auto-generated method stub
		// webview.loadData((String) result.getReturnObject(),
		// "text/html; charset=UTF-8", null);
		// }
		// };
		// LoadingTask loading = new LoadingTask(task, mProgress);
		// loading.execute();

	}

	@Override
	public boolean onBackPressed() {
//		if (webview.canGoBack()) {
//			webview.goBack();
//			return false;
//		} else {
			return true;
//		}
	}

}
