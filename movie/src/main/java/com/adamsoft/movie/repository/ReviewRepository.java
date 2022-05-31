package com.adamsoft.movie.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.adamsoft.movie.model.Member;
import com.adamsoft.movie.model.Movie;
import com.adamsoft.movie.model.Review;

public interface ReviewRepository extends JpaRepository<Review, Long>{
	//영화 정보를 가지고 리뷰 목록을 가져오는 메서드
	//영화 정보를 자세히 출력할 때 필요
	//속성에 해당하는 데이터는 EAGER로 처리해서 바로 가져옵니다.
	@EntityGraph(attributePaths= {"member"}, type=EntityGraph.EntityGraphType.FETCH)
	List<Review> findByMovie(Movie movie);
	
	//회원 정보를 가지고 삭제하는 메서드 - 댓글의 개수만큼 SQL을 수행
	//void deleteByMember(Member member);
	
	@Modifying
	@Query("delete from Review mr where mr.member = :member")
	void deleteByMember(@Param("member") Member member);
}
