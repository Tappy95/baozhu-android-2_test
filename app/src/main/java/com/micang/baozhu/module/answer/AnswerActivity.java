package com.micang.baozhu.module.answer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.micang.baselibrary.base.BaseActivity;
import com.micang.baozhu.R;
import com.micang.baozhu.constant.CommonConstant;
import com.micang.baozhu.http.BaseResult;
import com.micang.baozhu.http.bean.anwer.QuestionTopResult;
import com.micang.baozhu.http.net.HttpUtils;
import com.micang.baozhu.http.net.Observer;
import com.micang.baozhu.module.web.ToQustionActivity;
import com.micang.baozhu.util.EmptyUtils;
import com.jaeger.library.StatusBarUtil;

/**

 */
public class AnswerActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout llBack;
    private TextView tvTitle;
    private Button btToAnswer;
    private Button btFriendAnswer;
    private ImageButton btQuestion;
    private int questionState;
    private QuestionTopResult data;
    private int res;


    @Override
    public int layoutId() {
        return R.layout.activity_answer;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        StatusBarUtil.setTransparent(this);
        llBack = (LinearLayout) findViewById(R.id.ll_back);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        btToAnswer = (Button) findViewById(R.id.bt_to_answer);
        btFriendAnswer = (Button) findViewById(R.id.bt_friend_answer);
        btQuestion = (ImageButton) findViewById(R.id.bt_question);
        tvTitle.setText("对战答题");
        initClick();


    }

    private void checkTopResult() {

        HttpUtils.checkTopResult().enqueue(new Observer<BaseResult<QuestionTopResult>>() {
            @Override
            public void onSuccess(BaseResult response) {
                data = (QuestionTopResult) response.data;
                res = data.res;
                if (res == -1) {

                    startActivity(new Intent(AnswerActivity.this, SetQuestionActivity.class));
                } else {

                    if (EmptyUtils.isNotEmpty(data.question)) {
                        QuestionTopResult.QuestionBean question = data.question;
                        questionState = question.questionState;
                        checkState();
                    }
                }
            }
        });

    }

    private void initClick() {
        llBack.setOnClickListener(this);
        btToAnswer.setOnClickListener(this);
        btFriendAnswer.setOnClickListener(this);
        btQuestion.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.bt_to_answer:
                startActivity(new Intent(AnswerActivity.this, ToQustionActivity.class));
                break;
            case R.id.bt_friend_answer:
                startActivity(new Intent(AnswerActivity.this, FriendAnswerActivity.class));
                break;
            case R.id.bt_question:
                checkTopResult();
                break;
            default:
                break;
        }
    }

    private void checkState() {
        if (EmptyUtils.isEmpty(questionState)) {
            checkTopResult();
            return;
        }
        if (questionState == -1) {
            startActivity(new Intent(AnswerActivity.this, SetQuestionActivity.class));
        } else if (questionState == 1) {
            Intent intent = new Intent(AnswerActivity.this, CheckQuestionActivity.class);
            intent.putExtra(CommonConstant.QUESTIONSTATE, 1);
            startActivity(intent);
        } else if (questionState == 2) {
            Intent intent = new Intent(AnswerActivity.this, CheckQuestionSuccessActivity.class);
            intent.putExtra(CommonConstant.QUESTIONSTATE, 2);
            startActivity(intent);
        } else if (questionState == 3) {
            Intent intent = new Intent(AnswerActivity.this, CheckQuestionFailActivity.class);
            intent.putExtra(CommonConstant.QUESTIONSTATE, 3);
            startActivity(intent);
        } else {
            startActivity(new Intent(AnswerActivity.this, SetQuestionActivity.class));
        }
    }
}
