package com.andreasmarsh.SpringTest;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "movie")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long movieID;

    @Column(nullable = false, name = "title")
    private String title;

    @Column(nullable = false, name = "cast")
    private String cast;

    @Column(nullable = false, name = "director")
    private String director;

    @Column(nullable = false, name = "producer")
    private String producer;

    @Column(nullable = false, name = "synopsis", length = 8000)
    private String synopsis;

    @Column(nullable = false, name = "trailer", length = 800)
    private String trailer;

    @Column(nullable = false, name = "rating", length = 5)
    private String rating;

    private boolean nowShowing;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "movie", orphanRemoval = true)
    @NotFound(action = NotFoundAction.IGNORE)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "movie", orphanRemoval = true)
    @NotFound(action = NotFoundAction.IGNORE)
    private List<MovieShowing> movieShowings = new ArrayList<>();

    @ManyToMany
    @JoinTable(name="movieCategory",
            joinColumns=
            @JoinColumn(name="movieID", referencedColumnName="movieID"),
            inverseJoinColumns=
            @JoinColumn(name="categoryID", referencedColumnName="categoryID")
    )
    private Set<Category> categories = new HashSet<Category>();

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public Long getMovieID() {
        return movieID;
    }

    public void setMovieID(Long movieID) {
        this.movieID = movieID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCast() {
        return cast;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public List<MovieShowing> getMovieShowings() {
        return movieShowings;
    }

    public void setMovieShowings(List<MovieShowing> movieShowings) {
        this.movieShowings = movieShowings;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public boolean isNowShowing() {
        return nowShowing;
    }

    public void setNowShowing(boolean nowShowing) {
        this.nowShowing = nowShowing;
    }
}
