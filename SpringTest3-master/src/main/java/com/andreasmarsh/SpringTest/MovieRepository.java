package com.andreasmarsh.SpringTest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    @Query(value = "SELECT EXISTS(SELECT 1 FROM movie WHERE movie = ?1)", nativeQuery=true)
    public BigInteger doesMovieExist(String movie);

    @Query("SELECT u FROM Movie u WHERE u.nowShowing = ?1")
    public List<Movie> findByNowShowing(Boolean nowShowing);

    @Query("SELECT u FROM Movie u WHERE u.title LIKE ?1")
    public List<Movie> findBySearch(String search);
}
