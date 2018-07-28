package com.cxt.gps.util;

import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;


//Http请求的工具类
public class HttpUtils
{

	private static final int TIMEOUT_IN_MILLIONS = 30000;
	private int READ_TIME_OUT = 90 * 1000;
	/**设置读取数据的超时时间（一般不需要设置，只有一次下载大量数据时设置）
	 * @param sec 单位秒
	 */
	public void setReadTimeOut(int sec)
	{
		READ_TIME_OUT = sec * 1000;
	}

	public interface CallBack
	{
		void onRequestComplete(String result);
	}


	/**
	 * 异步的Get请求
	 *
	 * @param urlStr
	 * @param callBack
	 */
	public static void doGetAsyn(final String urlStr, final CallBack callBack)
	{
		new Thread()
		{
			public void run()
			{
				try
				{
					//String result = doGet(urlStr);
					if (callBack != null)
					{
						//callBack.onRequestComplete(result);
					}
				} catch (Exception e)
				{
					e.printStackTrace();
				}

			};
		}.start();
	}

	/**
	 * 异步的Post请求
	 * @param urlStr
	 * @param
	 * @param callBack
	 * @throws Exception
	 */
	public static void doPostAsyn(final String urlStr, final Map<String,String> map,
                                  final CallBack callBack) throws Exception
	{
		new Thread()
		{
			public void run()
			{
				try
				{
					//String result = doPost(urlStr, map);
					if (callBack != null)
					{
						//callBack.onRequestComplete(result);
					}
				} catch (Exception e)
				{
					e.printStackTrace();
				}

			};
		}.start();

	}

	/**
	 * Get请求，获得返回数据
	 *
	 * @param urlStr
	 * @return
	 * @throws Exception
	 */
	public String doGet(String urlStr)
	{
		URL url = null;
		HttpURLConnection conn = null;
		InputStream is = null;
		//ByteArrayOutputStream baos = null;
		try
		{
			Log.v("地址",urlStr);
			url = new URL(urlStr);
			conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(READ_TIME_OUT);
			conn.setConnectTimeout(TIMEOUT_IN_MILLIONS);
			conn.setRequestMethod("GET");
			//conn.setRequestProperty("accept", "*/*");
			//conn.setRequestProperty("connection", "Keep-Alive");
			if (conn.getResponseCode() == 200)
			{
				is = conn.getInputStream();
				/*baos = new ByteArrayOutputStream();
				int len = -1;
				byte[] buf = new byte[128];

				while ((len = is.read(buf)) != -1)
				{
					baos.write(buf, 0, len);
				}
				baos.flush();
				return baos.toString();*/
				byte[] data= StremTools.read(is);
				String jsonData=new String(data,"UTF-8");
				Log.v("地址",jsonData);
				return jsonData;
			} else
			{
				throw new RuntimeException(" responseCode is not 200 ... ");
			}

		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			try
			{
				if (is != null)
					is.close();
			} catch (IOException e)
			{
			}
			/*try
			{
				if (baos != null)
					baos.close();
			} catch (IOException e)
			{
			}*/
			conn.disconnect();
		}

		return null ;

	}

//	/**
//	 * 向指定 URL 发送POST方法的请求
//	 *
//	 * @param url
//	 *            发送请求的 URL
//	 * @param  param
//	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
//	 * @return 所代表远程资源的响应结果
//	 * @throws UnsupportedEncodingException
//	 * @throws Exception
//	 */
	public static String doPost(String url, Map<String, String> requestParams) throws UnsupportedEncodingException
	{

		StringBuilder x_params = new StringBuilder();
		for(Map.Entry<String, String> entry : requestParams.entrySet()){
			x_params.append(entry.getKey());
			x_params.append("=");
			x_params.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
			x_params.append("&");
		}
		if (x_params.length() > 0) x_params.deleteCharAt(x_params.length() - 1);
		String param = x_params.toString();

		DataOutputStream out = null;
		InputStream is = null;
		String result = "";
		try
		{
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			HttpURLConnection conn = (HttpURLConnection) realUrl
					.openConnection();
			// 设置通用的请求属性
			//conn.setRequestProperty("accept", "*/*");
			//conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			conn.setRequestProperty("charset", "utf-8");
			conn.setUseCaches(false);
			conn.setInstanceFollowRedirects(true);
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			//conn.setReadTimeout(TIMEOUT_IN_MILLIONS);
			conn.setConnectTimeout(TIMEOUT_IN_MILLIONS);
			conn.connect();

			if (param != null && !param.trim().equals(""))
			{
				// 获取URLConnection对象对应的输出流
				out = new DataOutputStream(conn.getOutputStream());
				//out = new PrintWriter(conn.getOutputStream());
				// 发送请求参数
				//out.print(param);
				Log.v("patam",param);
				out.write(param.getBytes());
				// flush输出流的缓冲
				out.flush();
			}
			/*// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null)
			{
				result += line;
			}*/
			if (conn.getResponseCode() == 200)
			{
				is = conn.getInputStream();
				byte[] data= StremTools.read(is);
				String jsonData=new String(data,"UTF-8");
				return jsonData;
			} else
			{
				throw new RuntimeException(" responseCode is not 200 ... ");
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally
		{
			try
			{
				if (out != null)
				{
					out.close();
				}
				/*if (in != null)
				{
					in.close();
				}*/
				if (is != null)
				{
					is.close();
				}
			} catch (IOException ex)
			{
				ex.printStackTrace();
			}
		}
		return result;
	}
	/**
	 * @param sUrl
	 * @return 获取httpUrlConnection
	 */
	private HttpURLConnection createHttpURLConnection(String sUrl)
	{
		try
		{
			URL url = new URL(sUrl);
			if (!isGprsNet())
			{
				java.net.Proxy p = new java.net.Proxy(java.net.Proxy.Type.HTTP, new InetSocketAddress(android.net.Proxy.getDefaultHost(), android.net.Proxy.getDefaultPort()));
				HttpURLConnection conn = (HttpURLConnection) url.openConnection(p);
				return conn;
			}
			else
			{
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				return conn;
			}
		}
		catch (Exception e)
		{

		}
		return null;
	}
	/**
	 * @return判断是否为net
	 */
	private boolean isGprsNet()
	{
		String proxyHost = android.net.Proxy.getDefaultHost();
		return proxyHost == null;
	}
	private void close(InputStream is, OutputStream os)
	{
		try
		{
			if (is != null)
			{
				is.close();
				is = null;
			}

			if (os != null)
			{
				os.close();
				os = null;
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}