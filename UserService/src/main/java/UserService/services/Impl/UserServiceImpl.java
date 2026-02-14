package UserService.services.Impl;


import UserService.entities.Hotel;
import UserService.entities.Rating;
import UserService.entities.User;
import UserService.external.service.HotelService;
import UserService.repositories.UserRepository;
import UserService.services.UserService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
   private UserRepository userRepository;
    @Autowired
     private RestTemplate restTemplate;
    @Autowired
    private HotelService hotelService;

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Override
    public User saveUser(User user) {
      String randomUserId=  UUID.randomUUID ().toString ();
      user.setUserId (randomUserId);
        return userRepository.save (user);
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll ();
    }

    @Override
    @Retry (name = "ratingUser")
    @CircuitBreaker (name = "ratingUser" ,fallbackMethod = "ratingUserFallback")
    public User getUser(String userId) {
        User user= userRepository.findById (userId).orElseThrow (()->new RuntimeException ("User with given is not founf"+userId));
        // fetch rating of the above  user from RATING SERVICE
        //http://localhost:8083/ratings/users/47e38dac-c7d0-4c40-8582-11d15f185fad

        Rating[] ratingsOfUser = restTemplate.getForObject("http://RATING-SERVICE/ratings/users/" + user.getUserId(), Rating[].class);
        logger.info("{} ", ratingsOfUser);
        List<Rating> ratings = Arrays.stream(ratingsOfUser).toList();
        List<Rating> ratingList = ratings.stream().map(rating -> {
            //api call to hotel service to get the hotel
            //http://localhost:8082/hotels/1cbaf36d-0b28-4173-b5ea-f1cb0bc0a791
            //ResponseEntity<Hotel> forEntity = restTemplate.getForEntity("http://HOTEL-SERVICE/hotels/"+rating.getHotelId(), Hotel.class);
            Hotel hotel = hotelService.getHotel(rating.getHotelId());
            // logger.info("response status code: {} ",forEntity.getStatusCode());
            //set the hotel to rating
            rating.setHotel (hotel);
            //return the rating
            return rating;
        }).collect(Collectors.toList());

        user.setRatings(ratingList);

        return user;
    }
    // 2. Return type 'User' hona chahiye, 'String' nahi
// 3. Naam wahi hona chahiye jo upar fallbackMethod mein diya hai
    public User ratingUserFallback(String userId, Throwable ex) {
        logger.info("Fallback executed because service is down: {}", ex.getMessage());

        // Ek dummy User object banakar return karein
        User dummyUser = User.builder()
                .email("dummy@gmail.com")
                .name("Dummy User")
                .about("This user is created dummy because RATING-SERVICE is down")
                .userId("00000")
                .build();

        return dummyUser;
    }
}
