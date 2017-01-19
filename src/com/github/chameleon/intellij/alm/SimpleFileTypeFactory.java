package com.github.chameleon.intellij.alm;

import com.intellij.openapi.fileTypes.*;
import org.jetbrains.annotations.NotNull;

public class SimpleFileTypeFactory extends FileTypeFactory {
    @Override
    public void createFileTypes(@NotNull FileTypeConsumer fileTypeConsumer) {
        fileTypeConsumer.consume(SimpleFileType.INSTANCE, "vb");
        fileTypeConsumer.consume(SimpleFileType.INSTANCE, new ExactFileNameMatcher("Config"));
        fileTypeConsumer.consume(SimpleFileType.INSTANCE, new ExactFileNameMatcher("MainActivity.java"));
        fileTypeConsumer.consume(SimpleFileType.INSTANCE, new ExactFileNameMatcher("activity_main.xml"));
//        fileTypeConsumer.consume(SimpleFileType.INSTANCE, "xml");
//        fileTypeConsumer.consume(SimpleFileType.INSTANCE, "java");
        fileTypeConsumer.consume(SimpleFileType.INSTANCE, "chameleon");
    }
}