package com.sameer.elkstack;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/user")
public class Controller {
  Logger logger= LoggerFactory.getLogger(Controller.class);

  @GetMapping("/{id}")
  public User getData(@PathVariable int id)
  {
    List<User> userList=initializeUser();
    final Optional<User> userOptional = userList.stream().filter(user -> user.getId() == id).findAny();
    if(userOptional.isEmpty())
    {
      logger.error("User not found for the id : {}",id);
      return new User(-1,"No user found");
    }
    logger.info("User found with with details as : {}",userOptional.get());
    return userOptional.get();
  }

  private List<User> initializeUser() {
    return Arrays.asList(new User(1,"sa,"),
                         new User(2,"ram"),
                         new User(3,"Shyam"));
  }


}
