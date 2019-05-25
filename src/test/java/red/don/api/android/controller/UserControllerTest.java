package red.don.api.android.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import red.don.api.android.entity.Response;
import red.don.api.android.entity.UserEntity;
import red.don.api.android.mapper.UserMapper;
import red.don.api.android.service.SignService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTest {

  @Autowired
  private WebApplicationContext context;
  @Autowired
  private UserMapper userMapper;
  @Autowired
  private SignService signService;

  private MockMvc mvc;
  private ObjectMapper json;
  private UserEntity user;
  private UserEntity nobody;
  private String jsonUser;
  private String jsonNobody;
  private String json401;
  private String token;

  @Before
  public void before() throws Exception {
    mvc = MockMvcBuilders.webAppContextSetup(context).build();
    json = new ObjectMapper();
    user = new UserEntity("email", "name", "password");
    nobody = new UserEntity("404", "404", "404");
    jsonUser = json.writeValueAsString(user);
    jsonNobody = json.writeValueAsString(nobody);
    json401 = json.writeValueAsString(new Response<String>(false, "401 Unauthorized", null));
    userMapper.deleteAll();
    assertEquals("table `user` should be empty", 0, userMapper.countAll());
    assertTrue("insert user should be success", userMapper.insert(user));
    token = signService.signIn(user);
  }

  @After
  public void after() {
    userMapper.deleteAll();
    assertEquals("table `user` should be empty", 0, userMapper.countAll());
  }

  @Test
  public void userDeleteWithoutAuthed() throws Exception {
    var response = mvc.perform(MockMvcRequestBuilders.delete("/user/delete")).andReturn().getResponse();
    assertEquals("HTTP status code should be 401", 401, response.getStatus());
    assertEquals("HTTP content should be " + json401, json401, response.getContentAsString());
  }

  @Test
  public void userModifyWithoutAuthed() throws Exception {
    var response = mvc.perform(MockMvcRequestBuilders.put("/user/modify")
        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(jsonNobody)).andReturn().getResponse();
    assertEquals("HTTP status code should be 401", 401, response.getStatus());
    assertEquals("HTTP content should be " + json401, json401, response.getContentAsString());
  }

  @Test
  public void userViewWithoutAuthed() throws Exception {
    var response = mvc.perform(MockMvcRequestBuilders.get("/user/view")).andReturn().getResponse();
    assertEquals("HTTP status code should be 401", 401, response.getStatus());
    assertEquals("HTTP content should be " + json401, json401, response.getContentAsString());
  }

  @Test
  public void userDelete() throws Exception {
    var responseTrue = mvc
        .perform(MockMvcRequestBuilders.delete("/user/delete").header("Authorization", "Bearer " + token)).andReturn()
        .getResponse();
    assertEquals("HTTP status code should be 200", 200, responseTrue.getStatus());
    String jsonTrue = json.writeValueAsString(new Response<>(true));
    assertEquals("HTTP content should be " + jsonTrue, jsonTrue, responseTrue.getContentAsString());
    var responseFalse = mvc
        .perform(MockMvcRequestBuilders.delete("/user/delete").header("Authorization", "Bearer " + token)).andReturn()
        .getResponse();
    assertEquals("HTTP status code should be 401", 401, responseFalse.getStatus());
    assertEquals("HTTP content should be " + json401, json401, responseFalse.getContentAsString());
  }

  @Test
  public void userModify() throws Exception {
    var responseTrue = mvc.perform(MockMvcRequestBuilders.put("/user/modify").header("Authorization", "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(jsonUser)).andReturn().getResponse();
    assertEquals("HTTP status code should be 200", 200, responseTrue.getStatus());
    String jsonTrue = json.writeValueAsString(new Response<>(true));
    assertEquals("HTTP content should be " + jsonTrue, jsonTrue, responseTrue.getContentAsString());
  }

  @Test
  public void userView() throws Exception {
    var responseTrue = mvc.perform(MockMvcRequestBuilders.get("/user/view").header("Authorization", "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(jsonUser)).andReturn().getResponse();
    assertEquals("HTTP status code should be 200", 200, responseTrue.getStatus());
    String jsonTrue = json
        .writeValueAsString(new Response<UserEntity>(true, null, userMapper.selectOne("email", user.getEmail())));
    assertEquals("HTTP content should be " + jsonTrue, jsonTrue, responseTrue.getContentAsString());
  }

}
