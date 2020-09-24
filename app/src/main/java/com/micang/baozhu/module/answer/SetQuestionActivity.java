package com.micang.baozhu.module.answer;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.micang.baselibrary.base.BaseActivity;
import com.micang.baselibrary.util.DensityUtil;
import com.micang.baozhu.R;
import com.micang.baozhu.constant.CommonConstant;
import com.micang.baozhu.http.BaseResult;
import com.micang.baozhu.http.bean.anwer.QuestionTypeBean;
import com.micang.baozhu.http.net.HttpUtils;
import com.micang.baozhu.http.net.Observer;
import com.micang.baozhu.util.EmptyUtils;
import com.micang.baozhu.util.ToastUtils;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SetQuestionActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout llBack;
    private LinearLayout llQuestionType;
    private TextView tvTitle;
    private ImageView ivStatus;
    private TextView tvQuestionType;
    private EditText etQuestionData;
    private EditText etQuestionAnswerTrue;
    private EditText etQuestionAnswerFalse1;
    private EditText etQuestionAnswerFalse2;
    private EditText etQuestionAnswerFalse3;
    private Button btSubmit;


    private Map<String, String> beanType = new HashMap<>();

    private List<String> questiontypeNameList = new ArrayList<>();
    private ListView listView;
    private QuestionTypeAdapter typeAdapter;
    private PopupWindow questionTypeSelectPopup;

    private String selectType;
    private String qustionID;
    private String questionType;
    private String questionName;
    private String trueAnswer;
    private String falseAnswer1;
    private String falseAnswer2;
    private String falseAnswer3;

    @Override
    public int layoutId() {
        return R.layout.activity_question;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setTransparent(this);
        llBack = (LinearLayout) findViewById(R.id.ll_back);
        llQuestionType = (LinearLayout) findViewById(R.id.ll_question_type);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setText("出题赢金币");
        ivStatus = (ImageView) findViewById(R.id.iv_status);
        tvQuestionType = (TextView) findViewById(R.id.tv_question_type);
        etQuestionData = (EditText) findViewById(R.id.et_question_data);
        etQuestionAnswerTrue = (EditText) findViewById(R.id.et_question_answer_true);
        etQuestionAnswerFalse1 = (EditText) findViewById(R.id.et_question_answer_false1);
        etQuestionAnswerFalse2 = (EditText) findViewById(R.id.et_question_answer_false2);
        etQuestionAnswerFalse3 = (EditText) findViewById(R.id.et_question_answer_false3);
        btSubmit = (Button) findViewById(R.id.bt_submit);
        initClick();


    }

    @Override
    protected void onResume() {
        super.onResume();
        checkQuestionType();
    }

    private void initQuestionTypeSelectPopup() {
        listView = new ListView(SetQuestionActivity.this);
        listView.setDividerHeight(0);//去掉分割线
        typeAdapter = new QuestionTypeAdapter(questiontypeNameList, selectType);
        listView.setAdapter(typeAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String value = questiontypeNameList.get(position);

                tvQuestionType.setText(value);
                selectType = value;


                questionTypeSelectPopup.dismiss();
            }
        });
        questionTypeSelectPopup = new PopupWindow(listView, llQuestionType.getWidth() / 2, DensityUtil.dip2px(SetQuestionActivity.this, 120), true);

        Drawable drawable = ContextCompat.getDrawable(SetQuestionActivity.this, R.drawable.shape_bc_radiogroup);
        questionTypeSelectPopup.setBackgroundDrawable(drawable);
        questionTypeSelectPopup.setFocusable(true);
        questionTypeSelectPopup.setOutsideTouchable(true);
        questionTypeSelectPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

                questionTypeSelectPopup.dismiss();
            }
        });
    }

    private void checkQuestionType() {

        HttpUtils.questionType().enqueue(new Observer<BaseResult<List<QuestionTypeBean>>>() {
            @Override
            public void onSuccess(BaseResult response) {
                List<QuestionTypeBean> typeBeans = (List<QuestionTypeBean>) response.data;
                if (EmptyUtils.isNotEmpty(typeBeans)) {
                    for (int i = 0; i < typeBeans.size(); i++) {
                        QuestionTypeBean questionTypeBean = typeBeans.get(i);
                        String dicValue = questionTypeBean.dicValue;
                        String id = questionTypeBean.id;
                        questiontypeNameList.add(dicValue);
                        beanType.put(dicValue, id);
                    }
                }
            }
        });
    }

    private void initClick() {
        llBack.setOnClickListener(this);
        llQuestionType.setOnClickListener(this);
        btSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.ll_question_type:
                initQuestionTypeSelectPopup();

                if (questionTypeSelectPopup != null && !questionTypeSelectPopup.isShowing()) {
                    questionTypeSelectPopup.showAsDropDown(llQuestionType, 0, 0);
                }
                break;
            case R.id.bt_submit:
                questionType = tvQuestionType.getText().toString();
                questionName = etQuestionData.getText().toString().trim();
                trueAnswer = etQuestionAnswerTrue.getText().toString().trim();
                falseAnswer1 = etQuestionAnswerFalse1.getText().toString().trim();
                falseAnswer2 = etQuestionAnswerFalse2.getText().toString().trim();
                falseAnswer3 = etQuestionAnswerFalse3.getText().toString().trim();
                if (EmptyUtils.isEmpty(questionType)) {
                    ToastUtils.show(SetQuestionActivity.this, "请选择题目类型");
                    return;
                }
                if (EmptyUtils.isEmpty(questionName) || questionName.length() < 10) {
                    ToastUtils.show(SetQuestionActivity.this, "题目内容最少10个字");
                    return;
                }
                if (questionName.length() > 30) {
                    ToastUtils.show(SetQuestionActivity.this, "题目内容最多30个字");
                    return;
                }
                if (EmptyUtils.isEmpty(trueAnswer)) {
                    ToastUtils.show(SetQuestionActivity.this, "请输入正确答案");
                    return;
                }
                if (EmptyUtils.isEmpty(falseAnswer1)) {
                    ToastUtils.show(SetQuestionActivity.this, "请输入错误答案");
                    return;
                }
                setQuestion(questionType, questionName, trueAnswer, falseAnswer1, falseAnswer2, falseAnswer3);
                break;
            default:
                break;
        }
    }

    private void setQuestion(String questionType, String questionName, String trueAnswer,
                             String falseAnswer1, String falseAnswer2, String falseAnswer3) {
        qustionID = geQustionTypeId(questionType);

        HttpUtils.setQuestion(qustionID, questionName, trueAnswer, falseAnswer1, falseAnswer2, falseAnswer3).enqueue(new Observer<BaseResult>() {
            @Override
            public void onSuccess(BaseResult response) {
                Intent intent = new Intent(SetQuestionActivity.this, CheckQuestionActivity.class);

                intent.putExtra(CommonConstant.QUESTIONSTATE, 2);
                startActivity(intent);
                finish();
            }
        });
    }


    @NonNull
    private String geQustionTypeId(String questionType) {
        String id = beanType.get(questionType);
        return id;
    }
}
