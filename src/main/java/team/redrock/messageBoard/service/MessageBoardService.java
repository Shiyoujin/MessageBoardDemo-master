package team.redrock.messageBoard.service;

import team.redrock.messageBoard.been.Message;

import java.util.List;


/**
 * @Description 留言service的接口
 * @Author 余歌
 * @Date 2018/12/16
 **/
public interface MessageBoardService {

    /**
     * 查找所有的留言
     *
     * @return 留言的集合
     */
    List<Message> findAllMessages();

    /**
     * 将留言组装成json
     * @param messageList 留言的集合
     * @return json
     */
    String messagesToJson(List<Message> messageList);

    /**
     * 插入一条留言
     *
     * @param message 留言
     * @return 成功与否
     */
    boolean insertContent(Message message);

}
