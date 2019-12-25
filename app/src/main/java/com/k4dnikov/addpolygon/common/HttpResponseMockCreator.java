package com.k4dnikov.addpolygon.common;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HttpResponseMockCreator  {

    public static String createMock(Context ctx, String uri) throws IOException {
        if(uri.contains("/get_markers")){
            AssetManager assetManager = ctx.getAssets();
            InputStream is = assetManager.open("markerResponse.json");
            return convertStreamToString(is);
        }else {
            return "";
        }
    }

    private static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
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
