package me.ponktacology.clashmc.backup.util.zip;

import lombok.SneakyThrows;


import java.util.zip.*;
import java.io.*;
import java.util.*;

public class ZipIt
{
    private List<String> fileList;
    private String input;

    public void zipIt( final String input,  final String out) {
        this.fileList = new ArrayList<String>();
        this.input = input;
        this.generateFileList(new File(input));
        final byte[] buffer = new byte[1024];
        final String source = new File(input).getName();
        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        try {
            fos = new FileOutputStream(out);
            zos = new ZipOutputStream(fos);
            FileInputStream in = null;
            for (final String file : this.fileList) {
                final ZipEntry ze = new ZipEntry(source + File.separator + file);
                zos.putNextEntry(ze);
                try {
                    in = new FileInputStream(input + File.separator + file);
                    int len;
                    while ((len = in.read(buffer)) > 0) {
                        zos.write(buffer, 0, len);
                    }
                }
                finally {
                    in.close();
                }
                in.close();
            }
            zos.closeEntry();
        }
        catch (IOException ex) {
            ex.printStackTrace();
            try {
                zos.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        finally {
            try {
                zos.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            zos.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void generateFileList( final File node) {
        if (node.isFile()) {
            this.fileList.add(this.generateZipEntry(node.toString()));
        }
        if (node.isDirectory()) {
            final String[] subNote = node.list();
            String[] array;
            for (int length = (array = subNote).length, i = 0; i < length; ++i) {
                final String filename = array[i];
                this.generateFileList(new File(node, filename));
            }
        }
    }


    private String generateZipEntry( final String file) {
        return file.substring(this.input.length() + 1, file.length());
    }

}