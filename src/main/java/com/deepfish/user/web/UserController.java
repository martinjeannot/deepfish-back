package com.deepfish.user.web;

import com.deepfish.user.domain.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

  @RequestMapping
  public User getUser() {
    return null;
  }
}
