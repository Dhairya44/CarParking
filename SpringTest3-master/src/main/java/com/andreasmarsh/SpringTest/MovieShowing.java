package com.andreasmarsh.SpringTest;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "movieShowing")
public class MovieShowing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long showID;

    @ManyToOne
    @JoinColumn(name = "movieID")
    private Movie movie;

    @Column(name = "theaterID", length = 11)
    private Long theaterID;

    @Temporal(TemporalType.DATE)
    private java.util.Date date;

    @Temporal(TemporalType.TIME)
    private java.util.Date time;

    public MovieShowing() {

    }

    public MovieShowing(Long showID, Movie movie, Long theaterID, Date date, Date time) {
        this.showID = showID;
        this.movie = movie;
        this.theaterID = theaterID;
        this.date = date;
        this.time = time;
    }

    public MovieShowing(Movie movie, Long theaterID, Date date, Date time) {
        this.movie = movie;
        this.theaterID = theaterID;
        this.date = date;
        this.time = time;
    }

    public Long getShowID() {
        return showID;
    }

    public void setShowID(Long showID) {
        this.showID = showID;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Long getTheaterID() {
        return theaterID;
    }

    public void setTheaterID(Long theaterID) {
        this.theaterID = theaterID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}

