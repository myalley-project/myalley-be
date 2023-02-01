package com.myalley.member.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.myalley.member.config.LocalDateSerializer;
import com.myalley.member.dto.LoginDto;
import com.myalley.member.dto.MemberRegisterDto;
import com.myalley.member.dto.Token;
import com.myalley.member.options.Gender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.transaction.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles(profiles="test")
@Transactional
@AutoConfigureMockMvc
class MemberServiceTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MemberService memberService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ObjectMapper objectMapper;

    private String accessToken;
    @Test
    void signup() throws Exception {

        MemberRegisterDto member1=MemberRegisterDto.builder()
                .email("test@naver.com")
                .password("Test1234!")
                .nickname("test")
                .birth(LocalDate.parse("2000-01-01"))
                .gender(Gender.valueOf("W"))
                .build();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class,new LocalDateSerializer());
        Gson gson= gsonBuilder.setPrettyPrinting().create();
        String content=gson.toJson(member1);

        //memberService.signup(member1);
        mockMvc.perform(post("/signup")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                .andDo(print());

        LoginDto loginDto=LoginDto.builder()
                .email("test@naver.com")
                .password("Test1234!")
                .build();
        content=gson.toJson(loginDto);


        MvcResult result=mockMvc.perform(post("/login")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        ObjectMapper mapper = new ObjectMapper();

        content=result.getResponse().getContentAsString();
        Token token=mapper.readValue(content, Token.class);



        //memberService.signup(member2);


    }
    @BeforeEach()
    void setup() throws Exception {

        MemberRegisterDto member1=MemberRegisterDto.builder()
                .email("test@naver.com")
                .password("Test1234!")
                .nickname("test")
                .birth(LocalDate.parse("2000-01-01"))
                .gender(Gender.valueOf("W"))
                .build();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class,new LocalDateSerializer());
        Gson gson= gsonBuilder.setPrettyPrinting().create();

        String content=gson.toJson(member1);

        mockMvc.perform(post("/signup")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        LoginDto loginDto=LoginDto.builder()
                .email("test@naver.com")
                .password("Test1234!")
                .build();
        content=gson.toJson(loginDto);


        MvcResult result=mockMvc.perform(post("/login")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        ObjectMapper mapper = new ObjectMapper();

        content=result.getResponse().getContentAsString();
        Token token=mapper.readValue(content, Token.class);
        accessToken="Bearer "+token.getAccessToken();
    }
    @Test
    void memberInfo() throws Exception {
        mockMvc.perform(get("/api/me")
                        .header("Authorization",accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
    @Test
    void signupAdmin() {
    }

    @Test
    void findByEmail() {
    }
}