package com.example.chating.global.exception;

import com.example.chating.global.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<String>> badRequestHandleException(BadRequestException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.fail("[ERROR: Request/BadRequest] " + e.getMessage()));
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ApiResponse<String>> forbiddenHandleException(ForbiddenException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.fail("[ERROR: Space/Forbidden] " + e.getMessage()));
    }

    @ExceptionHandler(SpaceNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> spaceNotFoundHandleException(SpaceNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.fail("[ERROR: Space/NotFound] " + e.getMessage()));
    }

    @ExceptionHandler(ScheduleNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> scheduleNotFoundHandleException(ScheduleNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.fail("[ERROR: Space/Schedule/NotFound] " + e.getMessage()));
    }

    @ExceptionHandler(ChatRoomNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> chatRoomNotFoundHandleException(ChatRoomNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.fail("[ERROR: Chat/NotFound] " + e.getMessage()));
    }

    @ExceptionHandler(SpaceClientException.class)
    public ResponseEntity<ApiResponse<String>> spaceClientHandleException(SpaceClientException e) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body(ApiResponse.fail("[ERROR: Chat/SpaceClient] " + e.getMessage()));
    }

    @ExceptionHandler(UserClientException.class)
    public ResponseEntity<ApiResponse<String>> userClientHandleException(UserClientException e) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body(ApiResponse.fail("[ERROR: Chat/UserClient] " + e.getMessage()));
    }

    @ExceptionHandler(SpaceException.class)
    public ResponseEntity<ApiResponse<String>> spaceHandleException(SpaceException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.fail("[ERROR: Space/?] " + e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> globalHandleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.fail("[ERROR: ?/?] 서버 내부 오류가 발생했습니다."));
    }
}
