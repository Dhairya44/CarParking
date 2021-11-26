package com.andreasmarsh.SpringTest;

import javax.persistence.*;

@Entity
@Table(name = "review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewID;

    @ManyToOne
    @JoinColumn(name = "movieID")
    private Movie movie;

    @Column(nullable = true, name = "reviewer")
    private String reviewer;

    @Column(nullable = true, name = "rating", length = 3)
    private Long rating;

    @Column(nullable = true, name = "review", length = 8000)
    private String review;

    public Review() {

    }

    public Review(Long reviewID, Movie movie, String reviewer, Long rating, String review) {
        this.reviewID = reviewID;
        this.movie = movie;
        this.reviewer = reviewer;
        this.rating = rating;
        this.review = review;
    }

    public Review(Movie movie, String reviewer, Long rating, String review) {
        this.movie = movie;
        this.reviewer = reviewer;
        this.rating = rating;
        this.review = review;
    }

    public Long getReviewID() {
        return reviewID;
    }

    public void setReviewID(Long reviewID) {
        this.reviewID = reviewID;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public String getReviewer() {
        return reviewer;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }

    public Long getRating() {
        return rating;
    }

    public void setRating(Long rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
