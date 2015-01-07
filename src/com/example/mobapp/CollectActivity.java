package com.example.mobapp;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

public class CollectActivity extends FragmentActivity implements ActionBar.TabListener {
	List<Fragment> frags;
	Fragment idCard, otherInfo;
	ActionBar actionBar;
	ViewPager vp;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewpager);

		initView();

		// ActionBar is initiated
		actionBar = getActionBar();

		// Declaration a ViewPager
		vp = (ViewPager) findViewById(R.id.viewpager);
		//vp.setOffscreenPageLimit(2);

		// Tell the ActionBar we want to use Tabs
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		ActionBar.Tab idcardTab = actionBar.newTab().setText("身份证");
		ActionBar.Tab otherTab = actionBar.newTab().setText("其他信息");
		
		// set the Tab listener. Now we can listen for clicks
		idcardTab.setTabListener(this);
		otherTab.setTabListener(this);
		
		// add the two tabs to the action bar
		actionBar.addTab(idcardTab);
		actionBar.addTab(otherTab);
		
		// Set a adapter for ViewPager
		vp.setAdapter(pagerAdapter);
		vp.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
		});
	}
	
	private void initView() {
		frags = new ArrayList<Fragment>();

		// create the 2 fragments to display content
		idCard = new FragmentIDCard();
		otherInfo = new FragmentOtherInfo();
		
		frags.add(idCard);
		frags.add(otherInfo);
	}

	FragmentPagerAdapter pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
		@Override
		public Fragment getItem(int  position) {
			return frags.get(position);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return frags.size();
		}
	};

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		vp.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
	}
}
