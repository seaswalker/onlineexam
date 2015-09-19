package exam.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**测试用
 * Created by skywalker on 2015/9/19.
 */
@Controller
public class TestController {

    @RequestMapping("/success")
    public String test() {
        return "success";
    }

}
