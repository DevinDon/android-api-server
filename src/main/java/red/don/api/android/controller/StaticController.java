package red.don.api.android.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StaticController {

  private int total = 0;

  @GetMapping("/")
  public String index(HttpServletRequest request) {
    request.setAttribute("total", ++total);
    return "index.html";
  }

}
