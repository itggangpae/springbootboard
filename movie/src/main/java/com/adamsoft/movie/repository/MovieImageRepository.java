package com.adamsoft.movie.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adamsoft.movie.model.MovieImage;

public interface MovieImageRepository extends JpaRepository<MovieImage, Long> {

}
