package com.pyonsnalcolor.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pyonsnalcolor.auth.dto.TokenDto;
import com.pyonsnalcolor.auth.service.MemberService;
import com.pyonsnalcolor.exception.AuthException;
import com.pyonsnalcolor.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import static com.pyonsnalcolor.exception.model.AuthErrorCode.REFRESH_TOKEN_NOT_EXIST;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@ContextConfiguration(classes = { GlobalExceptionHandler.class })
@AutoConfigureMockMvc
public class AuthControllerTest {

    private static final ContentNegotiationManager contentNegotiationManager = new ContentNegotiationManager();
    private static final StaticApplicationContext applicationContext = new StaticApplicationContext();
    private static final WebMvcConfigurationSupport webMvcConfigurationSupport = new WebMvcConfigurationSupport();

    @Autowired
    private MockMvc mockMvc;
    @Mock
    private MemberService memberService;
    @InjectMocks
    private AuthController authController;

    @BeforeEach
    public void setUp() {
        applicationContext.registerSingleton("exceptionHandler", GlobalExceptionHandler.class);
        webMvcConfigurationSupport.setApplicationContext(applicationContext);

        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .setHandlerExceptionResolvers(webMvcConfigurationSupport
                        .handlerExceptionResolver(contentNegotiationManager))
                .build();
    }

    @DisplayName(value = "Access 토큰 재발급 요청 실패 - 존재하지 않는 Refresh 토큰일 경우 회원 조회 실패")
    @Test
    public void reissueAccessToken_ThrowsException_RefreshTokenNotFound() throws Exception {
        // given
        TokenDto tokenDto = TokenDto.builder().accessToken("accessToken")
                                .refreshToken("refreshToken")
                                .build();
        Mockito.when(memberService.reissueAccessToken(any()))
                .thenThrow(new AuthException(REFRESH_TOKEN_NOT_EXIST));

        // when & then
        mockMvc.perform(
                    post("/auth/reissue")
                        .content(new ObjectMapper().writeValueAsString(tokenDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf())) // /auth/reissue는 security 필터를 타지 않으므로 csrf 설정
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(REFRESH_TOKEN_NOT_EXIST.name()))
                .andExpect(jsonPath("$.message").value(REFRESH_TOKEN_NOT_EXIST.getMessage()))
                .andDo(print());
    }
}