package com.micang.baozhu.module;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.micang.baozhu.R;
import com.micang.baozhu.module.home.MainActivity;
import com.micang.baozhu.module.home.adapter.GuideViewPagerAdapter;
import com.micang.baselibrary.base.BaseActivity;
import com.micang.baselibrary.util.SPUtils;
import com.micang.baselibrary.util.WindowUtils;

import java.util.ArrayList;

public class GuideActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    private ImageView[] docImgView;
    private int[] docs = {R.id.doc1, R.id.doc2, R.id.doc3};
    private ArrayList<View> viewList;
    private ViewPager guideViewPager;
    private ImageView doc1;
    private ImageView doc2;
    private ImageView doc3;

    @Override
    public int layoutId() {
        return R.layout.activity_guide;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        WindowUtils.setPicTranslucentToStatus(this);
        guideViewPager = findViewById(R.id.guideViewPager);
        doc1 = findViewById(R.id.doc1);
        doc2 = findViewById(R.id.doc2);
        doc3 = findViewById(R.id.doc3);

        viewList = new ArrayList<>();
        LayoutInflater inflater = getLayoutInflater();
        viewList.add(inflater.inflate(R.layout.guide_viewpager_item1, null));
        viewList.add(inflater.inflate(R.layout.guide_viewpager_item2, null));
        viewList.add(inflater.inflate(R.layout.guide_viewpager_item3, null));

        GuideViewPagerAdapter pagerAdapter = new GuideViewPagerAdapter(this, viewList);
        guideViewPager.setAdapter(pagerAdapter);
        guideViewPager.addOnPageChangeListener(this);
        initDoc();
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < docs.length; i++) {
            if (position == i) {
                docImgView[i].setImageResource(R.drawable.icon_doc);
            } else {
                docImgView[i].setImageResource(R.drawable.icon_doc_1);
            }
        }

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    public void goHome(View view) {
        enterHome();
    }

    private void initDoc() {
        docImgView = new ImageView[viewList.size()];
        for (int i = 0; i < viewList.size(); i++) {
            docImgView[i] = (ImageView) findViewById(docs[i]);
        }
    }

    public void enterHome() {
        startActivity(new Intent(this, MainActivity.class));
        SPUtils.saveBoolean(this, "guide", false);
        finish();
//        overridePendingTransition(0, R.anim.anim_fade_in);

    }
}
