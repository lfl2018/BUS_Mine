package com.mogu.utils;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.mogu.activity.LoginActivity;
import com.mogu.app.BusApp;
import com.mogu.entity.common.CommonJson;

import org.json.JSONException;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.common.util.LogUtil;
import org.xutils.ex.HttpException;


public abstract class FriendListHttpCallBack implements CommonCallback<String> {

    private Context context;

    public FriendListHttpCallBack() {
        this.context = BusApp.getInstance();
    }

    public FriendListHttpCallBack(Context context) {
        this.context = context;
    }

    public abstract void success(String result) throws JSONException;

    @Override
    public void onSuccess(String result) {

        Gson gson = new Gson();
        try {
            JsonElement parse = new JsonParser().parse(result);
            if (parse.isJsonObject()) {
                JsonObject jsonObject = parse.getAsJsonObject();
                CommonJson json = gson.fromJson(jsonObject, CommonJson.class);
                if ("0".equals(json.getCode())
                        && "登录超时，请重新登录".equals(json.getMsg())) {
//                    Intent intent = new Intent(context, LoginActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    context.startActivity(intent);
//                    Toast.makeText(context, "请登录!", Toast.LENGTH_SHORT).show();
                    //
                    return;
                }
            }

        } catch (JsonSyntaxException e1) {
            ToastShow.shortShowToast("json解析失败");
            return;
        }

        LogUtil.e("result: " + result);

        try {
            success(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onFailure(HttpException arg0, String arg1) {

        Toast.makeText(context, "网络出错", Toast.LENGTH_SHORT).show();
    }

}
