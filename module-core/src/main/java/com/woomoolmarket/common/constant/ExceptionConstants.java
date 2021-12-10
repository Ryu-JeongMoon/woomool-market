package com.woomoolmarket.common.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionConstants {

    public static final String MEMBER_NOT_FOUND = "존재하지 않는 회원입니다";
    public static final String MEMBER_BLOCKED = "비밀번호를 5회 이상 틀려 인증이 필요합니다";
    public static final String MEMBER_EMAIL_DUPLICATED = "이미 존재하는 회원 아이디입니다";
    public static final String MEMBER_NOT_LOGIN = "로그아웃 된 사용자입니다";

    public static final String PRODUCT_NOT_FOUND = "존재하지 않는 상품입니다";
    public static final String PRODUCT_NOT_ENOUGH_STOCK = "재고를 초과해 주문할 수 없습니다";

    public static final String ORDER_NOT_FOUND = "존재하지 않는 주문입니다";
    public static final String ORDER_CANNOT_CANCEL = "배송이 완료된 상품은 취소할 수 없습니다";

    public static final String BOARD_NOT_FOUND = "존재하지 않는 게시글입니다";
    public static final String BOARD_DATE_NOT_PROPER = "시작일시는 종료일시보다 늦을 수 없습니다";

    public static final String IMAGE_NOT_FOUND = "존재하지 않는 이미지입니다";
    public static final String IMAGE_NOT_PROPER_EXTENSION = "확장자가 올바르지 않습니다";
    public static final String IMAGE_CANNOT_TRANSFER = "이미지를 업로드할 수 없습니다";
    public static final String IMAGE_FOLDER_NOT_FOUND = "이미지를 업로드할 수 없습니다";

    public static final String CART_NOT_FOUND = "존재하지 않는 장바구니입니다";

    public static final String REFRESH_TOKEN_NOT_FOUND = "유효하지 않은 토큰입니다";

    public static final String NOT_ENOUGH_BALANCE = "문자를 전송할 수 없는 상태입니다";

    public static final String CONVERT_FAILED = "File 형식의 문제가 있어 전환이 어렵습니다";
}
