package exam.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 字符串工具
 * @author skywalker
 *
 */
public abstract class StringUtil {
	

	/**
	 * md5加密
	 */
	public static String md5(String str) {
		StringBuilder result = new StringBuilder("");
		if(DataUtil.isValid(str)) {
			char []chars = {'0', '1', '2','3','4','5','6','7',
					'8','9','a','b','c','d','e', 'f'};
			byte []origin = str.getBytes();
			MessageDigest md = null;;
			try {
				md = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			byte []after = md.digest(origin);
			for(byte b : after) {
				result.append(chars[(b >> 4) & 0xF]);
				result.append(chars[b & 0xF]);
			}
		}
		return result.toString();
	}
	
	/**
	 * 转义htnl字符，防止XSS攻击
	 * @param html 从客户端输入的内容
	 * @return
	 */
	public static String htmlEncode(String html) {
		if (DataUtil.isValid(html)) {
			char c;
			StringBuilder result = new StringBuilder();
			for (int i = 0, l = html.length();i < l;i ++) {
				c = html.charAt(i);
				switch (c) {
				case '&':
					result.append("&amp;");
					break;
				case '<':
					result.append("&lt;");
			        break;
		        case '>':
		        	result.append("&gt;");
		        	break;
		        case '"':
		        	result.append("&quot;");
		        	break;
		        case ' ':
		        	result.append("&nbsp;");
		        	break;
		        default:
		        	result.append(c);
				}
			}
			return result.toString();
		}
		return "";
	}
	
}
