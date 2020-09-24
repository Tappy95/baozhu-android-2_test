package com.micang.baozhu.module.answer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.micang.baselibrary.base.BaseActivity;
import com.micang.baozhu.R;
import com.micang.baozhu.http.BaseResult;
import com.micang.baozhu.http.bean.anwer.QuestionTopResult;
import com.micang.baozhu.http.net.HttpUtils;
import com.micang.baozhu.http.net.Observer;
import com.micang.baozhu.util.EmptyUtils;
import com.jaeger.library.StatusBarUtil;

import java.util.List;


public class CheckQuestionSuccessActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout llBack;
    private TextView tvTitle;
    private ImageView ivStatus;
    private TextView tvQuestionType;
    private TextView tvQuestion;
    private TextView tvQuestionAnswerTrue;
    private TextView tvQuestionAnswerFalse1;
    private TextView tvQuestionAnswerFalse2;
    private TextView tvQuestionAnswerFalse3;
    private Button btSubmit;
    private TextView tvCoinNumb;
    private QuestionTopResult data;
    private int res;
    @Override
    public int layoutId() {
        return R.layout.activity_check_question_success;
    }

    @Override
    public void init(Bundle savedInstanceState) {

        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setTransparent(this);
        llBack = (LinearLayout) findViewById(R.id.ll_back);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setText("出题赢金币");
        ivStatus = (ImageView) findViewById(R.id.iv_status);
        tvQuestionType = (TextView) findViewById(R.id.tv_question_type);
        tvQuestion = (TextView) findViewById(R.id.tv_question);
        tvQuestionAnswerTrue = (TextView) findViewById(R.id.tv_question_answer_true);
        tvQuestionAnswerFalse1 = (TextView) findViewById(R.id.tv_question_answer_false1);
        tvQuestionAnswerFalse2 = (TextView) findViewById(R.id.tv_question_answer_false2);
        tvQuestionAnswerFalse3 = (TextView) findViewById(R.id.tv_question_answer_false3);
        btSubmit = (Button) findViewById(R.id.bt_submit);
        llBack.setOnClickListener(this);
        btSubmit.setOnClickListener(this);
        tvCoinNumb = findViewById(R.id.tv_Coin_numb);
        checkTopResult();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.bt_submit:
                startActivity(new Intent(CheckQuestionSuccessActivity.this, SetQuestionActivity.class));
                finish();
                break;

            default:
                break;
        }
    }

    private void checkTopResult() {
        HttpUtils.checkTopResult().enqueue(new Observer<BaseResult<QuestionTopResult>>() {
            @Override
            public void onSuccess(BaseResult response) {
                data = (QuestionTopResult) response.data;
                res = data.res;
                if (res == 1) {

                    if (EmptyUtils.isNotEmpty(data.question) && EmptyUtils.isNotEmpty(data.answer)) {
                        QuestionTopResult.QuestionBean question = data.question;
                        List<QuestionTopResult.AnswerBean> answer = data.answer;

                        setData(question, answer);
                    }
                }
            }
        });
    }

    private void setData(QuestionTopResult.QuestionBean question, List<QuestionTopResult.AnswerBean> answer) {
        String questionTypeName = question.questionTypeName;
        int coin = question.coin;
        tvCoinNumb.setText("恭喜获得" + coin + "金币！");
        String questionName = question.question;
        for (int i = 0; i < answer.size(); i++) {
            if (i == 0) {
                String answertrue = answer.get(0).answer;
                tvQuestionAnswerTrue.setText(answertrue);
            } else if (i == 1) {
                String answerfalse1 = answer.get(1).answer;
                tvQuestionAnswerFalse1.setText(answerfalse1);
            } else if (i == 2) {
                String answerfalse2 = answer.get(2).answer;
                tvQuestionAnswerFalse2.setText(answerfalse2);
            } else if (i == 3) {
                String answerfalse3 = answer.get(3).answer;
                tvQuestionAnswerFalse3.setText(answerfalse3);
            } else {
            }
        }

        tvQuestionType.setText(questionTypeName);
        tvQuestion.setText(questionName);

    }


}
