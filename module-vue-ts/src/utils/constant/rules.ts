/* eslint-disable @typescript-eslint/no-explicit-any,@typescript-eslint/explicit-module-boundary-types */
import { isNumeric } from "@/utils/common/conversion";

class INDISPENSABLE {
  static VALUE = (v: any) => !!v || "필수 입력사항입니다";
  static OPTION = (v: any) => !!v || "필수 선택사항입니다";
}

class LENGTH {
  static MUST_BETWEEN = (v: any, start: number, end: number) =>
    !((v && v.length < start) || v.length >= end) ||
    `${start}-${end}자 내로 입력 가능합니다`;
}

class FORMAT {
  static MUST_BE_EMAIL = (v: any) =>
    /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/.test(
      v
    ) || "이메일 형식이 맞지 않습니다 - abcd@efg.com";
  static MUST_BE_PHONE = (v: any) =>
    /^01(\d{8,9})$/.test(v) || "01012345678 형식으로 입력 가능합니다";
  static MUST_BE_NUMBER = (v: any) => isNumeric(v) || "숫자만 입력 가능합니다";
  static MUST_BE_LETTER = (v: any) =>
    /^[a-zA-Z가-힣]*$/.test(v) || "문자만 입력 가능합니다";
  static MUST_BE_NUMBER_AND_LETTER = (v: any) =>
    /^[a-zA-Z가-힣0-9]*$/.test(v) || "문자 혹은 숫자만 입력 가능합니다";
  static MUST_BE_ENGLISH_LETTER = (v: any) =>
    /^[a-zA-Z]*$/.test(v) || "영문만 입력 가능합니다";
  static MUST_BE_KOREAN_LETTER = (v: any) =>
    /^[가-힣]*$/.test(v) || "한글만 입력 가능합니다";
  static MUST_NOT_BE_BLANK = (v: any) =>
    !/\s/.test(v) || "공백은 허용되지 않습니다";
}

export type Rule = (value: any) => boolean | string;
export type Rules = {
  [key: string]: Rule[];
};

export const RULES: Rules = {
  EMPTY_RULE: [(v) => !!v],

  COMMON_NUMBER: [INDISPENSABLE.VALUE, FORMAT.MUST_BE_NUMBER],
  COMMON_LETTER: [INDISPENSABLE.VALUE, FORMAT.MUST_BE_LETTER],
  COMMON_LETTER_NUMBER: [INDISPENSABLE.VALUE, FORMAT.MUST_BE_NUMBER_AND_LETTER],

  BOARD_TITLE: [INDISPENSABLE.VALUE],
  BOARD_CONTENT: [INDISPENSABLE.VALUE],
  BOARD_CATEGORY: [INDISPENSABLE.OPTION],

  MEMBER_EMAIL: [
    INDISPENSABLE.VALUE,
    FORMAT.MUST_BE_EMAIL,
    (v) => LENGTH.MUST_BETWEEN(v, 9, 64),
  ],
  MEMBER_PASSWORD: [INDISPENSABLE.VALUE, (v) => LENGTH.MUST_BETWEEN(v, 4, 24)],
  MEMBER_NICKNAME: [INDISPENSABLE.VALUE, (v) => LENGTH.MUST_BETWEEN(v, 4, 24)],
  MEMBER_PHONE: [INDISPENSABLE.VALUE, FORMAT.MUST_BE_PHONE],

  PRODUCT_NAME: [INDISPENSABLE.VALUE],
  PRODUCT_PRICE: [INDISPENSABLE.VALUE],
  PRODUCT_STOCK: [INDISPENSABLE.VALUE],
  PRODUCT_DESCRIPTION: [INDISPENSABLE.VALUE],
  PRODUCT_CATEGORY: [INDISPENSABLE.OPTION],
  PRODUCT_IMAGE: [INDISPENSABLE.OPTION],
  PRODUCT_REGION: [INDISPENSABLE.OPTION],

  PRICE_LIMIT: [],

  DATE: [INDISPENSABLE.OPTION],
  TIME: [INDISPENSABLE.OPTION],
  COST: [INDISPENSABLE.VALUE, FORMAT.MUST_BE_NUMBER],
};
