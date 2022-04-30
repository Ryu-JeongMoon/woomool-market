package com.woomoolmarket.util.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionMessages {

  public static class Common {

    public static final String NOT_FOUND = "존재하지 않는 %s 입니다";
    public static final String ILLEGAL_STATE = "요청이 처리될 수 없는 상태입니다";
    public static final String ILLEGAL_ARGUMENT = "올바르지 않은 인자 값 입니다";
    public static final String UNAUTHORIZED_URI = "접근할 수 없는 경로입니다";
    public static final String UNSUPPORTED_OPERATION = "지원하지 않는 요청입니다";
  }

  public static class Member {

    public static final String NOT_FOUND = "존재하지 않는 회원입니다";
    public static final String NOT_LOGIN = "로그인이 필요합니다";
    public static final String ALREADY_LOGOUT = "로그아웃 된 사용자입니다";
    public static final String NOT_LEFT = "탈퇴하지 않은 회원입니다";
    public static final String ALREADY_LEFT = "이미 탈퇴한 회원입니다";
    public static final String ACCESS_DENIED = "요청 권한이 없습니다";
    public static final String BLOCKED = "비밀번호를 5회 이상 틀려 인증이 필요합니다";
    public static final String EMAIL_DUPLICATED = "이미 존재하는 회원 아이디입니다";
    public static final String PASSWORD_NOT_CORRECT = "비밀번호가 맞지 않습니다";
    public static final String PASSWORD_NOT_ENCODED = "비밀번호 암호화에 실패했습니다";
    public static final String PASSWORD_WRONG_FORMAT = "비밀번호가 형식에 맞지 않습니다";
  }

  public static class Board {

    public static final String NOT_FOUND = "존재하지 않는 게시글입니다";
    public static final String NOT_PROPER_DATE = "시작 시간이 종료 시간보다 늦을 수 없습니다";

  }

  public static class Cart {

    public static final String NOT_FOUND = "존재하지 않는 장바구니입니다";
  }

  public static class Order {

    public static final String NOT_FOUND = "존재하지 않는 주문입니다";
    public static final String CANNOT_CANCEL = "배송이 완료된 상품은 취소할 수 없습니다";
  }

  public static class Product {

    public static final String NOT_FOUND = "존재하지 않는 상품입니다";
    public static final String NOT_ENOUGH_STOCK = "재고를 초과해 주문할 수 없습니다";
  }

  public static class Image {

    public static final String FOLDER_NOT_FOUND = "이미지를 업로드할 수 없습니다";
    public static final String NOT_FOUND = "존재하지 않는 이미지입니다";
    public static final String NOT_PROPER_EXTENSION = "확장자가 올바르지 않습니다";
    public static final String CANNOT_TRANSFER = "이미지를 업로드할 수 없습니다";
  }

  public static class Token {

    public static final String NOT_SIGNED = "토큰에 서명할 수 없습니다";
    public static final String NOT_FOUND = "토큰이 존재하지 않습니다";
    public static final String NOT_VALID = "토큰의 데이터가 올바르지 않습니다";
    public static final String WRONG_FORMAT = "토큰의 형태가 올바르지 않습니다";
    public static final String REQUEST_REJECTED = "토큰 요청이 실패했습니다";
    public static final String VALIDATION_REJECTED = "토큰 검증이 실패했습니다";
  }

  public static class File {

    public static final String CONVERT_FAILED = "File 형식의 문제가 있어 전환이 어렵습니다";
  }

  public static class CoolSms {

    public static final String NOT_ENOUGH_BALANCE = "문자를 전송할 수 없는 상태입니다";
  }

}
