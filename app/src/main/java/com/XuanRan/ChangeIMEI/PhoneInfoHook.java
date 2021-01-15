package com.XuanRan.ChangeIMEI;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

import java.util.Random;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created By XuanRan on 2021/1/13
 */
public class PhoneInfoHook implements IXposedHookLoadPackage {
    Class clazz;

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        clazz = XposedHelpers.findClass("tb.lnh",lpparam.classLoader);
        XposedBridge.log("************进入PhoneInfoHook****************");
        HookIMSI(lpparam);
        HookAndroidId(lpparam);
        HookMAC(lpparam);
        HookMode(lpparam);
        HookSerial(lpparam);
       // HookLocalUtdid(lpparam);
    }

    private void HookLocalUtdid(XC_LoadPackage.LoadPackageParam lpparam) {
        final Class<?> clazz = XposedHelpers.findClass("mtopsdk.mtop.deviceid.DeviceIDManager", lpparam.classLoader);
        XposedHelpers.findAndHookMethod(clazz, "getLocalUtdid", Context.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                XposedHelpers.setStaticObjectField(clazz,"$ipChange",null);
                param.setResult(getCharAndNumr3(24));

               /* XposedBridge.log("---------结果抓取-------------");
                String s= (String) param.getResult();
                XposedBridge.log("结果："+(String) param.getResult());


                XposedBridge.log("----------------------------");*/
            }
        });
    }

    /**
     * 方法3：生成随机数字和字母组合
     * @param length
     * @return
     */

    public static String getCharAndNumr3(int length) {

        StringBuffer valSb = new StringBuffer();

        for (int i = 0; i < length; i++) {

            String charOrNum = Math.round(Math.random()) % 2 == 0 ? "char" : "num"; // 输出字母还是数字
            if ("char".equalsIgnoreCase(charOrNum)) {

                // 字符串
                int choice = Math.round(Math.random()) % 2 == 0 ? 65 : 97;  // 取得大写字母还是小写字母
                valSb.append((char) (choice + Math.round(Math.random()*25)));

            } else if ("num".equalsIgnoreCase(charOrNum)) {
                // 数字
                valSb.append(String.valueOf(Math.round(Math.random()*9)));
            }
        }
        return valSb.toString();
    }


    private void HookSerial(XC_LoadPackage.LoadPackageParam lpparam) {
        XposedHelpers.findAndHookMethod(clazz, "a", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                param.setResult(getCharAndNumr3(10));
                XposedBridge.log("@@@@@@@a方法返回@@@@@@@@@@@@@@@@@@"+(String)param.getResult() );
            }
        });
    }

    private void HookMode(XC_LoadPackage.LoadPackageParam lpparam) {
        Class<?> clazz =XposedHelpers.findClass("android.os.Build",lpparam.classLoader);
        XposedHelpers.setStaticObjectField(clazz,"MODEL",getRandomMode());
    }

    private String getRandomMode() {
        String[] mode={"XiaoMi 9","OPPO Reno 1","OPPO Reno 5 Pro","Vivo x60","XiaoMi 11","XiaoMi 10","HUAWEI P30","HUAWEI P40 Pro","RedMi K30i","RedMi K30","RedMi K30S","RedMi K30Ultra","RedMi 10X","XiaoMi 6","XiaoMi MIX 2","XiaoMi MIX3"};
        int  temp= (int) (Math.random()*mode.length);
        for (int i = 0; i <= mode.length; i++) {
            if (i==temp){
                return mode[i];
            }
        }
        return null;
    }

    private void HookAndroidId(XC_LoadPackage.LoadPackageParam lpparam) {
        XposedHelpers.findAndHookMethod(clazz, "e", Context.class, new XC_MethodReplacement() {
            @Override
            protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                //XposedBridge.log("############原结构###############"+()String.valueOf(param.getResult()));
                return getCharAndNumr3(12);
            }
        });
    }

    private String getRandomAndroidID() {

        Random random = new Random();
        String[] id = {
      /*          String.format("%02x", random.nextInt(0xff)),
                String.format("%02x", random.nextInt(0xff)),
                String.format("%02x", random.nextInt(0xff)),*/
                String.format("%02x", random.nextInt(0xff)),
                String.format("%02x", random.nextInt(0xff)),
                String.format("%02x", random.nextInt(0xff)),
                String.format("%02x", random.nextInt(0xff)),
                String.format("%02x", random.nextInt(0xff))
        };
        String result = null;
        for (int i = 0; i < id.length; i++) {
            result+=id[i];
        }
        return result;
    }

    private void HookIMSI(XC_LoadPackage.LoadPackageParam loadPackageParam) {

        XposedHelpers.findAndHookMethod(TelephonyManager.class,"getSubscriberId", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                param.setResult((long) (Math.random() * 100000000) + "" + (long) (Math.random() * 10000000));
            }
        });



    }

    private void HookMAC(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        final Class<?> clazz = XposedHelpers.findClass("tb.lnh", loadPackageParam.classLoader);

        XposedHelpers.findAndHookMethod(clazz, "f", Context.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                XposedHelpers.setStaticObjectField(clazz,"$ipChange",null);
                XposedBridge.log("++++++++++++++GetMacAddress方法被调用-++++++++++++++++++++++++-");
                param.setResult(randomMac4Qemu());
            }
        });
    }

    /**
     * @return 随机生成的MAC
     */
    public static String randomMac4Qemu() {
        Random random = new Random();
        String[] mac = {
                String.format("%02x", random.nextInt(0xff)),
                String.format("%02x", random.nextInt(0xff)),
                String.format("%02x", random.nextInt(0xff)),
                String.format("%02x", random.nextInt(0xff)),
                String.format("%02x", random.nextInt(0xff)),
                String.format("%02x", random.nextInt(0xff))
        };
        String result="";
        for(int i=0;i<mac.length;i++){
            result+=mac[i];
            if(i!=mac.length-1){
                result+=":";
            }
        }
        return result;
    }

}
