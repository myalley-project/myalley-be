package com.myalley.member;
//
//import com.amazonaws.services.s3.AmazonS3;
//import com.myalley.S3MockConfig;
//import com.myalley.member.domain.Member;
//import com.myalley.member.jwt.JwtUtils;
//import io.findify.s3mock.S3Mock;
//import org.junit.jupiter.api.*;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.http.MediaType;
//
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.context.web.WebAppConfiguration;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.web.context.WebApplicationContext;
//
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@AutoConfigureMockMvc
//@SpringBootTest
//@ActiveProfiles(profiles="test")
//@Import(S3MockConfig.class)
//@WebAppConfiguration
//@ExtendWith(SpringExtension.class)
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//class MemberApiTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private AmazonS3 amazonS3;
//
//    @Autowired
//    private WebApplicationContext context;
//
//    private static String accessToken;
//
//    @BeforeAll()
//    void setup(@Autowired S3Mock s3Mock, @Autowired AmazonS3 amazonS3) throws Exception {
//        //mockMvc= MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
//
//        s3Mock.start();
//        amazonS3.createBucket("profile");
//    }
//    @AfterAll
//    static void tearDown(@Autowired S3Mock s3Mock, @Autowired AmazonS3 amazonS3) throws Exception {
//
//        amazonS3.shutdown();
//        s3Mock.stop();
//    }
//    @Test
//    @DisplayName("본인 정보 조회 성공")
//    @WithMockCustomUser
//    void memberInfo() throws Exception {
////        accessToken= JwtUtils.createToken((Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
////
////        this.mockMvc.perform(get("/api." +
////                        "/me")
////                        .header("Authorization","Bearer "+accessToken)
////                        .contentType(MediaType.APPLICATION_JSON)
////                        .accept(MediaType.APPLICATION_JSON))
////                    .andExpect(status().isOk())
////                    .andDo(print());
////                        document("get-memberInfo",
////                                responseFields(
////                                        fieldWithPath("memberId").description("memberId")
////                                ))
////                );
//    }
//
//}