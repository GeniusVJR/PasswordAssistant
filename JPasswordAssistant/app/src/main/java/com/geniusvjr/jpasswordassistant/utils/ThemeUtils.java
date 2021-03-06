package com.geniusvjr.jpasswordassistant.utils;

import android.app.Activity;
import android.content.Context;

import com.geniusvjr.jpasswordassistant.R;

/**
 * 主题工具类
 * Created by dream on 16/5/28.
 */
public class ThemeUtils {

    /**
     * 改变主题
     * @param activity
     * @param theme
     */
    public static void changeTheme(Activity activity, Theme theme){
        if(activity == null){
            return;
        }
        //默认为红色主题
        int style = R.style.RedTheme;
        switch (theme){
            case BROWN:
                style = R.style.BrownTheme;
                break;
            case BLUE:
                style = R.style.BlueTheme;
                break;
            case BLUE_GREY:
                style = R.style.BlueGreyTheme;
                break;
            case YELLOW:
                style = R.style.YellowTheme;
                break;
            case DEEP_PURPLE:
                style = R.style.DeepPurpleTheme;
                break;
            case PINK:
                style = R.style.PinkTheme;
                break;
            case GREEN:
                style = R.style.GreenTheme;
                break;
            default:
                break;
        }
        //给activity设置主题
        activity.setTheme(style);
    }

    //得到当前主题
    public static Theme getCurrentTheme(Context context){
        //可通过设置默认颜色
        int value = (int) SPUtils.get(context,context.getResources().getString(R.string.change_theme_key), 7);
        return ThemeUtils.Theme.mapValueToTheme(value);
    }

    //主题枚举
    public enum Theme{

        RED(0x00),
        BROWN(0x01),
        BLUE(0x02),
        BLUE_GREY(0x03),
        YELLOW(0x04),
        DEEP_PURPLE(0x05),
        PINK(0x06),
        GREEN(0x07);

        private int mValue;

        Theme(int value){
            this.mValue = value;
        }
        //值转换为theme
        public static Theme mapValueToTheme(final int value){
            for(Theme theme : Theme.values()){
                if(value == theme.getIntValue()){
                    return theme;
                }
            }
            //默认为红色
            return RED;
        }

        static Theme getDefault(){
            return RED;
        }

        public int getIntValue(){
            return mValue;
        }

    }
}
