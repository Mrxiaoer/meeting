package com.spider.modules.spider.downloader;

import com.spider.modules.spider.entity.MyPage;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.openqa.selenium.Cookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.AbstractDownloader;
import us.codecraft.webmagic.downloader.HttpClientGenerator;
import us.codecraft.webmagic.downloader.HttpClientRequestContext;
import us.codecraft.webmagic.downloader.HttpUriRequestConverter;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.ProxyProvider;
import us.codecraft.webmagic.selector.PlainText;
import us.codecraft.webmagic.utils.CharsetUtils;
import us.codecraft.webmagic.utils.HttpClientUtils;

/**
 * HttpClientDownloader
 *
 * @Author : lolilijve
 * @Email : 1042703214@qq.com
 * @Date : 2018-07-09
 */
public class HttpClientDownloader extends AbstractDownloader {

	private final Map<String, CloseableHttpClient> httpClients = new HashMap<String, CloseableHttpClient>();
	private Logger logger = LoggerFactory.getLogger(getClass());
	private HttpClientGenerator httpClientGenerator = new HttpClientGenerator();

	private HttpUriRequestConverter httpUriRequestConverter = new HttpUriRequestConverter();

	private ProxyProvider proxyProvider;

	private boolean responseHeader = true;

	public void setHttpUriRequestConverter(HttpUriRequestConverter httpUriRequestConverter) {
		this.httpUriRequestConverter = httpUriRequestConverter;
	}

	public void setProxyProvider(ProxyProvider proxyProvider) {
		this.proxyProvider = proxyProvider;
	}

	private CloseableHttpClient getHttpClient(Site site) {
		if (site == null) {
			return httpClientGenerator.getClient(null);
		}
		String domain = site.getDomain();
		CloseableHttpClient httpClient = httpClients.get(domain);
		if (httpClient == null) {
			synchronized (this) {
				httpClient = httpClients.get(domain);
				if (httpClient == null) {
					httpClient = httpClientGenerator.getClient(site);
					httpClients.put(domain, httpClient);
				}
			}
		}
		return httpClient;
	}

	@Override
	public MyPage download(Request request, Task task) {
		if (task == null || task.getSite() == null) {
			throw new NullPointerException("task or site can not be null");
		}
		CloseableHttpResponse httpResponse = null;
		CloseableHttpClient httpClient = getHttpClient(task.getSite());
		Proxy proxy = proxyProvider != null ? proxyProvider.getProxy(task) : null;
		HttpClientRequestContext requestContext = httpUriRequestConverter.convert(request, task.getSite(), proxy);
		MyPage page = MyPage.fail();
		try {
			httpResponse = httpClient.execute(requestContext.getHttpUriRequest(), requestContext.getHttpClientContext());
			page = handleResponse(request, request.getCharset() != null ? request.getCharset() :
							task.getSite().getCharset(),
					httpResponse, task);
			onSuccess(request);
			logger.info("downloading page success {}", request.getUrl());
			return page;
		} catch (IOException e) {
			logger.warn("download page {} error", request.getUrl(), e);
			onError(request);
			return page;
		} finally {
			if (httpResponse != null) {
				//ensure the connection is released back to pool
				EntityUtils.consumeQuietly(httpResponse.getEntity());
			}
			if (proxyProvider != null && proxy != null) {
				proxyProvider.returnProxy(proxy, page, task);
			}
		}
	}

	@Override
	public void setThread(int thread) {
		httpClientGenerator.setPoolSize(thread);
	}

	protected MyPage handleResponse(Request request, String charset, HttpResponse httpResponse, Task task)
			throws IOException {
		byte[] bytes = IOUtils.toByteArray(httpResponse.getEntity().getContent());
		String contentType = httpResponse.getEntity().getContentType() == null ? ""
				: httpResponse.getEntity().getContentType().getValue();
		MyPage page = new MyPage();
		page.setBytes(bytes);
		if (!request.isBinaryContent()) {
			if (charset == null) {
				charset = getHtmlCharset(contentType, bytes);
			}
			page.setCharset(charset);
			page.setRawText(new String(bytes, charset));
		}
		//获取重定向之后的主机地址信息
		HttpContext httpContext = new BasicHttpContext();
		HttpHost targetHost = (HttpHost) httpContext.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
		HttpUriRequest realRequest = (HttpUriRequest) httpContext.getAttribute(ExecutionContext.HTTP_REQUEST);
		page.setUrl(new PlainText(targetHost.toString() + realRequest.toString()));
		page.setRequest(request);
		page.setStatusCode(httpResponse.getStatusLine().getStatusCode());
		page.setDownloadSuccess(true);
		//设置path
		String urlPath = null;
		if (task.getSite().getCookies() != null) {
			Iterator var6 = task.getSite().getCookies().entrySet().iterator();
			if (var6.hasNext()) {
				Map.Entry<String, String> cookieEntry = (Map.Entry) var6.next();
				Cookie cookie = new Cookie(cookieEntry.getKey(), cookieEntry.getValue());
				urlPath = cookie.getPath();
			}
		}
		page.setUrlPath(urlPath);
		if (responseHeader) {
			page.setHeaders(HttpClientUtils.convertHeaders(httpResponse.getAllHeaders()));
		}
		return page;
	}

	private String getHtmlCharset(String contentType, byte[] contentBytes) throws IOException {
		String charset = CharsetUtils.detectCharset(contentType, contentBytes);
		if (charset == null) {
			charset = Charset.defaultCharset().name();
			logger.warn("Charset autodetect failed, use {} as charset. Please specify charset in Site.setCharset()",
					Charset.defaultCharset());
		}
		return charset;
	}
}

