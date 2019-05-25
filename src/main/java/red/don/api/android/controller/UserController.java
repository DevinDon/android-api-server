package red.don.api.android.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import red.don.api.android.aspect.AuthTarget;
import red.don.api.android.aspect.AuthUser;
import red.don.api.android.entity.Response;
import red.don.api.android.entity.UserEntity;
import red.don.api.android.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

  @Autowired
  private UserService service;

  @AuthTarget
  @DeleteMapping("/delete")
  public Response<String> delete(@AuthUser UserEntity user) {
    return new Response<>(service.delete(user.getEmail()));
  }

  @AuthTarget
  @PutMapping("/modify")
  public Response<String> modify(@AuthUser UserEntity user, @RequestBody UserEntity modified) {
    return new Response<>(service.modify(user.getEmail(), modified));
  }

  @AuthTarget
  @GetMapping("/view")
  public Response<UserEntity> view(@AuthUser UserEntity user) {
    UserEntity content = service.view(user.getEmail());
    return new Response<UserEntity>(content != null, content);
  }

}
