package com.example.WouldILie.cont;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")

public class AdminCont {

  @GetMapping
  public String returnDashbord() {
    return "admin/dashboard";
  }
}
