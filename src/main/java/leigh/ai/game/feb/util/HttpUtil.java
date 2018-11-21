package leigh.ai.game.feb.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import leigh.ai.game.feb.service.LoginService;

import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpUtil {
	public static String FEB_HOST = "http://o.feb2.cn/";
	public static CloseableHttpClient HC;
	static {
		RequestConfig rc = RequestConfig.custom()
				.setConnectTimeout(10000)
				.build();
		HC = HttpClients.custom()
				.setUserAgent("Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 10.0; WOW64; Trident/7.0; Touch; .NET4.0C; .NET4.0E; .NET CLR 2.0.50727; .NET CLR 3.0.30729; .NET CLR 3.5.30729; Tablet PC 2.0; LCJB)")
				.setDefaultCookieStore(new BasicCookieStore())
				.setDefaultRequestConfig(rc)
				.build();
	}
	private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);
	
	public static String get(String url) {
		return get(url, logger);
	}
	public static String get(String url, Logger logger) {
		url = FEB_HOST + url;
		String result = null;
		CloseableHttpResponse response = null;
		for(int i = 0; i < 5; i++) {
			try {
				HttpGet get = new HttpGet(url);
				response = HC.execute(get);
				result = EntityUtils.toString(response.getEntity(), "utf8");
				if(result.contains("让你的鼠标和键盘休息一下")) {
					if(logger.isDebugEnabled()) {
						logger.debug("遭遇让你的鼠标和键盘休息一下！");
					}
					LoginService.logout();
					FakeSleepUtil.sleep(1, 2);
					LoginService.login();
					continue;
				}
				if(logger.isDebugEnabled()) {
					logger.debug("url=" + url + ",,,,,,response=" + result);
				}
				break;
			} catch (IOException e) {
				LogUtil.errorStackTrace(logger, e);
			} finally {
				if(response != null) {
					try {
						response.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			logger.warn("http连接出错！重试第" + i + "次");
		}
		FakeSleepUtil.sleep(0.3);
		return result;
	}
	public static void getWithoutReturn(String url, Logger logger) {
		url = FEB_HOST + url;
		CloseableHttpResponse response = null;
		try {
			HttpGet get = new HttpGet(url);
			response = HC.execute(get);
			if(logger.isDebugEnabled()) {
				logger.debug("url=" + url + ",,,,,,who knows the return?");
			}
		} catch (IOException e) {
			LogUtil.errorStackTrace(logger, e);
		} finally {
			if(response != null) {
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		FakeSleepUtil.sleep(0.3);
	}
	public static String post(String url, Map<String, Object> param) {
		return post(url, param, logger);
	}
	public static String post(String url, Map<String, Object> param, Logger logger) {
		url = FEB_HOST + url;
		String result = null;
		CloseableHttpResponse response = null;
		for(int i = 0; i < 5; i++) {
			try {
				HttpPost post = new HttpPost(url);
				List<NameValuePair> nvps = new ArrayList<NameValuePair>();
				for(String key: param.keySet()) {
					nvps.add(new BasicNameValuePair(key, param.get(key).toString()));
				}
				post.setEntity(new UrlEncodedFormEntity(nvps, "utf8"));
				response = HC.execute(post);
				result = EntityUtils.toString(response.getEntity(), "utf8");
				if(logger.isDebugEnabled()) {
					logger.debug("url=" + url + ",,,,,,response=" + result);
				}
				break;
			} catch (IOException e) {
				LogUtil.errorStackTrace(logger, e);
			} finally {
				if(response != null) {
					try {
						response.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		FakeSleepUtil.sleep(0.5);
		return result;
	}
	public static void postWithoutReturn(String url, Map<String, Object> param, Logger logger) {
		url = FEB_HOST + url;
		CloseableHttpResponse response = null;
		try {
			HttpPost post = new HttpPost(url);
			post.addHeader("", "");
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			for(String key: param.keySet()) {
				nvps.add(new BasicNameValuePair(key, param.get(key).toString()));
			}
			post.setEntity(new UrlEncodedFormEntity(nvps, "utf8"));
			response = HC.execute(post);
			EntityUtils.consume(response.getEntity());
			if(logger.isDebugEnabled()) {
				logger.debug("url=" + url + ",,,,,,do not concern about response");
			}
		} catch (IOException e) {
			LogUtil.errorStackTrace(logger, e);
		} finally {
			if(response != null) {
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		FakeSleepUtil.sleep(0.5);
	}
}
