package exam.util.json;

import exam.util.DataUtil;

/**
 * 生成json数据工具
 * @author skywalker
 *
 */
public class JSONObject extends JSON {
	
	/**
	 * 添加一个元素
	 */
	@Override
	public JSONObject addElement(String key, String value) {
		if(DataUtil.isValid(key) && value != null) {
			json.append("\"").append(key).append("\":\"").append(value).append("\",");
		}
		return this;
	}
	
	/**
	 * 添加一个对象元素
	 */
	@Override
	public JSONObject addElement(String key, JSON object) {
		if(DataUtil.isValid(key) && object != null) {
			json.append("\"").append(key).append("\":").append(object).append(",");
		}
		return this;
	}
	
	@Override
	protected char getBrace() {
		return '{';
	}

}
