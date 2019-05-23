package red.don.api.android.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import red.don.api.android.entity.Response;
import red.don.api.android.entity.UserEntity;
import red.don.api.android.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

  @Autowired
  private UserService service;

  // TODO: Refactor by AOP
  @PostMapping("/delete")
  public Response<String> delete(@RequestBody UserEntity user) {
    return new Response<>(service.delete(user.getEmail()));
  }

  // TODO: Refactor by AOP
  @PostMapping("/modify/{email}")
  public Response<String> modify(@PathVariable("email") String email, @RequestBody UserEntity user) {
    return new Response<>(service.modify(email, user));
  }

  // TODO: Refactor by AOP
  @PostMapping("/view")
  public Response<UserEntity> view(@RequestBody UserEntity user) {
    UserEntity content = service.view(user.getEmail());
    return new Response<UserEntity>(content != null, content);
  }

}
