package com.geniusvjr.jpasswordassistant.mvp.presenter.impl;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.geniusvjr.jpasswordassistant.mvp.model.Constants;
import com.geniusvjr.jpasswordassistant.mvp.model.Realm.RealmHelper;
import com.geniusvjr.jpasswordassistant.mvp.model.bean.God;
import com.geniusvjr.jpasswordassistant.mvp.model.eventbus.EventCenter;
import com.geniusvjr.jpasswordassistant.mvp.presenter.FragmentPresenter;
import com.geniusvjr.jpasswordassistant.mvp.ui.activity.EditActivity;
import com.geniusvjr.jpasswordassistant.mvp.ui.adapter.IndexViewAdapter;
import com.geniusvjr.jpasswordassistant.mvp.ui.view.LoginTypeFView;

import java.util.ArrayList;

/**
 * Created by dream on 16/5/28.
 */
public class IndexFImpl implements FragmentPresenter, IndexViewAdapter.OnRecyclerItemClickListener {


    private final Context mContext;
    private final LoginTypeFView mLoginTypeFView;
    private IndexViewAdapter mAdapter;
    private ArrayList<God> selector;
    private int position;
    private boolean isCreate;

    public IndexFImpl(Context context, LoginTypeFView view) {
        mContext = context;
        mLoginTypeFView = view;
    }
    @Override
    public void onFirstUserVisible() {
        isCreate = true;
        selector = selector();
        mAdapter = new IndexViewAdapter(mContext, selector);
        mAdapter.setOnRecyclerItemClick(this);
        mLoginTypeFView.initRecycler(new LinearLayoutManager(mContext), mAdapter);
        if(selector != null && selector.size() > 0){
            mLoginTypeFView.hideException();
        }else {
            mLoginTypeFView.showException();
        }
    }

    private ArrayList<God> selector(){
        return RealmHelper.getInstances(mContext).selector(mContext, position);
    }

    @Override
    public void onUserVisible() {

    }

    @Override
    public void onUserInvisible() {

    }

    public void onEventComing(EventCenter eventCenter) {
        if (isCreate) {
            if (eventCenter.getEventCode() == 1) {
                boolean data = (boolean) eventCenter.getData();
                if (data) {
                    selector = selector();
                    if (null != selector && selector.size() > 0) {
                        mLoginTypeFView.hideException();
                        mAdapter.addAll(selector);
                    } else {
                        mLoginTypeFView.showException();
                        mAdapter.clearData();
                    }
                    mAdapter.notifyDataSetChanged();
                }
            } else if (eventCenter.getEventCode() == Constants.EVEN_BUS.CHANGE_PASS_WORD_SHOW) {
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onRecyclerItemClick(View view, int position) {
        mLoginTypeFView.readGo(EditActivity.class, Constants.VIEW_MODE, position, this.position);
    }
    public void getArgus(Bundle arguments) {
        position = arguments.getInt("position");
    }
}
