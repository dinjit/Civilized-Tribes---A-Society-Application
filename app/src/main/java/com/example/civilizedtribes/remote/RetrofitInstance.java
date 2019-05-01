package com.example.civilizedtribes.remote;


import com.example.civilizedtribes.interfaces.IApiService;
import com.example.civilizedtribes.utils.NetworkConstant;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
    private static Retrofit retrofit = null;
    public static Retrofit getRetrofitInstance() {
        Retrofit retrofit = null;
        OkHttpClient.Builder httpClient2 = new OkHttpClient.Builder();
        httpClient2.addNetworkInterceptor(new Interceptor() {

            @Override
            public Response intercept(Chain chain) throws IOException {
                Request.Builder builder = chain.request().newBuilder();
                return chain.proceed(builder.build());
            }
        });
        OkHttpClient client = httpClient2.build();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkConstant.BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public IApiService getApiService() {
        return getRetrofitInstance().create(IApiService.class);
    }
}
