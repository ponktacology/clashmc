package me.ponktacology.loader.plugin;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

@Slf4j
@RequiredArgsConstructor
public class SecureDownloader {

  private final String url;

  @SneakyThrows
  public File download() {
    String fileURL = this.url;
    File tempFile = File.createTempFile("temp", ".jar");

    URLConnection connection = new URL(fileURL).openConnection();
    connection.setConnectTimeout(60_000);
    connection.setReadTimeout(60_000);
    connection.setRequestProperty("secret-key", "loltestXD!");
    InputStream input = connection.getInputStream();
    FileUtils.copyInputStreamToFile(input, tempFile);

    return tempFile;
  }
}
