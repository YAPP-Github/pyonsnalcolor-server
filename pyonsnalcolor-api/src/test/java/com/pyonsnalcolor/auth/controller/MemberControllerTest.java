package com.pyonsnalcolor.auth.controller;

import com.pyonsnalcolor.auth.service.MemberService;
import com.pyonsnalcolor.exception.ApiException;
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
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import static com.pyonsnalcolor.exception.model.AuthErrorCode.ACCESS_TOKEN_NOT_BEARER;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
@ContextConfiguration(classes = { GlobalExceptionHandler.class })
@AutoConfigureMockMvc
public class MemberControllerTest {

    private static final ContentNegotiationManager contentNegotiationManager = new ContentNegotiationManager();
    private static final StaticApplicationContext applicationContext = new StaticApplicationContext();
    private static final WebMvcConfigurationSupport webMvcConfigurationSupport = new WebMvcConfigurationSupport();

    @Autowired
    private MockMvc mockMvc;
    @Mock
    private MemberService memberService;
    @InjectMocks
    private MemberController memberController;

    @BeforeEach
    public void setUp() {
        applicationContext.registerSingleton("exceptionHandler", GlobalExceptionHandler.class);
        webMvcConfigurationSupport.setApplicationContext(applicationContext);

        mockMvc = MockMvcBuilders.standaloneSetup(memberController)
                .setHandlerExceptionResolvers(webMvcConfigurationSupport
                        .handlerExceptionResolver(contentNegotiationManager))
                .build();
    }

    @DisplayName(value = "사용자 정보 요청 실패")
    @WithAnonymousUser
    @Test
    public void fail_getMemberInfo() throws Exception {
        //when
        Mockito.when(memberService.getMemberInfo(any()))
                .thenThrow(new ApiException(ACCESS_TOKEN_NOT_BEARER));

        // then
        mockMvc.perform(get("/member/infos"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(ACCESS_TOKEN_NOT_BEARER.name()))
                .andExpect(jsonPath("$.message").value(ACCESS_TOKEN_NOT_BEARER.getMessage()))
                .andDo(print());
    }
}