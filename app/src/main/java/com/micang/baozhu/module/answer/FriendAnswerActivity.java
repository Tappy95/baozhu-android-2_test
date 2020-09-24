package com.micang.baozhu.module.answer;

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
import com.micang.baozhu.module.web.CreatedRoomActivity;
import com.micang.baozhu.module.web.EnterRoomActivity;
import com.micang.baozhu.util.ToastUtils;
import com.jaeger.library.StatusBarUtil;



public class FriendAnswerActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout llBack;
    private TextView tvTitle;
    private EditText etCode;
    private Button btIn;
    private Button btCreaterom;


    @Override
    public int layoutId() {
        return R.layout.activity_friend_answer;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        StatusBarUtil.setTransparent(this);
        llBack = (LinearLayout) findViewById(R.id.ll_back);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        btCreaterom = (Button) findViewById(R.id.bt_createrom);
        etCode = (EditText) findViewById(R.id.et_code);
        btIn = (Button) findViewById(R.id.bt_in);
        tvTitle.setText("对战答题");
        initClick();
    }


    private void initClick() {
        llBack.setOnClickListener(this);
        btCreaterom.setOnClickListener(this);
        btIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.bt_createrom:
                Intent intent = new Intent(FriendAnswerActivity.this, CreatedRoomActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.bt_in:
                String code = etCode.getText().toString().trim();
                getNotRoom(code);
                break;

            default:
                break;
        }
    }


    private void getNotRoom(final String code) {
        HttpUtils.getNotRoom(code).enqueue(new Observer<BaseResult>() {
            @Override
            public void onSuccess(BaseResult response) {
                String data = (String) response.data;
                if ("1".equals(data)) {
                    Intent intentFriend = new Intent(FriendAnswerActivity.this, EnterRoomActivity.class);
                    intentFriend.putExtra("code", code);
                    startActivity(intentFriend);
                    finish();
                } else {
                    ToastUtils.show(FriendAnswerActivity.this, "房间不存在");
                }
            }
        });
    }
}
