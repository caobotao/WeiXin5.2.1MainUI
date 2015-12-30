package com.cbt.weixin;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jauker.widget.BadgeView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {
    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mDatas;

    private TextView mTvChat;
    private TextView mTvFriend;
    private TextView mTvContact;

    private BadgeView mBadgeView;
    private LinearLayout mLayoutChat;

    private ImageView mIvTabline;

    private int mScreen1_3;
    private int mCurrentPageIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        initView();
        initTabline();
    }

    private void initTabline() {
        Display display = getWindow().getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        mScreen1_3 = displayMetrics.widthPixels / 3;
        LayoutParams layoutParams = mIvTabline.getLayoutParams();
        layoutParams.width = mScreen1_3;
        mIvTabline.setLayoutParams(layoutParams);
    }

    private void initView() {
        mTvChat = (TextView) findViewById(R.id.id_tv_chat);
        mTvFriend = (TextView) findViewById(R.id.id_tv_friend);
        mTvContact = (TextView) findViewById(R.id.id_tv_contact);

        mLayoutChat = (LinearLayout) findViewById(R.id.id_ll_chat);

        mIvTabline = (ImageView) findViewById(R.id.id_iv_tabline);
        mViewPager = (ViewPager) findViewById(R.id.id_ViewPager);
        mDatas = new ArrayList<>();
        ChatMainTabFragment tab01 = new ChatMainTabFragment();
        FriendMainTabFragment tab02 = new FriendMainTabFragment();
        ContactMainTabFragment tab03 = new ContactMainTabFragment();
        mDatas.add(tab01);
        mDatas.add(tab02);
        mDatas.add(tab03);

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mDatas.get(position);
            }

            @Override
            public int getCount() {
                return mDatas.size();
            }
        };

        mViewPager.setAdapter(mAdapter);

        mViewPager.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                可以通过setTranslationX实现同样的效果
//                mIvTabline.setTranslationX((positionOffset + position) * mScreen1_3);

                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mIvTabline.getLayoutParams();
                if (mCurrentPageIndex == 0 && position == 0) { // 1页 -> 2页
                    lp.leftMargin = (int) (mCurrentPageIndex * mScreen1_3 + positionOffset * mScreen1_3);
                } else if (mCurrentPageIndex == 1 && position == 0) {// 2页 -> 1页
                    lp.leftMargin = (int) (mCurrentPageIndex * mScreen1_3 + (positionOffset - 1) * mScreen1_3);
                } else if (mCurrentPageIndex == 1 && position == 1) {// 2页 -> 3页
                    lp.leftMargin = (int) (mCurrentPageIndex * mScreen1_3 + positionOffset * mScreen1_3);
                } else if (mCurrentPageIndex == 2 && position == 1) {// 3页 -> 2页
                    lp.leftMargin = (int) (mCurrentPageIndex * mScreen1_3 + (positionOffset - 1) * mScreen1_3);
                }

                mIvTabline.setLayoutParams(lp);
            }

            @Override
            public void onPageSelected(int position) {
                resetTextView();
                switch (position) {
                    case 0:
                        if (mBadgeView != null) {
                            mLayoutChat.removeView(mBadgeView);
                        }
                        mBadgeView = new BadgeView(MainActivity.this);
                        mBadgeView.setBadgeCount(7);
                        mLayoutChat.addView(mBadgeView);
                        mTvChat.setTextColor(Color.parseColor("#008000"));
                        break;
                    case 1:
                        mTvFriend.setTextColor(Color.parseColor("#008000"));
                        break;
                    case 2:
                        mTvContact.setTextColor(Color.parseColor("#008000"));
                        break;
                }
                mCurrentPageIndex = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void resetTextView() {
        mTvChat.setTextColor(Color.BLACK);
        mTvFriend.setTextColor(Color.BLACK);
        mTvContact.setTextColor(Color.BLACK);
    }
}
