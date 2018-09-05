package com.spider.modules.spider.entity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.utils.HttpConstant;

/**
 * @Author : lolilijve
 * @Email : 1042703214@qq.com
 * @Date : 2018-08-20
 */
public class MySite extends Site {

	private static final Set<Integer> DEFAULT_STATUS_CODE_SET = new HashSet<Integer>();

	static {
		DEFAULT_STATUS_CODE_SET.add(HttpConstant.StatusCode.CODE_200);
	}

	private String domain;
	private String userAgent;
	private Map<String, String> defaultCookies = new LinkedHashMap<>();
	private Map<String, Map<String, String>> cookies = new HashMap<>();
	private PhantomJSDriver phantomJSDriver;
	private String charset;
	private int sleepTime = 5000;
	private int retryTimes = 0;
	private int cycleRetryTimes = 0;
	private int retrySleepTime = 1000;
	private int timeOut = 5000;
	private Set<Integer> acceptStatCode = DEFAULT_STATUS_CODE_SET;
	private Map<String, String> headers = new HashMap<>();
	private boolean useGzip = true;
	private boolean disableCookieManagement = false;
	private List<String> clickXpathList;

	/**
	 * new a MySite
	 *
	 * @return new site
	 */
	public static MySite me() {
		return new MySite();
	}

	/**
	 * Add a cookie with domain {@link #getDomain()}
	 *
	 * @param name  name
	 * @param value value
	 * @return this
	 */
	@Override
	public MySite addCookie(String name, String value) {
		defaultCookies.put(name, value);
		return this;
	}

	/**
	 * Add a cookie with specific domain.
	 *
	 * @param domain domain
	 * @param name   name
	 * @param value  value
	 * @return this
	 */
	@Override
	public MySite addCookie(String domain, String name, String value) {
		if (!cookies.containsKey(domain)) {
			cookies.put(domain, new HashMap<>(2));
		}
		cookies.get(domain).put(name, value);
		return this;
	}

	public PhantomJSDriver getPhantomJSDriver() {
		return phantomJSDriver;
	}

	public void setPhantomJSDriver(PhantomJSDriver phantomJSDriver) {
		this.phantomJSDriver = phantomJSDriver;
	}

	/**
	 * get cookies
	 *
	 * @return get cookies
	 */
	@Override
	public Map<String, String> getCookies() {
		return defaultCookies;
	}

	/**
	 * get cookies of all domains
	 *
	 * @return get cookies
	 */
	@Override
	public Map<String, Map<String, String>> getAllCookies() {
		return cookies;
	}

	/**
	 * get user agent
	 *
	 * @return user agent
	 */
	@Override
	public String getUserAgent() {
		return userAgent;
	}

	/**
	 * set user agent
	 *
	 * @param userAgent userAgent
	 * @return this
	 */
	@Override
	public MySite setUserAgent(String userAgent) {
		this.userAgent = userAgent;
		return this;
	}

	/**
	 * get domain
	 *
	 * @return get domain
	 */
	@Override
	public String getDomain() {
		return domain;
	}

	/**
	 * set the domain of site.
	 *
	 * @param domain domain
	 * @return this
	 */
	@Override
	public MySite setDomain(String domain) {
		this.domain = domain;
		return this;
	}

	/**
	 * get charset set manually
	 *
	 * @return charset
	 */
	@Override
	public String getCharset() {
		return charset;
	}

	/**
	 * Set charset of page manually.<br>
	 * When charset is not set or set to null, it can be auto detected by Http header.
	 *
	 * @param charset charset
	 * @return this
	 */
	@Override
	public MySite setCharset(String charset) {
		this.charset = charset;
		return this;
	}

	@Override
	public int getTimeOut() {
		return timeOut;
	}

	/**
	 * set timeout for downloader in ms
	 *
	 * @param timeOut timeOut
	 * @return this
	 */
	@Override
	public MySite setTimeOut(int timeOut) {
		this.timeOut = timeOut;
		return this;
	}

	/**
	 * get acceptStatCode
	 *
	 * @return acceptStatCode
	 */
	@Override
	public Set<Integer> getAcceptStatCode() {
		return acceptStatCode;
	}

	/**
	 * Set acceptStatCode.<br>
	 * When status code of http response is in acceptStatCodes, it will be processed.<br>
	 * {200} by default.<br>
	 * It is not necessarily to be set.<br>
	 *
	 * @param acceptStatCode acceptStatCode
	 * @return this
	 */
	@Override
	public MySite setAcceptStatCode(Set<Integer> acceptStatCode) {
		this.acceptStatCode = acceptStatCode;
		return this;
	}

	/**
	 * Get the interval between the processing of two pages.<br>
	 * Time unit is micro seconds.<br>
	 *
	 * @return the interval between the processing of two pages,
	 */
	@Override
	public int getSleepTime() {
		return sleepTime;
	}

	/**
	 * Set the interval between the processing of two pages.<br>
	 * Time unit is micro seconds.<br>
	 *
	 * @param sleepTime sleepTime
	 * @return this
	 */
	@Override
	public MySite setSleepTime(int sleepTime) {
		this.sleepTime = sleepTime;
		return this;
	}

	/**
	 * Get retry times immediately when download fail, 0 by default.<br>
	 *
	 * @return retry times when download fail
	 */
	@Override
	public int getRetryTimes() {
		return retryTimes;
	}

