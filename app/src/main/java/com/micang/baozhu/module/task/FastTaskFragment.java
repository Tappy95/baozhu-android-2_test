package com.micang.baozhu.module.task;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.micang.baozhu.R;
import com.micang.baselibrary.base.BaseFragment;

/**
 * @author
 * @version 1.0
 * @E-mail
 * @time 2019/6/24 10:15
 * @describe describe
 */
public class FastTaskFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener {
    private FragmentManager childFragmentManager;
    private RadioGroup rgGroup;
    private RadioButton rtAll;
    private RadioButton rt1;
    private RadioButton rt2;
    private RadioButton rt3;
    private RadioButton rt4;
    private RadioButton rt5;
    private FrameLayout fragmentFramelayout;
    private AllTaskFragment allTaskFragment;
    private Task1Fragment task1Fragment;
    private Task2Fragment task2Fragment;
    private Task3Fragment task3Fragment;
    private Task4Fragment task4Fragment;
    private Task5Fragment task5Fragment;

    @Override
    protected int layoutId() {
        return R.layout.fragment_fast_task;
    }

    @Override
    protected void init(View rootView) {
        rgGroup = rootView.findViewById(R.id.rg_group);
        rtAll = rootView.findViewById(R.id.rt_all);
        rt1 = rootView.findViewById(R.id.rt_1);
        rt2 = rootView.findViewById(R.id.rt_2);
        rt3 = rootView.findViewById(R.id.rt_3);
        rt4 = rootView.findViewById(R.id.rt_4);
        rt5 = rootView.findViewById(R.id.rt_5);
        fragmentFramelayout = rootView.findViewById(R.id.fragment_framelayout);
        childFragmentManager = getChildFragmentManager();
        rgGroup.setOnCheckedChangeListener(this);
        rtAll.setChecked(true);
    }

    @Override
    protected void initData() throws NullPointerException {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        FragmentTransaction fragmentTransaction = childFragmentManager.beginTransaction();
        hideAllFragment(fragmentTransaction);
        switch (checkedId) {
            case R.id.rt_all:
                if (allTaskFragment == null) {
                    allTaskFragment = new AllTaskFragment();
                    fragmentTransaction.add(R.id.fragment_framelayout, allTaskFragment);
                } else {
                    fragmentTransaction.show(allTaskFragment);
                }

                break;
            case R.id.rt_1:
                if (task1Fragment == null) {
                    task1Fragment = new Task1Fragment();
                    fragmentTransaction.add(R.id.fragment_framelayout, task1Fragment);
                } else {
                    fragmentTransaction.show(task1Fragment);
                }
                break;
            case R.id.rt_2:
                if (task2Fragment == null) {
                    task2Fragment = new Task2Fragment();
                    fragmentTransaction.add(R.id.fragment_framelayout, task2Fragment);
                } else {
                    fragmentTransaction.show(task2Fragment);
                }
                break;
            case R.id.rt_3:
                if (task3Fragment == null) {
                    task3Fragment = new Task3Fragment();
                    fragmentTransaction.add(R.id.fragment_framelayout, task3Fragment);
                } else {
                    fragmentTransaction.show(task3Fragment);
                }
                break;
            case R.id.rt_4:
                if (task4Fragment == null) {
                    task4Fragment = new Task4Fragment();
                    fragmentTransaction.add(R.id.fragment_framelayout, task4Fragment);
                } else {
                    fragmentTransaction.show(task4Fragment);
                }
                break;
            case R.id.rt_5:
                if (task5Fragment == null) {
                    task5Fragment = new Task5Fragment();
                    fragmentTransaction.add(R.id.fragment_framelayout, task5Fragment);
                } else {
                    fragmentTransaction.show(task5Fragment);
                }
                break;
        }
        fragmentTransaction.commit();
    }

    public void hideAllFragment(FragmentTransaction transaction) {
        if (allTaskFragment != null) {
            transaction.hide(allTaskFragment);
        }
        if (task1Fragment != null) {
            transaction.hide(task1Fragment);
        }
        if (task2Fragment != null) {
            transaction.hide(task2Fragment);
        }
        if (task3Fragment != null) {
            transaction.hide(task3Fragment);
        }
        if (task4Fragment != null) {
            transaction.hide(task4Fragment);
        }
        if (task5Fragment != null) {
            transaction.hide(task5Fragment);
        }
    }

}
