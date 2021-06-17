package com.welb.organization_check.controller;

import com.welb.util.DateUtil;
import com.welb.util.LogUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @author luoyaozu
 * @title: FileUploadController
 * @projectName xh-360appraisal-interface
 * @description: 附件上传接口
 * @date 2019/5/3022:57
 */
@RestController
@RequestMapping("/summaryattachment")
public class FileUploadController {
    private final Logger log = LogManager.getLogger(this.getClass());
    public static final String path="C://";
    @RequestMapping(value = "/upload", produces = "application/json;charset=utf-8")
    public Object uploadFile(@RequestParam("file") MultipartFile file, HttpServletRequest req) {
        ModelMap map = new ModelMap();
        //获取YYYYMMDD格式的日期
        String days = DateUtil.getDays();
        //获取yyyyMMddHHmmss格式的日期
        String times = DateUtil.getSdfTimes();
        //获取路径
        String realPath = req.getContextPath() + File.separator + "attachment" + File.separator + "file";
        try {
            //创建文件夹
            File floder = new File(realPath + File.separator + days + File.separator + times);
            if (!floder.isDirectory()) {
                floder.mkdirs();
            }
            //获取文件名称
            String filename = file.getOriginalFilename();

            //保存文件
            File newfile = new File(floder + File.separator + filename);
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(String.valueOf(newfile))));
            out.write(file.getBytes());
            out.flush();
            out.close();
            //文件保存路径
            String savepath = newfile.getPath().replaceAll("\\\\", "/");
            map.put("filename", filename);
            map.put("savepath", savepath);
            map.put("msg", "文件上传成功");
            map.put("code", 0);

        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("msg", "文件上传失败");
            map.put("code", 1);
        }
        return map;
    }

    /**
     * 下载附件
     *
     * @param response
     * @param savepath
     * @return
     */
    @RequestMapping(value = "/download", produces = "application/json;charset=utf-8")
    public Object download(HttpServletResponse response, String savepath) {
        ModelMap map = new ModelMap();

        if (savepath != null) {
            //设置文件路径
            File file = new File(path, savepath);
            if (file.exists()) {
                response.setContentType("application/force-download");// 设置强制下载不打开
                // 文件名转码一下，不然会出现中文乱码
                try {
                    response.addHeader("Content-Disposition", "attachment;fileName=" + new String(file.getName().getBytes("utf-8"), "ISO8859-1"));// 设置文件名
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                    map.put("msg", "下载成功");
                    map.put("code", "0");
                } catch (Exception e) {
                    log.error(LogUtil.getTrace(e));
                    map.put("msg", "下载失败");
                    map.put("code", "1");
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return map;
    }


    /**
     * 图片下载
     *
     * @param file
     * @param req
     * @return
     */
    @RequestMapping(value = "/uploadPicture", produces = "application/json;charset=utf-8")
    public Object uploadPicture(@RequestParam("file") MultipartFile file, HttpServletRequest req) {
        ModelMap map = new ModelMap();
        //获取YYYYMMDD格式的日期
        String days = DateUtil.getDays();
        //获取yyyyMMddHHmmss格式的日期
        String times = DateUtil.getSdfTimes();
        String path = req.getSession().getServletContext().getRealPath("/");
        String path1 = path.substring(0, path.lastIndexOf("360kao"));

        //获取路径
        String realPath = path1 + "360check" + File.separator + "picture" + File.separator + "file";
        try {
            //创建文件夹
            File floder = new File(realPath + File.separator + days + File.separator + times);
            if (!floder.isDirectory()) {
                floder.mkdirs();
            }
            //获取文件名称
            String filename = file.getOriginalFilename();

            //保存文件
            File newfile = new File(floder + File.separator + filename);
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(String.valueOf(newfile))));
            out.write(file.getBytes());
            out.flush();
            out.close();
            //文件保存路径
            String picturepath = newfile.getPath().replaceAll("\\\\", "/");
            int i = picturepath.lastIndexOf("picture");
            picturepath = picturepath.substring(i, picturepath.length());
            if (filename.contains(".jpg") || filename.contains(".png") || filename.contains(".jpeg")) {
                if (file.getSize() > 10240000) {
                    map.put("msg", "图片过大,请重新上传");
                    map.put("code", 1);
                } else {
                    map.put("picturepath", picturepath);
                    map.put("msg", "图片上传成功");
                    map.put("code", 0);
                }
            } else {
                map.put("msg", "不支持该格式图片上传，请上传jpg、jpeg、png格式的图片");
                map.put("code", 1);
            }
        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("msg", "图片上传失败");
            map.put("code", 1);
        }
        return map;
    }

   /* @RequestMapping("/deleteFile")
    public Object deleteFile(String filePath) {
        ModelMap map = new ModelMap();
        filePath = "C:\\picture";
        int i1 = filePath.lastIndexOf("\\");
      //  String substring = filePath.substring(0, filePath.length() - filePath.substring(i1).length());
        //文件保存路径
//        String path="D:\\apache-tomcat-8.5.40\\webapps\\"+filePath;//119服务器文件保存路径
        String path=filePath;
//        String path = "C:\\soft\\apache-tomcat-8.5.42\\webapps\\" + filePath;

        try {
            File file = new File(path);
            deleteDir(file);
            map.put("msg", "删除成功");
            map.put("code", 0);
        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("msg", "删除失败");
            map.put("code", 1);
            map.put("error", e.getMessage());

        }
        return map;
    }


    public static void deleteDir(File f) {
        //数组指向文件夹中的文件和文件夹
        File[] fi = f.listFiles();
        //遍历文件和文件夹
        if (fi.length>0) {
            for (File file : fi) {
                //如果是文件夹，递归查找
                if (file.isDirectory())
                    deleteDir(file);
                else if (file.isFile()) {
                    file.delete();
                } else {
                    file.delete();
                }
            }
        }else {

        }
    }*/

}
