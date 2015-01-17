package com.stackbase.mobapp;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.stackbase.mobapp.utils.Constant;
import com.stackbase.mobapp.utils.Helper;

import java.util.ArrayList;
import java.util.List;

public class CollectActivity extends FragmentActivity implements ActionBar.TabListener {
    List<Fragment> frags;
    Fragment idCard, otherInfo;
    ActionBar actionBar;
    ViewPager vp;
    FragmentPagerAdapter pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
        @Override
        public Fragment getItem(int position) {
            return frags.get(position);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return frags.size();
        }
    };
    private TextView nameText;
    private TextView idText;

    private boolean invalidInputs = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collect);

        initView();

        // ActionBar is initiated
        actionBar = getActionBar();

        // Declaration a ViewPager
        vp = (ViewPager) findViewById(R.id.collect);
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
                if (position == 1) {
                    // if move to other info page, need input the id and name first
                    invalidInputs = validateIdCardInputs();
                    if (invalidInputs) {
                        position = 0;
                        vp.setCurrentItem(position);
                    }
                }
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

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        // TODO Auto-generated method stub
        if (invalidInputs) {
            vp.setCurrentItem(0);
            actionBar.setSelectedNavigationItem(0);
        }
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
        // TODO Auto-generated method stub
    }

    private boolean validateIdCardInputs() {
        if (idText == null) {
            idText = (TextView) idCard.getView().findViewById(R.id.idText);
        }
        if (nameText == null) {
            nameText = (TextView) idCard.getView().findViewById(R.id.nameText);
        }
        String id = "";
        String name = "";
        if (idText != null) {
            id = idText.getText().toString();
        }
        if (nameText != null) {
            name = nameText.getText().toString();
        }
        if (id.equals("") || name.equals("")) {
            DialogInterface.OnClickListener alertListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (nameText != null) {
                        nameText.requestFocus();
                    }
                }
            };
            Helper.showErrorMessage(CollectActivity.this, "错误", "必须输入姓名和身份证号码.",
                    null, alertListener);
            return true;
        } else {
            getIntent().putExtra(Constant.INTENT_KEY_ID, id);
            getIntent().putExtra(Constant.INTENT_KEY_NAME, name);
            return false;
        }

    }
}
