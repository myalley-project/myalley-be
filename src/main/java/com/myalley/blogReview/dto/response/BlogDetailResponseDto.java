package com.myalley.blogReview.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.myalley.blogReview.domain.BlogReview;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogDetailResponseDto {
    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate viewDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
    private String title;
    private String content;
    private String time;
    private Integer likeCount;
    private Integer viewCount;
    private Integer bookmarkCount;
    private String transportation;
    private String revisit;
    private String congestion;
    private boolean likeStatus;
    private boolean bookmarkStatus;
    private List<ImageDto> imageInfo;

    //멤버에서 가져올 예정
    private SimpleMemberDto memberInfo;
    //전시회에서 가져올 예정
    private SimpleExhibitionDto exhibitionInfo;

    public static BlogDetailResponseDto of(BlogReview blog, boolean likeStatus, boolean bookmarkStatus){
        return new BlogDetailResponseDto(blog.getId(),blog.getViewDate(),blog.getCreatedAt(),
                blog.getTitle(),blog.getContent(),blog.getTime(), blog.getLikeCount(), blog.getViewCount(),
                blog.getBookmarkCount(), blog.getTransportation(), blog.getRevisit(), blog.getCongestion(),
                likeStatus,bookmarkStatus,blog.getImages().stream().map(ImageDto::from).collect(Collectors.toList()),
                SimpleMemberDto.from(blog.getMember()), SimpleExhibitionDto.from(blog.getExhibition()));

        //blogDto.setImageInfo(imageToImageDtoList(blog.getBlogReview().getImages())); -> null인 경우랑 아닌 경우를 나눌까?XX 여기서 확인하고 케이스에 맞게 생성하자
        //blogDto.setMemberInfo(); -> Member를 받아서 dto 생성하고 넣기. 그니까 blogDto.setMemberInfo(MemberDto.from(member));
        //blogDto.setExhibitionInfo(); -> 위와 동일한 방식으로 진행하기 (근데 이걸 여기서 할까 서비스에서 해서 보내줄까.. 고민)
        
        //더 생각이 필요한 부분 - 해당 버튼에 불이 들어와야되는지 (클릭된 글인지) -> 서비스 쪽에서 결과를 매개변수 String 값으로 넘겨줘서 케이스별로 처리하도록?
        //switch case 사용? 한다면 아래처럼 하는 게 어떨지

        // 1)none : break; 2)like :
        // 3)bookmark : blogDto.setBookmarkStatus = true; break; 4)both : blogDto.setLikeStatus = true; blogDto.setBookmarkStatus = true; break;
        //1. blogDto.setLikeStatus(blog);
        //2. blogDto.setBookmarkStatus(blog.isBookmarkStatus());
        /*
        BlogDetailResponseDto blogDto = new BlogDetailResponseDto();
        blogDto.setId(blog.getId());
        blogDto.setTitle(blog.getTitle());
        blogDto.setContent(blog.getContent());
        blogDto.setTime(blog.getTime());
        blogDto.setLikeCount(blog.getLikeCount());
        blogDto.setBookmarkCount(blog.getBookmarkCount());
        blogDto.setTransportation(blog.getTransportation());
        blogDto.setCongestion(blog.getCongestion());
        blogDto.setRevisit(blog.getRevisit());
        blogDto.setViewDate(blog.getViewDate());
        blogDto.setCreatedAt(blog.getCreatedAt());
        blogDto.setViewCount(blog.getViewCount());

        blogDto.setLikeStatus(likeStatus);
        blogDto.setBookmarkStatus(bookmarkStatus);
         */
    }
}
