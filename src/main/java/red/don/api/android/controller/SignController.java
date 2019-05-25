package red.don.api.android.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import red.don.api.android.aspect.AuthTarget;
import red.don.api.android.aspect.AuthUser;
import red.don.api.android.entity.Response;
import red.don.api.android.entity.UserEntity;
import red.don.api.android.service.SignService;

@RestController
@RequestMapping("/sign")
public class SignController {

  @Autowired
  private SignService service;

  @PostMapping("/in")
  public Response<String> signIn(@RequestBody UserEntity user) {
    String token = service.signIn(user);
    return new Response<>(token != null, null, token);
  }

  @AuthTarget
  @PostMapping("/out")
  public Response<String> signOut(@AuthUser UserEntity user) {
    return new Response<>(service.signOut(user));
  }

  @PostMapping("/up")
  public Response<UserEntity> signUp(@RequestBody UserEntity user) {
    boolean result = service.signUp(user);
    return new Response<>(result, null, result ? user : null);
  }

}
