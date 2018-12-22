package team.redrock.messageBoard.dao;

import team.redrock.messageBoard.been.Message;

import java.util.List;

/**
 * @Description 留言板的dao层接口
 * @Author 余歌
 * @Date 2018/12/16
 **/
public interface MessageBoardDao {

    /**
     * 找pid为${pid}的留言的集合
     *
     * @param pid 留言的父节点
     * @return 父节点为pid的留言的集合
     */
    List<Message> findMessagesByPid(int pid);

    /**
     * 插入一条留言
     *
     * @param message 留言
     */
    void insertMessage(Message message);

}
