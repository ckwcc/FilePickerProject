package com.ckw.filepickerlib.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.ckw.filepickerlib.R;
import com.ckw.filepickerlib.model.ParamEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ckw
 * on 2021/10/15.
 */
public class FilePickerActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    ImageView mIvBack;
//    private Toolbar mToolbar;
    List<String> titles = new ArrayList<>();
    private List<Fragment> mFragments;
    private ParamEntity mParamEntity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mParamEntity = (ParamEntity) getIntent().getExtras().getSerializable("param");
        setTheme(mParamEntity.getTheme());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_picker);
//        mToolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(mToolbar);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initToolbar();
        tabLayout = findViewById(R.id.tl_tabs);
        viewPager = findViewById(R.id.vp_content);
        mIvBack = findViewById(R.id.iv_file_back);
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initFragments();

        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                super.destroyItem(container, position, object);
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {

                return titles.get(position);
            }
        });
        tabLayout.setupWithViewPager(viewPager);
    }

    /**
     * 更新Toolbar展示
     */
    private void initToolbar() {
//        getSupportActionBar().setTitle("附件上传");
//        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
    }

    private List<Fragment>  initFragments(){
        titles.add("微信");
        titles.add("QQ");
        titles.add("钉钉");
        titles.add("全部");
        mFragments = new ArrayList<>(4);

        mFragments.add( FileTypeFragment.newInstance(1,mParamEntity));
        mFragments.add( FileTypeFragment.newInstance(2,mParamEntity));
        mFragments.add( FileTypeFragment.newInstance(3,mParamEntity));
        mFragments.add( FileTypeFragment.newInstance(4,mParamEntity));

        return mFragments;
    }

}
