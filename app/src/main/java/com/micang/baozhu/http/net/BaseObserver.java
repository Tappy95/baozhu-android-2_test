package com.micang.baozhu.http.net;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class BaseObserver<T> implements Callback<T> {
    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        onNext(call,response);
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        onFail(call,t);
    }

    abstract void onNext(Call<T> call, Response<T> response);
    abstract void onFail(Call<T> call, Throwable t);
}
