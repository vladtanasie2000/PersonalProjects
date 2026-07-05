package com.example.WouldILie.cont;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserCont {

  @GetMapping("/user-page")
  public String userPage() {
    return "user/user-dashboard";
  }
}
