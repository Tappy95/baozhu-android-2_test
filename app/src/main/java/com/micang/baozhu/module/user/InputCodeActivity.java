package com.micang.baozhu.module.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.micang.baselibrary.base.BaseActivity;
import com.micang.baozhu.R;
import com.micang.baozhu.http.BaseResult;
import com.micang.baozhu.http.net.HttpUtils;
import com.micang.baozhu.http.net.Observer;
import com.micang.baozhu.util.EmptyUtils;
import com.micang.baozhu.util.ToastUtils;
import com.jaeger.library.StatusBarUtil;


public class InputCodeActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout llBack;
    private TextView tvTitle;
    private EditText etCode;
    private Button btnSure;

    @Override
    public int layoutId() {
        return R.layout.activity_input_code;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        StatusBarUtil.setTransparent(this);
        StatusBarUtil.setLightMode(this);
        llBack = findViewById(R.id.ll_back);
        tvTitle = findViewById(R.id.tv_title);
        Intent intent = getIntent();
        String referrerCode = intent.getStringExtra("referrerCode");

        tvTitle.setText("输入邀请码");
        etCode = findViewById(R.id.et_code);
        btnSure = findViewById(R.id.btn_sure);
        llBack.setOnClickListener(this);
        btnSure.setOnClickListener(this);
        if (EmptyUtils.isNotEmpty(referrerCode)) {
            etCode.setText(referrerCode);
            etCode.setEnabled(false);
            btnSure.setEnabled(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.btn_sure:

                String code = etCode.getText().toString().trim();
                if (EmptyUtils.isEmpty(code)) {
                    ToastUtils.show(InputCodeActivity.this, "邀请码不能为空");
                    return;
                }
                bindQrCode(code);
                break;
            default:
                break;
        }
    }

    private void bindQrCode(String code) {
        HttpUtils.bindQrCode(code).enqueue(new Observer<BaseResult>() {
            @Override
            public void onSuccess(BaseResult response) {
                ToastUtils.show(response.message);
                finish();
            }
        });
    }
}
