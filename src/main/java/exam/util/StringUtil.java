package exam.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 字符串工具
 * @author skywalker
 *
 */
public class StringUtil {
	

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
	
}
