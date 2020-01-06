package com.example.zhaojing5.myapplication.Utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;

/**
 * Created by congtaowang on 2017/10/26.
 */

public class FileUtils {

    public static String read(String filePath ) throws Exception {

        if ( filePath == null ) return null;
        final File file = new File( filePath );
        if ( !file.isFile() || !file.exists() ) return null;
        FileInputStream fis = new FileInputStream( file );
        BufferedReader reader = new BufferedReader( new InputStreamReader( fis ) );
        StringBuilder builder = new StringBuilder();
        String line = "";
        while ( ( line = reader.readLine() ) != null ) {
            builder.append( line ).append( "\n" );
        }
        reader.close();
        fis.close();
        return builder.toString();
    }

    public static Path getPhoneRootPath(Context context) {
        // 是否有SD卡
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)
                || !Environment.isExternalStorageRemovable()) {
            // 获取SD卡根目录
            if (context.getExternalCacheDir() == null) {
                Log.d("zhaojing","=getPhoneRootPath is null=>");
                return context.getCacheDir().toPath();
            }
            return context.getExternalCacheDir().toPath();
        } else {
            // 获取apk包下的缓存路径
            return context.getCacheDir().toPath();
        }
    }
}
