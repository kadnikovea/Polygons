package com.k4dnikov.addpolygon;

import android.app.Application;
import android.content.res.AssetManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.k4dnikov.addpolygon.common.AppConstants;
import com.k4dnikov.addpolygon.common.HttpResponseMockCreator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.net.ssl.HttpsURLConnection;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {

    private static App instance;

    public static Retrofit sRetrofit;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initRealm();
        initRetrofit();
    }

    private void initRetrofit() {

        Gson gson = new GsonBuilder().create();

        MockIntercepter mockIntercepter = new MockIntercepter();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(mockIntercepter)
                .build();

        sRetrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL)
//                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

    }

    private void initRealm() {
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(config);
    }

    public static App getInstance() {
        return instance;
    }

    public static Retrofit getRetrofit() {
        return sRetrofit;
    }

    class MockIntercepter implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            if (BuildConfig.DEBUG) {
                String uri = chain.request().url().uri().toString();
                String response = HttpResponseMockCreator.createMock(instance, uri);

                return chain.proceed(chain.request())
                        .newBuilder().code(HttpsURLConnection.HTTP_OK)
                        .protocol(Protocol.HTTP_2)
                        .message(response)
                        .body(ResponseBody.create(MediaType.parse("application/json"),
                                response.getBytes()))
                        .addHeader("content-type", "application/json")
                        .build();

            } else {
                throw new IllegalAccessError("Interceptor for debug purpose only");
            }
        }
    }
}
