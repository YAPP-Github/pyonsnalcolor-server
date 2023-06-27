package com.pyonsnalcolor.fcm;

import com.pyonsnalcolor.dto.DeviceTokenRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.concurrent.ExecutionException;

@Controller
@RequiredArgsConstructor
public class FcmController {

    private final FcmPushService fcmPushService;

    // ios 테스트용
    @GetMapping("/fcm/test")
    public ResponseEntity fcmTest(DeviceTokenRequestDto deviceTokenRequestDto) throws ExecutionException, InterruptedException {
        fcmPushService.pbFcmTest(deviceTokenRequestDto);
        return new ResponseEntity(HttpStatus.OK);
    }
}