package exam.util.json;

import exam.util.DataUtil;

/**
 * JSON数组生成
 * @author skywalker
 *
 */
public class JSONArray extends JSON {
	
	/**
	 * 添加一个对象
	 * @param json 用json代表一个对象
	 */
	@Override
	public JSONArray addObject(JSON object) {
		if (json != null) {
			json.append(object).append(",");
		}
		return this;
	}
	
	/**
	 * 必须保证此处使用的key-value和addObject()里面的结构是一致的
	 * 使用此方法的条件应该是数组内部的对象是只有一个key-value的简单结构
	 */
	@Override
	public JSON addElement(String key, String value) {
		if (DataUtil.isValid(key) && value != null) {
			json.append("{\"").append(key).append("\":\"").append(value).append("\"},");
		}
		return this;
	}
	
	/**
	 * 必须保证此处使用的key-value和addObject()里面的结构是一致的
	 * 使用此方法的条件应该是数组内部的对象是只有一个key-value的简单结构
	 */
	@Override
	public JSON addElement(String key, JSON object) {
		if (DataUtil.isValid(key) && object != null) {
			json.append("{\"").append(key).append("\":").append(object).append("},");
		}
		return this;
	}

	@Override
	protected char getBrace() {
		return '[';
	}

}
