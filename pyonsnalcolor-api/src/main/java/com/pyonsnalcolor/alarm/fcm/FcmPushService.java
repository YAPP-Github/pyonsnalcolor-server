package com.pyonsnalcolor.alarm.fcm;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.pyonsnalcolor.alarm.dto.DeviceTokenRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import static com.pyonsnalcolor.alarm.dto.FcmMessageType.PB;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmPushService {

    @Value("${fcm.key}")
    private String FCM_KEY;

    @Value("${fcm.scope}")
    private String FIREBASE_SCOPE;

    private FirebaseMessaging firebaseMessaging;

    // @PostConstruct
    public void fcmInit() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(FCM_KEY.getBytes());

        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(inputStream)
                .createScoped((Arrays.asList(FIREBASE_SCOPE)));

        FirebaseOptions firebaseOptions = FirebaseOptions.builder()
                .setCredentials(googleCredentials)
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp app = FirebaseApp.initializeApp(firebaseOptions);
            this.firebaseMessaging = FirebaseMessaging.getInstance(app);
            log.info("Firebase 어플리케이션을 초기화 했습니다.");
        }
    }

    // 테스트용 PB 상품 알림
    public void pbFcmTest(DeviceTokenRequestDto deviceTokenRequestDto) throws ExecutionException, InterruptedException {
        String deviceToken = deviceTokenRequestDto.getDeviceToken();
        log.info("알림 테스트 시작, 사용자 토큰 : {}", deviceToken);

        Message message = PB.createMessage(deviceToken);
        this.firebaseMessaging.sendAsync(message).get();
    }
}