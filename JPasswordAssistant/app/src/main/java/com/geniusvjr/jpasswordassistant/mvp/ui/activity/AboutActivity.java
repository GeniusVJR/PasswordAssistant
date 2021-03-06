package com.geniusvjr.jpasswordassistant.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.geniusvjr.jpasswordassistant.R;
import com.geniusvjr.jpasswordassistant.databinding.ActivityAboutBinding;
import com.geniusvjr.jpasswordassistant.mvp.model.eventbus.EventCenter;
import com.geniusvjr.jpasswordassistant.mvp.presenter.impl.AboutAImpl;
import com.geniusvjr.jpasswordassistant.mvp.ui.activity.base.Base;
import com.geniusvjr.jpasswordassistant.mvp.ui.activity.base.BaseSwipeBackActivity;
import com.geniusvjr.jpasswordassistant.mvp.ui.view.AboutAView;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by dream on 16/5/27.
 */
public class AboutActivity extends BaseSwipeBackActivity implements AboutAView{

    @Bind(R.id.common_toolbar)
    Toolbar mToolBar;
    private AboutAImpl mAboutImpl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityAboutBinding mDataBinding = (ActivityAboutBinding) super.mDataBinding;
        mAboutImpl = new AboutAImpl(this, this, mDataBinding);
        mAboutImpl.onCreate(savedInstanceState);
        mAboutImpl.getIntent(getIntent());
    }

    @Override
    protected void onEventComing(EventCenter eventCenter) {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_about;
    }

    @Override
    protected void initToolbar() {
        initToolBar(mToolBar);
        mToolBar.setTitle("关于");

    }

    @Override
    protected boolean isApplyTranslucency() {
        return true;
    }

    @Override
    protected boolean isApplyButterKnife() {
        return true;
    }

    @Override
    protected boolean isApplyEventBus() {
        return false;
    }

    @Override
    protected TransitionMode getOverridePendingTransitionMode() {
        return TransitionMode.RIGHT;
    }

    @Override
    protected boolean toggleOverridePendingTransition() {
        return true;
    }

    @OnClick(R.id.codeButton) public void onClick(View view) {
        mAboutImpl.codeClick(view);
    }

    @Override
    public void go2Activity(Class clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
