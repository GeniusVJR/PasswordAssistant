package com.geniusvjr.jpasswordassistant.mvp.presenter.impl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;

import com.geniusvjr.jpasswordassistant.R;
import com.geniusvjr.jpasswordassistant.mvp.model.Realm.RealmHelper;
import com.geniusvjr.jpasswordassistant.mvp.model.bean.God;
import com.geniusvjr.jpasswordassistant.mvp.presenter.ActivityPresenter;
import com.geniusvjr.jpasswordassistant.mvp.ui.view.EditAView;
import com.geniusvjr.jpasswordassistant.utils.TimeUtils;
import com.google.common.collect.ArrayListMultimap;

import java.util.ArrayList;

import me.imid.swipebacklayout.lib.SwipeBackLayout;






/**
 * Created by dream on 16/5/29.
 */
public class EditAImpl implements ActivityPresenter, SwipeBackLayout.SwipeListener, View.OnFocusChangeListener, TextWatcher, View.OnClickListener, CompoundButton.OnCheckedChangeListener, AdapterView.OnItemSelectedListener {

    private final Context mContext;
    private final EditAView mEditAView;
    private int mPosition = 0;
    private int createMode;
    private boolean isEdit;
    private boolean isCreate;
    private God mGodInfo;
    private int positionType;
    private String mPositiveButtonMsg;
    private int p;

    public EditAImpl(Context context, EditAView view) {
        mContext = context;
        mEditAView = view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        String[] stringArray = mContext.getResources().getStringArray(R.array.spinner_item);
        ArrayList<String> arrayList = new ArrayList<>();
        for(String str : stringArray){
            arrayList.add(str);
        }
        mEditAView.initSpinner(arrayList);
        mEditAView.getSwipeBack().addSwipeListener(this);
    }

    @Override
    public void getIntent(Intent intent) {
        createMode = intent.getIntExtra("CREATE_MODE", 1);
        switch (createMode){
            case 0:   //查看
                p = intent.getIntExtra("position", 0);
                //密码类型
                mPosition = positionType = intent.getIntExtra("positionType", 0);
                ArrayList<God> selector = selector(positionType);
                mGodInfo = selector.get(p);
                mEditAView.initViewModel(mGodInfo, positionType);
                mEditAView.setToolBarTitle(R.string.view_mode);
                mEditAView.setTime(TimeUtils.getTime(mGodInfo.getTime()));
                isEdit = false;
                break;
            case 1:  //添加
                isCreate = true;
                mEditAView.initCreateModel();
                break;
        }
    }

    private ArrayList<God> selector(int positionType){
        return RealmHelper.getInstances(mContext).selector(mContext, positionType);
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onScrollStateChange(int state, float scrollPercent) {
        mEditAView.hideKeyBoard();
    }

    @Override
    public void onEdgeTouch(int edgeFlag) {

    }

    @Override
    public void onScrollOverThreshold() {

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus){
            isEdit = true;
            mEditAView.setToolBarTitle(R.string.edit_mode);
        }
    }

    /**
     * 在text改变之前
     * @param s
     * @param start
     * @param count
     * @param after
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    /**
     * 在改变的时候需要显示菜单的确定图标
     * @param s
     * @param start
     * @param before
     * @param count
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String titleName = mEditAView.getTitleName();
        String userName = mEditAView.getUserName();
        String passWord = mEditAView.getPassWord();
        if (!TextUtils.isEmpty(passWord)) {
            if (!TextUtils.isEmpty(titleName) && !TextUtils.isEmpty(userName)) {
                mEditAView.setItemMenuVisible(true);
            } else {
                mEditAView.setItemMenuVisible(false);
            }
        } else {
            mEditAView.setItemMenuVisible(false);
        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public boolean onOptionItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.done:
                saveData();
                return true;

            case android.R.id.home:
                return comeBack();
            default: return false;
        }
    }




    public boolean comeBack(){
        if (isEdit) {
            String userName = mEditAView.getUserName();
            String passWord = mEditAView.getPassWord();
            mEditAView.hideKeyBoard();
            if (positionType != mPosition || !TextUtils.equals(userName, mGodInfo.getUserName()) || !TextUtils.equals(passWord, mGodInfo.getPassWord())) {
                mPositiveButtonMsg = "保存";
                mEditAView.showDialog("密码还未保存，是否先保存在退出", mPositiveButtonMsg);
            } else {
                mEditAView.finishActivity();
            }
        } else {
            mEditAView.hideKeyBoard();
            mEditAView.finishActivity();
        }
        return true;
    }

    private void saveData() {
        String titleName = mEditAView.getTitleName();
        String userName = mEditAView.getUserName();
        String passWord = mEditAView.getPassWord();
        String memoInfo = mEditAView.getMemoInfo();
        God god = new God(mPosition, titleName, userName, passWord, TimeUtils.getCurrentTimeInLong(), memoInfo);
        switch (createMode) {
            case 0:
                if (!RealmHelper.update(mContext, god)) {
                    mEditAView.showSnackToast("修改失败");
                    mEditAView.hideKeyBoard();
                    return;
                }
                break;
            case 1:
                if (RealmHelper.save(mContext, god)) {
                    mEditAView.showSnackToast("保存失败，已经存在-"+god.getTitle()+"-的标题");
                    mEditAView.hideKeyBoard();
                    return;
                }
                break;
        }

        mEditAView.hideKeyBoard();
        mEditAView.finishActivity();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.deleteButton) {
            mPositiveButtonMsg = "确定";
            mEditAView.showDialog("将永久删除该密码备忘记录", mPositiveButtonMsg);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        mEditAView.setPassWordVisible(isChecked);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        isEdit = true;
        mPosition = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
