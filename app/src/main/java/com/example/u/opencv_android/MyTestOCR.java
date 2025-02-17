package com.example.u.opencv_android;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class MyTestOCR {

    private String datapath;
    private TessBaseAPI mTess;
    Context context;

    MyTestOCR(Context context) {
        // TODO Auto-generated constructor stub
        this.context = context;
        datapath = Environment.getExternalStorageDirectory() + "/ocrctz/";
        File dir = new File(datapath + "/tessdata/");
        File file = new File(datapath + "/tessdata/" + "eng.traineddata");
        if (!file.exists()) {
            Log.d("mylog", "in file doesn't exist");
            dir.mkdirs();
            copyFile(context);
        }

        mTess = new TessBaseAPI();
        String language = "eng";

        mTess.setPageSegMode(TessBaseAPI.PageSegMode.PSM_SINGLE_BLOCK);
        mTess.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST,"<0123456789");
        mTess.setVariable(TessBaseAPI.VAR_CHAR_BLACKLIST,".:;'@#$%^&*()_- \" !ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmopqrstuvwxyz");
        mTess.init(datapath, language);
    }

    public void stopRecognition() {
        mTess.stop();
    }

    String getOCRResult(Bitmap bitmap) {
        mTess.setImage(bitmap);
        return mTess.getUTF8Text();
    }

    public void onDestroy() {
        if (mTess != null)
            mTess.end();
    }

    private void copyFile(Context context) {
        AssetManager assetManager = context.getAssets();
        try {
            InputStream in = assetManager.open("eng.traineddata");
            OutputStream out = new FileOutputStream(datapath + "/tessdata/" + "eng.traineddata");
            byte[] buffer = new byte[1024];
            int read = in.read(buffer);
            while (read != -1) {
                out.write(buffer, 0, read);
                read = in.read(buffer);
            }
        } catch (Exception e) {
            Log.d("mylog", "couldn't copy with the following error : "+e.toString());
        }
    }
}