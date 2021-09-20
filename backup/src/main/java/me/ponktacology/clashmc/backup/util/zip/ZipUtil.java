package me.ponktacology.clashmc.backup.util.zip;

import lombok.SneakyThrows;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipUtil {

    public static void zip( String input,  String out) {
        ZipIt zipIt = new ZipIt();

        zipIt.zipIt(input, out);
    }

    @SneakyThrows
    public static void unzip( String input, File out) {

        try (ZipInputStream zip = new ZipInputStream(new FileInputStream(input))) {
            ZipEntry entry;

            while ((entry = zip.getNextEntry()) != null) {
                File file = new File(out, entry.getName());


                if (entry.isDirectory()) {
                    file.mkdirs();
                    continue;
                }

                byte[] buffer = new byte[4096];
                file.getParentFile().mkdirs();
                BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(file));
                int count;

                while ((count = zip.read(buffer)) != -1) {
                    output.write(buffer, 0, count);
                }

                output.close();
            }
        }
    }
}
