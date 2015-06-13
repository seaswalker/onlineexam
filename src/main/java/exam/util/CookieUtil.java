package exam.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * cookie操作
 * @author skywalker
 *
 */
public class CookieUtil {
	
	/**
	 * 设置cookie
	 * @param days 有效的天数
	 */
	public static void addCookie(String name, String value, int days, HttpServletResponse response) {
		if(!DataUtil.isValid(name)) {
			throw new NullPointerException("cookie的名不能为空");
		}
		Cookie cookie = new Cookie(name, value);
		cookie.setPath("/");
		cookie.setMaxAge(days * 24 * 3600); 
		response.addCookie(cookie);
	}
	
	/**
	 * 删除cookie
	 */
	public static void removeCookie(String name, HttpServletResponse response) {
		addCookie(name, null, 0, response);
	}
	
	/**
	 * 获得cookie
	 * 用户cookie是username-password形式
	 */
	public static String getCookie(String name, HttpServletRequest request) {
		if(!DataUtil.isValid(name)) {
			throw new NullPointerException("cookie名不能为空");
		}
		Cookie[] cookies = request.getCookies();
		for(Cookie cookie : cookies) {
			if(cookie.getName().equals(name)) {
				return cookie.getValue();
			}
		}
		return null;
	}
	
}
