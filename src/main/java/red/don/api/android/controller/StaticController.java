package red.don.api.android.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import red.don.api.android.service.LogService;

@Controller
public class StaticController {

  @Autowired
  private LogService service;

  @GetMapping("/")
  public String index(HttpServletRequest request) {
    request.setAttribute("total", service.getCount());
    return "index.html";
  }

}
