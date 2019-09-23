package com.example.zhaojing5.myapplication.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

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
}
