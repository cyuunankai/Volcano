package com.example.volcanoalarm.service;

import java.io.IOException;

import android.app.IntentService;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Handler;

import com.example.volcanoalarm.util.DateUtil;
import com.example.volcanoalarm.util.LogUtil;

public class ReturnUserWallPaperService extends IntentService {
    
    Handler mMainThreadHandler = null;
    
    /** 
     * A constructor is required, and must call the super IntentService(String)
     * constructor with a name for the worker thread.
     */
    public ReturnUserWallPaperService() {
        super("ReturnUserWallPaperService");
        
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
                  LogUtil.appendLog(DateUtil.getSysTimeStr() + "execute return to wallPaper service");
                  WallpaperManager myWallpaperManager = WallpaperManager.getInstance(getApplicationContext());
                  String fileName = getApplicationContext().getFilesDir() + "/userWallPaper.jpg";
                  
                  try {
                      myWallpaperManager.setBitmap(BitmapFactory.decodeFile(fileName));
                  } catch (IOException e) {
                      LogUtil.appendLog("return to wallPaper error : " + e.getMessage());
                  }
                  
              }
          });
    }
    
    

}
