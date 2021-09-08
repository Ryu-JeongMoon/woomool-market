package com.woomoolmarket.entity.reply.entity;

import com.woomoolmarket.entity.board.entity.Board;
import com.woomoolmarket.common.BaseTimeEntity;
import com.woomoolmarket.entity.member.entity.Member;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@EqualsAndHashCode(of = "reply_id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reply extends BaseTimeEntity {

    @Id
    @Column(name = "reply_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "board_id")
    @ManyToOne(fetch = LAZY, cascade = ALL)
    private Board board;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = LAZY)
    private Member member;

    private String content;
}
