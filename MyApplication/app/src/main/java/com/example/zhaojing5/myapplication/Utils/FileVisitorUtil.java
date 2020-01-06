package com.example.zhaojing5.myapplication.Utils;

import android.util.Log;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * created by zhaojing 2019/12/30 上午10:59
 */
public class FileVisitorUtil extends SimpleFileVisitor<Path> {

    private void find(Path path){
        //%n-换行符
        Log.d("zhaojing", String.format( "访问-%s:%s%n", (Files.isDirectory(path) ? "目录" : "文件"), path.getFileName() ) );
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        find(dir);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        find(file);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        exc.printStackTrace();
        return FileVisitResult.CONTINUE;
    }
}
