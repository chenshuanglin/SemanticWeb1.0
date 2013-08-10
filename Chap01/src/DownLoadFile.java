

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class DownLoadFile {
	
	protected static int page_count = 0;
	public static int getPage_count() {
		return page_count;
	}

	public static void setPage_count(int page_count) {
		DownLoadFile.page_count = page_count;
	}

	protected static String downloadFileDir = null;
	public static String getDownloadFileDir() {
		return downloadFileDir;
	}

	public static void setDownloadFileDir(String downloadFileDir) {
		DownLoadFile.downloadFileDir = downloadFileDir;
	}

	/**
	 * 根据 url 和网页类型生成需要保存的网页的文件名 去除掉 url 中非文件名字符
	 */
	
	public  String getFileNameByUrl(String url,String contentType)
	{
		//remove http://
		url=url.substring(7);
		//text/html类型
		if(contentType.indexOf("html")!=-1)
		{
		   
			url= url.replaceAll("[\\?/:*|<>\"]", "_");
			if(true != url.endsWith("html") && true != url.endsWith("htm"))
			    url += ".html";
			return url;
		}
		//如application/pdf类型
		else
		{
          return url.replaceAll("[\\?/:*|<>\"]", "_")+"."+
          contentType.substring(contentType.lastIndexOf("/")+1);
		}	
	}

	/**
	 * 保存网页字节数组到本地文件 filePath 为要保存的文件的相对地址
	 */
	private void saveToLocal(byte[] data, String filePath) {
	    if(filePath == null) return;
		try {
			DataOutputStream out = new DataOutputStream(new FileOutputStream(
					new File(filePath)));
			for (int i = 0; i < data.length; i++)
				out.write(data[i]);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
		/* 另外一种用InputStream，比较保险的方法*/
	    public void saveToLocalFile(InputStream response, String path) {
		FileOutputStream fos;
		if(path == null) return;
		try {
		    fos = new FileOutputStream(path);
		
		    byte bty[] = new byte[1024];
		    int readBy = 0;
		    try {
			while((readBy=response.read(bty)) > 0)
			    fos.write(bty, 0, readBy);
			fos.flush();
			fos.close();
		    } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    }
		    
		    } catch (FileNotFoundException e) {
		    // TODO Auto-generated catch block
			e.printStackTrace();
		    }

		    System.out.println("file:" + path +" ---- (download ok!)");
	    }

	/* 下载 url 指向的网页 */
	public String downloadFile(String url) {
		String filePath = null;
		/* 1.生成 HttpClinet 对象并设置参数 */
		HttpClient httpClient = new HttpClient();
		// 设置 Http 连接超时 5s
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(
				5000);

		/* 2.生成 GetMethod 对象并设置参数 */
		GetMethod getMethod = new GetMethod(url);
		// 设置 get 请求超时 5s
		getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);
		// 设置请求重试处理
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler());

		/* 3.执行 HTTP GET 请求 */
		try {
			int statusCode = httpClient.executeMethod(getMethod);
			// 判断访问的状态码
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: "
						+ getMethod.getStatusLine());
				filePath = null;
			}

			/* 4.处理 HTTP 响应内容 */
			//byte[] responseBody = getMethod.getResponseBody();// 读取为字节数组
			InputStream response = getMethod.getResponseBodyAsStream();// 改用这个较好
			
			// 根据网页 url 生成保存时的文件名
			filePath = downloadFileDir;
			filePath += getFileNameByUrl(url, getMethod.getResponseHeader(
							"Content-Type").getValue());
			saveToLocalFile(response, filePath);
			response.close();
			
			//saveToLocal(responseBody, filePath);
		} catch (HttpException e) {
			// 发生致命的异常，可能是协议不对或者返回的内容有问题
			System.out.println("Please check your provided http address!");
			e.printStackTrace();
			filePath = null;
		} catch (IOException e) {
			// 发生网络异常
		    //System.out.println("network ex...");
		    	filePath  = null;
			e.printStackTrace();
		} catch (Exception e1) {
		    
		}
		
		finally {
			// 释放连接
		    if(getMethod != null)
			getMethod.releaseConnection();
		}
		
		return filePath;
	}
}