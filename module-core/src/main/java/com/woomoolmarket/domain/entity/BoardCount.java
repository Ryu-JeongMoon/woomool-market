package com.woomoolmarket.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "board_count")
@EqualsAndHashCode(of = "id", callSuper = false)
public class BoardCount {

  @Id
  @Column(name = "board_count_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private int hitCount;

  private int likeCount;

  @OneToOne
  @JoinColumn(name = "board_id")
  private Board board;
}
