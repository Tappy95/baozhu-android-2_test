package com.micang.baozhu.http.bean.pay;

public class WithdrawBalanceBean {

    private int balance;
    private boolean isSelected;

    public WithdrawBalanceBean(int balance) {
        this.balance = balance;
    }

    public WithdrawBalanceBean(int balance, boolean isSelected) {
        this.balance = balance;
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}
