package com.k4dnikov.addpolygon;

import android.app.Application;
import android.content.res.AssetManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.k4dnikov.addpolygon.common.AppConstants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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

import static com.k4dnikov.addpolygon.common.AppConstants.SUCCESS_CODE;

public class App extends Application {

    public static App instance;

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
            if(BuildConfig.DEBUG){
                String uri = chain.request().url().uri().toString();
                String response;
                if(uri.contains("/get_markers")){

                    AssetManager assetManager = getAssets();
                    InputStream is = assetManager.open("markerResponse.json");
                    response = convertStreamToString(is);
                }else {
                    response = "";
                }

            return chain.proceed(chain.request())
                    .newBuilder().code(SUCCESS_CODE)
                    .protocol(Protocol.HTTP_2)
                    .message(response)
                    .body(ResponseBody.create(MediaType.parse("application/json"),
                            response.getBytes()))
                    .addHeader("content-type", "application/json")
                    .build();

            }else {
                throw new IllegalAccessError("Interceptor for debug purpose only");
            }

        }

        private String convertStreamToString(InputStream is) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();

            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append('\n');
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return sb.toString();
        }

    }
}
