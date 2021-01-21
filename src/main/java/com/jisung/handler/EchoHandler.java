package com.jisung.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import lombok.extern.log4j.Log4j;

@Log4j
public class EchoHandler extends TextWebSocketHandler {
	
	//로그인한 전체
	List<WebSocketSession> sessions = new ArrayList<WebSocketSession>();
	
	//아이디는 고유한값 , 로그인한 유저목록
	Map<String, WebSocketSession> userSession = new HashMap<String, WebSocketSession>();
	
	//웹소켓 email 가져오기
	private String getEmail(WebSocketSession session) {
		Map<String, Object> httpSession = session.getAttributes();
		//현재 유저의 아이디 가져오기
		Object principal = ((SecurityContext)httpSession.get("SPRING_SECURITY_CONTEXT")).getAuthentication().getPrincipal();
		String userId = ((UserDetails)principal).getUsername();
		if(userId == null) {
			return session.getId();
		}else {
			return userId;
		}
	}
	
	//클라이언트와의 웹소켓 연결이 수립된 직후에 실행되는 핸들러 메소드이다
	//사용자 접속 시 서버에서 수행해야 할 작업이 있다면 이곳에서 처리한다.(ex : 사용자 등록, 접속 알림 등)
	// 서버에 접속이 성공 했을때
	// session :  사용자의 웹소켓 정보(HttpSession 아님)
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		log.info("afterConnectionEstablished ");
		sessions.add(session);
		
		String senderId = getEmail(session);
		log.info("senderId "+ senderId);
		userSession.put(senderId,session);
		
		log.info("sessions : "+sessions);
		log.info("useSession : "+userSession);
		
	}

	//클라이언트가 연결된 웹소켓을 통해 전송한 텍스트 메세지를 수신한 직후에 실행되는 핸들러 메소드
	//메세지 수신 시 실행될 메소드
	/*
	 * session : 사용자(전송한 사용자)의 웹소켓 정보(HttpSession이 아님)
	 * message : 사용자가 전송한 메세지 정보
	 *			payload : 실제 보낸 내용
	 *			byteCount : 보낸 메세지 크기(byte)
	 *			last : 메세지 종료 여부
	 */
	//2020-12-30 : socket으로 보낸 메세지 처리
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		//socket.send한 내용을 받음
		String msg = message.getPayload();
		System.out.println(msg);
		//msg가 비어있지 않으면
		if(!StringUtils.isEmpty(msg)) {
			String[] strs = msg.split(",");
			//메세지를 쪼갬
			if(strs != null && strs.length == 4) {
				String cmd = strs[0];
				String receiverId = strs[1];
				String callerId = strs[2]; 
				String boardId = strs[3];
				log.info(cmd+","+receiverId+","+callerId+","+boardId);
				
				WebSocketSession loginUser = userSession.get(receiverId);
				
				//receiverId에 메세지를 보냄
				if("reply".equals(cmd) && loginUser != null) {
					TextMessage tmpMsg = new TextMessage(callerId + "님이 " + 
										"<a type='external' href='/board/get?pageNumber=1&boardId="+boardId+"'>" + boardId + "</a> 번 게시글에 댓글을 남겼습니다.");
					loginUser.sendMessage(tmpMsg);
				}
			}
		}
	}

	//클라이언트와의 웹소켓 연결이 종료된 직후에 실행되는 핸들러 메소드이다
	// 사용자 접속 종료 시 실행되는 메소드
	// session : 사용자(전송한 사용자)의 웹소켓 정보(HttpSession이 아님)
	// status : 접속이 종료된 원인과 관련된 정보
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		userSession.remove(session.getId());
		sessions.remove(session);
	}
	
}
