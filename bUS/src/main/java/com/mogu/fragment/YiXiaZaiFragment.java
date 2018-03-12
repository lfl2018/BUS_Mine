package com.mogu.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mogu.activity.MeActivity;
import com.mogu.activity.R;

public class YiXiaZaiFragment extends Fragment {
	private View Mlayout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (Mlayout == null) {
			Mlayout = inflater.inflate(R.layout.fragment_yixiazai, null);
		}
		return Mlayout;
	}

	public YiXiaZaiFragment() {
	}
}
