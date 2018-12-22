package team.redrock.messageBoard.servlet;


import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Filter;

//-----------------------验证登录状态的Filter
@WebFilter(filterName = "PemissionFilte",urlPatterns = "/*")
public abstract class PemissionFilte implements Filter {

    public PemissionFilte(){

    }
    private FilterConfig filterConfig;
    public void init (FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }


    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,ServletException{
        //将参数中的SerevletRequest和ServletResponse 强转为 Http...
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        //获得访问界面的url文件地址
        String servletPath = req.getServletPath();
        //获取session对象
        HttpSession session = req.getSession();
        //获取session对象中的flag的值，即登录状态，强转为String类型
        String flag = (String) session.getAttribute("flag");
        //判断是否是
        if (flag!=null){
            if (flag.equals("login_success")){
                //登录成功，直接转发到下一组件
                chain.doFilter(request,response);
            } else if(flag!=null &&flag.equals("logi_error")){
                //登录失败，跳转到登录页，并保存当前页面的url文件路径
                req.setAttribute("msg","登录失败");
                req.setAttribute("return_uri",servletPath);
                RequestDispatcher rd = req.getRequestDispatcher("index.jsp");
                rd.forward(req,res);
            }
            //没有登录则也跳转到login.jsp界面，并提示“您尚未登录！！！”
        } else {
            //未登录，跳转到登录页，并保存当前页面的url文件路径
            req.setAttribute("msg","您当前未登录，请登录");
            req.setAttribute("return_uri",servletPath);
            RequestDispatcher rd = req.getRequestDispatcher("index.jsp");
            rd.forward(req,res);
        }
    }

public void destroy() {
}
}
