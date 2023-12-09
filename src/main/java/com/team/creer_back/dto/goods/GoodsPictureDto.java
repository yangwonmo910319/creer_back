package com.team.creer_back.dto.goods;

import com.team.creer_back.dto.member.MemberDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GoodsPictureDto {
    private Long goodsPictureId;
    private Long goodsDetailId; // 연결된 GoodsDetail의 ID
    private List<String> goodsPictures;       // 이미지 주소
}