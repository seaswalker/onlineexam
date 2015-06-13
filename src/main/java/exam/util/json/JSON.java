package exam.util.json;

/**
 * json抽象类
 * @author skywalker
 *
 */
public abstract class JSON {

	private char brace = getBrace();
	protected StringBuffer json = new StringBuffer(new Character(brace).toString());
	
	/**
	 * 工厂方法，获得括号
	 */
	protected abstract char getBrace();
	
	/**
	 * 添加一个元素
	 */
	public JSON addElement(String key, String value) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * 添加一个元素
	 */
	public JSON addElement(String key, JSON object) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * 添加一个json对象
	 */
	public JSON addObject(JSON json) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * 添加末尾符号
	 * 删除最后的,
	 */
	private void appendTail() {
		char tail = json.charAt(json.length() - 1);
		if(tail == ',') {
			json.deleteCharAt(json.length() - 1);
		}
		char rightBrace = (brace == '{' ? '}' : ']');
		//添加末尾]
		if(tail != rightBrace) {
			json.append(rightBrace);
		}
	}
	
	@Override
	public String toString() {
		appendTail();
		return json.toString();
	}
	
	/**
	 * 清空内容
	 */
	public void clear() {
		json.delete(1, json.length());
	}
	
}
