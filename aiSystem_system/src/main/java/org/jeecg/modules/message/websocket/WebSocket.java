package org.jeecg.modules.message.websocket;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.apache.commons.lang3.StringUtils;
import org.jeecg.modules.ability.service.FaceTask;
import org.jeecg.modules.ability.vo.PicDataInput;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author scott
 * @Date 2019/11/29 9:41
 * @Description: 此注解相当于设置访问URL
 */

@Component
@Slf4j
@ServerEndpoint("/websocket/{userId}") //此注解相当于设置访问URL
public class WebSocket {
    
    private Session session;


    private static CopyOnWriteArraySet<WebSocket> webSockets =new CopyOnWriteArraySet<>();
    private static Map<String,Session> sessionPool = new HashMap<String,Session>();
    //数据缓冲区
    private BlockingQueue queue ;
    private List<Float>  frameXList ;
    private List<Float>  frameYList ;

    /**
     * 解决无法注入
     */
    public static ApplicationContext applicationContext;

    //你要注入的bean
    private FaceTask faceTask;
    //在主入口中注入applicationContext
    public static void setApplicationContext(ApplicationContext applicationContext){
        WebSocket.applicationContext = applicationContext;
    }
    @OnError
    public void onerror(Session session, Throwable throwable){
        try {
            webSockets.remove(this);
            log.info("【websocket消息】err 连接断开，总数为:"+webSockets.size());
        } catch (Exception e) {
        }
    }

    @OnOpen
    public void onOpen(Session session, @PathParam(value="userId")String userId) {
        faceTask = (FaceTask) applicationContext.getBean("faceTask");
        try {
			this.session = session;
            this.queue = new ArrayBlockingQueue(300);
            this.frameXList = new LinkedList<>();
            this.frameYList = new LinkedList<>();
			webSockets.add(this);
			sessionPool.put(userId, session);
            faceTask.delFace(session,this.queue,this.frameXList,this.frameYList);
            session.setMaxTextMessageBufferSize(25000000);
            log.info("【websocket消息】有新的连接，userID:"+userId);
			log.info("【websocket消息】有新的连接，总数为:"+webSockets.size());
		} catch (Exception e) {
		}
    }
    
    @OnClose
    public void onClose() {
        try {
			webSockets.remove(this);
			log.info("【websocket消息】连接断开，总数为:"+webSockets.size());
		} catch (Exception e) {
		}
    }
    
    @OnMessage
    public void onMessage(String message) {

    	log.debug("【websocket消息】收到客户端消息:"+message);
    	if(StringUtils.isEmpty(message)|| JSONObject.isValidObject(message)){
            //todo 视屏针有可能丢弃 后面处理
            if (!queue.offer( JSONObject.parseObject(message,PicDataInput.class)))log.error("视屏缓冲区已满");
        }else{
            //todo 异常信息返回
            session.getAsyncRemote().sendText("错误");
        }
    }
    
    // 此为广播消息
    public void sendAllMessage(String message) {
    	log.info("【websocket消息】广播消息:"+message);
        for(WebSocket webSocket : webSockets) {
            try {
            	if(webSocket.session.isOpen()) {
            		webSocket.session.getAsyncRemote().sendText(message);
            	}
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    // 此为单点消息
    public void sendOneMessage(String userId, String message) {
        Session session = sessionPool.get(userId);
        if (session != null&&session.isOpen()) {
            try {
            	log.debug("【websocket消息】 单点消息:"+message);
                session.getAsyncRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    // 此为单点消息(多人)
    public void sendMoreMessage(String[] userIds, String message) {
    	for(String userId:userIds) {
    		Session session = sessionPool.get(userId);
            if (session != null&&session.isOpen()) {
                try {
                	log.debug("【websocket消息】 单点消息:"+message);
                    session.getAsyncRemote().sendText(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
    	}
        
    }
    
}