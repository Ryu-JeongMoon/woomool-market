package com.woomoolmarket.entity.board.repository;

import com.woomoolmarket.entity.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findAllByTitle(String title);

}
