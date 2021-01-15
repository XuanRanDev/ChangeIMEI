package com.XuanRan.ChangeIMEI;

import android.os.Bundle;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.HashMap;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created By XuanRan on 2021/1/14
 */
public class DuiZhanPrint implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {




        //PrintActivityDuiZhan(lpparam);

        PrintSignParam(lpparam);

    }

    private void PrintSignParam(final XC_LoadPackage.LoadPackageParam lpparam) {

        Class<?> clazz =XposedHelpers.findClass("tb.lnf",lpparam.classLoader);

        XposedHelpers.findAndHookMethod(clazz, "a", HashMap.class, HashMap.class, String.class, String.class, Boolean.TYPE,new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
               // XposedBridge.log("--------Sign参数打印-----------");


                XposedBridge.log("\n--------------Sign堆栈打印-------------------\n");
                Exception e = new Exception(lpparam.packageName);
                StackTraceElement[] stackElements = e.getStackTrace();
                StringBuilder sb = new StringBuilder();

                for (int i = 0; i < stackElements.length; i++) {
                    sb.append("类：").append(stackElements[i].getClassName()).append("\n");
                    sb.append("方法：").append(stackElements[i].getMethodName());
                    sb.append("行号：").append(stackElements[i].getLineNumber()).append("\n");
                }
                XposedBridge.log(sb.toString());
                XposedBridge.log("-------------------------------------");


                HashMap hashMap = (HashMap) param.args[0];
                HashMap hashMap2 = (HashMap) param.args[1];
                String str = (String) param.args[2];
                String str2 = (String) param.args[3];
                boolean booleanValue = ((Boolean) param.args[4]).booleanValue();
                for (Object str3 : hashMap.keySet()) {
                    Log.i("HashMap1:", str3 + " param1: " + hashMap.get(str3));
                }
                for (Object str4 : hashMap2.keySet()) {
                    Log.i("HashMap2:", str4 + " param2: " + hashMap2.get(str4));
                }
                Log.i("SignParam3", "param3 beforeHookedMethod: " + str);
                Log.i("SignParam4", "param4 beforeHookedMethod: " + str2);
                Log.i("SignParam5", "param5 beforeHookedMethod: " + booleanValue);

            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                XposedBridge.log("----------SignParam结果打印------------------");
                HashMap hashMap = (HashMap) param.getResult();
                for (Object str : hashMap.keySet()) {
                    XposedBridge.log("SignParam_Result:"+hashMap.get(str));
                }

            }
        });


    }





    private void PrintActivityDuiZhan(final XC_LoadPackage.LoadPackageParam lpparam) {

        Class<?> clazz =  XposedHelpers.findClass("com.taobao.android.detail.wrapper.activity.DetailActivity",lpparam.classLoader);

        XposedHelpers.findAndHookMethod(clazz, "onCreate", Bundle.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);

                XposedBridge.log("\n--------------Activity堆栈打印-------------------\n");
                Exception e = new Exception(lpparam.packageName);
                StackTraceElement[] stackElements = e.getStackTrace();
                StringBuilder sb = new StringBuilder();

                for (int i = 0; i < stackElements.length; i++) {
                    sb.append("类：").append(stackElements[i].getClassName()).append("\n");
                    sb.append("方法：").append(stackElements[i].getMethodName());
                    sb.append("行号：").append(stackElements[i].getLineNumber()).append("\n");
                }
                XposedBridge.log(sb.toString());
                XposedBridge.log("-------------------------------------");
            }
        });

    }
}
