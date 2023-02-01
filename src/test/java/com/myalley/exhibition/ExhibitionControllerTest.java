package com.myalley.exhibition;

import com.google.gson.Gson;
import com.myalley.exhibition.controller.ExhibitionController;
import com.myalley.exhibition.dto.request.ExhibitionRequest;
import com.myalley.exhibition.dto.request.ExhibitionUpdateRequest;
import com.myalley.exhibition.dto.response.ExhibitionBasicResponse;
import com.myalley.exhibition.service.ExhibitionService;
import com.myalley.member.config.SpringSecurityConfig;
import com.myalley.member.jwt.JwtAuthenticationFilter;
import com.myalley.member.jwt.JwtAuthorizationFilter;
import com.myalley.member.jwt.JwtUtils;
import com.myalley.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static com.myalley.util.ApiDocumentUtils.getRequestPreProcessor;
import static com.myalley.util.ApiDocumentUtils.getResponsePreProcessor;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;


@WebMvcTest(controllers = ExhibitionController.class,
excludeFilters = {
        @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {SpringSecurityConfig.class, JwtUtils.class,
                JwtAuthenticationFilter.class, JwtAuthorizationFilter.class}
        )})
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
public class ExhibitionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExhibitionService exhibitionService;

    @Autowired Gson gson;

    @MockBean
    private MemberService memberService;

    private static final ExhibitionRequest NEW_EXHIBITION =
            new ExhibitionRequest("제목", "현재 전시", "기획 전시", "DDP",
                    10000, "file 이름", "S3URL", "2023-01-10 ~ 2023 ~ 03-31",
                    "웹사이트 링크", "전시회 소개", "작가 소개");
    private static final ExhibitionBasicResponse RESPONSE_1 = ExhibitionBasicResponse.builder()
            .id(1L)
            .title("제목1")
            .space("미술관1")
            .posterUrl("포스터 URL 1")
            .duration("2023-01-10 ~ 2023 ~ 03-31")
            .type("기획 전시")
            .status("현재 전시")
            .viewCount(0)
            .build();

    private static final ExhibitionBasicResponse RESPONSE_2 = ExhibitionBasicResponse.builder()
            .id(2L)
            .title("제목2")
            .space("미술관2")
            .posterUrl("포스터 URL 2")
            .duration("2023-01-20 ~ 2023 ~ 04-30")
            .type("기획 전시")
            .status("현재 전시")
            .viewCount(0)
            .build();

    private static final ExhibitionBasicResponse updated = ExhibitionBasicResponse.builder()
            .id(1L)
            .title("제목 수정")
            .space("미술관1")
            .posterUrl("포스터 URL 1")
            .duration("2023-01-10 ~ 2023 ~ 03-31")
            .type("기획 전시")
            .status("지난 전시")
            .viewCount(0)
            .build();


    @DisplayName("전시글 등록 요청이 유효하면 새로운 글을 등록한다.")
    @Test
    @WithMockUser(username= "user1", password="password1", authorities= "ROLE_ADMIN" )
    public void postSuccess() throws Exception {

        String content = gson.toJson(NEW_EXHIBITION);

        given(exhibitionService.save(Mockito.any(ExhibitionRequest.class))).willReturn(RESPONSE_1);

        // when
        ResultActions actions =
                mockMvc.perform(
                        post("/api/exhibitions")
                                .header("Authorization", "Bearer {AccessToken}")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                                .with(csrf())
                );
        // then
        actions
                .andExpect(status().isOk())
                .andDo(document("exhibition/save-success",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestParameters(
                                parameterWithName("_csrf").description("csrf").ignored()
                        ),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer + {로그인한 관리자의 유효한 Access 토큰}")
                        ),
                        requestFields(
                                List.of(
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
                                        fieldWithPath("type").type(JsonFieldType.STRING).description("전시회 유형"),
                                        fieldWithPath("space").type(JsonFieldType.STRING).description("전시회 장소"),
                                        fieldWithPath("status").type(JsonFieldType.STRING).description("전시회 관람 여부"),
                                        fieldWithPath("adultPrice").type(JsonFieldType.NUMBER).description("관람 비용"),
                                        fieldWithPath("fileName").type(JsonFieldType.STRING).description("파일명"),
                                        fieldWithPath("posterUrl").type(JsonFieldType.STRING).description("포스터 이미지 Url"),
                                        fieldWithPath("duration").type(JsonFieldType.STRING).description("전시회 기간"),
                                        fieldWithPath("webLink").type(JsonFieldType.STRING).description("웹사이트 링크"),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("전시회 소개"),
                                        fieldWithPath("author").type(JsonFieldType.STRING).description("작가 소개")
                                ))
                ));

    }

    @DisplayName("전시글 수정 요청이 유효하고 포스터를 변경하지 않은 경우 기존 전시글 내용을 수정한다.")
    @Test
    @WithMockUser(username= "user1", password="password1", authorities= "ROLE_ADMIN" )
    public void updateSuccess() throws Exception {
        ExhibitionUpdateRequest updateRequest =
                new ExhibitionUpdateRequest("제목 수정", "지난 전시", "기획 전시", "DDP",
                        10000, "file 이름", "S3URL", "2023-01-10 ~ 2023 ~ 03-31",
                        "웹사이트 링크", "전시회 소개", "작가 소개");

        String content = gson.toJson(updateRequest);
        Long exhibitionId = 1L;

        given(exhibitionService.update(Mockito.any(ExhibitionUpdateRequest.class), Mockito.anyLong())).willReturn(updated);

        // when
        ResultActions actions =
                mockMvc.perform(
                        put("/api/exhibitions/{exhibition-id}", exhibitionId)
                                .header("Authorization", "Bearer {AccessToken}")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                                .with(csrf())
                );
        // then
        actions
                .andExpect(status().isOk())
                .andDo(document("exhibition/update-success",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        pathParameters(
                                parameterWithName("exhibition-id").description("전시글 식별자")
                        ),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer + {로그인한 관리자의 유효한 Access 토큰}")
                        ),
                        requestFields(
                                List.of(
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
                                        fieldWithPath("type").type(JsonFieldType.STRING).description("전시회 유형"),
                                        fieldWithPath("space").type(JsonFieldType.STRING).description("전시회 장소"),
                                        fieldWithPath("status").type(JsonFieldType.STRING).description("전시회 관람 여부"),
                                        fieldWithPath("adultPrice").type(JsonFieldType.NUMBER).description("관람 비용"),
                                        fieldWithPath("fileName").type(JsonFieldType.STRING).description("파일명"),
                                        fieldWithPath("posterUrl").type(JsonFieldType.STRING).description("포스터 이미지 Url"),
                                        fieldWithPath("duration").type(JsonFieldType.STRING).description("전시회 기간"),
                                        fieldWithPath("webLink").type(JsonFieldType.STRING).description("웹사이트 링크"),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("전시회 소개"),
                                        fieldWithPath("author").type(JsonFieldType.STRING).description("작가 소개")
                                ))
                ));

    }

    @DisplayName("전시글 삭제 요청이 유효하면 해당 전시글을 삭제한다.")
    @Test
    @WithMockUser(username= "user1", password="password1", authorities= "ROLE_ADMIN" )
    public void deleteSuccess() throws Exception {

        Long exhibitionId = 1L;

        doNothing().when(exhibitionService).delete(Mockito.anyLong());

        // when
        ResultActions actions =
                mockMvc.perform(
                        delete("/api/exhibitions/{exhibition-id}", exhibitionId)
                                .header("Authorization", "Bearer {AccessToken}")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf())
                );
        // then
        actions
                .andExpect(status().isOk())
                .andDo(document("exhibition/delete-success",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        pathParameters(
                                parameterWithName("exhibition-id").description("전시글 식별자")
                        ),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer + {로그인한 관리자의 유효한 Access 토큰}")
                        )
                ));
    }

}
