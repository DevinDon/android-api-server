package red.don.api.android.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import red.don.api.android.entity.Response;
import red.don.api.android.entity.UserEntity;
import red.don.api.android.service.CheckService;

@RestController
@RequestMapping("/check")
public class CheckController {

  @Autowired
  private CheckService service;

  // TODO: Refactor by AOP
  @PostMapping("/in")
  public Response<String> check(@RequestBody UserEntity user) {
    return new Response<>(service.check(user));
  }

  // TODO: Refactor by AOP
  @PostMapping("/view/{year}/{month}/{day}")
  public Response<String> view(@RequestBody UserEntity user, @PathVariable("year") int year,
      @PathVariable("month") int month, @PathVariable("day") int day) {
    return new Response<>(service.view(user, year, month, day));
  }

}
