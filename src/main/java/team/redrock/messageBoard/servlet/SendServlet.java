package team.redrock.messageBoard.servlet;

import team.redrock.messageBoard.been.Message;
import team.redrock.messageBoard.service.MessageBoardService;
import team.redrock.messageBoard.service.impl.MessageBoardServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

@WebServlet("/send")
public class SendServlet extends HttpServlet {

    /**
     * 这里使用两个静态常量 来节省开销
     */
    //失败
    private static final String ERROR="{\"status\":\"10001\"}";
    //成功
    private static final String OK="{\"status\":\"10000\"}";

    //service的单例
    private MessageBoardService messageBoardService;

    /**
     * 重写初始化方法 为service赋值
     */
    @Override
    public void init() {
        messageBoardService = MessageBoardServiceImpl.getInstance();
    }

    /**
     * send是一个Post请求 它对数据库做了修改 所以要用post请求
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String username = request.getParameter("username");
        String text = request.getParameter("text");
        int pid = Integer.parseInt(request.getParameter("pid"));

        Message message = new Message(username, text, pid);

        String res = ERROR;

        //如果插入成功 就回复成功
        if (messageBoardService.insertContent(message)) {
            res = OK;
        }

        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(
                        response.getOutputStream()
                )
        );

        writer.write(res);
        writer.flush();
        writer.close();
    }
}
