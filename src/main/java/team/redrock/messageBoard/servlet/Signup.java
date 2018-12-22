package team.redrock.messageBoard.servlet;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import team.redrock.messageBoard.dao.impl.MessageBoardDaoImpl;

import javax.security.sasl.SaslException;
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

@WebServlet("/sign")
public class Signup extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection conn = new MessageBoardDaoImpl().getConnection();
        String userName = request.getParameter("u");
        String password = request.getParameter("p");
        response.setContentType("text/html;charset=utf-8");
        response.setCharacterEncoding("UTF-8");
        if (userName ==null || password ==null){
            response.getWriter().write("抱歉！用户名或密码不能为空");
    }
        PreparedStatement pstmt =null;
        ResultSet resultSet = null;
        try {
            pstmt =conn.prepareStatement("select username from user where username = ?");
            pstmt.setString(1,userName);
            resultSet = pstmt.executeQuery();
            if (resultSet.next()){
                response.getWriter().write("用户名已经被注册");
            }
            else {
                pstmt = conn.prepareStatement("insert into user(username,password) values (?,?)");
                pstmt.setString(1,userName);
                pstmt.setString(2,password);
                pstmt.executeUpdate();
                response.getWriter().write("恭喜！注册成功！");
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        }
        public void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException,ServletException{
    }
}

