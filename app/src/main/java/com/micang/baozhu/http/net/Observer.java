package com.micang.baozhu.http.net;

import android.content.Intent;

import com.micang.baselibrary.util.SPUtils;
import com.micang.baselibrary.util.TLog;
import com.micang.baozhu.AppContext;
import com.micang.baozhu.constant.CommonConstant;
import com.micang.baozhu.http.BaseResult;
import com.micang.baozhu.module.login.NewLoginActivity;
import com.micang.baozhu.util.EmptyUtils;
import com.micang.baozhu.util.ToastUtils;

import retrofit2.Call;
import retrofit2.Response;

public abstract class Observer<T> extends BaseObserver<T> {
    @Override
    void onNext(Call<T> call, Response<T> response) {
        if (response.isSuccessful() && response.body() != null) {
            if (response.body() instanceof BaseResult) {
                BaseResult body = (BaseResult) response.body();
                if (EmptyUtils.isNotEmpty(body.token)) {
                    SPUtils.saveString(AppContext.getInstance(), CommonConstant.USER_TOKEN, body.token);
                }
                if (EmptyUtils.isNotEmpty(body)) {
                    if (body.statusCode.startsWith("2")) {
                        onSuccess(body);
                    } else if (body.statusCode.equals("3104") || body.statusCode.equals("3006")) {
                        //未登录,进行下一步操作
                        ToastUtils.show(body.message);
                        SPUtils.saveString(AppContext.getInstance(), CommonConstant.USER_TOKEN, "");
                        Intent intent = new Intent(AppContext.getInstance(), NewLoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        AppContext.getInstance().startActivity(intent);
                    } else {
                        onFailed(body.statusCode, body.message);
                        onFailed(body);

                    }
                }
            }
        } else {
            ToastUtils.show("没有网络哦");
            String code = response.code() + "";
            String message = response.message();
            TLog.d("error", response.toString());
            onFailed(code, message);
        }
    }

    public void onFailed(BaseResult body) {

    }

    @Override
    void onFail(Call<T> call, Throwable t) {
        ToastUtils.show("当前网络状态不好,稍后再试");
        TLog.d("error", t.getMessage());
        onFailure(t);
    }

    public abstract void onSuccess(BaseResult response);

    /**
     * @param t
     */
    public void onFailure(Throwable t) {

    }

    /**
     * @param code
     * @param msg
     */
    public void onFailed(String code, String msg) {
        ToastUtils.show(msg);
    }
}
