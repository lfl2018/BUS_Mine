package com.mogu.fragment;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.common.util.LogUtil;
import org.xutils.image.ImageOptions;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mogu.activity.BindingPhoneActivity;
import com.mogu.activity.ChengjiuActivity;
import com.mogu.activity.ConActivity;
import com.mogu.activity.FeedBackActivity;
import com.mogu.activity.IdentityAuthenticationActivity;
import com.mogu.activity.JuDianActivity;
import com.mogu.activity.LoginActivity;
import com.mogu.activity.MeActivity;
import com.mogu.activity.MissionActivity;
import com.mogu.activity.QRCodeActivity;
import com.mogu.activity.R;
import com.mogu.activity.SettingActivity;
import com.mogu.activity.wxapi.WXEntryActivity;
import com.mogu.app.Constant;
import com.mogu.app.MyCookieStore;
import com.mogu.app.Url;
import com.mogu.entity.myinfo.MyInfo;
import com.mogu.utils.FileUtils;
import com.mogu.utils.GsonUtils;
import com.mogu.utils.HttpCallBack;
import com.mogu.utils.ImageUtils;
import com.mogu.utils.IsMobile;
import com.mogu.utils.SessionRequestParams;
import com.mogu.utils.ToastShow;
import com.mogu.utils.WifiUtils;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXWebpageObject;
import com.tencent.mm.sdk.platformtools.Util;

public class MeMeFragment extends Fragment implements OnClickListener {
    private ImageView ivHead;
    private ImageView img_set;
    private TextView down;
    private TextView ccsign;
    private View mLayout;
    private LinearLayout linec;
    private LinearLayout ll_yy;
    private LinearLayout ll_yx;
    private TextView feedback;
    private TextView judian;
    private ImageView share;
    private View Mydialog;
    private AlertDialog dialog;
    // dialog tv_jifen
    private EditText name;
    private EditText pass;
    private TextView ssd;
    private TextView chengjiu;
    private TextView jifen;
    private TextView mission;
    private TextView friend;
    private TextView identity;
    private TextView sign;
    private Button btn_share;

    private TextView userNameTextView;
    private ImageOptions options;
    private WifiUtils wifiUtils;

    private static final String APP_ID = "wx5e14bf8ddb337087";
    private IWXAPI api;
    private ImageView qrImageView;
    private boolean islogin;

    // 文件路径
    private String mPath = "";
    //你好
    private StringBuffer path;
    private AlertOnClickListener alertOnClickListener;
    private static final int PHOTO_ALBUM = 1; // 相册
    private static final int IMAGE_RESCUT = 2;// 裁剪
    private static final int PHOTO_TAKE = 3;// 拍照

    private Bitmap bm = null;// 图片
    private static final String PHOTO_TYPE = "image/*";

    private ProgressDialog pDialog;
    private TextView tvMobile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mLayout == null) {
            mLayout = inflater.inflate(R.layout.fragment_me_me, null);
            alertOnClickListener = new AlertOnClickListener();

            initView(mLayout);
        }
        return mLayout;
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sp1 = getActivity().getSharedPreferences(
                Constant.MAIN_SHARE_FILE_NAME, Context.MODE_PRIVATE);
        islogin = sp1.getBoolean("islogin", false);
        if (MyCookieStore.user == null) {
            return;
        }
