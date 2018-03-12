package com.mogu.fragment;

import com.mogu.activity.R;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 新闻资讯板块
 * 
 * @author Administrator
 *
 */
public class NewsCenterFragement extends Fragment {
	private FragmentTabHost mTabHost;
	private TextView apolloText;
	private View tabLayout;
	private View Mlayout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (Mlayout == null) {
			Mlayout = inflater.inflate(R.layout.fragment_news_center, null);
			mTabHost = (FragmentTabHost) Mlayout.findViewById(
					android.R.id.tabhost);
			// 初始化TabHost
			mTabHost.setup(getActivity(), 
					getChildFragmentManager(), R.id.realtabcontent);

			addTab(R.string.news_text1, R.drawable.ic_menu_mc_selector,
					TxtNewsFragment.class);
			addTab(R.string.news_text2, R.drawable.ic_menu_mc_selector1,
					PicNewsFragment.class);
			addTab(R.string.news_text3, R.drawable.ic_menu_mc_selector2,
					VideoNewsFragment.class);
		}
		return Mlayout;
		
	}

	private void addTab(int strRes, int imgRes, Class class1) {
		tabLayout = getActivity().getLayoutInflater().inflate(
				R.layout.top_tab_apollo_layout, null);
		apolloText = (TextView) tabLayout.findViewById(R.id.apollo_text);
		apolloText.setText(strRes);
//		Drawable top = getResources().getDrawable(imgRes);
//		top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());
//		apolloText.setCompoundDrawables(null, top, null, null);
		mTabHost.addTab(mTabHost.newTabSpec("" + imgRes)
				.setIndicator(tabLayout), class1, null);
	}
}
