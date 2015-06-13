package exam.controller.admin;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import exam.model.Major;
import exam.model.page.PageBean;
import exam.service.MajorService;
import exam.util.DataUtil;

/**
 * 管理员专业控制器
 * @author skywalker
 *
 */
@Controller
@RequestMapping("/admin/major")
public class AdminMajorController {

	@Resource
	private MajorService majorService;
	@Value("#{properties['major.pageSize']}")
	private int pageSize;
	//显示的页码数量
	@Value("#{properties['major.pageNumber']}")
	private int pageNumber;
	
	/**
	 * 显示专业列表
	 * bug? 模糊搜索不能使用参数?
	 */
	@RequestMapping("/list")
	public String list(String pn, String search, Model model) {
		int pageCode = DataUtil.getPageCode(pn);
		String where = null;
		if(DataUtil.isValid(search)) {
			where = " where name like '%" + search + "%'";
		}
		PageBean<Major> pageBean = majorService.pageSearch(pageCode, pageSize, pageNumber, where, null, null);
		model.addAttribute("pageBean", pageBean);
		model.addAttribute("search", search);
		return "admin/major_list";
	}
	
}
