package com.andreasmarsh.SpringTest;

import java.io.UnsupportedEncodingException;
import java.util.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import net.bytebuddy.utility.RandomString;

@Service
public class MovieServices {

    @Autowired
    private MovieRepository repo;

    @Autowired
    private ReviewRepository repo2;

    /**
    public List<User> listAll() {
        return repo.findAll();
    }
     */

    public void addMovie(Movie movie, MovieShowing showing, Review rev, String siteURL)
            throws UnsupportedEncodingException, MessagingException {


        movie.setNowShowing(true);
        movie.setRating("PG-13");

        showing.setMovie(movie);
        showing.setTheaterID(1L);


        List<MovieShowing> showings = new ArrayList<>();
        showings.add(showing);

        movie.setMovieShowings(showings);

        Review review = new Review();
        review.setMovie(movie);
        review.setReview("A good film. Would recommend!");
        review.setRating(95L);
        review.setReviewer("Andreas Marsh");

        List<Review> reviews = new ArrayList<>();
        reviews.add(review);

        movie.setReviews(reviews);

     //   Category category = repo2.findByCategory("Action");

    //    Category category2 = repo2.findByCategory("Superhero");

      //  Set<Category> categories = new HashSet<Category>();
       // categories.add(category);
       // categories.add(category2);

      //  movie.setCategories(categories);

        Movie savedMovie = repo.save(movie);

       // Movie existMovie = entityManager.find(Movie.class, savedMovie.getMovieID());

      //  assertThat(movie.getTitle()).isEqualTo(existMovie.getTitle());

        //save the movies to the database
        repo.save(movie);
        repo2.save(review);

    }


}