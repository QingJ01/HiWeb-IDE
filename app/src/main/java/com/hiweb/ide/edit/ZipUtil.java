package com.hiweb.ide.edit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author leon
 * @createDate 2019年2月12日 下午1:57:06
 * @version v1.0
 * @classRemarks 压缩zip方法
 */
public class ZipUtil {
    private static final int BUFFER_SIZE = 2 * 1024;

    /**
     * 压缩成ZIP 方法1
     * 
     * @param srcDir
     *                         压缩文件夹路径
     * @param out
     *                         压缩文件输出流
     * @param KeepDirStructure
     *                         是否保留原来的目录结构,true:保留目录结构;
     *                         false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
     * @throws RuntimeException
     *                          压缩失败会抛出运行时异常
     */
    public static void toZip(String srcDir, OutputStream out, boolean KeepDirStructure) throws Exception {

        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(out);
            File sourceFile = new File(srcDir);
            compress(sourceFile, zos, null, KeepDirStructure);

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new Exception(e);
                }
            }
        }
    }

    /**
     * 压缩成ZIP 方法2
     * 
     * @param srcFiles
     *                 需要压缩的文件列表
     * @param out
     *                 压缩文件输出流
     * @throws RuntimeException
     *                          压缩失败会抛出运行时异常
     */
    public static void toZip(List<File> srcFiles, OutputStream out) throws Exception {

        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(out);
            for (File srcFile : srcFiles) {
                byte[] buf = new byte[BUFFER_SIZE];
                zos.putNextEntry(new ZipEntry(srcFile.getName()));
                int len;
                FileInputStream in = new FileInputStream(srcFile);
                while ((len = in.read(buf)) != -1) {
                    zos.write(buf, 0, len);
                }
                zos.closeEntry();
                in.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new Exception(e);
                }
            }
        }
    }

    /**
     * 压缩成ZIP 方法2
     * 
     * @param srcFiles
     *                 需要压缩的文件列表
     * @param out
     *                 压缩文件输出流
     * @throws RuntimeException
     *                          压缩失败会抛出运行时异常
     */
    public static void toZip(File[] srcFiles, OutputStream out) throws Exception {

        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(out);
            for (File srcFile : srcFiles) {
                byte[] buf = new byte[BUFFER_SIZE];
                zos.putNextEntry(new ZipEntry(srcFile.getName()));
                int len;
                FileInputStream in = new FileInputStream(srcFile);
                while ((len = in.read(buf)) != -1) {
                    zos.write(buf, 0, len);
                }
                zos.closeEntry();
                in.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new Exception(e);
                }
            }
        }
    }

    /**
     * 
     * 递归压缩方法
     * 
     * @param sourceFile
     *                         源文件
     * 
     * @param zos
     *                         zip输出流
     * 
     * @param name
     *                         压缩后的名称
     * 
     * @param KeepDirStructure
     *                         是否保留原来的目录结构,true:保留目录结构;
     * 
     *                         false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
     * 
     * @throws Exception
     * 
     */
    private static void compress(File sourceFile, ZipOutputStream zos, String name,
            boolean KeepDirStructure) throws Exception {
        byte[] buf = new byte[BUFFER_SIZE];
        try {
            if (sourceFile.isFile()) {
                // 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
                zos.putNextEntry(new ZipEntry(name));
                // copy文件到zip输出流中
                int len;
                FileInputStream in = new FileInputStream(sourceFile);
                while ((len = in.read(buf)) != -1) {
                    zos.write(buf, 0, len);
                }
                // Complete the entry

                in.close();
            } else {
                File[] listFiles = sourceFile.listFiles();
                if (listFiles == null || listFiles.length == 0) {
                    // 需要保留原来的文件结构时,需要对空文件夹进行处理
                    if (KeepDirStructure) {
                        // 空文件夹的处理
                        zos.putNextEntry(new ZipEntry(name == null ? "" : (name + "/")));
                        // 没有文件，不需要文件的copy

                    }
                } else {
                    for (File file : listFiles) {
                        // 判断是否需要保留原来的文件结构
                        if (KeepDirStructure) {
                            // 注意：file.getName()前面需要带上父文件夹的名字加一斜杠,
                            // 不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了
                            compress(file, zos, (name == null ? "" : (name + "/")) + file.getName(), KeepDirStructure);
                        } else {
                            compress(file, zos, file.getName(), KeepDirStructure);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        } finally {

        }
    }
}
