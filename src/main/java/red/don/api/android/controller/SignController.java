package red.don.api.android.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import red.don.api.android.entity.Response;
import red.don.api.android.entity.UserEntity;
import red.don.api.android.service.SignService;

@RestController
@RequestMapping("/sign")
public class SignController {

  @Autowired
  private SignService service;

  // TODO: Refactor by AOP
  @PostMapping("/in")
  public Response<String> signIn(@RequestBody UserEntity user) {
    String token = service.signIn(user);
    return new Response<>(token != null, "", token);
  }

  // TODO: Refactor by AOP
  @PostMapping("/out")
  public Response<String> signOut(@RequestBody UserEntity user) {
    return new Response<>(service.signOut(user));
  }

  // TODO: Refactor by AOP
  @PostMapping("/up")
  public Response<UserEntity> signUp(@RequestBody UserEntity user) {
    return new Response<>(service.signUp(user), null, user);
  }

}
