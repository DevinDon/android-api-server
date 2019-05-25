package red.don.api.android.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import red.don.api.android.entity.Response;
import red.don.api.android.entity.UserEntity;
import red.don.api.android.mapper.CheckMapper;
import red.don.api.android.mapper.UserMapper;
import red.don.api.android.service.CheckService;
import red.don.api.android.service.SignService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CheckControllerTest {

  @Autowired
  private WebApplicationContext context;
  @Autowired
  private UserMapper userMapper;
  @Autowired
  private CheckMapper checkMapper;
  @Autowired
  private CheckService checkService;
  @Autowired
  private SignService signService;

  private MockMvc mvc;
  private UserEntity user;
  private ObjectMapper json = new ObjectMapper();
  private String json401;

  @Before
  public void before() throws Exception {
    user = new UserEntity("email", "name", "password");
    json401 = json.writeValueAsString(new Response<String>(false, "401 Unauthorized", null));
    mvc = MockMvcBuilders.webAppContextSetup(context).build();
    userMapper.deleteAll();
    assertEquals("table `user` should be empty", 0, userMapper.countAll());
    checkMapper.deleteAll();
    assertEquals("table `check` should be empty", 0, checkMapper.countAll());
    assertTrue("insert should be success", userMapper.insert(user));
  }

  @After
  public void after() {
    userMapper.deleteAll();
    assertEquals("table `user` should be empty", 0, userMapper.countAll());
    checkMapper.deleteAll();
    assertEquals("table `check` should be empty", 0, checkMapper.countAll());
  }

  @Test
  public void checkInWithoutAuthed() throws Exception {
    var responseWithoutAuthed = mvc.perform(MockMvcRequestBuilders.post("/check/in")).andReturn().getResponse();
    assertEquals("HTTP status code should be 401", 401, responseWithoutAuthed.getStatus());
    assertEquals("HTTP content should be " + json401, json401, responseWithoutAuthed.getContentAsString());
  }

  @Test
  public void checkIn() throws Exception {
    String token = signService.signIn(user);
    String jsonContentTrue = json.writeValueAsString(new Response<>(true));
    var responseTrue = mvc.perform(MockMvcRequestBuilders.post("/check/in").header("Authorization", "Bearer " + token))
        .andReturn().getResponse();
    assertEquals("HTTP status code should be 200", 200, responseTrue.getStatus());
    assertEquals("HTTP content should be " + jsonContentTrue, jsonContentTrue, responseTrue.getContentAsString());
    var responseAgain = mvc.perform(MockMvcRequestBuilders.post("/check/in").header("Authorization", "Bearer " + token))
        .andReturn().getResponse();
    assertEquals("HTTP status code should be 200", 200, responseAgain.getStatus());
    assertEquals("HTTP content should be " + jsonContentTrue, jsonContentTrue, responseAgain.getContentAsString());
  }

  @Test
  public void checkViewWithoutAuthed() throws Exception {
    var responseWithoutAuthed = mvc.perform(MockMvcRequestBuilders.get("/check/view/2012/12/21")).andReturn()
        .getResponse();
    assertEquals("HTTP status code should be 401", 401, responseWithoutAuthed.getStatus());
    assertEquals("HTTP content should be " + json401, json401, responseWithoutAuthed.getContentAsString());
  }

  @Test
  public void checkView() throws Exception {
    String token = signService.signIn(user);
    String jsonContentTrue = json.writeValueAsString(new Response<>(true));
    String jsonContentFalse = json.writeValueAsString(new Response<>(false));
    var date = Calendar.getInstance();
    int year = date.get(Calendar.YEAR);
    int month = date.get(Calendar.MONTH) + 1;
    int day = date.get(Calendar.DATE);
    assertTrue("check in should be true", checkService.check(user));
    var responseTrue = mvc.perform(MockMvcRequestBuilders.get("/check/view/" + year + "/" + month + "/" + day)
        .header("Authorization", "Bearer " + token)).andReturn().getResponse();
    assertEquals("HTTP status code should be 200", 200, responseTrue.getStatus());
    assertEquals("HTTP content should be " + jsonContentTrue, jsonContentTrue, responseTrue.getContentAsString());
    var responseFalse = mvc
        .perform(MockMvcRequestBuilders.get("/check/view/2000/1/1").header("Authorization", "Bearer " + token))
        .andReturn().getResponse();
    assertEquals("HTTP status code should be 200", 200, responseFalse.getStatus());
    assertEquals("HTTP content should be " + jsonContentFalse, jsonContentFalse, responseFalse.getContentAsString());
  }

}
