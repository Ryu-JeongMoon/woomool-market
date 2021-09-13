package com.woomoolmarket.domain.board.repository;

import com.woomoolmarket.domain.board.entity.Board;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findAllByTitle(String title);

}
