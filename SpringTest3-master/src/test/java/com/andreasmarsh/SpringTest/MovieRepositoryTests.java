package com.andreasmarsh.SpringTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class MovieRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MovieRepository repo;

    @Autowired
    private CategoryRepository repo2;

    //test methods go below
    @Test
    public void testCreateCategory() {
        Category fantasy = new Category((long)(repo2.findAll().get(repo2.findAll().size() - 1).getCategoryID() + 1), "Fantasy");
        repo2.save(fantasy);

        Category adventure = new Category((long)(repo2.findAll().get(repo2.findAll().size() - 1).getCategoryID() + 1), "Adventure");
        repo2.save(adventure);

        Category comedy = new Category((long)(repo2.findAll().get(repo2.findAll().size() - 1).getCategoryID() + 1), "Comedy");
        repo2.save(comedy);

        Category scifi = new Category((long)(repo2.findAll().get(repo2.findAll().size() - 1).getCategoryID() + 1), "Sci-Fi");
        repo2.save(scifi);

        Category animation = new Category((long)(repo2.findAll().get(repo2.findAll().size() - 1).getCategoryID() + 1), "Animation");
        repo2.save(animation);

        Category crime = new Category((long)(repo2.findAll().get(repo2.findAll().size() - 1).getCategoryID() + 1), "Crime");
        repo2.save(crime);

        Category drama = new Category((long)(repo2.findAll().get(repo2.findAll().size() - 1).getCategoryID() + 1), "Drama");
        repo2.save(drama);
    }

    // test methods go below
    @Test
    public void testCreateMovie() {
        //Adding new movies
        Movie shangChi = new Movie();
        shangChi.setTitle("Shang-Chi 2");
        shangChi.setCast("Simu Liu, Awkwafina, Tony Chiu-Wai Leung");
        shangChi.setTrailer("8YjFbMbfXaQ");
        shangChi.setDirector("Destin Daniel Cretton");
        shangChi.setProducer("Jonathan Schwartz");
        shangChi.setSynopsis("Shang-Chi and the Legend of the Ten Rings is a 2021 American superhero film based on Marvel Comics featuring the character Shang-Chi.");
        shangChi.setRating("PG-13");
        shangChi.setNowShowing(true);

        Movie blackWid = new Movie();
        blackWid.setTitle("Black Widow");
        blackWid.setCast("Scarlett Johansson, Florence Pugh, David Harbour");
        blackWid.setTrailer("ybji16u608U");
        blackWid.setDirector("Cate Shortland");
        blackWid.setProducer("Victoria Alonso");
        blackWid.setSynopsis("Natasha Romanoff confronts the darker parts of her ledger when a dangerous conspiracy with ties to her past arises.");
        blackWid.setRating("PG-13");
        blackWid.setNowShowing(true);
        repo.save(blackWid);

        Movie venom = new Movie();
        venom.setTitle("Venom: Let There Be Carnage");
        venom.setCast("Tom Hardy, Woody Harrelson, Michelle Williams");
        venom.setTrailer("-FmWuCgJmxo");
        venom.setDirector("Andy Serkis");
        venom.setProducer("Avi Arad");
        venom.setSynopsis("Eddie Brock attempts to reignite his career by interviewing serial killer Cletus Kasady, who becomes the host of the symbiote Carnage and escapes prison after a failed execution.");
        venom.setRating("PG-13");
        venom.setNowShowing(true);
        repo.save(venom);

        Movie addamsFa = new Movie();
        addamsFa.setTitle("The Addams Family 2");
        addamsFa.setCast("Oscar Isaac, Charlize Theron, Chloë Grace Moretz");
        addamsFa.setTrailer("Kd82bSBDE84");
        addamsFa.setDirector("Greg Tiernan, Conrad Vernon, Laura Brousseau");
        addamsFa.setProducer("Gail Berman");
        addamsFa.setSynopsis("The Addams get tangled up in more wacky adventures and find themselves involved in hilarious run-ins with all sorts of unsuspecting characters.");
        addamsFa.setRating("PG");
        addamsFa.setNowShowing(true);
        repo.save(addamsFa);

        Movie matrixRes = new Movie();
        matrixRes.setTitle("The Matrix Resurrections");
        matrixRes.setCast("Keanu Reeves, Christina Ricci, Carrie-Anne Moss");
        matrixRes.setTrailer("9ix7TUGVYIo");
        matrixRes.setDirector("Lana Wachowski");
        matrixRes.setProducer("James McTeigue");
        matrixRes.setSynopsis("The plot is currently unknown");
        matrixRes.setRating("R");
        matrixRes.setNowShowing(false);
        repo.save(matrixRes);

        Movie encanto = new Movie();
        encanto.setTitle("Encanto");
        encanto.setCast("Stephanie Beatriz, Maria Cecilia Botero, John Leguizamo");
        encanto.setTrailer("CaimKeDcudo");
        encanto.setDirector("Jared Bush, Byron Howard, Charise Castro Smith");
        encanto.setProducer("Yvett Merino");
        encanto.setSynopsis("A young Colombian girl has to face the frustration of being the only member of her family without magical powers.");
        encanto.setRating("PG");
        encanto.setNowShowing(false);
        repo.save(encanto);

        Movie spiderman = new Movie();
        spiderman.setTitle("Spider-Man: No Way Home");
        spiderman.setCast("Tom Holland, Zendaya, Benedict Cumberbatch");
        spiderman.setTrailer("ZYzbalQ6Lg8");
        spiderman.setDirector("Jon Watts");
        spiderman.setProducer("Kevin Feige");
        spiderman.setSynopsis("With Spider-Man's identity now revealed, Peter asks Doctor Strange for help. When a spell goes wrong, dangerous foes from other worlds start to appear, forcing Peter to discover what it truly means to be Spider-Man.");
        spiderman.setRating("PG-13");
        spiderman.setNowShowing(false);
        repo.save(spiderman);

        Movie batman = new Movie();
        batman.setTitle("The Batman");
        spiderman.setCast("Robert Pattinson, Barry Keoghan, Colin Farrell");
        spiderman.setTrailer("mqqft2x_Aa4");
        spiderman.setDirector("Matt Reeves");
        spiderman.setProducer("Dylan Clark");
        spiderman.setSynopsis("In his second year of fighting crime, Batman uncovers corruption in Gotham City that connects to his own family while facing a serial killer known as the Riddler.");
        spiderman.setRating("PG-13");
        spiderman.setNowShowing(false);
        repo.save(batman);


        //Giving each movie categories
        Category categoryAction = repo2.findByCategory("Action");
        Category categorySuperhero = repo2.findByCategory("Superhero");
        Category categoryFantasy = repo2.findByCategory("Fantasy");
        Category categoryScifi = repo2.findByCategory("Sci-Fi");
        Category categoryAdventure = repo2.findByCategory("Adventure");
        Category categoryAnimation = repo2.findByCategory("Animation");
        Category categoryComedy = repo2.findByCategory("Comedy");
        Category categoryCrime = repo2.findByCategory("Crime");
        Category categoryDrama = repo2.findByCategory("Drama");


        Set<Category> catsASF = new HashSet<Category>();
        catsASF.add(categoryAction);
        catsASF.add(categorySuperhero);
        catsASF.add(categoryFantasy);

        Set<Category> catsAAS = new HashSet<Category>();
        catsAAS.add(categoryAction);
        catsAAS.add(categoryAdventure);
        catsAAS.add(categoryScifi);

        Set<Category> catsAAC = new HashSet<Category>();
        catsAAC.add(categoryAdventure);
        catsAAC.add(categoryAnimation);
        catsAAC.add(categoryComedy);

        Set<Category> catsAS = new HashSet<Category>();
        catsAS.add(categoryAction);
        catsAS.add(categoryScifi);

        Set<Category> catsACD = new HashSet<Category>();
        catsACD.add(categoryCrime);
        catsACD.add(categoryAction);
        catsACD.add(categoryDrama);


        shangChi.setCategories(catsASF);
        blackWid.setCategories(catsAAS);
        venom.setCategories(catsAAS);
        addamsFa.setCategories(catsAAC);
        matrixRes.setCategories(catsAS);
        encanto.setCategories(catsAAC);
        spiderman.setCategories(catsAAS);
        batman.setCategories(catsACD);


        //Giving each movie showing times
        MovieShowing showing = new MovieShowing();
        Date date = new Date();
        showing.setMovie(shangChi);
        showing.setTheaterID(1L);
        showing.setDate(date);
        showing.setTime(date);

        List<MovieShowing> showings = new ArrayList<>();
        showings.add(showing);

        shangChi.setMovieShowings(showings);


        //Giving each movie reviews
        Review reviewShang1 = new Review();
        reviewShang1.setMovie(shangChi);
        reviewShang1.setReview("Shang-Chi and the Legend of the Ten Rings is a confident introduction to Marvel’s first Asian superhero, delivering the MCU’s best fight choreography and one of its most emotionally complex villains.");
        reviewShang1.setRating(80L);
        reviewShang1.setReviewer("IGN");

        Review reviewShang2 = new Review();
        reviewShang2.setMovie(shangChi);
        reviewShang2.setReview("Bolstered by a star-making performance from Simu Liu, Shang-Chi and the Legend of the Ten Rings gets the MCU's newest hero off to a promising start.");
        reviewShang2.setRating(80L);
        reviewShang2.setReviewer("Screen Rant");

        List<Review> reviewsShang = new ArrayList<>();
        reviewsShang.add(reviewShang1);
        reviewsShang.add(reviewShang2);

        shangChi.setReviews(reviewsShang);

        Review reviewWidow1 = new Review();
        reviewWidow1.setMovie(blackWid);
        reviewWidow1.setReview("It shouldn’t really have taken 11 years for the Widow to get her own standalone adventure. But thanks to some zesty new character dynamics and smart twists, Marvel have finally done her right.");
        reviewWidow1.setRating(80L);
        reviewWidow1.setReviewer("Empire");

        Review reviewWidow2 = new Review();
        reviewWidow2.setMovie(blackWid);
        reviewWidow2.setReview("Marvel’s Black Widow is a strong solo superhero effort that feels both timely and also way too late.");
        reviewShang2.setRating(75L);
        reviewWidow2.setReviewer("USA Today");

        List<Review> reviewsWidow = new ArrayList<>();
        reviewsWidow.add(reviewWidow1);
        reviewsWidow.add(reviewWidow2);

        blackWid.setReviews(reviewsWidow);

        Review reviewVenom1 = new Review();
        reviewVenom1.setMovie(venom);
        reviewVenom1.setReview("Venom: Let There Be Carnage is a bold and brisk superhero story, unlike any other mainstream Hollywood film in the genre. It crams a heck of a lot of movie into an hour and a half, but it doesn’t feel like it needed to be longer. It just feels like we need more movies like it.");
        reviewVenom1.setRating(85L);
        reviewVenom1.setReviewer("TheWrap");

        Review reviewVenom2 = new Review();
        reviewVenom2.setMovie(venom);
        reviewVenom2.setReview("It’s hard to overstate just how much the relative success of this film comes down to Hardy and his go for broke performances as Eddie and Venom.");
        reviewVenom2.setRating(63L);
        reviewVenom2.setReviewer("The Associated Press");

        List<Review> reviewsVenom = new ArrayList<>();
        reviewsVenom.add(reviewVenom1);
        reviewsVenom.add(reviewVenom2);

        venom.setReviews(reviewsVenom);

        Review reviewAddams1 = new Review();
        reviewAddams1.setMovie(addamsFa);
        reviewAddams1.setReview("This sequel makes up for some of the problems with the 2019 \"Addams Family\" animated family film, which suffered from an uneven tone and a meandering storyline.");
        reviewAddams1.setRating(75L);
        reviewAddams1.setReviewer("RogerEbert.com");

        Review reviewAddams2 = new Review();
        reviewAddams2.setMovie(addamsFa);
        reviewAddams2.setReview("Where it could lean into the typically bone-dry Addams family humor, this film more often relies on poop jokes, explosions and the musical talents of Snoop Dogg. It’s sure to entertain little ones, but parents may find themselves itching for something more impish.");
        reviewAddams2.setRating(50L);
        reviewAddams2.setReviewer("The New York Times");

        List<Review> reviewsAddams = new ArrayList<>();
        reviewsAddams.add(reviewAddams1);
        reviewsAddams.add(reviewAddams2);

        addamsFa.setReviews(reviewsAddams);


        //Saving the movies to the database
        Movie savedMovieShang = repo.save(shangChi);

        Movie existMovie = entityManager.find(Movie.class, savedMovieShang.getMovieID());

        assertThat(shangChi.getTitle()).isEqualTo(existMovie.getTitle());

    }
}