package exam.intercepter;

import exam.model.role.Manager;
import exam.model.role.Student;
import exam.model.role.Teacher;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**登录拦截器
 * Created by skywalker on 2015/9/18.
 */
public class LoginIntercepter extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getServletPath();
        String contextPath = request.getContextPath();
        HttpSession session = request.getSession();
        //如果是登录页面，直接放行
        if (path.indexOf("login") != -1) {
            return true;
        }
        //如果是去向管理员页面
        if (path.indexOf("admin") != -1) {
            Manager manager = (Manager) session.getAttribute("admin");
            if (manager == null) {
                response.sendRedirect(contextPath + "/login");
                return false;
            }
            return true;
        } else if (path.indexOf("teacher") != -1) {
            //如果是去向教师页面
            Teacher teacher = (Teacher) session.getAttribute("teacher");
            if (teacher == null) {
                response.sendRedirect(contextPath + "/login");
                return false;
            }
            return true;
        } else if (path.indexOf("student") != -1) {
        	Student student = (Student) session.getAttribute("student");
        	if (student == null) {
        		 response.sendRedirect(contextPath + "/login");
                 return false;
        	}
        }
        return true;
    }
}

