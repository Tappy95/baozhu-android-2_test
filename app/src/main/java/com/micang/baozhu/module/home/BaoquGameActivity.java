package com.micang.baozhu.module.home;

import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cmcm.cmgame.CmGameSdk;
import com.cmcm.cmgame.GameView;
import com.cmcm.cmgame.IAppCallback;
import com.cmcm.cmgame.IGameAccountCallback;
import com.cmcm.cmgame.IGameAdCallback;
import com.cmcm.cmgame.IGamePlayTimeCallback;
import com.cmcm.cmgame.view.CmGameTopView;
import com.jaeger.library.StatusBarUtil;
import com.micang.baozhu.R;
import com.micang.baozhu.http.BaseResult;
import com.micang.baozhu.http.bean.home.BaoquGamereward;
import com.micang.baozhu.http.bean.home.BaoquGamerewardinformation;
import com.micang.baozhu.http.bean.home.VideoCountBean;
import com.micang.baozhu.http.net.HttpUtils;
import com.micang.baozhu.http.net.Observer;
import com.micang.baozhu.module.view.CountdownView1;
import com.micang.baozhu.module.view.CountdownView2;
import com.micang.baozhu.module.view.NewCommonDialog;
import com.micang.baozhu.util.EmptyUtils;
import com.micang.baozhu.util.OnMultiClickListener;
import com.micang.baozhu.util.ToastUtils;
import com.micang.baselibrary.base.BaseActivity;
import com.micang.baselibrary.util.TLog;

/**
 * 豹趣小游戏
 */
public class BaoquGameActivity extends BaseActivity implements IAppCallback, IGamePlayTimeCallback, IGameAdCallback, IGameAccountCallback {
    private LinearLayout llBack;
    private TextView tvTitle;
    private int state;  //是否可领（1-不可领 2-可领
    private NewCommonDialog rewardDialog;
    private int second; //游戏时长


    @Override
    public int layoutId() {
        return R.layout.activity_baoqu;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        StatusBarUtil.setTransparent(this);
        StatusBarUtil.setLightMode(this);

        llBack = findViewById(R.id.ll_back);
        tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText("小游戏");
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 目前只支持anrdoid 5.0或以上
        if (Build.VERSION.SDK_INT < 21) {
            Toast.makeText(this, "不支持低版本，仅支持android 5.0或以上版本!", Toast.LENGTH_LONG).show();
        }
        // 把默认游戏中心view添加进媒体指定界面
        GameView gameTabsClassifyView = (GameView) findViewById(R.id.gameView);
        gameTabsClassifyView.inflate(this);
        ////【如下为可选功能】/////// ///////////////////////////////////////////
        /// 如没必要，不要使用
        // 默认游戏中心页面，点击游戏试，触发回调
        CmGameSdk.setGameClickCallback(this);

        // 点击游戏右上角或物理返回键，退出游戏时触发回调，并返回游戏时长
        CmGameSdk.setGamePlayTimeCallback(this);

        // 游戏内增加自定义view，提供产品多样性
        initMoveViewSwitch();

        // 所有广告类型的展示和点击事件回调，仅供参考，数据以广告后台为准
        CmGameSdk.setGameAdCallback(this);

        // 账号信息变化时触发回调，若需要支持APP卸载后游戏信息不丢失，需要注册该回调
        CmGameSdk.setGameAccountCallback(this);
        xyxRewardConfig();
    }

    private void xyxRewardConfig() {
        HttpUtils.xyxRewardConfig().enqueue(new Observer<BaseResult<BaoquGamerewardinformation>>() {
            @Override
            public void onSuccess(BaseResult response) {
                if (EmptyUtils.isNotEmpty(response.data)) {
                    BaoquGamerewardinformation data = ((BaoquGamerewardinformation) response.data);
                    state = data.get_limit;
                    second = data.xyx_times_limit_second;
                }
            }
        });
    }

    @Override
    public void gameClickCallback(String gameName, String gameID) {
        TLog.d("BaoquGameActivity", gameID + "----" + gameName);
    }

    /**
     * @param playTimeInSeconds 玩游戏时长，单位为秒
     */
    @Override
    public void gamePlayTimeCallback(String gameId, int playTimeInSeconds) {
        TLog.d("BaoquGameActivity", "play game ：" + gameId + "playTimeInSeconds : " + playTimeInSeconds);
        if (state == 1) {
            return;
        }

        if (playTimeInSeconds >= second) {
            getReward(playTimeInSeconds);
        } else {
            ToastUtils.show("游戏时长不足哦");
        }
    }

    private void getReward(int playTimeInSeconds) {
        HttpUtils.xyxReward(playTimeInSeconds).enqueue(new Observer<BaseResult<BaoquGamereward>>() {
            @Override
            public void onSuccess(BaseResult response) {
                if (EmptyUtils.isNotEmpty(response.data)) {
                    BaoquGamereward data = ((BaoquGamereward) response.data);
                    if (data.hasCount < data.coinRewardLimit) {
                        showVideoReward(data);
                    } else if (data.hasCount == data.coinRewardLimit) {
                        showVideoReward(data);
                        state = 1;
                    }
                }
            }
        });
    }

