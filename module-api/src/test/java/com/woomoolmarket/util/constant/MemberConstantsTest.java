package com.woomoolmarket.util.constant;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberConstantsTest {

    @Test
    @DisplayName("올바른 문자열 반환 - leave-member")
    void constantLeave() {
        Assertions.assertThat(MemberConstants.LEAVE).isEqualTo("leave-member");
    }

    @Test
    @DisplayName("올바른 문자열 반환 - modify-member")
    void constantModify() {
        Assertions.assertThat(MemberConstants.MODIFY).isEqualTo("modify-member");
    }

    @Test
    @DisplayName("올바른 문자열 반환 - next-member")
    void constantNextMember() {
        Assertions.assertThat(MemberConstants.NEXT_MEMBER).isEqualTo("next-member");
    }

    @Test
    @DisplayName("올바른 문자열 반환 - previous-member")
    void constantPreviousMember() {
        Assertions.assertThat(MemberConstants.PREVIOUS_MEMBER).isEqualTo("previous-member");
    }
}