package com.team.creer_back.dto.goods;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor  // 모든 필드 값을 파라미터로 받는 생성자를 생성
@NoArgsConstructor   // 파라미터가 없는 디폴트 생성자를 생성
@Builder  // 클래스 레벨에 붙이거나 생성자에 붙여주면 파라미터를 활용하여 빌더 패턴을 자동으로 생성해줌
public class GoodsReviewDto {
    private Long goodsReviewId;
    private Long goodsDetailId;          // FK-물품 번호 찾기
    private String nickName;            // FK-구매자 닉네임
    private String memberProfileImg;             // FK-구매자 프사
    private LocalDateTime reviewDate;       // 후기 작성 시각
    private String reviewStar;              // 별점
    private String reviewImg;               // 후기 사진
    private String reviewContent;           // 후기 글
}