//		x.image()
//				.bind(ivHead,
//						ImageUtils.getImageUrl(MyCookieStore.user.getTxiang()),
//						options);
        GetData();
    }

    // 微信注册
    private void regToWx() {
        api = WXAPIFactory.createWXAPI(getActivity(), APP_ID, true);
        api.registerApp(APP_ID);
    }

    private void wxShare() {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = "http://dnf.qq.com/";
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "大佬";
        msg.description = "卧槽8个9";
        Bitmap thumb = BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_launcher);
        msg.thumbData = Util.bmpToByteArray(thumb, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        api.sendReq(req);

    }

    //
    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis())
                : type + System.currentTimeMillis();
    }

    private void initView(View view) {
        // TODO Auto-generated method stub

        judian = (TextView) view.findViewById(R.id.tv_judian);
        sign = (TextView) view.findViewById(R.id.tv_sign);
        ivHead = (ImageView) view.findViewById(R.id.head);
        userNameTextView = (TextView) view.findViewById(R.id.tv_user_name);
        jifen = (TextView) view.findViewById(R.id.tv_jifen);
        img_set = (ImageView) view.findViewById(R.id.img_set);
        feedback = (TextView) view.findViewById(R.id.tv_feedback);
        chengjiu = (TextView) view.findViewById(R.id.tv_chengjiu);
        mission = (TextView) view.findViewById(R.id.tv_mission);
        friend = (TextView) view.findViewById(R.id.tv_friend);
        identity = (TextView) view.findViewById(R.id.tv_identity);
        tvMobile = (TextView) view.findViewById(R.id.tv_mobile);

        qrImageView = (ImageView) view.findViewById(R.id.iv_qr_code);
        qrImageView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (islogin) {
                    startActivity(new Intent().setClass(getActivity(),
                            QRCodeActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    ToastShow.shortShowToast("请登录");
                }
            }
        });
        options = new ImageOptions.Builder().setCircular(true)
                // 是否忽略GIF格式的图片
                .setIgnoreGif(false)
                // 图片缩放模式
                .setImageScaleType(ScaleType.CENTER_CROP)
                // 下载中显示的图片
                .setLoadingDrawableId(R.drawable.ic_head)
                // 下载失败显示的图片
                .setFailureDrawableId(R.drawable.ic_head)
                // 得到ImageOptions对象
                .build();
        chengjiu.setOnClickListener(this);
        sign.setOnClickListener(this);
        mission.setOnClickListener(this);
        friend.setOnClickListener(this);
        identity.setOnClickListener(this);
        img_set.setOnClickListener(this);
        ivHead.setOnClickListener(this);
        feedback.setOnClickListener(this);
        judian.setOnClickListener(this);
        tvMobile.setOnClickListener(this);
        share = (ImageView) view.findViewById(R.id.img_share);
        share.setOnClickListener(this);

        Mydialog = getActivity().getLayoutInflater().inflate(
                R.layout.dialog_share, null);
        name = (EditText) Mydialog.findViewById(R.id.edt_name);
        pass = (EditText) Mydialog.findViewById(R.id.edt_pass);
        ssd = (TextView) Mydialog.findViewById(R.id.tv_ssd);
        btn_share = (Button) Mydialog.findViewById(R.id.btn_share);
        btn_share.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head:
                if (islogin) {
                    // startActivity(new Intent(getActivity(), MeActivity.class));
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            getActivity());
                    builder.setTitle("请选择");
                    builder.setNegativeButton("取消", alertOnClickListener);
                    builder.setNeutralButton("相册", alertOnClickListener);
                    builder.setPositiveButton("拍照", alertOnClickListener);
                    builder.create();
                    builder.show();
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    ToastShow.shortShowToast("请登录");
                }
                break;
            case R.id.tv_identity:
                if (islogin) {
                    startActivity(new Intent(getActivity(),
                            IdentityAuthenticationActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    ToastShow.shortShowToast("请登录");
                }
                break;
            case R.id.tv_sign:
                mySign();
                // startActivity(new Intent(getActivity(), FeedBackActivity.class));
                break;
            case R.id.tv_chengjiu:
                // mySign();
                if (islogin) {
                    startActivity(new Intent(getActivity(), ChengjiuActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    ToastShow.shortShowToast("请登录");
                }
                break;
            case R.id.tv_feedback:
                if (islogin) {
                    startActivity(new Intent(getActivity(), FeedBackActivity.class));
                    // HuatiList huatiList=new HuatiList();
                    // huatiList.setBiaoti("biaoti");
                    // huatiList.setNrong0("nrong0");
                    // huatiList.setHuatbh("http://192.168.0.123:8080/BizReleaseJsp.action?mac000=08:57:00:09:c2:52");
                    // startActivity(new Intent(getActivity(),
                    // HtmlTopicDetailActivity.class).putExtra(Constant.TOPIC_VALUE,
                    // huatiList));

                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    ToastShow.shortShowToast("请登录");
                }
                break;
            case R.id.tv_judian:
                if (islogin) {
                    startActivity(new Intent(getActivity(), JuDianActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    ToastShow.shortShowToast("请登录");
                }
                break;
            case R.id.img_set:
                if (islogin) {
                    startActivity(new Intent(getActivity(), SettingActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    ToastShow.shortShowToast("请登录");
                }
                break;
            case R.id.tv_mission:
                if (islogin) {
                    startActivity(new Intent(getActivity(), MissionActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    ToastShow.shortShowToast("请登录");
                }
                break;
            case R.id.tv_friend:
                if (islogin) {
                    startActivity(new Intent(getActivity(), ConActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    ToastShow.shortShowToast("请登录");
                }
                break;
            case R.id.img_share:
                // regToWx();
                // wxShare();
                if (islogin) {
                    startActivity(new Intent(getActivity(), WXEntryActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    ToastShow.shortShowToast("请登录");
                }
                break;
            case R.id.btn_share:
                if (dialog != null) {
                    dialog.dismiss();
                }
                break;
            case R.id.tv_mobile:
                if (IsMobile.isMobileNO(MyCookieStore.user.getShouji())) {
                    ToastShow.shortShowToast("已绑定手机！");
                    return;
                }
                startActivity(new Intent(getActivity(), BindingPhoneActivity.class));
                break;
            default:
                break;
        }
    }

    private void mySign() {
        SessionRequestParams params = new SessionRequestParams(Url.Sign_url);
        x.http().post(params, new HttpCallBack() {

            @Override
            public void onFinished() {
                // TODO Auto-generated method stub

            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onCancelled(CancelledException arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void success(String result) throws JSONException {
                JSONObject json = new JSONObject(result);
                if (json.getString("code").equals("1")) {
                    ToastShow.shortShowToast("签到成功");
                } else {
                    ToastShow.shortShowToast(json.getString("msg"));
                }
            }
        });
    }

    private void GetData() {
        SessionRequestParams params = new SessionRequestParams(Url.MyInfo_URL);
        x.http().post(params, new CommonCallback<String>() {

            @Override
            public void onCancelled(CancelledException arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onFinished() {
                // TODO Auto-generated method stub

            }

            @Override
            public void onSuccess(String arg0) {
                Log.e("爸爸", arg0);
                MyInfo json = GsonUtils.parseJSON(arg0, MyInfo.class);
                if (json.getCode().equals("1")) {
                    jifen.setText(json.getUserHyb000().getJifen0() + "");
                    userNameTextView.setText(json.getUserHyb000().getHyming());
                    x.image()
                            .bind(ivHead,
                                    ImageUtils.getImageUrl(json.getUserHyb000().getTxiang()),
                                    options);
                   //更换头像后   更换cookie中保存的头像
                    if(json.getUserHyb000().getTxiang() != null ){
                        MyCookieStore.user.setTxiang(json.getUserHyb000().getTxiang());
                    }
                }
            }
        });
    }

    private class AlertOnClickListener implements
            DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            // TODO Auto-generated method stub

            switch (which) {
                // 取消
                case DialogInterface.BUTTON_NEGATIVE:
                    break;
                // 相册
                case DialogInterface.BUTTON_NEUTRAL:
                    // 进入到相册 只取相册中所有的图片文件
                    Intent intent1 = new Intent(Intent.ACTION_PICK, null);
                    intent1.setDataAndType(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            PHOTO_TYPE);

                    startActivityForResult(intent1, PHOTO_ALBUM);
                    break;
                // 拍照
                case DialogInterface.BUTTON_POSITIVE:
                    takepic();
                    break;
            }
        }

    }

    public void takepic() {
        path = new StringBuffer();

        path.append(Environment.getExternalStorageDirectory().getPath())
                .append("/123.jpg");
        File file = new File(path.toString());
        Uri uri = Uri.fromFile(file);
        Intent itent = new Intent(
                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        itent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(itent, PHOTO_TAKE);
    }

    public void uploadMethod() {
        SessionRequestParams params = new SessionRequestParams(
                Url.UPDATE_AVATAR);
        params.addBodyParameter("picfiles", new File(mPath));
        params.addBodyParameter("picfilesFileName", mPath);
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("正在上传照片...");
        pDialog.show();
        x.http().post(params, new CommonCallback<String>() {

            @Override
            public void onCancelled(CancelledException arg0) {

            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                Toast.makeText(getActivity(), "上传图片失败！", 0).show();
            }

            @Override
            public void onFinished() {
                pDialog.dismiss();

            }

            @Override
            public void onSuccess(String result) {
                String statusCode = null;
                String msg = null;

                JSONObject jsonObject = null;
                try {
                    LogUtil.e("result:" + result);
                    jsonObject = new JSONObject(result);
                    statusCode = jsonObject.getString("code");
                    msg = jsonObject.getString("msg");
                    LogUtil.i(jsonObject.toString() + "ggg" + statusCode);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if ("1".equals(statusCode)) {

                    // 更新头像
                    x.image().bind(ivHead, mPath, options);
                    GetData();
                    Toast.makeText(getActivity(), "保存成功！", 0).show();
                } else {

                    AlertDialog dialog = new AlertDialog.Builder(getActivity())
                            .setMessage(msg)
                            .setPositiveButton("确定",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {
                                        }
                                    }).create();
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();

                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            // handleImageWithoutCut(requestCode, data);
            handleImageWithCut(requestCode, data);
        }
    }

    /**
     * 带裁剪的图片处理
     *
     * @param requestCode
     * @param data
     */
    public void handleImageWithCut(int requestCode, Intent data) {
        switch (requestCode) {
            case PHOTO_TAKE:
                File file = new File(path.toString());
                startPhotoZoom(Uri.fromFile(file));
                break;
            case PHOTO_ALBUM:
                // 取到选择的相片
                Uri uri = data.getData();
                // 截图处理
                startPhotoZoom(uri);
                break;
            case IMAGE_RESCUT:
                Bundle bundle = data.getExtras();
                bm = bundle.getParcelable("data");

                String currentTimeMillis = System.currentTimeMillis() + "";
                FileUtils.saveBitmap(bm, currentTimeMillis);
                mPath = FileUtils.SDPATH + currentTimeMillis + ".JPEG";
                uploadMethod();
                LogUtil.i(mPath);
                break;
        }

    }

    /**
     * 将获得的图片进行裁剪
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, PHOTO_TYPE);
        intent.putExtra("crop", "true");

        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        intent.putExtra("outputX", 300);// 返回数据的时候的 X 像素大小。
        intent.putExtra("outputY", 300);

        intent.putExtra("return-data", true);
        // 截图完成后 进行跳转
        startActivityForResult(intent, IMAGE_RESCUT);
    }

    /**
     * 结束时 释放图片资源
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (bm != null)
            // 释放图片资源
            bm.recycle();
        bm = null;
    }

}
