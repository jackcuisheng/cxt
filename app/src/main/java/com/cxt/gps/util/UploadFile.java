package com.cxt.gps.util;

import android.content.Context;

import com.nanchen.compresshelper.CompressHelper;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UploadFile {

    public static String httpPost(Context context, String urlstr, String uploadFile, String newName) {
        String response = "";
//		String end = "\r\n";
//		String twoHyphens = "--";
//		String boundary = "*****";//边界标识
        int TIME_OUT = 10*1000;   //超时时间
        HttpURLConnection con = null;
        DataOutputStream ds = null;
        InputStream is = null;
        try {
            URL url = new URL(urlstr);
            con = (HttpURLConnection) url.openConnection();
            con.setReadTimeout(TIME_OUT);
            con.setConnectTimeout(TIME_OUT);
            /* 允许Input、Output，不使用Cache */
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);

            // 设置http连接属性
            con.setRequestMethod("POST");//请求方式
            con.setRequestProperty("Connection", "Keep-Alive");//在一次TCP连接中可以持续发送多份数据而不会断开连接
            con.setRequestProperty("Charset", "UTF-8");//设置编码
            con.setRequestProperty("Content-Type",//multipart/form-data能上传文件的编码格式
                    "application/x-www-form-urlencoded");

            ds = new DataOutputStream(con.getOutputStream());
//			ds.writeBytes(twoHyphens + boundary + end);
//			ds.writeBytes("Content-Disposition: form-data; "
//					+ "name=\"stblog\";filename=\"" + newName + "\"" + end);
//			ds.writeBytes(end);

            //压缩图片
            File oldFile = new File(uploadFile);
            //Bitmap bitmap = Light.getInstance().compress(uploadFile);
            File newFile = CompressHelper.getDefault(context).compressToFile(oldFile);
            // 取得文件的FileInputStream
            FileInputStream fStream = new FileInputStream(newFile);

            /* 设置每次写入1024bytes */
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int length = -1;
            /* 从文件读取数据至缓冲区 */
            while ((length = fStream.read(buffer)) != -1) {
                /* 将资料写入DataOutputStream中 */
                ds.write(buffer, 0, length);
            }
            //ds.writeBytes(end);
            //ds.writeBytes(twoHyphens + boundary + twoHyphens + end);//结束

            fStream.close();
            ds.flush();
            /* 取得Response内容 */
            is = con.getInputStream();
            int ch;
            StringBuffer b = new StringBuffer();
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
            }
            /* 将Response显示于Dialog */
//			showDialog(activity,true,uploadFile,"上传成功" + b.toString().trim());
            response = b.toString().trim();
        } catch (Exception e) {
            //ToastUtil.show(BindCarInfo.this,"");
        }finally {
            /* 关闭DataOutputStream */
            if(ds!=null){
                try {
                    ds.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (con != null) {
                con.disconnect();
            }
        }
        return response;
    }
}
