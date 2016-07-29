package com.example.ryanzicky.phone360save.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ryanzicky.phone360save.R;
import com.example.ryanzicky.phone360save.utils.ConstantValue;
import com.example.ryanzicky.phone360save.utils.SpUtil;
import com.example.ryanzicky.phone360save.utils.ToastUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class SplashActivity extends Activity {

    protected static final String tag = "SplashActivity";

    /**
     * 更新状态码
     */
    protected static final int UPDATE_VERSION = 100;

    /**
     * 进入应用程序主界面的状态码
     */
    private static final int ENTER_HOME = 101;

    /**
     * url地址出错的状态码
     */
    private static final int URL_ERROR = 102;
    private static final int IO_ERROR = 103;
    private static final int JSON_ERROR = 104;

    private TextView tv_version_name;
    private int mLocalVersionCode;
    private String mVersionDes;
    private String mDownloadUrl;

    private RelativeLayout rl_root;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case UPDATE_VERSION:
                    //弹出对话框，提示用户更新
                    showUpdateDialog();
                    break;
                case ENTER_HOME:
                    //进入程序主界面,activity跳转过程
                    enterHome();
                    break;
                case URL_ERROR:
                    ToastUtil.show(getApplicationContext(),"url异常");
                    enterHome();
                    break;
                case IO_ERROR:
                    ToastUtil.show(getApplicationContext(),"io异常");
                    enterHome();
                    break;
                case JSON_ERROR:
                    ToastUtil.show(getApplicationContext(),"json异常");
                    enterHome();
                    break;
            }
        }
    };

    /**
     * 弹出对话框，提示用户更新
     */
    private void showUpdateDialog() {
        //对话框,是依赖activity存在的
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //设置左上角图标
        builder.setIcon(R.drawable.ic_launcher);
        //设置标题
        builder.setTitle("版本更新");
        //设置描述内容
        builder.setMessage(mVersionDes);

        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //下载apk,apk链接地址
                downloadApk();
            }
        });
        builder.setNegativeButton("稍后再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //取消对话框,进入主界面
                enterHome();
            }
        });

        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                //即使用户点击取消，也需要让其竟如主界面
                enterHome();
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    private void downloadApk() {
        //apk下载链接地址,放置apk所在的路径

        //1.判断sd卡是否可用，是否挂在上
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            //2.获取sd卡路径
            String path = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"360MobialSave.apk";
            //3.发送请求，获取apk，并且放置到指定路径
            HttpUtils httpUtils = new HttpUtils();
            //4.发送请求，传递参数(下载地址，下载应用放置位置)
            httpUtils.download(mDownloadUrl, path, new RequestCallBack<File>() {
                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {
                    //下载成功
                    Log.i(tag,"下载成功");
                    File file = responseInfo.result;
                    //提示用户安装
                    installApk(file);
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    //下载失败
                    Log.i(tag,"下载失败");
                }
                //刚刚开始下载方法

                @Override
                public void onStart() {
                    Log.i(tag,"开始下载");
                    super.onStart();
                }

                //下载过程中的方法(下载apk大小，当前的下载位置，是否正在下载)
                @Override
                public void onLoading(long total, long current, boolean isUploading) {
                    Log.i(tag,"下载......");
                    super.onLoading(total, current, isUploading);
                }
            });
        }
    }

    /**
     * 安装对应apk
     * @param file 安装文件
     */
    private void installApk(File file) {
        //系统应用界面，源码，安装apk入口
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.catagory.DEFAULT");
        /*//文件作为数据源
        intent.setData(Uri.fromFile(file));
        //设置安装的类型
        intent.setType("application/vnd.android.package-archive");*/
        intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
//        startActivity(intent);
        startActivityForResult(intent,0);
    }

    /**
     * 开启一个activity后，返回结果调用的方法
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        enterHome();
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 进入应用程序主界面方法
     */
    private void enterHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);

        //在开启一个新的界面后将导航界面关闭（导航界面只可见一次）
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

        //初始化UI
        initUI();
        //初始化数据
        initData();
        //初始化动画
        initAnimation();
    }

    private void initAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(3000);
        rl_root.startAnimation(alphaAnimation);
    }

    /**
     * 获取数据方法
     */
    private void initData() {
        //1.应用版本名称
        tv_version_name.setText("版本名称："+ getVersionName());
        //2.获取本地版本号
        mLocalVersionCode = getVersionCode();
        //获取服务器版本号
        if(SpUtil.getBoolean(this, ConstantValue.OPEN_UPDATE,false)){
            checkVersion();
        }else {
            //直接进入应用程序主界面
//            enterHome();
            //消息机制
            //在发送消息4秒后去处理ENTER_HOME状态吗指向的消息
            mHandler.sendEmptyMessageDelayed(ENTER_HOME,4000);
        }
    }

    /**
     * 检测版本号
     */
    private void checkVersion() {
        /*new Thread(){
            @Override
            public void run() {
                Message msg = Message.obtain();
                long startTime = System.currentTimeMillis();
                try{
                    //1.封装url地址
                    URL url = new URL("http://192.168.11.104:8080/update.json");
                    //2.开启一个链接
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    //3.设置常见请求参数（请求头）

                    //请求超时
                    connection.setConnectTimeout(2000);
                    //读取超时
                    connection.setReadTimeout(2000);
                    //默认就是get请求方式
                    connection.setRequestMethod("POST");
                    //4.获取响应码
                    if(connection.getResponseCode() == 200){
                        System.out.println("请求成功");
                        //5.以流的形式，将数据获取下来
                        InputStream is = connection.getInputStream();
                        //6.将流转换成字符串（工具类封装）
                        String json = StreamUtit.streamToString(is);
                        Log.i(tag,json);
                        //7.json解析
                        JSONObject jsonObject = new JSONObject(json);

                        String versionName = jsonObject.getString("versionName");
                        String versionCode = jsonObject.getString("versionCode");
                        String versionDes = jsonObject.getString("versionDes");
                        String downloadUrl = jsonObject.getString("downloadUrl");

                        Log.i(tag,versionName);
                        Log.i(tag,versionCode);
                        Log.i(tag,versionDes);
                        Log.i(tag,downloadUrl);

                        //8.对比版本号（服务器版本号大于本地版本号，提示用户更新）
                        if(mLocalVersionCode<Integer.parseInt(versionCode)){
                            //提示用户更新，弹出对话框(UI),消息机制
                            msg.what = UPDATE_VERSION;
                        }else{
                            //进入程序主界面
                            msg.what = ENTER_HOME;
                        }
                    }
                }catch (MalformedURLException e){
                    e.printStackTrace();
                    msg.what = URL_ERROR;
                }catch (IOException e){
                    e.printStackTrace();
                    msg.what = IO_ERROR;
                } catch (JSONException e) {
                    e.printStackTrace();
                    msg.what = JSON_ERROR;
                }finally{
                    //指定睡眠时间,请求网络时长超过4秒不做处理
                    //请求网络时长小于4秒，强制让其睡眠满4秒
                    long endTime = System.currentTimeMillis();
                    if(endTime - startTime < 4000){
                        try{
                            Thread.sleep(4000 - (endTime - startTime));
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                    mHandler.sendMessage(msg);
                }
            }
        }.start();*/

        /*new Thread(new Runnable() {
            @Override
            public void run() {

            }
        });*/

        new Thread(){
            @Override
            public void run() {
                Message msg = Message.obtain();
                long startTime = System.currentTimeMillis();
                String json = "{'versionName':'2.0','versionCode':'2','versionDes':'2.0发布了','downloadUrl':'http://www.baidu.com'}";
                Log.i(tag,json);

                //7.json解析
                try{
                    JSONObject jsonObject = new JSONObject(json);

                    String versionName = jsonObject.getString("versionName");
                    String versionCode= jsonObject.getString("versionCode");
                    mVersionDes = jsonObject.getString("versionDes");
                    mDownloadUrl = jsonObject.getString("downloadUrl");

                    Log.i(tag,versionName);
                    Log.i(tag,versionCode);
                    Log.i(tag,mVersionDes);
                    Log.i(tag,mDownloadUrl);

                    //8.对比版本号（服务器版本号大于本地版本号，提示用户更新）
                    if(mLocalVersionCode<Integer.parseInt(versionCode)){
                        //提示用户更新，弹出对话框(UI),消息机制
                        msg.what = UPDATE_VERSION;
                    }else{
                        //进入程序主界面
                        msg.what = ENTER_HOME;
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }finally{
                    //指定睡眠时间,请求网络时长超过4秒不做处理
                    //请求网络时长小于4秒，强制让其睡眠满4秒
                    long endTime = System.currentTimeMillis();
                    if(endTime - startTime < 4000){
                        try{
                            Thread.sleep(4000 - (endTime - startTime));
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                    mHandler.sendMessage(msg);
                }
            }
        }.start();
    }

    /**
     * 返回版本号 非0则获取成功
     * @return
     */
    private int getVersionCode() {
        //1.包管理者对象PackageManager
        PackageManager pm = getPackageManager();
        //2.从包管理者对象中，获取指定包名的基本信息(版本名称,版本号),传0代表获取基本信息
        try{
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(),0);
            //3.获取版本号
            return packageInfo.versionCode;
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取版本名称：清单文件中
     * @return 应用版本名称 返回null代表异常
     */
    private String getVersionName() {
        //1.包管理者对象PackageManager
        PackageManager pm = getPackageManager();
        //2.从包管理者对象中，获取指定包名的基本信息(版本名称,版本号),传0代表获取基本信息
        try{
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(),0);
            return packageInfo.versionName;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 初始化UI
     */
    private void initUI() {
        tv_version_name = (TextView) findViewById(R.id.tv_version_name);
        rl_root = (RelativeLayout) findViewById(R.id.rl_root);
    }
}
