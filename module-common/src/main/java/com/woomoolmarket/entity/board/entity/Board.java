package com.woomoolmarket.entity.board.entity;

import static javax.persistence.FetchType.LAZY;

import com.woomoolmarket.common.BaseEntity;
import com.woomoolmarket.entity.member.entity.Member;
import com.woomoolmarket.entity.reply.entity.Reply;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@EqualsAndHashCode(of = "board_id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseEntity {

    @Id
    @Column(name = "board_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "board")
    private List<Reply> replies = new ArrayList<>();

    private String title;

    @Lob
    private String content;

    private int hit;

    @Enumerated(EnumType.STRING)
    private BoardStatus boardStatus;

    private LocalDateTime deletedDate;

    @Enumerated(EnumType.STRING)
    private BoardCategory boardCategory;

    public void changeHit() {
        hit++;
    }

    public void changeStatus(BoardStatus boardStatus, LocalDateTime deletedDate) {
        this.boardStatus = boardStatus;
        this.deletedDate = deletedDate;
    }

    public Board changeBoard(Board newBoard) {
        return this;
    }
}
