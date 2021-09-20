package me.ponktacology.clashmc.backup.util;



import java.io.File;
import java.util.Objects;

public class FileUtil {

  public static void deleteRecursive( File path) {
    if (!path.exists()) {
      return;
    }

    for (File file : Objects.requireNonNull(path.listFiles())) {
      if (file.isDirectory()) {
        deleteRecursive(file);
      } else {
        file.delete();
      }
    }
  }
}
