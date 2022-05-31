package com.adamsoft.movie;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;

import com.adamsoft.movie.model.Member;
import com.adamsoft.movie.model.Movie;
import com.adamsoft.movie.model.MovieImage;
import com.adamsoft.movie.model.Review;
import com.adamsoft.movie.repository.MemberRepository;
import com.adamsoft.movie.repository.MovieImageRepository;
import com.adamsoft.movie.repository.MovieRepository;
import com.adamsoft.movie.repository.ReviewRepository;

@SpringBootTest
public class RepositoryTest {

	@Autowired
	private MovieRepository movieRepository;
	
	@Autowired
	private MovieImageRepository movieImageRepository;
	
	//@Test
	//여러 개의 데이터를 삽입하므로 모두 성공하거나 실패하도록 하기 위해서 추가
	@Transactional
	@Commit
	public void insertMovie() {
		IntStream.rangeClosed(1, 100).forEach(i -> {
			Movie movie = Movie.builder().title("Movie..." + i).build();
			movieRepository.save(movie);
			
			int count = (int)(Math.random() * 5) + 1;
			for(int j = 0; j<count; j++) {
				MovieImage movieImage = MovieImage.builder()
						.uuid(UUID.randomUUID().toString())
						.movie(movie)
						.imgName("test" + j + ".jpg")
						.build();
				movieImageRepository.save(movieImage);
			}
		});
	}
	
	@Autowired
	private MemberRepository memberRepository;
	
	//@Test
	//여러 개의 데이터를 삽입하므로 모두 성공하거나 실패하도록 하기 위해서 추가
	@Transactional
	@Commit
	public void insertMember() {
		IntStream.rangeClosed(1, 100).forEach(i -> {
			Member member = Member.builder()
					.email("r" + i + "@gmail.com")
					.pw("1111").build();
			memberRepository.save(member);
			
		});
	}
	
	@Autowired
	private ReviewRepository reviewRepository;
	
	//@Test
	@Transactional
	@Commit
	public void insertReview() {
	
		IntStream.range(1,  200).forEach(i -> {
			//존재하는 영화번호
			Long mno = (long)(Math.random() * 100) + 1;
			//회원 번호
			Long mid = (long)(Math.random() * 100) + 1;
			
			Movie movie = Movie.builder().mno(mno).build();
			Member member = Member.builder().mid(mid).build();
			
			Review movieReview = Review.builder().member(member).movie(movie)
					.grade((int)(Math.random() * 5) + 1)
					.text("영화 리뷰..." + i)
					.build();
			reviewRepository.save(movieReview);
		});
	}
	
	//@Test
	public void testListPage() {
		PageRequest pageRequest = PageRequest.of(
				0,  10, Sort.by(Sort.Direction.DESC, "mno"));
		Page<Object [] > result = movieRepository.getListPage(pageRequest);
		for(Object [] objects : result.getContent()) {
			System.out.println(Arrays.toString(objects));
		}
	}
	
	//@Test
	public void testGetMovieWithAll() {
		List<Object []> result = movieRepository.getMovieAll(92L);
		for(Object [] ar : result) {
			System.out.println(Arrays.toString(ar));
		}
	}
	
	//@Test
	public void testGetMovieReviews() {
		Movie movie = Movie.builder().mno(100L).build();
		
		List<Review> list = reviewRepository.findByMovie(movie);
		for(Review review : list){
			System.out.println(review.getMember().getEmail());
		}
	}
	
	@Test
	@Transactional
	@Commit
	public void testDeleteMember() {
		Long mid = 96L;
		Member member = Member.builder().mid(mid).build();
		reviewRepository.deleteByMember(member);
		memberRepository.deleteById(mid);
	}
}








