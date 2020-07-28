package org.jeecg;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by fengjiening on 2020/7/16.
 */
public class testF {

    public static void main (String [] a ) throws Exception{
        String id="20180917-6";
        String type="afr";
        String savePath = "/home/jietong/kaoqi/my.png";
        //String savePath = "c:/my.png";

        File file = new File(savePath);
        InputStream inputFile = new FileInputStream(file);

        String path = "/home/jietong/kaoqi/data/";
        String newDay = "2020-10-05";
        System.out.println(newDay);

        File dir1 = new File(path+newDay+"/"+id);
        if (!dir1.isDirectory()) {
            System.out.println("时间索引文件不存在 to 创建");
            dir1.mkdirs();
        }
        String newFileName="afr".equals(type)?id+"_"+type+".png":id+"_"+type+".mp3";
        //保存音頻
        String filePath =  path+newDay+"/"+id+"/"+newFileName;

        FileOutputStream downloadFile=null;
        try {
            int index;
            byte[] bytes = new byte[1024];
            downloadFile= new FileOutputStream(filePath);
            while ((index = inputFile.read(bytes)) != -1) {
                downloadFile.write(bytes, 0, index);
                downloadFile.flush();
            }

        }catch(Exception e){
            e.printStackTrace();
        }finally {
            try {
                if(downloadFile!=null)downloadFile.close();
                if(inputFile!=null)inputFile.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
