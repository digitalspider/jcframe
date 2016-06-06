package au.com.javacloud.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;


public class LinuxUtil
{
    private static final Logger LOG = Logger.getLogger(LinuxUtil.class);

    public static String executeLinuxCmd(String cmd) throws IOException {
        String[] cmdArray = { "/bin/sh", "-c", cmd };
        LOG.debug("cmd="+cmdArray[2]);
        Process p = Runtime.getRuntime().exec(cmdArray);
//        String output = IOUtils.toString(p.getInputStream());
//        LOG.debug("output="+output);
//        return output;
        return null;
    }

    public static InputStream executeLinuxCmdAsStream(String cmd) throws IOException {
        String[] cmdArray = { "/bin/sh", "-c", cmd };
        LOG.debug("cmd="+cmdArray[2]);
        Process p = Runtime.getRuntime().exec(cmdArray);
        return p.getInputStream();
    }

    public static int getLinesCountForFile(File readFile) throws Exception {
        String cmd = "wc -l " + readFile+" | cut -f 1 -d ' '";
        String output = executeLinuxCmd(cmd);
        if (output.trim().length()>0) {
            return Integer.parseInt(output.trim());
        }
        return 0;
    }

    public static List<String> getLastNLogLines(File readFile, int batchSize, int linesToRead) {
        List<String> content = new ArrayList<String>();
        InputStream is = null;
        try {
            String cmd = "tail -" + linesToRead + " "+readFile+" | head -"+batchSize;
            is = executeLinuxCmdAsStream(cmd);
            BufferedReader input = new BufferedReader(new java.io.InputStreamReader(is));
            String line = null;
            while ((line = input.readLine()) != null) {
                content.add(0,line);
            }
        } catch (java.io.IOException e) {
            LOG.error(e,e);
        } finally {
            if (is!=null) {
                try {
                    is.close();
                } catch (IOException e) {
                    LOG.error(e,e);
                }
            }
        }
        return content;
    }


    public static String getHost(String url) {
        int startIndex = url.indexOf("://");
        if (startIndex>0) {
            startIndex+=3;
            int endIndex = url.indexOf("/",startIndex);
            if (endIndex>startIndex) {
                String currentHost = url.substring(startIndex,endIndex);
                return currentHost;
            }
        }
        return null;
    }

    public static String getTextFileName(String filename) {
        int index = filename.lastIndexOf(".");
        filename = filename.substring(0,index);
        return filename+".txt";
    }

    public static String getFileExtension(String filename) {
        int index = filename.lastIndexOf(".");
        return filename.substring(index+1);
    }

    public static String firstCharUpperCase(String input) {
        return input.substring(0, 1).toUpperCase()+input.substring(1);
    }

}