package com.mogu.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.google.gson.Gson;
import com.mogu.activity.CaptureActivity;
import com.mogu.activity.NearbyWiFiActivity;
import com.mogu.activity.R;
import com.mogu.activity.WifiUserListActivity;
import com.mogu.activity.wxapi.WXEntryActivity;
import com.mogu.app.Constant;
import com.mogu.app.MrLiUrl;
import com.mogu.app.MyCookieStore;
import com.mogu.app.Url;
import com.mogu.entity.login.Login;
import com.mogu.entity.wfuser.FriendsList;
import com.mogu.entity.wfuser.WfUser;
import com.mogu.entity.wifi.FreeWifiList;
import com.mogu.entity.wifi.WiFiJson;
import com.mogu.utils.GsonUtils;
import com.mogu.utils.HttpCallBack;
import com.mogu.utils.ImageUtils;
import com.mogu.utils.IsMobile;
import com.mogu.utils.SessionRequestParams;
import com.mogu.utils.ToastShow;
import com.mogu.utils.WifiUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass. 首页
 */
public class IndexFragment extends Fragment implements AMapLocationListener,
        OnClickListener {
    private View mLayout;
    private TextView ssd;
    private TextView tv1;

    private TextView nearbyWiFiTextView;

    private FragmentTabHost mTabHost;
    private View tabLayout;
    private TextView apolloText;

    public WifiUtils localWifiUtils;
    public List<ScanResult> wifiResultList;
    public List<FreeWifiList> freeWifiList = new ArrayList<FreeWifiList>();
    public List<ScanResult> needCrackWifiList = new ArrayList<ScanResult>();
    public boolean isOpen = false;
    private FragmentManager childFragmentManager;

    // 定位相关
    private Double lat;
    private Double lgt;

    private TextView Tshare;
    private TextView Tnear;

    private WifiUtils wifiUtils;
    private boolean islogin;

    public static final int UPDATE_WIFI_INFO = 0;
    public static final int WIFI_CONNECTED = 1;
    public static final int WIFI_DISCONNECT = 2;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATE_WIFI_INFO:

                    break;
                case WIFI_CONNECTED:
                    linkWiFiSuccess();
                    break;
                case WIFI_DISCONNECT:
                    linkWiFiFail();
                    break;
                default:
                    break;
            }
        }
    };

    private NetworkConnectChangedReceiver receiver;
    private ImageView scanImageView;
    private LinearLayout heads;

    @ViewInject(R.id.iv_head1)
    private ImageView headImageView1;

    @ViewInject(R.id.iv_head2)
    private ImageView headImageView2;

    @ViewInject(R.id.iv_head3)
    private ImageView headImageView3;

    private ImageView[] headImageViews;
    private ImageOptions options;

    @ViewInject(R.id.iv_wifi_state)
    private ImageView ivWifiState;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mLayout == null) {
            options = new ImageOptions.Builder().setCircular(true)
                    // 是否忽略GIF格式的图片
                    .setIgnoreGif(false)
                    // 图片缩放模式
                    .setImageScaleType(ScaleType.CENTER_CROP)
                    // 下载中显示的图片
                    // .setLoadingDrawableId(R.drawable.ic_launcher)
                    // 下载失败显示的图片
                    .setFailureDrawableId(R.drawable.ic_head)
                    // 得到ImageOptions对象
                    .build();
            wifiUtils = new WifiUtils(getActivity());
            mLayout = inflater
                    .inflate(R.layout.fragment_home, container, false);
            x.view().inject(this, mLayout);
            headImageViews = new ImageView[3];
            headImageViews[0] = headImageView1;
            headImageViews[1] = headImageView2;
            headImageViews[2] = headImageView3;
            localWifiUtils = new WifiUtils(getActivity());
            childFragmentManager = getChildFragmentManager();
            initView(mLayout);
            IntentFilter filter = new IntentFilter();
            filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
            filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            receiver = new NetworkConnectChangedReceiver();
            getActivity().registerReceiver(receiver, filter);
        }
        return mLayout;
    }

    private void initView(View view) {

        mTabHost = (FragmentTabHost) mLayout.findViewById(android.R.id.tabhost);
        // 初始化TabHost
        mTabHost.setup(getActivity(), childFragmentManager, R.id.realtabcontent);
        addTab(R.string.wifi_text1, FreeWiFiFragment.class);
        addTab(R.string.wifi_text2, NeedCrackWiFiFragment.class);
        heads = (LinearLayout) view.findViewById(R.id.ll_heads);
        Tnear = (TextView) view.findViewById(R.id.t_near);
        Tshare = (TextView) view.findViewById(R.id.t_share);
        Tnear.setOnClickListener(this);
        Tshare.setOnClickListener(this);

        ssd = (TextView) view.findViewById(R.id.ssd);
        tv1 = (TextView) view.findViewById(R.id.tv1);
        nearbyWiFiTextView = (TextView) view.findViewById(R.id.tv_nearby_wifi);
        nearbyWiFiTextView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),
                        NearbyWiFiActivity.class));
            }
        });

        scanImageView = (ImageView) view.findViewById(R.id.iv_scan_qr);
        scanImageView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent().setClass(getActivity(),
                        CaptureActivity.class));
            }
        });
    }

    private void addTab(int strRes, Class class1) {
        tabLayout = getActivity().getLayoutInflater().inflate(
                R.layout.tab_bus_layout, null);
        apolloText = (TextView) tabLayout.findViewById(R.id.apollo_text);
        apolloText.setText(strRes);
        mTabHost.addTab(mTabHost.newTabSpec("" + strRes)
                .setIndicator(tabLayout), class1, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtil.i("onActivityCreated");

    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sp1 = getActivity().getSharedPreferences(
                Constant.MAIN_SHARE_FILE_NAME, Context.MODE_PRIVATE);
        islogin = sp1.getBoolean("islogin", false);
        Log.e("爸爸", islogin + "");
        if (islogin) {
            heads.setVisibility(View.VISIBLE);
            scanImageView.setVisibility(View.VISIBLE);
        } else {
            heads.setVisibility(View.GONE);
            scanImageView.setVisibility(View.INVISIBLE);
        }
        LogUtil.i("onResume");
        scanWiFi();
        // updateCurrentWiFiInfo();
        if (mlocationClient == null) {
            intiLoca();
        } else {
            mlocationClient.startLocation();
        }
    }

    public void scanWiFi() {

        localWifiUtils.WifiOpen();
        localWifiUtils.WifiStartScan();
        // 0正在关闭,1WIFi不可用,2正在打开,3可用,4状态不可zhi
        while (localWifiUtils.WifiCheckState() != WifiManager.WIFI_STATE_ENABLED) {// 等待Wifi开启
            Log.i("WifiState", String.valueOf(localWifiUtils.WifiCheckState()));
        }
        if (!isOpen) {
            isOpen = true;
            try {
                Thread.sleep(5000);// 休眠3s，不休眠则会在程序首次开启WIFI时候，处理getScanResults结果，wifiResultList.size()发生异常
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        wifiResultList = localWifiUtils.getScanResults();
        Log.e("WIFIButtonListener", "dataChange"+wifiResultList);
        localWifiUtils.getConfiguration();
        if (freeWifiList != null) {
            Log.i("WIFIButtonListener", "dataChange"+wifiResultList);
            // scanResultToString(wifiResultList, freeWifiList);

            // 得到WiFi列表
            getWiFiList();
        }
    }

    private void getWiFiList() {
        // for (int i = wifiResultList.size() - 1; i >= 0; i--) {
        // int level =
        // WifiManager.calculateSignalLevel(wifiResultList.get(i).level, 5);
        // if (level<2) {
        // wifiResultList.remove(i);
        // }
        //
        // }

        RequestParams params = new RequestParams(Url.WIFI_LIST);
        // params.addBodyParameter("ssid00", value);
        Gson gson = new Gson();
        String jsonString = gson.toJson(wifiResultList);
        // params.setAsJsonContent(true);
        // params.setBodyContent(jsonString);
        // params.setCharset("utf-8");
        LogUtil.e(jsonString);
        params.addBodyParameter("yyzedit_wifi00", jsonString);
        x.http().post(params, new CommonCallback<String>() {

            @Override
            public void onCancelled(CancelledException arg0) {

            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                needCrackWifiList.clear();
                needCrackWifiList.addAll(wifiResultList);

                refresh();
            }

            @Override
            public void onFinished() {

            }

            @Override
            public void onSuccess(String arg0) {

                // ToastShow.testShortShowToast("success");
                WiFiJson json = GsonUtils.parseJSON(arg0, WiFiJson.class);
                String code = json.getCode();
                String msg = json.getMsg();
                if ("1".equals(code)) {

                    freeWifiList.clear();
                    needCrackWifiList.clear();
                    List<FreeWifiList> mFreeWifiLists = json.getWifiList1();
                    List<ScanResult> scanResults = new ArrayList<ScanResult>();
                    for (int i = 0; i < mFreeWifiLists.size(); i++) {
                        for (ScanResult scanResult : wifiResultList) {
                            if (scanResult.BSSID.equals(mFreeWifiLists.get(i)
                                    .getMac000())) {
                                mFreeWifiLists.get(i).scanResult = scanResult;
                                break;
                            }
                        }
                    }
                    for (int i = 0; i < json.getWifiList2().size(); i++) {
                        for (ScanResult scanResult : wifiResultList) {
                            if (scanResult.SSID.equals(json.getWifiList2()
                                    .get(i).getSsid00())) {
                                scanResults.add(scanResult);
                                break;
                            }
                        }
                    }

                    for (int i = mFreeWifiLists.size() - 1; i >= 0; i--) {
                        int level = WifiManager.calculateSignalLevel(
                                mFreeWifiLists.get(i).scanResult.level, 5);
                        if (level < 2) {
                            mFreeWifiLists.remove(i);
                        }

                    }

                    freeWifiList.addAll(mFreeWifiLists);
                    needCrackWifiList.addAll(scanResults);

                    refresh();
                } else {
                    ToastShow.shortShowToast(msg);
                }

            }

        });
    }

    public void refresh() {
        int currentTab = mTabHost.getCurrentTab();
        Fragment currentFragment = childFragmentManager
                .findFragmentByTag(mTabHost.getCurrentTabTag());
        if (currentFragment == null) {
            return;
        }
        if (currentTab == 0) {
            ((FreeWiFiFragment) currentFragment).refresh();
        } else if (currentTab == 1) {
            ((NeedCrackWiFiFragment) currentFragment).refresh();
        }
    }

    // ScanResult类型转为String
    public void scanResultToString(List<ScanResult> listScan,
                                   List<String> listStr) {
        for (int i = 0; i < listScan.size(); i++) {
            ScanResult strScan = listScan.get(i);
            // String str = strScan.SSID + "--" + strScan.BSSID;
            String str = strScan.SSID;
            boolean bool = listStr.add(str);
            // if (bool) {
            // arrayWifiAdapter.notifyDataSetChanged();//
            // 数据更新,只能单个Item更新，不能够整体List更新
            // } else {
            // Log.i("scanResultToSting", "fail");
            // }
            Log.i("scanResultToString", listStr.get(i));
        }
        // arrayWifiAdapter.notifyDataSetChanged();//
        // 数据更新,只能单个Item更新，不能够整体List更新
        // 执行更新
        int currentTab = mTabHost.getCurrentTab();
        Fragment currentFragment = childFragmentManager
                .findFragmentByTag(mTabHost.getCurrentTabTag());
        if (currentFragment == null) {
            return;
        }
        if (currentTab == 0) {
            ((FreeWiFiFragment) currentFragment).refresh();
        } else if (currentTab == 1) {
            ((NeedCrackWiFiFragment) currentFragment).refresh();
        }
    }

    // 定位
    public AMapLocationClientOption mLocationOption = null;
    public AMapLocationClient mlocationClient = null;
    private AlertDialog dialog;
    private List<FriendsList> friendList = new ArrayList<FriendsList>();

    private void intiLoca() {
        mlocationClient = new AMapLocationClient(getActivity());
        // 初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        // 设置定位监听
        mlocationClient.setLocationListener(this);
        // 设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
        // 设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        // 设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        // 启动定位
        mlocationClient.startLocation();
    }

    // @Override
    // public void onLocationChanged(AMapLocation amapLocation) {
    // if (amapLocation != null) {
    // if (amapLocation.getErrorCode() == 0) {
    // //定位成功回调信息，设置相关消息
    // amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
    // amapLocation.getLatitude();//获取纬度
    // amapLocation.getLongitude();//获取经度
    // amapLocation.getAccuracy();//获取精度信息
    // SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    // Date date = new Date(amapLocation.getTime());
    // df.format(date);//定位时间
    // } else {
    // //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
    // Log.e("AmapError","location Error, ErrCode:"
    // + amapLocation.getErrorCode() + ", errInfo:"
    // + amapLocation.getErrorInfo());
    // }
    // }
    // }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        // TODO Auto-generated method stub
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                // 定位成功回调信息，设置相关消息
                // amapLocation.getLocationType();// 获取当前定位结果来源，如网络定位结果，详见定位类型表
                // amapLocation.getLatitude();// 获取纬度
                // amapLocation.getLongitude();// 获取经度
                // amapLocation.getAccuracy();// 获取精度信息
                lat = amapLocation.getLatitude();
                lgt = amapLocation.getLongitude();

                Log.e("爸爸", amapLocation.getAddress());
                SimpleDateFormat df = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss");
                Date date = new Date(amapLocation.getTime());
                df.format(date);// 定位时间
            } else {
                // 显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError",
                        "location Error, ErrCode:"
                                + amapLocation.getErrorCode() + ", errInfo:"
                                + amapLocation.getErrorInfo());
            }
        }

    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        if (receiver != null) {
            getActivity().unregisterReceiver(receiver);
        }
        mlocationClient = null;
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
        }
    }

    /**
     * 已连接WiFi
     */
    public void linkWiFiSuccess() {
        if (dialog != null) {
            dialog.dismiss();
        }
        localWifiUtils.getConnectedInfo();
        String connectedSSID = localWifiUtils.getConnectedSSID();

        // ssd.setText("已成功连接到\n" + connectedSSID);
        ssd.setText(connectedSSID);
        tv1.setText("Internet访问");

        if (connectedSSID.contains("无限城WIFI_")) {
            String mobile = null;
            Login user = MyCookieStore.user;
            if (user != null) {
                mobile = user.getShouji();
            }
            if (IsMobile.isMobileNO(mobile)) {

                // 认证WiFi
                authenticateWifi(mobile);
            }

        }

        ivWifiState.setImageResource(R.drawable.icon_wifi_connected);
        if (islogin) {

            setHead();

        } else {
            return;
        }
    }

    /**
     * 认证自己的WiFi热点
     */
    private void authenticateWifi(String mobile) {
        RequestParams params = new RequestParams(Url.AUTHENTICATE_WIFI);
        params.addQueryStringParameter("phone", mobile);
        x.http().post(params, new CommonCallback<String>() {

            @Override
            public void onCancelled(CancelledException arg0) {
                LogUtil.i("onCancelled");
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                LogUtil.i("onError:" + arg0.toString());

            }

            @Override
            public void onFinished() {
                LogUtil.i("onFinished");

            }

            @Override
            public void onSuccess(String json) {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    int status = jsonObject.getInt("status");
                    if (status == 1) {
                        ToastShow.shortShowToast("认证WiFi成功，可免费上网！");
                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 无WiFi连接
     */
    public void linkWiFiFail() {
        ssd.setText("当前未连接");
        tv1.setText("无Internet");
        ivWifiState.setImageResource(R.drawable.icon_wifi_no_connected);

    }

    /**
     * 延迟两秒更新WiFi信息
     */
    public void updateCurrentWiFiInfo() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                localWifiUtils.getConnectedInfo();
                String connectedSSID = localWifiUtils.getConnectedSSID();
                if ("NULL".equals(connectedSSID) || "0x".equals(connectedSSID)) {
                    ssd.setText("当前未连接");
                    tv1.setText("无Internet");
                    return;
                }
                ssd.setText("已成功连接到\n" + connectedSSID);
                tv1.setText("Internet访问");
            }
        }, 2000);

    }

    View dview;
    private AnimationDrawable animationDrawable;

    /**
     * 延迟两秒更新WiFi信息
     */
    public void showLinkWiFiDialog() {
        if (dialog == null) {

            dview = getActivity().getLayoutInflater().inflate(R.layout.dialog_wifi, null);
            dialog = new AlertDialog.Builder(getActivity())
                    .setView(dview).create();
            ImageView img = (ImageView) dview.findViewById(R.id.iv_3);
            animationDrawable = (AnimationDrawable) img.getDrawable();
            animationDrawable.start();
            dialog.setCancelable(false);
            dialog.show();
            dialog.getWindow().setLayout(400, 300);
            final LinearLayout ll_1 = (LinearLayout) dview.findViewById(R.id.ll_ip);
            final LinearLayout ll_2 = (LinearLayout) dview
                    .findViewById(R.id.ll_conect);
            ll_1.postDelayed(new Runnable() {

                @Override
                public void run() {
                    ll_1.setVisibility(View.VISIBLE);
                }
            }, 1000);
            ll_2.postDelayed(new Runnable() {

                @Override
                public void run() {
                    ll_2.setVisibility(View.VISIBLE);
                }
            }, 2000);
        }
        if (dialog.isShowing()) {
            return;
        }
        dialog.show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                dialog.dismiss();
            }
        }, 10000);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.t_near:
                Intent intent1 = new Intent(getActivity(), NearbyWiFiActivity.class);
                intent1.putExtra("weidu0", lat + "");
                intent1.putExtra("jingdu", lgt + "");

                startActivity(intent1);
                break;
            case R.id.t_share:
                Intent intent = new Intent(getActivity(), WXEntryActivity.class);
                intent.putExtra(Constant.SHARE_URL, "http://www.wxcw.net/");
                intent.putExtra(Constant.SHARE_TITLE, "同在WIFI");
                intent.putExtra(Constant.SHARE_DESCRIPTION, "再也不用担心流量不够用了");
                startActivity(intent);
                break;

            default:
                break;
        }
    }

    public class NetworkConnectChangedReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (WifiManager.WIFI_STATE_CHANGED_ACTION
                    .equals(intent.getAction())) {// 这个监听wifi的打开与关闭，与wifi的连接无关
                int wifiState = intent.getIntExtra(
                        WifiManager.EXTRA_WIFI_STATE, 0);
                Log.e("H3c", "wifiState" + wifiState);
                switch (wifiState) {
                    case WifiManager.WIFI_STATE_DISABLED:
                        Toast.makeText(getActivity(), "请打开wifi", Toast.LENGTH_SHORT)
                                .show();
                        break;
                    case WifiManager.WIFI_STATE_DISABLING:
                        break;
                    //
                }
            }
            // 这个监听wifi的连接状态即是否连上了一个有效无线路由，当上边广播的状态是WifiManager.WIFI_STATE_DISABLING，和WIFI_STATE_DISABLED的时候，根本不会接到这个广播。
            // 在上边广播接到广播是WifiManager.WIFI_STATE_ENABLED状态的同时也会接到这个广播，当然刚打开wifi肯定还没有连接到有效的无线
            if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent
                    .getAction())) {
                Parcelable parcelableExtra = intent
                        .getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                if (null != parcelableExtra) {
                    NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
                    State state = networkInfo.getState();
                    LogUtil.e("WiFi state" + state.toString() + " "
                            + networkInfo.getDetailedState().toString());
                    if (state == State.CONNECTING) {
                        showLinkWiFiDialog();
                    }
                    boolean isConnected = state == State.CONNECTED;// 当然，这边可以更精确的确定状态
                    Log.e("H3c", "isConnected" + isConnected);
                    if (isConnected) {
                        LogUtil.e("连接WiFi");
                        wifiUtils.getConnectedInfo();
                        String mymac = wifiUtils.getConnectedBSSID();
                        // 显示连接WiFi的名称
                        handler.sendEmptyMessage(WIFI_CONNECTED);
                    } else {
                        LogUtil.e("连接失败");
                        // 无WiFi连接
                        handler.sendEmptyMessage(WIFI_DISCONNECT);

                    }
                }
            }
            // 这个监听网络连接的设置，包括wifi和移动数据的打开和关闭。.
            // 最好用的还是这个监听。wifi如果打开，关闭，以及连接上可用的连接都会接到监听。见log
            // 这个广播的最大弊端是比上边两个广播的反应要慢，如果只是要监听wifi，我觉得还是用上边两个配合比较合适
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent
                    .getAction())) {
                ConnectivityManager manager = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo gprs = manager
                        .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                NetworkInfo wifi = manager
                        .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                Log.e("baba",
                        "网络状态改变:" + wifi.isConnected() + " 3g:"
                                + gprs.isConnected());
                NetworkInfo info = intent
                        .getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
                if (info != null) {
                    Log.e("H3c", "info.getTypeName()" + info.getTypeName());
                    Log.e("H3c", "getSubtypeName()" + info.getSubtypeName());
                    Log.e("H3c", "getState()" + info.getState());
                    Log.e("H3c", "getDetailedState()"
                            + info.getDetailedState().name());
                    Log.e("H3c", "getDetailedState()" + info.getExtraInfo());
                    Log.e("H3c", "getType()" + info.getType());

                    if (NetworkInfo.State.CONNECTED == info.getState()) {

                    } else if (info.getType() == 1) {
                        if (NetworkInfo.State.DISCONNECTING == info.getState()) {

                        }
                    }
                }
            }
        }
    }

    @Event(R.id.ll_heads)
    private void toWifiUserListActivity(View v) {
        if (islogin) {
            Intent intent = new Intent(getActivity(),
                    WifiUserListActivity.class);
            wifiUtils.getConnectedInfo();
            intent.putExtra(Constant.WIFI_MAC, wifiUtils.getConnectedBSSID());
            startActivity(intent);
        } else {
            return;
        }
    }

    private void setHead() {

        SessionRequestParams params = new SessionRequestParams(
                MrLiUrl.URL_Wifi_friends_list);

        params.addBodyParameter("pageIndex", "1");
        params.addBodyParameter("pageSize", "3");
        wifiUtils.getConnectedInfo();
        params.addBodyParameter("mac000", wifiUtils.getConnectedBSSID());

        x.http().post(params, new HttpCallBack() {

            @Override
            public void success(String result) throws JSONException {
                WfUser json = GsonUtils.parseJSON(result, WfUser.class);
                List<FriendsList> mList = json.getFriendsList();
                friendList.clear();

                friendList.addAll(mList);
                for (int i = 0; i < headImageViews.length; i++) {
                    headImageViews[i].setVisibility(View.GONE);
                }
                for (int i = 0; i < friendList.size(); i++) {
                    headImageViews[i].setVisibility(View.VISIBLE);
                    x.image().bind(
                            headImageViews[i],
                            ImageUtils.getImageUrl(friendList.get(i)
                                    .getTxiang()), options);
                    heads.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(CancelledException arg0) {

            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {

            }

            @Override
            public void onFinished() {

            }

        });

    }

}
