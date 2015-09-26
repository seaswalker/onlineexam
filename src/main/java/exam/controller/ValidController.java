package exam.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 当用户被其它位置的登录挤掉时，转向提示页面
 * @author skywalker
 *
 */
@Controller
public class ValidController {

	@RequestMapping("/valid")
	public String valid() {
		return "valid";
	}
	
}
