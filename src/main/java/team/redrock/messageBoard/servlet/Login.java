package team.redrock.messageBoard.servlet;

import team.redrock.messageBoard.dao.MessageBoardDao;
import team.redrock.messageBoard.dao.impl.MessageBoardDaoImpl;
import team.redrock.messageBoard.service.MessageBoardService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/login")
public class Login extends HttpServlet {
    private Connection conn;
    private PreparedStatement pstmt;
    private ResultSet resultSet;
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        conn = new MessageBoardDaoImpl().getConnection();
        String uesrName = request.getParameter("name");
        String password = request.getParameter("pwd");
        //访问登录页面之前所访问的页面，可通过这个值跳转至之前的页面
        String returnUri = request.getParameter("return_uri");

        response.setContentType("text/html;charset=utf-8");
        response.setCharacterEncoding("UTF-8");
        RequestDispatcher rd = null;
        try {
            pstmt = conn.prepareStatement("select username from user where username = ?");
            pstmt.setString(1, uesrName);
            resultSet = pstmt.executeQuery();
            if (!resultSet.next()) {
                request.setAttribute("msg", "用户名不存在!");
            } else if (uesrName.equals(resultSet.getString("username")) && password.equals(resultSet.getString("password")))
            {
                //如果登录成功，则在用户的session对象中保存一个kvflag，值为login_success
                request.getSession().setAttribute("flag", "login_success");
                //假如用户登录前界面不为空，则跳转到用户登录前界面
                //若用户登录前界面为空，则跳转到首页
                if (returnUri!=null){
                    rd = request.getRequestDispatcher(returnUri);
                    rd.forward(request,response);
                }else {
                    rd =request.getRequestDispatcher("index.jsp");
                    rd.forward(request,response);
                }
            }
            else
            {
                request.getSession().setAttribute("flag","login_error");
                request.setAttribute("msg","用户名或密码错误");
//                rd =request.getRequestDispatcher("..jsp");
//                rd.forward(request,response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void doPost(HttpServletRequest request,HttpServletResponse response)throws ServletException,IOException{
        doGet(request,response);
    }
}

