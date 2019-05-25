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
import red.don.api.android.util.JWTUtil;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SignControllerTest {

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
  }

  @After
  public void after() {
    userMapper.deleteAll();
    assertEquals("table `user` should be empty", 0, userMapper.countAll());
  }

  @Test
  public void signIn() throws Exception {
    var responseTrue = mvc.perform(
        MockMvcRequestBuilders.post("/sign/in").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(jsonUser))
        .andReturn().getResponse();
    assertEquals("HTTP status code should be 200", 200, responseTrue.getStatus());
    String jsonTrue = json.writeValueAsString(
        new Response<String>(true, null, JWTUtil.generate(userMapper.selectOne("email", user.getEmail()))));
    assertEquals("HTTP content should be " + jsonTrue, jsonTrue, responseTrue.getContentAsString());
    var responseFalse = mvc.perform(
        MockMvcRequestBuilders.post("/sign/in").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(jsonNobody))
        .andReturn().getResponse();
    assertEquals("HTTP status code should be 200", 200, responseFalse.getStatus());
    String jsonFalse = json.writeValueAsString(new Response<>(false));
    assertEquals("HTTP content should be " + jsonFalse, jsonFalse, responseFalse.getContentAsString());
  }

  @Test
  public void signOut() throws Exception {
    String token = signService.signIn(user);
    var responseTrue = mvc.perform(MockMvcRequestBuilders.post("/sign/out").header("Authorization", "Bearer " + token))
        .andReturn().getResponse();
    assertEquals("HTTP status code should be 200", 200, responseTrue.getStatus());
    String jsonTrue = json.writeValueAsString(new Response<>(true));
    assertEquals("HTTP content should be " + jsonTrue, jsonTrue, responseTrue.getContentAsString());
    var responseFalse = mvc.perform(MockMvcRequestBuilders.post("/sign/out").header("Authorization", "Bearer " + token))
        .andReturn().getResponse();
    assertEquals("HTTP status code should be 401", 401, responseFalse.getStatus());
    assertEquals("HTTP content should be " + json401, json401, responseFalse.getContentAsString());
  }

  @Test
  public void signUp() throws Exception {
    var responseTrue = mvc.perform(
        MockMvcRequestBuilders.post("/sign/up").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(jsonNobody))
        .andReturn().getResponse();
    assertEquals("HTTP status code should be 200", 200, responseTrue.getStatus());
    String jsonTrue = json.writeValueAsString(new Response<UserEntity>(true, null, nobody));
    assertEquals("HTTP content should be " + jsonTrue, jsonTrue, responseTrue.getContentAsString());
    var responseFalse = mvc.perform(
        MockMvcRequestBuilders.post("/sign/up").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(jsonUser))
        .andReturn().getResponse();
    assertEquals("HTTP status code should be 200", 200, responseFalse.getStatus());
    String jsonFalse = json.writeValueAsString(new Response<UserEntity>(false));
    assertEquals("HTTP content should be " + jsonFalse, jsonFalse, responseFalse.getContentAsString());
  }

}
