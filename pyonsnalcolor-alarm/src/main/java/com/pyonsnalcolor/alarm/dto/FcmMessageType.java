package com.pyonsnalcolor.alarm.dto;

import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.Getter;

@Getter
public enum FcmMessageType {

    PB("새로운 차별화 상품이 업데이트 되었어요! \uD83D\uDC95", ""),
    EVENT("새로운 행사가 시작되었어요! \uD83C\uDF81", ""),
    KEYWORD_PB("가 업데이트되었어요\uD83D\uDC95", ""),
    KEYWORD_EVENT("의 행사가 시작되었어요\uD83C\uDF81", "");

    private static final String DEEPLINK = "deeplink";
    private static final String PYONSNAL_COLOR = "편스널 컬러";
    private static final String IMAGE = ""; // 후에 이미지 추가

    private final String bodyMessage;
    private final String deeplink;

    FcmMessageType(String bodyMessage, String deeplink) {
        this.bodyMessage = bodyMessage;
        this.deeplink = deeplink;
    }

    public Message createMessage(String deviceToken) {
        Notification notification = createNotification(deviceToken);

        return Message.builder()
                .setToken(deviceToken)
                .setNotification(notification)
                .putData(DEEPLINK, deeplink) // 이후에 상품id 추가
                .build();
    }

    private Notification createNotification(String deviceToken) {
        return Notification.builder()
                .setTitle(PYONSNAL_COLOR)
                .setBody(PB.getBodyMessage())
                // .setImage() // 이후에 아이콘 이미지 추가
                .build();
    }
}