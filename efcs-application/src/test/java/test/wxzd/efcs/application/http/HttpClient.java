package test.wxzd.efcs.application.http;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.wxzd.dispatcher.wcs.command.log.WCSHttpClientLogFactory;
import com.wxzd.gaia.common.base.core.log.FileLogUnity;
import com.wxzd.protocol.ProtocolException;
import com.wxzd.protocol.wcs.domain.exception.CommunicationException;
import com.wxzd.protocol.wcs.domain.exception.ServiceException;

/**
 * 
 * 
 * @version 1
 * @author y
 * @.create 2017-04-25
 */
public class HttpClient {

	public static String postJson(String url, String json) throws ProtocolException {
		String result = null;
		try {
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(url);
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("json", json));
			nvps.add(new BasicNameValuePair("sign", "developing"));
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
			CloseableHttpResponse response = httpclient.execute(httpPost);
			try {
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					HttpEntity entity = response.getEntity();
					result = EntityUtils.toString(entity, "UTF-8");
				} else {
					throw new ServiceException(response.getStatusLine().toString());
				}
			} finally {
				response.close();
			}
		} catch (Exception e) {
			throw new CommunicationException("通信异常", e);
		}
		return result;
	}

	public static String postJson(String url, String json, String method) throws ProtocolException {
		FileLogUnity log = WCSHttpClientLogFactory.gethttpclientFileLogUnity();
		String result = null;
		try {
			log.begin(method, url, json);
			return postJson(url, json);
		} catch (ProtocolException e) {
			log.warn(method, url, e.getMessage());
			throw e;
		} finally {
			log.end(method, url, result);
		}
	}
}