	/**
	 * Set retry times when download fail, 0 by default.<br>
	 *
	 * @param retryTimes retryTimes
	 * @return this
	 */
	@Override
	public MySite setRetryTimes(int retryTimes) {
		this.retryTimes = retryTimes;
		return this;
	}

	@Override
	public Map<String, String> getHeaders() {
		return headers;
	}

	/**
	 * Put an Http header for downloader. <br>
	 * Use {@link #addCookie(String, String)} for cookie and {@link #setUserAgent(String)} for user-agent. <br>
	 *
	 * @param key   key of http header, there are some keys constant in {@link HttpConstant.Header}
	 * @param value value of header
	 * @return this
	 */
	@Override
	public MySite addHeader(String key, String value) {
		headers.put(key, value);
		return this;
	}

	/**
	 * When cycleRetryTimes is more than 0, it will add back to scheduler and try download again. <br>
	 *
	 * @return retry times when download fail
	 */
	@Override
	public int getCycleRetryTimes() {
		return cycleRetryTimes;
	}

	/**
	 * Set cycleRetryTimes times when download fail, 0 by default. <br>
	 *
	 * @param cycleRetryTimes cycleRetryTimes
	 * @return this
	 */
	@Override
	public MySite setCycleRetryTimes(int cycleRetryTimes) {
		this.cycleRetryTimes = cycleRetryTimes;
		return this;
	}

	@Override
	public boolean isUseGzip() {
		return useGzip;
	}

	/**
	 * Whether use gzip. <br>
	 * Default is true, you can set it to false to disable gzip.
	 *
	 * @param useGzip useGzip
	 * @return this
	 */
	@Override
	public MySite setUseGzip(boolean useGzip) {
		this.useGzip = useGzip;
		return this;
	}

	@Override
	public int getRetrySleepTime() {
		return retrySleepTime;
	}

	/**
	 * Set retry sleep times when download fail, 1000 by default. <br>
	 *
	 * @param retrySleepTime retrySleepTime
	 * @return this
	 */
	@Override
	public MySite setRetrySleepTime(int retrySleepTime) {
		this.retrySleepTime = retrySleepTime;
		return this;
	}

	@Override
	public boolean isDisableCookieManagement() {
		return disableCookieManagement;
	}

	/**
	 * Downloader is supposed to store response cookie.
	 * Disable it to ignore all cookie fields and stay clean.
	 * Warning: Set cookie will still NOT work if disableCookieManagement is true.
	 *
	 * @param disableCookieManagement disableCookieManagement
	 * @return this
	 */
	@Override
	public MySite setDisableCookieManagement(boolean disableCookieManagement) {
		this.disableCookieManagement = disableCookieManagement;
		return this;
	}

	public List<String> getClickXpathList() {
		return clickXpathList;
	}

	public void setClickXpathList(List<String> clickXpathList) {
		this.clickXpathList = clickXpathList;
	}

	@Override
	public Task toTask() {
		return new Task() {
			@Override
			public String getUUID() {
				String uuid = MySite.this.getDomain();
				if (uuid == null) {
					uuid = UUID.randomUUID().toString();
				}
				return uuid;
			}

			@Override
			public MySite getSite() {
				return MySite.this;
			}
		};
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		MySite site = (MySite) o;

		if (cycleRetryTimes != site.cycleRetryTimes) {
			return false;
		}
		if (retryTimes != site.retryTimes) {
			return false;
		}
		if (sleepTime != site.sleepTime) {
			return false;
		}
		if (timeOut != site.timeOut) {
			return false;
		}
		if (acceptStatCode != null ? !acceptStatCode.equals(site.acceptStatCode) : site.acceptStatCode != null) {
			return false;
		}
		if (charset != null ? !charset.equals(site.charset) : site.charset != null) {
			return false;
		}
		if (defaultCookies != null ? !defaultCookies.equals(site.defaultCookies) : site.defaultCookies != null) {
			return false;
		}
		if (domain != null ? !domain.equals(site.domain) : site.domain != null) {
			return false;
		}
		if (headers != null ? !headers.equals(site.headers) : site.headers != null) {
			return false;
		}
		if (userAgent != null ? !userAgent.equals(site.userAgent) : site.userAgent != null) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = domain != null ? domain.hashCode() : 0;
		result = 31 * result + (userAgent != null ? userAgent.hashCode() : 0);
		result = 31 * result + (defaultCookies != null ? defaultCookies.hashCode() : 0);
		result = 31 * result + (charset != null ? charset.hashCode() : 0);
		result = 31 * result + sleepTime;
		result = 31 * result + retryTimes;
		result = 31 * result + cycleRetryTimes;
		result = 31 * result + timeOut;
		result = 31 * result + (acceptStatCode != null ? acceptStatCode.hashCode() : 0);
		result = 31 * result + (headers != null ? headers.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "MySite{" + "domain='" + domain + '\'' + ", userAgent='" + userAgent + '\'' + ", cookies=" + defaultCookies
				+ ", charset='" + charset + '\'' + ", sleepTime=" + sleepTime + ", retryTimes=" + retryTimes
				+ ", cycleRetryTimes=" + cycleRetryTimes + ", timeOut=" + timeOut + ", acceptStatCode=" + acceptStatCode
				+ ", headers=" + headers + '}';
	}

}
