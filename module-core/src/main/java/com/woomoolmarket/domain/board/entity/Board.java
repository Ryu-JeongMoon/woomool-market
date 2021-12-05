package com.woomoolmarket.domain.board.entity;

import static javax.persistence.FetchType.LAZY;

import com.woomoolmarket.common.auditing.BaseEntity;
import com.woomoolmarket.common.constant.ExceptionConstant;
import com.woomoolmarket.common.enumeration.Status;
import com.woomoolmarket.domain.image.entity.Image;
import com.woomoolmarket.domain.member.entity.Member;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
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
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseEntity {

    @Id
    @Column(name = "board_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Image> images = new ArrayList<>();

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    private int hit;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    private LocalDateTime deletedDateTime;

    @Enumerated(EnumType.STRING)
    private BoardCategory boardCategory;

    @Builder
    public Board(Member member, String title, String content, BoardCategory boardCategory,
        LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.member = member;
        this.title = title;
        this.content = content;
        this.boardCategory = boardCategory;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;

        if (startDateTime != null && endDateTime != null && startDateTime.isAfter(endDateTime)) {
            throw new IllegalArgumentException(ExceptionConstant.BOARD_DATE_NOT_PROPER);
        }
    }

    public void addImages(List<Image> images) {
        if (images == null || images.isEmpty()) {
            return;
        }

        this.images.addAll(images);
        images.forEach(i -> i.setBoard(this));
    }

    public void increaseHit() {
        hit++;
    }

    public void delete() {
        changeStatusAndDeletedDateTime(Status.INACTIVE, LocalDateTime.now());
    }

    public void restore() {
        changeStatusAndDeletedDateTime(Status.ACTIVE, null);
    }

    private void changeStatusAndDeletedDateTime(Status status, LocalDateTime deletedDateTime) {
        this.status = status;
        this.deletedDateTime = deletedDateTime;
    }
}