    /**
     * 广告曝光/点击回调
     *
     * @param gameId   游戏Id
     * @param adType   广告类型：1：激励视频广告；2：Banner广告；3：原生Banner广告；4：全屏视频广告；
     *                 5：原生插屏广告；6：开屏大卡广告；7：模板Banner广告；8：模板插屏广告
     * @param adAction 广告操作：1：曝光；2：点击；3：关闭；4：跳过
     */
    @Override
    public void onGameAdAction(String gameId, int adType, int adAction) {
        TLog.d("BaoquGameActivity", "onGameAdAction gameId: " + gameId + " adType: " + adType + " adAction: " + adAction);
    }

    /**
     * 游戏账号信息回调，需要接入方保存，下次进入或卸载重装后设置给SDK使用，可以支持APP卸载后，游戏信息不丢失
     *
     * @param loginInfo 用户账号信息
     */
    @Override
    public void onGameAccount(String loginInfo) {
        TLog.d("BaoquGameActivity", "onGameAccount loginInfo: " + loginInfo);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        CmGameSdk.removeGameClickCallback();
        CmGameSdk.setMoveView(null);
        CmGameSdk.removeGamePlayTimeCallback();
        CmGameSdk.removeGameAdCallback();
        CmGameSdk.removeGameAccountCallback();

    }

    private void initMoveViewSwitch() {
        CheckBox moveViewSwitch = findViewById(R.id.move_view_switch);
        moveViewSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    initMoveView();
                } else {
                    CmGameSdk.setMoveView(null);
                }
            }
        });
    }

    // 请确保在游戏界面展示前传入目标View，并在豹趣游戏模块关闭后控制自己View的回收避免泄漏
    private void initMoveView() {
        final View view = LayoutInflater.from(this).inflate(R.layout.test_move_layout, null);
        //不能在根布局设置点击事件，否则影响拖动，我们会进行点击回调，在回调中处理即可
        CmGameTopView cmGameTopView = new CmGameTopView(view, new CmGameTopView.CmViewClickCallback() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BaoquGameActivity.this, "这里被点击了", Toast.LENGTH_SHORT).show();
                view.findViewById(R.id.button_two).setVisibility(View.VISIBLE);
            }
        });
        //目前不支持多个可点击View，可以设置类似菜单格式，在顶层View点击后再进行展示，菜单内的点击自己控制，但是记得用完后隐藏
        view.findViewById(R.id.button_two).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.findViewById(R.id.button_two).setVisibility(View.GONE);
            }
        });

        //控件初始位置位于游戏界面右上角,距离顶部100dp，右边距10dp，这里可以进行设定布局格式，参数必须为FrameLayout.LayoutParam
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.END;
        layoutParams.rightMargin = 100; //注意这里是px，记得dp转化
        layoutParams.topMargin = 350;
        cmGameTopView.setLayoutParams(layoutParams);

        // 控件是否可滑动，默认可滑动
        cmGameTopView.setMoveEnable(true);
        // 顶层View是否需要等到游戏加载成功再显示，默认是
        cmGameTopView.setNeedShowAfterGameShow(true);

        //设置屏幕事件监听
        cmGameTopView.setScreenCallback(new CmGameTopView.ScreenEventCallback() {
            @Override
            public void onDrag(MotionEvent event) {
                //TopView被拖拽时候的回调
                TLog.d("BaoquGameActivity", "控件拖拽：" + event.getAction() + ":" + event.getX() + "," + event.getY());
            }

            @Override
            public void onScreenTouch(MotionEvent event) {
                //游戏界面被触摸时候的回调
                TLog.d("BaoquGameActivity", "屏幕点击：" + event.getAction() + ":" + event.getX() + "," + event.getY());
            }
        });
        CmGameSdk.setMoveView(cmGameTopView);
    }

    private void showVideoReward(BaoquGamereward countBean) {
        rewardDialog = new NewCommonDialog(BaoquGameActivity.this, true, true, Gravity.CENTER, 1);
        View contentView = LayoutInflater.from(BaoquGameActivity.this).inflate(R.layout.dialog_show_baoqu_reward, null);
        rewardDialog.setContentView(contentView);
        TextView tvReward;
        TextView tvVideoCount;
        tvReward = contentView.findViewById(R.id.tv_reward);
        tvVideoCount = contentView.findViewById(R.id.tv_video_count);

        double anInt = countBean.coins / 10000.0;
        tvReward.setText(anInt + "");
        tvVideoCount.setText("今日收益" + countBean.hasCount + "/" + countBean.coinRewardLimit + "次");
        rewardDialog.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rewardDialog.dismiss();
            }
        });
        rewardDialog.show();

    }
}
