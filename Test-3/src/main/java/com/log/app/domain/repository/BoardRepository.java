package com.log.app.domain.repository;

import com.log.app.domain.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
public interface BoardRepository extends JpaRepository<Board, Long>{

}
