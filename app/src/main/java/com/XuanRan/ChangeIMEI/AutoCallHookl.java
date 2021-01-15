package com.XuanRan.ChangeIMEI;

import android.content.Context;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created By XuanRan on 2021/1/13
 */
public class AutoCallHookl implements IXposedHookLoadPackage {
    private static final String TAG ="AutoCallHookl" ;

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {

        if (!lpparam.packageName.equals("com.taobao.taobao")){
            return;
        }




    }


}
