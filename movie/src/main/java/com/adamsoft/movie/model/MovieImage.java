package com.adamsoft.movie.model;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Entity
//다른 Entity에 포함될 수 있다라는 의미의 어노테이션
//여러 개로 구성되어 있지만 하나의 값 처럼 사용한다는 의미입니다,
@Embeddable
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString(exclude ="movie")
public class MovieImage extends BaseEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long inum;
	private String uuid;
	private String imgName;
	private String path;
	
	@ManyToOne(fetch = FetchType.LAZY)
	//JPA에서는 Movie 객체를 연결하지만 데이터베이스에서는 Movie 테이블의 기본키인 mno를 외래키로 생성
	//테이블이름_mon 의 형태로 데이터베이스에는 추가됩니다.
	private Movie movie;
}
