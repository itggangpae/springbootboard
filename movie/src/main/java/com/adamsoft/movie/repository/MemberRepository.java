package com.adamsoft.movie.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adamsoft.movie.model.Member;

public interface MemberRepository extends JpaRepository<Member, Long>{

}
