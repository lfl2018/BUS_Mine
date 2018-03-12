package com.mogu.activity;

import com.mogu.fragment.ShopPubFragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class ShopPubActivity extends FragmentActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop_pub);
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		ShopPubFragment fragment = new ShopPubFragment();
		transaction.add(R.id.fl_shop_pub, fragment);
		transaction.commit();
		
		
	}
}
