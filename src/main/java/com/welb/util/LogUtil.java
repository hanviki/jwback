//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.welb.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

public class LogUtil {
    private static FileWriter ralation = null;
    private static FileWriter huizong = null;
    private static FileWriter pingfen = null;

    public LogUtil() {
    }

    public static Log getLogger() {
        return LogFactory.getLog("");
    }

    public static void main(String[] args) {
        getLogger();
    }

    public static void writeralationlog(String log) {
        try {
            Date d = new Date();
            ralation.write(d + "-----" + log + "\n");
            ralation.flush();
        } catch (IOException var2) {
            var2.printStackTrace();
        }

    }

    public static void writehuizonglog(String log) {
        try {
            Date d = new Date();
            huizong.write(d + "-----" + log + "\n");
            huizong.flush();
        } catch (IOException var2) {
            var2.printStackTrace();
        }

    }

    public static void writepingfenlog(String log) {
        try {
            Date d = new Date();
            pingfen.write(d + "-----" + log + "\n");
            pingfen.flush();
        } catch (IOException var2) {
            var2.printStackTrace();
        }

    }

    public static String getTrace(Throwable e){
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        StringBuffer sb = sw.getBuffer();
        return sb.toString();
    }
}
