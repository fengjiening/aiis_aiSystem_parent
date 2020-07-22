package org.jeecg;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.constant.SystemConstant;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.util.CommonMethod;
import org.jeecg.common.util.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.concurrent.*;

/**
 * Created by fengjiening on 2020/6/28.
 */

@Slf4j
public class mainTees {
    String path="c:\\kaoqi\\";
    @Test
    public synchronized void testExecutorService2() {

        String s = null;
        try {
            String savePath = "c:/pcm/34796210.pcm";
            s = CommonMethod.encodeBase64File(savePath);
            InputStream vStream =  CommonMethod.decoderBase64ToStream(s);

            s("11111","vpr",vStream);

        } catch (Exception e) {
            e.printStackTrace();
        }



    }
    @Test
    public void testDataLogSave() throws Exception {
        String id="20180911-9";
        String type="afr";
        String savePath = "e://home//jietong//kaoqi//data";

        File file = new File("c://face1.png");
        InputStream inputFile = new FileInputStream(file);

        String path = savePath+"\\";
        String newDay =  DateUtils.formatDate();
        System.out.println(newDay);
//        File dir = new File(path+newDay);
//        if (!dir.isDirectory()) {
//            System.out.println("用户ID索引文件不存在 to 创建");
//            dir.mkdirs();
//            System.out.println("创建文件夹");
//        }
        File dir1 = new File(path+newDay+"\\"+id);
        if (!dir1.isDirectory()) {
            System.out.println("时间索引文件不存在 to 创建");

            dir1.setWritable(true, false);
            dir1.mkdirs();
        }
        String newFileName="afr".equals(type)?id+"_"+type+".png":id+"_"+type+".mp3";
        //保存音頻
        String filePath =  path+newDay+"\\"+id+"\\"+newFileName;
        CommonMethod.writeToLocal(filePath,inputFile);
    }

    private void s(String id,String type, InputStream vStream ){
        String newDay =  DateUtils.formatDate();
        System.out.println(newDay);
        File dir = new File(path+id);
        if (!dir.isDirectory()) {
            System.out.println("文件夹不存在");
            dir.mkdir();
            System.out.println("创建文件夹");
        }
        File dir1 = new File(path+id+"\\"+newDay);
        if (!dir1.isDirectory()) {
            System.out.println("文件夹不存在1");
            dir1.mkdir();
            System.out.println("创建文件夹1");
        }
       String newFileName="afr".equals(type)?id+"_"+type+".png":id+"_"+type+".mp3";
        //保存音頻
        CommonMethod.writeToLocal(path+id+"\\"+newDay+"\\"+newFileName,vStream);

    }
    @Test
    public synchronized void testExecutorService1() {
        System.err.println(DateUtils.formatDate());
        System.err.println(DateUtils.formatDateTime());

    }
    @Test
    public synchronized void testExecutorService() {
//        int corePoolSize,
//        int maximumPoolSize,
//        long keepAliveTime,
//        TimeUnit unit,
//        BlockingQueue<Runnable> workQueue,
//        ThreadFactory threadFactory,
//        RejectedExecutionHandler handler
        RejectedExecutionHandler handler = new MyIgnorePolicy();
        ThreadFactory build = new ThreadFactoryBuilder().setNameFormat("name-th%d").setUncaughtExceptionHandler(new ErrHandler("ddd")).build();
        ThreadPoolExecutor threadPool =new  ThreadPoolExecutor(1,1,1, TimeUnit.HOURS,new ArrayBlockingQueue(1),build, handler);
        threadPool.execute(new t("1111111"));
        threadPool.execute(new t("2222222"));
        threadPool.execute(new t("3333333"));
        threadPool.shutdown();
    }
    public static class MyIgnorePolicy implements RejectedExecutionHandler {

        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            doLog(r, e);
        }

        private void doLog(Runnable r, ThreadPoolExecutor e) {
            // 可做日志记录等
            System.err.println( r.toString() + " rejected");
//          System.out.println("completedTaskCount: " + e.getCompletedTaskCount());
        }
    }
    //author:lvdandan-----date：20190315---for:添加数据日志测试----
}
class t implements Runnable{
    String a1;
    public t(String a ){
        a1=a;
    }
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName());
        System.out.println("***********************************"+a1);
        throw new JeecgBootException(Thread.currentThread().getName()+"开始报错");
    }
}
/**
 * 自定义的一个UncaughtExceptionHandler
 */
class ErrHandler implements Thread.UncaughtExceptionHandler {
    String a;
    public ErrHandler(String a){
        this.a=a;
    }
    /**
     * 这里可以做任何针对异常的处理,比如记录日志等等
     */
    public void uncaughtException(Thread a, Throwable e) {
        System.out.println("This is:" + a.getName() + ",Message:"
                + e.getMessage());
        e.printStackTrace();
    }
}
