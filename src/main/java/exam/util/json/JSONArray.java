package exam.util.json;

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
		if(json != null) {
			json.append(object).append(",");
		}
		return this;
	}

	@Override
	protected char getBrace() {
		return '[';
	}

}
