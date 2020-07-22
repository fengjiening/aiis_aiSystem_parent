package org.jeecg.common.util;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.time.StopWatch;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.springframework.web.context.request.RequestContextHolder;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class CommonMethod {
	/**缓冲区大小*/
	private static final int BUFFER_SIZE = 5 * 1024;
	
	
	public static MultiThreadedHttpConnectionManager multiThreadedHttpConnectionManager = new MultiThreadedHttpConnectionManager();
	static {

		multiThreadedHttpConnectionManager.setMaxTotalConnections(300);
		multiThreadedHttpConnectionManager.setMaxConnectionsPerHost(150);

	}
	/**
	 * 将文件转成base64 字符串
	 * 
	 * @param path文件路径
	 * @return String
	 * @throws Exception
	 */
	public static String encodeBase64File(String path) throws Exception {
		File file = new File(path);
		FileInputStream inputFile = new FileInputStream(file);
		byte[] buffer = new byte[(int) file.length()];
		inputFile.read(buffer);
		inputFile.close();
		return new BASE64Encoder().encode(buffer);

	}



	public static Double doubleRound(double score,int num){
		String result = String .format("%."+num+"f",score);
		return Double.parseDouble(result);
		
	}

	/**
	 * 将base64字符解码保存文件
	 * 
	 * @param base64Code
	 * @param targetPath
	 * @throws Exception
	 */
	public static void decoderBase64File(String base64Code, String targetPath)
			throws Exception {
		FileOutputStream out=null;
	
		try{
		byte[] buffer = new BASE64Decoder().decodeBuffer(base64Code);
		out= new FileOutputStream(targetPath);
		out.write(buffer);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		finally{
		out.close();
		}
	}
	/**
	 * 将InputStream写入本地文件
	 * @param destination 写入本地目录
	 * @param input	输入流
	 * @throws IOException
	 */
	public static void writeToLocal(String destination, InputStream input){
		FileOutputStream downloadFile=null;
		try {
			int index;
			byte[] bytes = new byte[1024];
			downloadFile= new FileOutputStream(destination);
			while ((index = input.read(bytes)) != -1) {
				downloadFile.write(bytes, 0, index);
				downloadFile.flush();
			}

		}catch(Exception e){
				e.printStackTrace();
		}finally {
			try {
				if(downloadFile!=null)downloadFile.close();
				if(input!=null)input.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}

	}

	/**
	 * 将base64字符解码转换成数据流
	 *
	 * @param base64Code
	 * @throws Exception
	 */
	public static InputStream decoderBase64ToStream(String base64Code) {

		try{
			byte[] buffer = new BASE64Decoder().decodeBuffer(base64Code);

			return  new ByteArrayInputStream(buffer);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将base64字符保存文本文件
	 * 
	 * @param base64Code
	 * @param targetPath
	 * @throws Exception
	 */
	public static void toFile(String base64Code, String targetPath)
			throws Exception {

		byte[] buffer = base64Code.getBytes();
		FileOutputStream out = new FileOutputStream(targetPath);
		out.write(buffer);
		out.close();
	}

	public static String getPath() {
		try {
			if (RequestContextHolder.getRequestAttributes() != null) {
				String path = CommonMethod.class.getClassLoader().getResource("/").getPath().replace("/WEB-INF/classes/", "");
				return path+"/";

			} else {
				String path = CommonMethod.class.getClassLoader()
						.getResource("").toString().replace("file:", "")
						.replace("/WEB-INF/classes/", "");
				return path +"/";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String getSignPath() {
		try {
			String path = CommonMethod.class.getClassLoader().getResource("/").getPath().replace("/WEB-INF/classes/", "");
			return path+"/";
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String doPostQuery(String url, String query, int time)
			throws Exception {
		if(null==query){
			query= "";
		}
		String result = null;
		HttpClient client = new HttpClient(multiThreadedHttpConnectionManager);
		PostMethod method = new PostMethod(url);
		method.setRequestHeader("Connection", "close");
		method.setRequestHeader("Content-type",
				"application/json;charset=UTF-8");
		client.getHttpConnectionManager().getParams()
				.setConnectionTimeout(time);
		method.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, time);
		try {
			RequestEntity requestEntity = new ByteArrayRequestEntity(
					query.getBytes("UTF-8"), "UTF-8");
			method.setRequestEntity(requestEntity);
		} catch (Exception e) {
			e.printStackTrace();
		} // 发出请求
		int stateCode = 0;
		StopWatch stopWatch = new StopWatch();
		try {
			stopWatch.start();
			stateCode = client.executeMethod(method);
		} catch (HttpException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				stopWatch.stop();
				if (stateCode == HttpStatus.SC_OK) {
					//result = method.getResponseBodyAsString();
					InputStream intStream = method.getResponseBodyAsStream();
					BufferedReader br = new BufferedReader(new InputStreamReader(intStream));  
			        StringBuffer resBuffer = new StringBuffer();  
			        String resTemp = "";  
			        while((resTemp = br.readLine()) != null){  
			            resBuffer.append(resTemp);  
			        }  
			        String response = resBuffer.toString();  
					result = response;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				method.abort();
				method.releaseConnection();
			}
			
		}
		return result;
	}
	
	public static String doPostQuery(String url, Map paramMap, int time)
			throws Exception {
		String result = null;
		HttpClient client = new HttpClient(multiThreadedHttpConnectionManager);
		PostMethod method = new PostMethod(url);
		method.setRequestHeader("Connection", "close");
		method.setRequestHeader("Content-type",
				"application/x-www-form-urlencoded;charset=UTF-8");
		client.getHttpConnectionManager().getParams()
				.setConnectionTimeout(time);
		method.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, time);
		NameValuePair[] params = new NameValuePair[paramMap.size()];  
        Object[] keys = paramMap.keySet().toArray();  
        for (int i = 0; i < keys.length; i++) {  
            NameValuePair nvp = new NameValuePair();  
            String key = String.valueOf(keys[i]);  
            nvp.setName(key);  
            nvp.setValue(paramMap.get(key).toString());  
            params[i] = nvp;  
        }  
        method.addParameters(params);
//		try {
//			RequestEntity requestEntity = new ByteArrayRequestEntity(
//					query.getBytes("UTF-8"), "UTF-8");
//			method.setRequestEntity(requestEntity);
//		} catch (Exception e) {
//			e.printStackTrace();
//		} // 发出请求
		int stateCode = 0;
		StopWatch stopWatch = new StopWatch();
		try {
			stopWatch.start();
			stateCode = client.executeMethod(method);
		} catch (HttpException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				stopWatch.stop();
				if (stateCode == HttpStatus.SC_OK) {
					//result = method.getResponseBodyAsString();
					InputStream intStream = method.getResponseBodyAsStream();
					BufferedReader br = new BufferedReader(new InputStreamReader(intStream));  
			        StringBuffer resBuffer = new StringBuffer();  
			        String resTemp = "";  
			        while((resTemp = br.readLine()) != null){  
			            resBuffer.append(resTemp);  
			        }  
			        String response = resBuffer.toString();  
					result = response;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				method.abort();
				method.releaseConnection();
			}
			
		}
		return result;
	}
	
	@SuppressWarnings("deprecation")
	public static String doPostQuery(String url, InputStream inputStream, Map<String, String> headerMap,int time)
			throws Exception {
		String result = null;
		HttpClient client = new HttpClient(multiThreadedHttpConnectionManager);
		PostMethod method = new PostMethod(url);
		client.getParams().setContentCharset("UTF-8");
		if(headerMap==null)
		{
			headerMap=new HashMap<String, String>();
		}
		headerMap.put("Connection", "close");
		headerMap.put("Content-type", "text/html; charset=utf-8");
		//method.setRequestHeader("Connection", "close");
		//method.setRequestHeader("Content-type","application/json;charset=UTF-8");
		Iterator<String> iterator= headerMap.keySet().iterator();
		while(iterator.hasNext())
		{
			String key=iterator.next();
			method.setRequestHeader(key, headerMap.get(key));
		}
		
		client.getHttpConnectionManager().getParams()
				.setConnectionTimeout(time);
		method.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, time);
		if(inputStream!=null){
			method.setRequestBody(inputStream);
		}
		int stateCode = 0;
		StopWatch stopWatch = new StopWatch();
		try {
			stopWatch.start();
			stateCode = client.executeMethod(method);
		} catch (HttpException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				stopWatch.stop();
				if (stateCode == HttpStatus.SC_OK) {
					//result = method.getResponseBodyAsString();
					InputStream intStream = method.getResponseBodyAsStream();
					BufferedReader br = new BufferedReader(new InputStreamReader(intStream));  
			        StringBuffer resBuffer = new StringBuffer();  
			        String resTemp = "";  
			        while((resTemp = br.readLine()) != null){  
			            resBuffer.append(resTemp);  
			        }  
			        String response = resBuffer.toString();  
					result = response;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				method.abort();
				method.releaseConnection();
			}
			
		}
		return result;
	}

	@SuppressWarnings("deprecation")
	public static String doPostQueryByte(String postUrl, byte[] bodyBytes, Map<String, String> headerMap,int time)
			throws Exception {
		String result = null;
		HttpClient client = new HttpClient(multiThreadedHttpConnectionManager);
		PostMethod method = new PostMethod(postUrl);
		client.getParams().setContentCharset("UTF-8");
		if(headerMap==null)
		{
			headerMap=new HashMap<String, String>();
		}
		headerMap.put("Connection", "close");
		headerMap.put("Content-type", "text/html; charset=utf-8");
		Iterator<String> iterator= headerMap.keySet().iterator();
		while(iterator.hasNext())
		{
			String key=iterator.next();
			method.setRequestHeader(key, headerMap.get(key));
		}
		
		try {
			method.setRequestEntity(new ByteArrayRequestEntity(bodyBytes));
			method.getParams()
					.setParameter(HttpMethodParams.SO_TIMEOUT, 100000);
			client.getHttpConnectionManager().getParams()
					.setConnectionTimeout(100000);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		int stateCode = 0;
		StopWatch stopWatch = new StopWatch();
		try {
			stopWatch.start();
			stateCode = client.executeMethod(method);
		} catch (HttpException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				stopWatch.stop();
				if (stateCode == HttpStatus.SC_OK) {
					//result = method.getResponseBodyAsString();
					InputStream intStream = method.getResponseBodyAsStream();
					BufferedReader br = new BufferedReader(new InputStreamReader(intStream));  
			        StringBuffer resBuffer = new StringBuffer();  
			        String resTemp = "";  
			        while((resTemp = br.readLine()) != null){  
			            resBuffer.append(resTemp);  
			        }  
			        String response = resBuffer.toString();  
					result = response;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				method.abort();
				method.releaseConnection();
			}
			
		}
		return result;
	}
	
	public static String sendGet(String url, String param) {
		String result = "";
		BufferedReader in = null;
		try {
			String urlNameString = url + "?" + param;
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 建立实际的连接
			connection.connect();
			in = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}
	
	
	public static String getRemoteHost(javax.servlet.http.HttpServletRequest request){
	    String ip = request.getHeader("x-forwarded-for");
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
	        ip = request.getHeader("Proxy-Client-IP");
	    }
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
	        ip = request.getHeader("WL-Proxy-Client-IP");
	    }
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
	        ip = request.getRemoteAddr();
	    }
	    return ip.equals("0:0:0:0:0:0:0:1")?"127.0.0.1":ip;
	}
	/** 
	 * 删除目录（文件夹）以及目录下的文件 
	 * @param   sPath 被删除目录的文件路径 
	 * @return  目录删除成功返回true，否则返回false 
	 */  
	public static boolean deleteDirectory(String sPath) {  
	    //如果sPath不以文件分隔符结尾，自动添加文件分隔符  
	    if (!sPath.endsWith(File.separator)) {  
	        sPath = sPath + File.separator;  
	    }  
	    File dirFile = new File(sPath);  
	    //如果dir对应的文件不存在，或者不是一个目录，则退出  
	    if (!dirFile.exists() || !dirFile.isDirectory()) {  
	        return false;  
	    }  
	    boolean flag = true;  
	    //删除文件夹下的所有文件(包括子目录)  
	    File[] files = dirFile.listFiles();  
	    for (int i = 0; i < files.length; i++) {  
	        //删除子文件  
	        if (files[i].isFile()) {  
	            flag = deleteFile(files[i].getAbsolutePath());  
	            if (!flag) break;  
	        } //删除子目录  
	        else {  
	            flag = deleteDirectory(files[i].getAbsolutePath());  
	            if (!flag) break;  
	        }  
	    }  
	    if (!flag) return false;  
	    //删除当前目录  
	    if (dirFile.delete()) {  
	        return true;  
	    } else {  
	        return false;  
	    }  
	} 
	/** 
	 * 删除单个文件 
	 * @param   sPath    被删除文件的文件名 
	 * @return 单个文件删除成功返回true，否则返回false 
	 */  
	public static boolean deleteFile(String sPath) {  
		boolean flag = false;  
		File file = new File(sPath);  
	    // 路径为文件且不为空则进行删除  
	    if (file.isFile() && file.exists()) {  
	        file.delete();  
	        flag = true;  
	    }  
	    return flag;  
	}

	/**
	 * 复制文件
	 * @param src本地文件
	 * @param dst目标文件
	 */
	public static void copy(File src, File dst) {
		InputStream in = null;
		OutputStream out = null;
		FileOutputStream f = null;
		try {
			f = new FileOutputStream(dst);
			in = new BufferedInputStream(new FileInputStream(src),BUFFER_SIZE);
			out = new BufferedOutputStream(f, BUFFER_SIZE);
			byte[] buffer = new byte[BUFFER_SIZE];
			while (in.read(buffer) > 0) {
				out.write(buffer);
			}
		 }catch(Exception e ){
			e.printStackTrace();
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != out) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(null != f){
				try {
					f.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	  //int32转换为二进制（4个字节）
    public static byte[] int2byte(int i){
    	byte[] res = new byte[4];
    	res[0] = (byte)(i & 0xff);
    	res[1] = (byte)(i >> 8 & 0xff);
    	res[2] = (byte)(i >> 16 & 0xff);
    	res[3] = (byte)(i >> 24 & 0xff);
    	return res;
    	}
	//获得指定文件的byte数组 
    public static byte[] getBytes(String filePath){  
        byte[] buffer = null;  
        try {  
            File file = new File(filePath);  
            FileInputStream fis = new FileInputStream(file);  
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);  
            byte[] b = new byte[1000];  
            int n;  
            while ((n = fis.read(b)) != -1) {  
                bos.write(b, 0, n);  
            }  
            fis.close();  
            bos.close();  
            buffer = bos.toByteArray();  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return buffer;  
    }
    
    public static String XmlToString(String path){  
        org.jdom.Document document=null;  
        document=load(path);  
          
        Format format =Format.getPrettyFormat();      
        format.setEncoding("UTF-8");//设置编码格式   
          
        StringWriter out=null; //输出对象  
        String sReturn =""; //输出字符串  
        XMLOutputter outputter =new XMLOutputter();   
        out=new StringWriter();   
        try {  
           outputter.output(document,out);  
        } catch (IOException e) {  
           e.printStackTrace();  
        }   
        sReturn=out.toString();   
        return sReturn;  
    }
    public static org.jdom.Document load(String path){  
        org.jdom.Document document=null;  
        try {  
            SAXBuilder reader = new SAXBuilder();   
            document=reader.build(new File(path));  
       } catch (Exception e) {  
            e.printStackTrace();  
       }  
        return document;  
    }
}
