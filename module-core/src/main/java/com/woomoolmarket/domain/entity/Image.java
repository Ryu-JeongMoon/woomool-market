package com.woomoolmarket.domain.entity;

import com.woomoolmarket.domain.entity.auditing.BaseTimeEntity;
import com.woomoolmarket.domain.entity.enumeration.Status;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = { "id" }, callSuper = false)
public class Image extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "image_id")
  private Long id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "board_id")
  private Board board;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id")
  private Product product;

  @Size(max = 255)
  @Column(nullable = false)
  private String originalFileName;

  @Size(max = 255)
  @Column(nullable = false)
  private String fileName;

  @Size(max = 255)
  @Column(nullable = false)
  private String filePath;

  private Long fileSize;

  @Column(nullable = false, length = 50)
  @Enumerated(EnumType.STRING)
  private Status status = Status.ACTIVE;

  @Builder
  public Image(String originalFileName, String fileName, String filePath, Long fileSize) {
    this.originalFileName = originalFileName;
    this.fileName = fileName;
    this.filePath = filePath;
    this.fileSize = fileSize;
  }

  public void setBoard(Board board) {
    this.board = board;

    if (!board.getImages().contains(this)) {
      board.getImages().add(this);
    }
  }

  public void delete() {
    changeStatus(Status.INACTIVE);
  }

  public void restore() {
    changeStatus(Status.ACTIVE);
  }

  private void changeStatus(Status status) {
    this.status = status;
  }
}