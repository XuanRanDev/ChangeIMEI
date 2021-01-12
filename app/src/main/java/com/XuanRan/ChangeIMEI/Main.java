package com.XuanRan.ChangeIMEI;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;


/*
 *   Created By XuanRan on 2021/01/09
 *
 */
public class Main implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        String packagename = loadPackageParam.packageName;
        //如果需要对全局生效，请注释下面if条件块
        if (packagename.equals("com.XuanRan.ChangeIMEI") || packagename.equals("com.taobao.taobao")|| packagename.equals("com.example.xiejun.xposedstudy.app")) {


            new HookXp().handleLoadPackage(loadPackageParam);



            XposedBridge.log("start hook app is " + loadPackageParam.packageName);



            XposedHelpers.findAndHookMethod(TelephonyManager.class, "getDeviceId", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    //HOOK getIMEI
                    XposedBridge.log("\n--------------堆栈打印-------------------\n");
                    Exception e = new Exception(loadPackageParam.packageName);
                    StackTraceElement[] stackElements = e.getStackTrace();
                    StringBuilder sb=new StringBuilder();

                    for (int i = 0; i < stackElements.length; i++) {
                        sb.append("类：").append(stackElements[i].getClassName()).append("\n");
                        sb.append("方法：").append(stackElements[i].getMethodName());
                        sb.append("行号：").append(stackElements[i].getLineNumber()).append("\n");
                    }
                    XposedBridge.log(sb.toString());
                    XposedBridge.log("-------------------------------------");

                    param.setResult((long) (Math.random() * 100000000) + "" + (long) (Math.random() * 10000000));
                }
            });

            Hooklnh(loadPackageParam);//主要信息获取类

            HookDeviceInfo1(loadPackageParam);
            HookDeviceInfo2(loadPackageParam);
            HookDeviceInfo3(loadPackageParam);



            try{
                Class<?> object=XposedHelpers.findClass("mtopsdk.mtop.util.MtopSDKThreadPoolExecutorFactory$MtopSDKThreadFactory$1",loadPackageParam.classLoader);
                Class ipchange= (Class) XposedHelpers.getStaticObjectField(object,"$ipChange");
                XposedHelpers.setStaticObjectField(object,"$ipChange",null);
                if (object==null||ipchange==null){
                    XposedBridge.log("?????????——————————————空对象————————————?????????");
                }

            }catch(Exception e){
                XposedBridge.log("________捕获异常_________");
                XposedBridge.log(e.getMessage()+"\n");
            }



            CheckThredCall(loadPackageParam);

            
            

        }


    }


    private void HookDeviceInfo3(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        Class<?> clazz =XposedHelpers.findClass("mtopsdk.mtop.deviceid.DeviceIDManager$2",loadPackageParam.classLoader);

     //   XposedHelpers.setStaticObjectField(clazz,"$ipChange",null);
        XposedHelpers.findAndHookMethod(clazz, "run", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                XposedBridge.log("++++++++++++++run方法被调用-++++++++++++++++++++++++-");
            }
        });
    }





    private void HookDeviceInfo2(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        final Class<?> clazz =XposedHelpers.findClass("mtopsdk.mtop.deviceid.DeviceIDManager$1",loadPackageParam.classLoader);
        final Class<?> clazz2 =XposedHelpers.findClass("mtopsdk.mtop.deviceid.DeviceIDManager",loadPackageParam.classLoader);
        /*final Context context = (Context) XposedHelpers.getObjectField(param.thisObject,"val$context");
        //  final Class objectField = (Class) XposedHelpers.getObjectField(param.thisObject,"this$0");
        final String appkey = (String) XposedHelpers.getObjectField(param.thisObject,"val$appKey");*/



        final Context[] context = new Context[1];
        final String[] string = new String[1];
        final Object[] object = new Object[1];

        XposedHelpers.findAndHookConstructor(clazz, clazz2, Context.class, String.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                object[0] =param.args[0];
                context[0] = (Context) param.args[1];
                string[0] = (String) param.args[2];
            }
        });
        XposedHelpers.findAndHookMethod(clazz, "call", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);

                XposedBridge.log("+++++++++++++++call方法被调用-++++++++++++++++++++++++-");

                XposedHelpers.setStaticObjectField(clazz,"$ipChange",null);

                String data= (String) XposedHelpers.callMethod(object[0],"getRemoteDeviceID", context[0], string[0]);
                XposedBridge.log("++++++++++++++call方法调用返回-++++++++++++++++++++++++-"+data);

            }
        });
    }







    private void HookDeviceInfo1(final XC_LoadPackage.LoadPackageParam loadPackageParam) {
        final boolean[] ReplyCall={true};

        final Class<?> clazz =XposedHelpers.findClass("mtopsdk.mtop.deviceid.DeviceIDManager",loadPackageParam.classLoader);
        XposedHelpers.setStaticObjectField(clazz,"$ipChange",null);
        XposedHelpers.findAndHookMethod(clazz, "getRemoteDeviceID", Context.class, String.class,new XC_MethodHook() {

            @Override
            protected void beforeHookedMethod(final MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                XposedHelpers.setStaticObjectField(clazz,"$ipChange",null);
                //AutoCall(loadPackageParam);
                //XposedHelpers.callMethod()
                XposedBridge.log("++++++++++++++getRemoteDeviceID方法被调用-++++++++++++++++++++++++-");
                XposedBridge.log("++++++++++++++getRemoteDeviceID方法调用返回-++++++++++++++++++++++++-"+param.getResult());
                if (ReplyCall[0]){
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        public void run()
                        {
                            XposedHelpers.callMethod(param.thisObject,"getRemoteDeviceID", param.args[0], param.args[1]);
                            XposedBridge.log("-------------主动调用----------------");
                        }
                    }, 0, 10000);
                    ReplyCall[0]=false;
                }
            }
        });
    }


    private void Hooklnh(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        Class<?> clazz =XposedHelpers.findClass("tb.lnh",loadPackageParam.classLoader);
    //    XposedHelpers.setStaticObjectField(clazz,"$ipChange",null);
        XposedHelpers.findAndHookMethod(clazz, "c", Context.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                XposedBridge.log("++++++++++++++GetIMEI方法被调用-++++++++++++++++++++++++-");
            }
        });
    }

    private void CheckMainActivityCall(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        Class<?> clazz = XposedHelpers.findClass("com.XuanRan.ChangeIMEI.MainActivity",loadPackageParam.classLoader);
//        final Field field = (Field) XposedHelpers.getStaticObjectField(clazz,"$test");
        XposedHelpers.setStaticObjectField(clazz,"$test",null);
        XposedHelpers.findAndHookMethod("com.XuanRan.ChangeIMEI.MainActivity", loadPackageParam.classLoader, "onCreate", Bundle.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                Activity activity = (Activity) param.thisObject;

            }
        });

    }

    private void CheckThredCall(final XC_LoadPackage.LoadPackageParam loadPackageParam) {

        final Class<?> clazz =XposedHelpers.findClass("mtopsdk.mtop.util.MtopSDKThreadPoolExecutorFactory$MtopSDKThreadFactory$1",loadPackageParam.classLoader);

        final Object[] object = {null};

        final int[] i = {0};

        XposedHelpers.findAndHookMethod("mtopsdk.mtop.util.MtopSDKThreadPoolExecutorFactory$MtopSDKThreadFactory$1", loadPackageParam.classLoader, "run", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(final MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                object[0] =param.thisObject;
                XposedHelpers.setStaticObjectField( clazz,"$ipChange",null);
                XposedBridge.log("________线程方法调用____________"+ i[0]);

            }
        });
    }

    public void AutoCall(XC_LoadPackage.LoadPackageParam loadPackageParam){
        XposedBridge.log("111111111____AutoCall_____111111111111111");
        final boolean[] ReplyCall = {true};
        final Context[] context = new Context[1];
        final String[] appKey = new String[1];

        final Class<?> clazz =XposedHelpers.findClass("mtopsdk.mtop.deviceid.DeviceIDManager",loadPackageParam.classLoader);
        XposedHelpers.findAndHookMethod(clazz, "getRemoteDeviceID", Context.class, String.class,new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                XposedHelpers.setStaticObjectField((Class<?>) param.thisObject,"$ipChange",null);
                context[0] = (Context) param.args[0];
                appKey[0] = (String) param.args[1];

            }
        });

        if (ReplyCall[0]){
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                public void run()
                {
                    XposedHelpers.callMethod(clazz,"getRemoteDeviceID", context[0], appKey[0]);
                    XposedBridge.log("-------------主动调用----------------");
                }
            }, 0, 10000);
            ReplyCall[0]=false;
        }
    }


    public void printCallStatck() {

        Throwable ex = new Throwable();
        StackTraceElement[] stackElements = ex.getStackTrace();
        if (stackElements != null) {
            for (int i = 0; i < stackElements.length; i++) {
                System.out.print(stackElements[i].getClassName()+"/t");
                System.out.print(stackElements[i].getFileName()+"/t");
                System.out.print(stackElements[i].getLineNumber()+"/t");
                System.out.println(stackElements[i].getMethodName());
                System.out.println("-----------------------------------");
            }
        }
    }

}
