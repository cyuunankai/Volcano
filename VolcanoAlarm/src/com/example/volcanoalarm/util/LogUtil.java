package com.example.volcanoalarm.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.os.Environment;

public class LogUtil {
	
	public static void appendLog(String text)
	{       
	   
	   File logFile = new File(Environment.getExternalStorageDirectory() + "/volcanoLog.txt");
	   if (!logFile.exists())
	   {
	      try
	      {
	         logFile.createNewFile();
	      } 
	      catch (IOException e)
	      {
	         // TODO Auto-generated catch block
	         e.printStackTrace();
	      }
	   }
	   try
	   {
	      //BufferedWriter for performance, true to set append to file flag
	      BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true)); 
	      buf.append(text);
	      buf.newLine();
	      buf.close();
	   }
	   catch (IOException e)
	   {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
	   }
	}

}
