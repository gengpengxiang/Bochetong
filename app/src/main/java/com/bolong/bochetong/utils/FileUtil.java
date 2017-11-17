package com.bolong.bochetong.utils;

import java.io.File;

public class FileUtil {

    public static boolean isExists(String strPath) {
        if (strPath == null) {
            return false;
        }

        final File strFile = new File(strPath);

        if (strFile.exists()) {
            return true;
        }
        return false;

    }
}
