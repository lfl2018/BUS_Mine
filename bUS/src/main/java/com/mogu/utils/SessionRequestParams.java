package com.mogu.utils;

import org.xutils.http.RequestParams;

import com.mogu.app.MyCookieStore;

/**
 * 带session的请求参数类
 * 
 * @author Administrator
 *
 */
public class SessionRequestParams extends RequestParams {

	public SessionRequestParams() {
		super();
		String sessionid = MyCookieStore.user.getSessionid();
		if (sessionid != null) {
			setUseCookie(false);
			this.addHeader("Cookie", "JSESSIONID=" + sessionid);
		}

	}

	public SessionRequestParams(String uri) {
		super(uri);
		if (MyCookieStore.user==null) {
			return;
		}
		
		String sessionid = MyCookieStore.user.getSessionid();
		if (sessionid != null) {
			setUseCookie(false);
			this.addHeader("Cookie", "JSESSIONID=" + sessionid);
		}
	}

}
