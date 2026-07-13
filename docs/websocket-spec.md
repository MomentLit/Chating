# WebSocket Spec (Chat)

채팅 실시간 메시지 송수신 규약. REST API(`POST /chat`, `GET /chat`, `GET /chat/{chat-room-id}/messages`)는 `API_SPEC.yaml`을 따른다.

## 프로토콜

STOMP over WebSocket (SockJS 미사용)

## 접속

- 엔드포인트: `ws://{host}/ws/chat`
- 허용 Origin: 전체 허용 (`setAllowedOriginPatterns("*")`)

## 인증

- STOMP `CONNECT` 프레임의 `Authorization` 네이티브 헤더로 JWT를 전달한다.

```
CONNECT
Authorization: Bearer {access_token}
```

- 토큰이 없거나 유효하지 않으면 서버가 `ERROR` 프레임을 보내고 연결을 종료한다.
- 인증된 사용자 정보(JWT subject = userId)가 세션 Principal로 설정된다.

## 메시지 발행 (전송)

- 목적지: `/app/chat/{chat-room-id}`
- payload:

```json
{
  "content": "메시지 내용"
}
```

- 처리: 메시지를 `is_read=false`로 저장한 뒤 해당 채팅방 구독자에게 브로드캐스트한다.
- 채팅방 참여자(host 또는 seller)가 아니거나, 채팅방이 없거나, `content`가 비어 있거나,
  공간이 삭제된 채팅방이면 메시지는 저장·브로드캐스트되지 않고 에러가 회신된다(아래 에러 회신 참고).

## 메시지 구독 (수신)

- 목적지: `/topic/chat/{chat-room-id}`
- 채팅방 참여자(host 또는 seller)만 구독할 수 있다. 참여자가 아니거나 채팅방이 없으면
  서버가 `ERROR` 프레임을 보내고 연결을 종료한다.
- 브로드캐스트 payload (`API_SPEC.yaml`의 `chatMessageHistorySearchResponse`와 동일):

```json
{
  "message_id": 1,
  "sender_name": "홍길동",
  "sender_id": "user-uuid",
  "content": "메시지 내용",
  "is_read": false,
  "created_at": "2026-07-13T12:00:00"
}
```

## 에러 회신

- 클라이언트는 `/user/queue/errors`를 구독해 발행 실패 에러를 수신할 수 있다.
- 발행 처리 중 오류(참여자 아님, 채팅방 없음, 빈 내용, 삭제된 공간 등)가 발생하면
  발행자에게만 아래 형식으로 회신된다.

```json
{
  "message": "[ERROR: Chat/Message] 삭제된 공간의 채팅방에는 메시지를 보낼 수 없습니다.",
  "data": null
}
```

## 읽음 처리

- 웹소켓 수신 자체로는 읽음 처리되지 않는다.
- `GET /chat/{chat-room-id}/messages` 조회 시 상대방이 보낸 읽지 않은 메시지가 읽음 처리된다.

## 삭제된 공간 정책

- 공간이 삭제된 채팅방은 메시지 전송이 불가하다(발행 시 공간 존재를 확인).
- 채팅방 목록 조회와 메시지 내역 조회는 계속 가능하다(공간·유저 이름은 채팅방 생성
  시점에 저장된 값을 사용).
