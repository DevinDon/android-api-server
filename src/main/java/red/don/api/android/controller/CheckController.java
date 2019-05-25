package red.don.api.android.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import red.don.api.android.aspect.AuthTarget;
import red.don.api.android.aspect.AuthUser;
import red.don.api.android.entity.Response;
import red.don.api.android.entity.UserEntity;
import red.don.api.android.service.CheckService;

@RestController
@RequestMapping("/check")
public class CheckController {

  @Autowired
  private CheckService service;

  @AuthTarget
  @PostMapping("/in")
  public Response<String> check(@AuthUser UserEntity user) {
    return new Response<>(service.check(user));
  }

  @AuthTarget
  @GetMapping("/view/{year}/{month}/{day}")
  public Response<String> view(@AuthUser UserEntity user, @PathVariable("year") int year,
      @PathVariable("month") int month, @PathVariable("day") int day) {
    return new Response<>(service.view(user, year, month, day));
  }

}
