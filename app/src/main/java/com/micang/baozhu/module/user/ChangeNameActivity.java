package com.micang.baozhu.module.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.micang.baselibrary.base.BaseActivity;
import com.micang.baozhu.R;
import com.micang.baozhu.http.BaseResult;
import com.micang.baozhu.http.net.HttpUtils;
import com.micang.baozhu.http.net.Observer;
import com.micang.baozhu.util.ToastUtils;
import com.jaeger.library.StatusBarUtil;


public class ChangeNameActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout llBack;
    private TextView tvTitle;
    private TextView tvSure;
    private EditText etName;
    private String name;
    private String type;

    @Override
    public int layoutId() {
        return R.layout.activity_change_name;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setTransparent(this);
        llBack = findViewById(R.id.ll_back);
        tvTitle = findViewById(R.id.tv_title);
        tvSure = findViewById(R.id.tv_sure);
        etName = findViewById(R.id.et_name);
        llBack.setOnClickListener(this);
        tvSure.setOnClickListener(this);
        Intent intent = getIntent();
        name = intent.getStringExtra("NAME");
        type = intent.getStringExtra("type");
        if ("1".equals(type)) {
            tvTitle.setText("修改昵称");
        } else {
            tvTitle.setText("输入QQ");
        }
        tvSure.setText("保存");
        etName.setText(name);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.tv_sure:
                String newName = etName.getText().toString().trim();
                if (name.equals(newName)) {
                    ToastUtils.show("修改成功");
                    finish();
                    return;
                }
                if ("1".equals(type)) {
                    changeName(newName);
                } else {
                    changeQQ(newName);
                }
                break;

            default:
                break;
        }
    }

    private void changeName(String newName) {
        HttpUtils.changeName(newName).enqueue(new Observer<BaseResult>() {
            @Override
            public void onSuccess(BaseResult response) {
                ToastUtils.show(ChangeNameActivity.this, "修改成功");
                finish();
            }
        });
    }

    private void changeQQ(String newName) {
        HttpUtils.changeQQ(newName).enqueue(new Observer<BaseResult>() {
            @Override
            public void onSuccess(BaseResult response) {
                ToastUtils.show(ChangeNameActivity.this, "修改成功");
                finish();
            }
        });
    }
}
