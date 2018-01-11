package com.deepfish.user.web;

import com.deepfish.user.domain.AbstractUser;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

  @RequestMapping
  public AbstractUser getUser() {
    return null;
  }
}
