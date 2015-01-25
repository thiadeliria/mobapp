package com.stackbase.mobapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.stackbase.mobapp.ocr.CaptureActivity;

public class FragmentIDCard extends Fragment {
	View content;
    ImageView frantIDPic;
    Activity active;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		content = inflater.inflate(R.layout.fragment_idcard, null);
		initView();

		return content;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	
	private void initView() {
        active = this.getActivity();
        frantIDPic = (ImageView) content.findViewById(R.id.frontView);

        ImageButton.OnClickListener clickListener = new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(active, CaptureActivity.class);
                startActivity(intent);
            }

        };

        frantIDPic.setOnClickListener(clickListener);
	}
}
