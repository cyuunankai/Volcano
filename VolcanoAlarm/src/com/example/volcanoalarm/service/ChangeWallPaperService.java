package com.example.volcanoalarm.service;

import java.io.IOException;

import android.app.IntentService;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;

import com.caldroid.R;
import com.example.volcanoalarm.util.DateUtil;
import com.example.volcanoalarm.util.FileUtil;
import com.example.volcanoalarm.util.LogUtil;

public class ChangeWallPaperService extends IntentService {
    
    Handler mMainThreadHandler = null;
    
    /** 
     * A constructor is required, and must call the super IntentService(String)
     * constructor with a name for the worker thread.
     */
    public ChangeWallPaperService() {
        super("ChangeWallPaperService");
        
        mMainThreadHandler = new Handler();
    }

    /**
     * The IntentService calls this method from the default worker thread with
     * the intent that started the service. When this method returns, IntentService
     * stops the service, as appropriate.
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        mMainThreadHandler.post(new Runnable() {
              @Override
              public void run() {
                  LogUtil.appendLog(DateUtil.getSysTimeStr() + "execute change wallPaper service");
                  
                  WallpaperManager myWallpaperManager = WallpaperManager.getInstance(getApplicationContext());
                  Bitmap bm = ((BitmapDrawable)myWallpaperManager.getDrawable()).getBitmap();
                  String fileName = getApplicationContext().getFilesDir() + "/userWallPaper.jpg";
                  FileUtil.saveToInternalStorage(fileName, bm);
                  
                  try {
                      myWallpaperManager.setResource(com.example.volcanoalarm.R.drawable.wallpaper);
                  } catch (IOException e) {
                      LogUtil.appendLog("set wallPaper error : " + e.getMessage());
                  }
                  
              }
          });
    }
    
    

}
