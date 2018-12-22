package team.redrock.messageBoard.service.impl;

import team.redrock.messageBoard.been.Message;
import team.redrock.messageBoard.dao.MessageBoardDao;
import team.redrock.messageBoard.dao.impl.MessageBoardDaoImpl;
import team.redrock.messageBoard.service.MessageBoardService;

import java.util.List;

/**
 * @Description 留言的service
 * @Author 余歌
 * @Date 2018/12/16
 **/
public class MessageBoardServiceImpl implements MessageBoardService {

    //service的单例
    private static MessageBoardService instance = null;

    //dao的单例
    private MessageBoardDao messageBoardDao = null;

    /**
     * 构造方法 实例化时为contentDao赋值
     */
    public MessageBoardServiceImpl() {
        messageBoardDao = MessageBoardDaoImpl.getInstance();
    }

    /**
     * 得到service的单例
     *
     * @return service
     */
    public static MessageBoardService getInstance() {
        //双重校验锁
        if (instance == null) {
            synchronized (MessageBoardServiceImpl.class) {
                if (instance == null) {
                    instance = new MessageBoardServiceImpl();
                }
            }
        }
        return instance;
    }

    /**
     * 找一个留言下面的所有评论---------------------------------
     * <p>
     * 这里使用了递归 深度优先搜索的思想
     * 先查这条评论的子节点
     * 然后遍历子节点 递归调用这个方法 讲评论压成一棵树
     *
     * @param content 评论
     * @return 评论的子节点
     */
    private List<Message> findContentChild(Message content) {
        //找该条评论的子节点 即pid为该条评论id的评论------------------------
        List<Message> list = messageBoardDao.findMessagesByPid(content.getId());

        //遍历它的子节点 然后递归找每条评论下的评论 即节点的子节点
        for (Message message : list) {
            //递归找这条评论的节点 然后赋值
            List<Message> childList = findContentChild(message);
            message.setChildContent(childList);
        }
        return list;
    }

    /**
     * 查找所有的留言及其评论--------------------------------------
     *
     * @return 留言的集合
     */
    public List<Message> findAllMessages() {

        //先找到pid为0的所有留言 即留言板上所有无父节点的留言
        List<Message> list = messageBoardDao.findMessagesByPid(0);

        //然后找 pid !=0 的每条留言的评论 并赋值
        for (Message message : list) {
            List<Message> childList = findContentChild(message);
            message.setChildContent(childList);
        }

        return list;
    }

    /**
     * 将留言的集合组装成json
     * @param messageList 留言的集合
     * @return  json?
     */
    @Override
    public String messagesToJson(List<Message> messageList) {
        //组装json 这里我是自己手动组装的 你们可以用JSONObject和JSONArray

        //使用  Builder线程相对之前会不安全，但是性能会更好
        StringBuilder sb = new StringBuilder();

        //前面的共同的部分
        sb.append("{\"status\":10000,\"data\":[");

        //如果它有子节点
        if (messageList != null && messageList.size() != 0) {

            //这里的思想和上面的思想一样 深度优先遍历(DFS) 组装出来评论的json
            for (Message content : messageList) {
                sb.append(createJson(content));
                sb.append(",");
            }

            if (sb.charAt(sb.length() - 1) == ',') {
                sb.delete(sb.length() - 1, sb.length());
            }
        }
        sb.append("]}");

        return sb.toString();
    }

    /**
     * 插入一条留言
     *
     * @param message 留言
     * @return 是否成功
     */
    @Override
    public boolean insertContent(Message message) {

        if (message.getUsername() != null && message.getText() != null) {

            messageBoardDao.insertMessage(message);
            return true;

        }

        return false;

    }

    /**
     * 递归调用用来组装json的方法 思想是和上面的一样的 不再赘述
     *
     * @param message 留言
     * @return
     */
    private String createJson(Message message) {


        //使用  Builder线程相对之前会不安全，但是性能会更好
        StringBuilder sb = new StringBuilder();

        sb.append("{\"id\":").append(message.getId())
                .append(",\"username\":\"").append(message.getUsername())
                .append("\",\"text\":\"").append(message.getText())
                .append("\"").append(",\"child\":[");


        List<Message> child = message.getChildContent();

        for (Message content : child) {

            String json = createJson(content);

            sb.append(json).append(",");

        }

        if (sb.charAt(sb.length() - 1) == ',') {
            sb.delete(sb.length() - 1, sb.length());
        }

        sb.append("]}");

        return sb.toString();
    }


}
