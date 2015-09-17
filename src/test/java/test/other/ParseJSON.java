package test.other;

/**
 * 测试解析json，json串
 * {
 * 	'singles': [
 * 		{'title':'阿森纳是第几?','point':'10','optionA':'3','optionB':'2','optionC':'4','optionD':'1','answer':'1'}],
 * 	'multis':[{'title':'阿森纳是第几?','point':'10','optionA':'3','optionB':'2','optionC':'4','optionD':'1','answer':'1,2'}],
 * 	'judges':[{'title':'娜娜有输球了吗','point':'100','answer':'1'}],
 * 	'setting':{'timeLimit':'60','grade':'1','major':'1','clazz':'1','status':1,'runTime':'7'}}
 * @author skywalker
 *
 */
public class ParseJSON {

	public static void main(String[] args) {
		String json = "{'singles':[{'title':'阿森纳是第几?','point':'10','optionA':'3','optionB':'2','optionC':'4','optionD':'1','answer':'1'}],'multis':[{'title':'阿森纳是第几?','point':'10','optionA':'3','optionB':'2','optionC':'4','optionD':'1','answer':'1,2'}],'judges':[{'title':'娜娜有输球了吗','point':'100','answer':'1'}],'setting':{'title':'试卷题目','timeLimit':'60','grade':'1','major':'1','clazz':'1','status':1,'runTime':'7'}}";
		System.out.println(json);
	}
	
	
	
}
