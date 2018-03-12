package com.mogu.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mogu.activity.LoginActivity;
import com.mogu.activity.MainActivity;
import com.mogu.activity.R;

public class GuideFragment extends Fragment implements OnClickListener {

    public static final int[] imgsRes = {R.drawable.pic_guide_page1,
            R.drawable.pic_guide_page2, R.drawable.pic_guide_page3,R.drawable.pic_guide_page4};
    public static final int[] imgsResP = {R.drawable.bg_guide_page1_phone,
            R.drawable.bg_guide_page2_phone, R.drawable.bg_guide_page3_phone};
    public static final int[] imgsResW = {R.drawable.bg_guide_page1_word,
            R.drawable.bg_guide_page2_word, R.drawable.bg_guide_page3_word};

    private ImageView mImageView;
    private ImageView mImageViewW;
    private ImageView mImageViewP;

    private int position;
    private boolean needVisible;
    private boolean needVisible1;
    private boolean needVisible2;
    private boolean needVisible3;
    private ImageView ivStart;
    private LinearLayout ll1;
    private LinearLayout ll2;
    private LinearLayout ll3;


    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_guide, null);
        mImageView = (ImageView) inflate.findViewById(R.id.imageView);
        mImageViewW = (ImageView) inflate.findViewById(R.id.imageViewTxt);
        mImageViewP = (ImageView) inflate.findViewById(R.id.imageViewPhone);
        ll1 = (LinearLayout) inflate.findViewById(R.id.ll_guid1);
        ll2 = (LinearLayout) inflate.findViewById(R.id.ll_guid2);
        ll3 = (LinearLayout) inflate.findViewById(R.id.ll_guid3);
        ivStart = (ImageView) inflate.findViewById(R.id.iv_start);
        ivStart.setOnClickListener(this);
        if (needVisible) {
            ivStart.setVisibility(View.VISIBLE);
        }
        if (needVisible1){
            ll1.setVisibility(View.VISIBLE);
        }
        if (needVisible2){
            ll2.setVisibility(View.VISIBLE);
        }
        if (needVisible3){
            ll3.setVisibility(View.VISIBLE);
        }

        mImageView.setImageResource(imgsRes[position]);
//        mImageViewW.setImageResource(imgsResW[position]);
//        mImageViewP.setImageResource(imgsResP[position]);

        return inflate;
    }

    public void setVisible() {
        this.needVisible = true;
    }
    public void setVisible1() {
        this.needVisible1 = true;
    }
    public void setVisible2() {
        this.needVisible2 = true;
    }
    public void setVisible3() {
        this.needVisible3 = true;
    }
    public void setImage(int position) {
        this.position = position;
    }

    @Override
    public void onClick(View v) {
        SharedPreferences sp = getActivity().getSharedPreferences("Test", 0);
        Editor edit = sp.edit();
        edit.putBoolean("isFirst", false);
        edit.commit();
        startActivity(new Intent().setClass(getActivity(), MainActivity.class));
        getActivity().finish();
    }
}
