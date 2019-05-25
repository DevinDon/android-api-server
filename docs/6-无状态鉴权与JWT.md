# Spring Boot 入门实战（六）：无状态鉴权与 JWT

项目代码已托管至 [GitHub, https://github.com/DevinDon/android-api-server](https://github.com/DevinDon/android-api-server) ，[点击此处](https://github.com/DevinDon/android-api-server/tree/c273b8eb841c1d4fde10783f5c23d931bb0e4ba4) :point_left: 查看本章节内容。

众所周知，我们在网络通信中常用的 HTTP 协议是一种无状态协议。但在实际的开发中，我们常常需要一些状态来标识，比如用户的登陆状态。为了通过 HTTP 协议实现状态标识，古老而又伟大的程序员们提出了诸如 `cookie - session` 、`token` 、`outh` 等解决方案。

为了遵循前后端分离的设计理念，解决多终端的兼容性问题，我们原则上不使用 `cookie` 来存储信息，而第三种方案 `outh` 的实现难度与成本又过高。在权衡利弊之后，我们决定使用 `JWT` （`token` 认证方式的一种）作为本项目的鉴权解决方案。

# 基础概念

## 概念定义

`JWT, JSON Web Token` ，它是基于 [RFC 7519](https://link.jianshu.com/?t=https://tools.ietf.org/html/rfc7519) :point_left: 标准定义的一种可以**安全**传输的 `JSON` 对象。

`JWT` 使用数字签名（`HMAC` 加密）来保证完整性与可信任性。

## 工作流程

`JWT` 的工作流程如下：

1. 用户进行登录；
2. 服务器验证用户信息；
3. 如果身份认证成功，服务器生成并向客户端返回 `JWT Token` ；
4. 客户端将 `token` 存储至本地；
5. 客户端在访问服务器时，将 `token` 放入请求头 `Authorization: Bearer ${token}` ；
6. 服务器验证请求头中的 `token` 是否合法，并对其进行业务权限检测。

## 编码格式

在进行 `Base64` 编码后，`JWT Token` 看起来像这样：

```text
eyJhbGciOiJIUzUxMiJ9.eyJuYW1lIjoidXNlcm5hbWUiLCJlbWFpbCI6ImVtYWlsQGVtYWlsLmNvbSJ9.fZalZFoyylIKcAgPHkDh27HP9OovC1tzo63_BWyFsr4bIkaImDku5tG-OhADFjehIX4aJRs5_adwhAWm5nYyFA
```

仔细观察后我们不难发现，这段 `token` 被点 `.` 分隔为了三部分，即：

```text
header.payload.signature
```

接下来，我们将要对这三部分进行解码 <https://www.base64decoder.io/> ，看看他们的庐山真面目。

### 头部 `header`

我们对第一部分 `eyJhbGciOiJIUzUxMiJ9` 进行解码，可以得到：

```json
{"alg":"HS512"}
```

该部分的被叫做 `header` 头，表明了 `HMAC` 采用的签名算法是 `HS512` 。

### 载体 `payload`

接下来对第二部分 `eyJuYW1lIjoidXNlcm5hbWUiLCJlbWFpbCI6ImVtYWlsQGVtYWlsLmNvbSJ9` 进行解码，可以得到：

```json
{"name":"username","email":"email@email.com"}
```

`payload` 即内容部分，包含了：

- 系统保留的字段，非必须，如：`iss` 签发人、`exp` 过期时间、`sub` 主题、`aud` 目标用户等；
- 公共字段，这类字段信息需在 [IANA JSON Web Token Registry](http://www.iana.org/assignments/jwt/jwt.xhtml) 中定义，避免命名冲突；
- 私有字段，业务定义。

需要注意的是，`Base64` 是一种编码方式，而非加密方式，该字段的内容属于公开内容，请勿将私密业务信息（如密码等）放入公开的 `payload` 字段。

### 签名 `signature`

签名 `signature` 部分是保障 `JWT` 安全的核心部分。

我们对签名部分 `fZalZFoyylIKcAgPHkDh27HP9OovC1tzo63_BWyFsr4bIkaImDku5tG-OhADFjehIX4aJRs5_adwhAWm5nYyFA` 进行解码，但是却提示无法解码。

对的，第三部分经过了密钥加密，保证了 `JWT` 的完整性与可信任性。同样的，该部分也必须通过密钥才可解码。

不过需要注意的是，签名部分仅保证了 `JWT` 不会被恶意篡改，但无法隐藏 `payload` 字段的公开信息。所以，请勿将私密业务信息（如密码等）放入公开的 `payload` 字段。

## 编码方法

导入 `io.jsonwebtoken.Jwts` ，使用 `builder()` 方法进行编码：

```java
import java.security.Key;
import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

/* ... */
public String generate() {
  Map<String, Object> claims = new HashMap<>();
  claims.put("email", "email@email.com");
  claims.put("name", "username");
  Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
  return Jwts.builder()
    .setClaims(claims)
    .signWith(key)
    .compact();
}

generate(); // eyJhbGciOiJIUzUxMiJ9.eyJuYW1lIjoidXNlcm5hbWUiLCJlbWFpbCI6ImVtYWlsQGVtYWlsLmNvbSJ9.fZalZFoyylIKcAgPHkDh27HP9OovC1tzo63_BWyFsr4bIkaImDku5tG-OhADFjehIX4aJRs5_adwhAWm5nYyFA
```

其中，`setClaims(claims)` 指定了 `payload` 字段的内容，`signWith(Key)` 指定了加密方式，`compact()` 用于生成编码后的 `JWT Token` 。

## 解码方法

使用 `parse()` 方法进行解码：

```java
public UserEntity parse(String token) {
  try {
    Map<String, Object> body = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
    return new UserEntity((String) body.get("email"), (String) body.get("name"), null);
  } catch (Exception exp) {
    return null;
  }
}

parse("eyJhbGciOiJIUzUxMiJ9.eyJuYW1lIjoidXNlcm5hbWUiLCJlbWFpbCI6ImVtYWlsQGVtYWlsLmNvbSJ9.fZalZFoyylIKcAgPHkDh27HP9OovC1tzo63_BWyFsr4bIkaImDku5tG-OhADFjehIX4aJRs5_adwhAWm5nYyFA"); // new UserEntity("email@email.com", "username", null)
```

其中，`setSigningKey(Key)` 指定了密钥，`parseClaimsJws(String)` 指定了需要解码的 `JWT Token` ，`getBody()` 用于获取 `payload` 字段。

# 设计思路

为了保证 `JWT` 的随机性，确保 `JWT` 可以被主动销毁，我们重新设计 `user` 表，为其添加一个 `token` 字段：

```sql
ALTER TABLE `shareddb`.`user`
ADD COLUMN `token` bigint(20) UNSIGNED NULL DEFAULT 0 AFTER `password`;
```

该字段记录最后的登录时间。当登录时间为 `0` 时，阻止所有登录；当 `JWT` 中的时间大于最后登录时间时，认为该 `JWT` 是有效的；否则视为无效。

# 代码实现

## 重构 `UserEntity`

打开 [`UserEntity`](https://github.com/DevinDon/android-api-server/blob/c273b8eb841c1d4fde10783f5c23d931bb0e4ba4/src/main/java/red/don/api/android/entity/UserEntity.java) :point_left: ，删除所有 `getter` 、`setter` 、`hashCode` 、 `equals` 以及 `toString` 方法，并添加一条新属性：

```java
/** Last sign in time, millsecond. */
private long token;
```

以及一个新的构造方法：

```java
/**
   * New user with email, name, password & token.
   *
   * @param email    Email.
   * @param name     Name.
   * @param password Password.
   * @param token    Token.
   */
public UserEntity(String email, String name, String password, long token) {
  this.email = email;
  this.name = name;
  this.password = password;
  this.token = token;
}
```

再通过右键生成上述被删除的方法即可。

## 重构 `UserMapper`

打开 [`UserMapper`](https://github.com/DevinDon/android-api-server/blob/c273b8eb841c1d4fde10783f5c23d931bb0e4ba4/src/main/java/red/don/api/android/mapper/UserMapper.java) :point_left: ，重构 `update` 和 `updateWhere` 方法：

```java
@Override
@Update("UPDATE `user` SET `email` = #{entity.email}, `name` = #{entity.name}, `password` = #{entity.password}, `token` = ${entity.token} WHERE `${field}` = #{value}")
public boolean update(@Param("field") String field, @Param("value") Object value, UserEntity entity);

@Override
@Update("UPDATE `user` SET `email` = #{entity.email}, `name` = #{entity.name}, `password` = #{entity.password}, `token` = ${entity.token} WHERE ${where}")
public boolean updateWhere(@Param("where") String where, UserEntity entity);
```

以及 `insert` 方法：

```java
@Override
@Insert("INSERT INTO `user`(`email`, `name`, `password`, `token`) VALUES (#{email}, #{name}, #{password}, ${token})")
public boolean insert(UserEntity entity);
```

## 实现 `JWTUtil`

在目录 `src/main/java/red/don/api/android/util` 下创建类 [`JWTUtil`](https://github.com/DevinDon/android-api-server/blob/0e7a88efe6b93dd52064ff653296e9c4bc4f4cde/src/main/java/red/don/api/android/util/JWTUtil.java) :point_left: ，实现编码解码功能：

**:wrench: 修复：parse(String), generate(UserEntity)** ，[点击查看对比](https://github.com/DevinDon/android-api-server/commit/0e7a88efe6b93dd52064ff653296e9c4bc4f4cde) :point_left: ​。

```java
package red.don.api.android.util;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import red.don.api.android.entity.UserEntity;

public class JWTUtil {

  private static Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

  /**
   * Parse JWT token to user entity.
   *
   * @param token JWT token.
   * @return User entity or null.
   */
  public static UserEntity parse(String token) {
    try {
      Map<String, Object> body = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
      return new UserEntity((String) body.get("email"), (String) body.get("name"), null, (long) body.get("token"));
    } catch (Exception exp) {
      return null;
    }
  }

  /**
   * Generate JWT token by user entity.
   *
   * @param user User entity.
   * @return JWT token.
   */
  public static String generate(UserEntity user) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("email", user.getEmail());
    claims.put("name", user.getName());
    claims.put("token", user.getToken());
    return Jwts.builder().setClaims(claims).signWith(key).compact();
  }

}
```

`JWTUtil` 会将用户信息编码生成 `JWT Token` ，并将合法的 `JWT Token` 解析为 `UserEntity` 。

如果解析时传入的 `JWT Token` 不合法，`parse(String)` 会返回 `null` 。

完整代码请参阅 [`GitHub, src/main/java/red/don/api/android/util/JWTUtil.java`](https://github.com/DevinDon/android-api-server/blob/c273b8eb841c1d4fde10783f5c23d931bb0e4ba4/src/main/java/red/don/api/android/util/JWTUtil.java) :point_left: 。

## 重构 `SignService`

打开 [`SignService`](https://github.com/DevinDon/android-api-server/blob/a2aa8bbcc71e023fc83509036d31365b2a9c62f3/src/main/java/red/don/api/android/service/SignService.java) :point_left: ，重构 `signIn` 方法：

**:wrench: 修复：fix: signIn(UserEntity)** ，[点击查看对比](https://github.com/DevinDon/android-api-server/commit/a2aa8bbcc71e023fc83509036d31365b2a9c62f3) :point_left: 。

```java
/**
   * Sign in and return JWT token.
   *
   * @param user User entity.
   * @return JWT token or null.
   */
public String signIn(UserEntity user) {
  user = mapper.selectOneWhere("`email` = '" + user.getEmail() + "' AND `password` = '" + user.getPassword() + "'");
  if (user == null) {
    return null;
  } else {
    user.setToken(System.currentTimeMillis());
    mapper.update("email", user.getEmail(), user);
    return JWTUtil.generate(user);
  }
}
```

以及 `signOut` 方法：

```java
/**
   * Sign out, set token to 0 in order to disable JWT token.
   *
   * @param user User entity.
   * @return Success or not.
   */
public boolean signOut(UserEntity user) {
  user.setToken(0);
  return mapper.update("email", user.getEmail(), user);
}
```

完整代码请参考 [`GitHub, src/main/java/red/don/api/android/service/SignService.java`](https://github.com/DevinDon/android-api-server/blob/c273b8eb841c1d4fde10783f5c23d931bb0e4ba4/src/main/java/red/don/api/android/service/SignService.java) :point_left: 。

## 重构 `SignController`

打开 [`SignController`](https://github.com/DevinDon/android-api-server/blob/c273b8eb841c1d4fde10783f5c23d931bb0e4ba4/src/main/java/red/don/api/android/controller/SignController.java) :point_left: ，并重构 `signIn` 方法：

```java
// TODO: Refactor by AOP
@PostMapping("/in")
public Response<String> signIn(@RequestBody UserEntity user) {
  String token = service.signIn(user);
  return new Response<>(token != null, "", token);
}
```

以及 `signOut` 方法：

```java
// TODO: Refactor by AOP
@PostMapping("/out")
public Response<String> signOut(@RequestBody UserEntity user) {
  return new Response<>(service.signOut(user));
}
```

完整代码请参考 [`GitHub, src/main/java/red/don/api/android/controller/SignController.java`](https://github.com/DevinDon/android-api-server/blob/c273b8eb841c1d4fde10783f5c23d931bb0e4ba4/src/main/java/red/don/api/android/controller/SignController.java) :point_left: 。

## 添加 `Maven` 依赖

首先，将 `JWT` 的 `maven` 依赖添加至项目根目录下的 [`pom.xml`](https://github.com/DevinDon/android-api-server/blob/c273b8eb841c1d4fde10783f5c23d931bb0e4ba4/pom.xml) :point_left: 中的 `dependencies` 标签内：

```xml
<!-- JWT Start -->
<dependency>
  <groupId>io.jsonwebtoken</groupId>
  <artifactId>jjwt-api</artifactId>
  <version>0.10.5</version>
</dependency>
<dependency>
  <groupId>io.jsonwebtoken</groupId>
  <artifactId>jjwt-impl</artifactId>
  <version>0.10.5</version>
  <scope>runtime</scope>
</dependency>
<dependency>
  <groupId>io.jsonwebtoken</groupId>
  <artifactId>jjwt-jackson</artifactId>
  <version>0.10.5</version>
  <scope>runtime</scope>
</dependency>
<!-- JWT End -->
```

随后，刷新项目并等待依赖加载完成。

# 单元测试

## 测试 `UserEntity`

打开 [`UserEntityTest`](https://github.com/DevinDon/android-api-server/blob/c273b8eb841c1d4fde10783f5c23d931bb0e4ba4/src/test/java/red/don/api/android/entity/UserEntityTest.java) :point_left: ，并重构测试：

```java
package red.don.api.android.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserEntityTest {

  private String email = "email@email.com";
  private String name = "name";
  private String password = "password";
  private long token = 1;

  @Test
  public void constructorWithNothing() {
    UserEntity entity = new UserEntity();
    assertNull("email should be null", entity.getEmail());
    assertNull("name shoule be null", entity.getName());
    assertNull("password should be null", entity.getPassword());
    assertEquals("token should be 0", 0, entity.getToken());
  }

  @Test
  public void constructorWithEmailAndPassword() {
    UserEntity entity = new UserEntity(email, password);
    assertEquals("email should be " + email, email, entity.getEmail());
    assertNull("name should be null", entity.getName());
    assertEquals("password should be " + password, password, entity.getPassword());
    assertEquals("token should be 0", 0, entity.getToken());
  }

  @Test
  public void constructorWithEmailAndNameAndPassword() {
    UserEntity entity = new UserEntity(email, name, password);
    assertEquals("email should be " + email, email, entity.getEmail());
    assertEquals("name should be " + name, name, entity.getName());
    assertEquals("password should be " + password, password, entity.getPassword());
    assertEquals("token should be 0", 0, entity.getToken());
  }

  @Test
  public void constructorWithEmailAndNameAndPasswordAndToken() {
    UserEntity entity = new UserEntity(email, name, password, token);
    assertEquals("email should be " + email, email, entity.getEmail());
    assertEquals("name should be " + name, name, entity.getName());
    assertEquals("password should be " + password, password, entity.getPassword());
    assertEquals("token should be " + token, token, entity.getToken());
  }

  @Test
  public void getterAndSetter() {
    UserEntity entity = new UserEntity();
    entity.setEmail(email);
    assertEquals("email should be set", email, entity.getEmail());
    entity.setName(name);
    assertEquals("name should be set", name, entity.getName());
    entity.setPassword(password);
    assertEquals("password should be set", password, entity.getPassword());
    entity.setToken(token);
    assertEquals("token should be " + token, token, entity.getToken());
  }

  @Test
  public void stringify() {
    UserEntity entity = new UserEntity(email, name, password, token);
    assertEquals("stringify should work",
        "UserEntity [email=" + email + ", name=" + name + ", password=" + password + ", token=" + token + "]",
        entity.toString());
  }

  @Test
  public void equalWithOther() {
    UserEntity entity = new UserEntity(email, name, password, token);
    UserEntity another = new UserEntity(email, name, password, token);
    assertEquals("these two entities should be equal", entity, another);
  }

}
```

完整代码请参阅 [`GitHub, src/test/java/red/don/api/android/entity/UserEntityTest.java`](https://github.com/DevinDon/android-api-server/blob/c273b8eb841c1d4fde10783f5c23d931bb0e4ba4/src/test/java/red/don/api/android/entity/UserEntityTest.java) :point_left: 。

## 测试 `UserMapper`

打开 [`UserMapperTest`](https://github.com/DevinDon/android-api-server/blob/c273b8eb841c1d4fde10783f5c23d931bb0e4ba4/src/test/java/red/don/api/android/mapper/UserMapperTest.java) :point_left: ，重构测试：

```java
package red.don.api.android.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import red.don.api.android.entity.UserEntity;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserMapperTest {

  @Autowired
  private UserMapper mapper;

  private String email = "email@email.com";
  private String name = "name";
  private String password = "password";
  private long token = 1;
  private UserEntity entity;

  @Before
  public void before() {
    entity = new UserEntity(email, name, password, token);
    mapper.deleteAll();
    assertEquals("database should be empty before each test", 0, mapper.countAll());
  }

  @After
  public void after() {
    mapper.deleteAll();
    assertEquals("database should be empty after each test", 0, mapper.countAll());
  }

  @Test
  public void all() {
    assertTrue("insert should be successeful", mapper.insert(entity));
    assertEquals("countAll() should be 1", 1, mapper.countAll());
    assertEquals("selectWhere(String).get(0) and entity should be equal", entity,
        mapper.selectWhere("`email` = '" + email + "'").get(0));
    assertEquals("select(String, String).get(0) and entity should be equal", entity,
        mapper.select("email", email).get(0));
    assertEquals("selectOneWhere(String) and entity should be equal", entity,
        mapper.selectOneWhere("`email` = '" + email + "'"));
    assertEquals("selectOne(String, String) and entity should be equal", entity, mapper.selectOne("email", email));
    assertEquals("selectAll().get(0) and entity should be equal", entity, mapper.selectAll().get(0));
    mapper.deleteAll();
    assertEquals("countAll() should be 0 after deleteAll()", 0, mapper.countAll());
  }

  @Test
  public void insert() {
    assertTrue("insert(entity) should return true", mapper.insert(entity));
    assertEquals("countAll() should be 1 after insert(entity)", 1, mapper.countAll());
  }

  @Test
  public void delete() {
    insert();
    assertTrue("delete(field, value) should return true", mapper.delete("email", email));
    assertEquals("countAll() should be 0 after delete(field, value)", 0, mapper.countAll());
    insert();
    assertTrue("deleteWhere(where) should return true", mapper.deleteWhere("`email` = '" + email + "'"));
    assertEquals("countAll() should be 0 after deleteWhere(where)", 0, mapper.countAll());
    insert();
    mapper.deleteAll();
    assertEquals("countAll() should be 0 after countAll()", 0, mapper.countAll());
  }

  @Test
  public void update() {
    insert();
    entity.setEmail("new@email.com");
    entity.setName("new");
    entity.setPassword("new");
    entity.setToken(token);
    assertTrue("update(entity) should return true", mapper.update("email", email, entity));
    assertEquals("selectOne(String, String) should return and equal to entity", entity,
        mapper.selectOne("email", entity.getEmail()));
  }

  @Test
  public void updateWhere() {
    insert();
    entity.setEmail("new@email.com");
    entity.setName("new");
    entity.setPassword("new");
    entity.setToken(token);
    assertTrue("update(entity) should return true", mapper.updateWhere("`email` ='" + email + "'", entity));
    assertEquals("selectOne(String, String) should return and equal to entity", entity,
        mapper.selectOne("email", entity.getEmail()));
  }

  @Test
  public void select() {
    assertNull("selectOne(String, String) should be null", mapper.selectOne("email", email));
    assertNull("selectOneWhere(where) should be null", mapper.selectOneWhere("`email` = '" + email + "'"));
    assertEquals("select(String, String).size() should be 0", 0, mapper.select("email", email).size());
    assertEquals("selectWhere(String).size() should be 0", 0, mapper.selectWhere("`email` = '" + email + "'").size());
    assertEquals("selectAll().size() should be 0", 0, mapper.selectAll().size());
    insert();
    assertEquals("selectOne(String, String) should return and equal to entity", entity,
        mapper.selectOne("email", email));
    assertEquals("selectOneWhere(where) should return and equal to entity", entity,
        mapper.selectOneWhere("`email` = '" + email + "'"));
    assertEquals("select(String, String).get(0) should equal to entity", entity, mapper.select("email", email).get(0));
    assertEquals("selectWhere(String).get(0) should equal to entity", entity,
        mapper.selectWhere("`email` = '" + email + "'").get(0));
    assertEquals("selectAll().get(0) should equal to entity", entity, mapper.selectAll().get(0));
  }

  @Test
  public void count() {
    assertEquals("count(String, String) should be 0", 0, mapper.count("email", email));
    assertEquals("countWhere(String) should be 0", 0, mapper.countWhere("`email` = '" + email + "'"));
    assertEquals("countAll() should be 0", 0, mapper.countAll());
    insert();
    assertEquals("count(String, String) should be 1", 1, mapper.count("email", email));
    assertEquals("countWhere(String) should be 1", 1, mapper.countWhere("`email` = '" + email + "'"));
    assertEquals("countAll() should be 1", 1, mapper.countAll());
  }

}
```

完整代码请参阅 [`GitHub, src/test/java/red/don/api/android/mapper/UserMapperTest.java`](https://github.com/DevinDon/android-api-server/blob/c273b8eb841c1d4fde10783f5c23d931bb0e4ba4/src/test/java/red/don/api/android/mapper/UserMapperTest.java) :point_left: 。

## 测试 `JWTUtil`

在测试目录 `src/test/java/red/don/api/android/util` 下新建类 [`JWTUtilTest`](https://github.com/DevinDon/android-api-server/blob/6b0cb1e22c3cc3b025d786652fed018348a295de/src/test/java/red/don/api/android/util/JWTUtilTest.java) :point_left: ，并测试所有方法：

**:wrench: 修复：all()** ，[点击查看对比](https://github.com/DevinDon/android-api-server/commit/6b0cb1e22c3cc3b025d786652fed018348a295de) :point_left: 。

```java
@Test
public void all() {
  UserEntity user = new UserEntity("email@email.com", "username", null, System.currentTimeMillis());
  String token = JWTUtil.generate(user);
  UserEntity parse = JWTUtil.parse(token);
  assertNotNull("generate(UserEntity) should return & not null", token);
  assertEquals("parse(String) should return & equal to user", user, parse);
  assertNull("parse(String) should return null because token is invalid", JWTUtil.parse("a invalid jwt token"));
}
```

完整代码请参阅 [`GitHub, src/test/java/red/don/api/android/util/JWTUtilTest.java`](https://github.com/DevinDon/android-api-server/blob/c273b8eb841c1d4fde10783f5c23d931bb0e4ba4/src/test/java/red/don/api/android/util/JWTUtilTest.java) :point_left: 。

## 测试 `SignService`

打开 [`SignServiceTest`](https://github.com/DevinDon/android-api-server/blob/c273b8eb841c1d4fde10783f5c23d931bb0e4ba4/src/test/java/red/don/api/android/service/SignServiceTest.java) :point_left: ，重构 `signIn` ：

```java
@Test
public void signIn() {
  assertNotNull("signIn(UserEntity) should return token because user is exist", service.signIn(user));
  assertNull("signIn(UserEntity) should return null because email is wrong",
             service.signIn(new UserEntity("wrong email", "password")));
  assertNull("signIn(UserEntity) should return null because password is wrong",
             service.signIn(new UserEntity("email", "wrong password")));
  assertNull("signIn(UserEntity) should return null because nobody is not exist", service.signIn(nobody));
}
```

以及 `signOut` ：

```java
@Test
public void signOut() {
  assertTrue("signOut(UserEntity) should return true", service.signOut(user));
  assertFalse("signOut(UserEntity) should return false because user not exist", service.signOut(nobody));
}
```

完整代码请参阅 [`GitHub, src/test/java/red/don/api/android/service/SignServiceTest.java`](https://github.com/DevinDon/android-api-server/blob/c273b8eb841c1d4fde10783f5c23d931bb0e4ba4/src/test/java/red/don/api/android/service/SignServiceTest.java) :point_left: 。

## 运行测试

打开 `Postman` ，并启动 Spring Boot 项目，我们进行一次模拟访问测试。

1. 注册，向数据库添加用户：

   访问 `http://localhost:8080/sign/up`

   数据

   ```json
   {"email":"new@email.com","name":"new name","password":"new password"}
   ```

   响应

   ```json
   {
       "status": true,
       "message": null,
       "content": {
           "email": "new@email.com",
           "name": "new name",
           "password": "new password",
           "token": 0
       }
   }
   ```

2. 登录，获取 `JWT Token` ：

   访问 `http://localhost:8080/sign/in`

   数据

   ```json
   {"email":"new@email.com","name":"new name","password":"new password"}
   ```

   响应

   ```json
   {
       "status": true,
       "message": "",
       "content": "eyJhbGciOiJIUzUxMiJ9.eyJuYW1lIjoibmV3IG5hbWUiLCJlbWFpbCI6Im5ld0BlbWFpbC5jb20iLCJ0b2tlbiI6MTU1ODYwODkzMTI3N30.vpfJkld6dV_0nakeciawdOndItYzOHxgir4Hs1qDbSPpOtrFZprKoMIXrc5h8SJ_z2zYENK4Q9gOFXC8HDE5yA"
   }
   ```

3. 注销，清除 `JWT Token` ：

   访问 `http://localhost:8080/sign/out`

   数据

   ```json
   {"email":"new@email.com","name":"new name","password":"new password"}
   ```

   响应

   ```json
   {
       "status": true,
       "message": null,
       "content": null
   }
   ```

4. 查看，确认 `token` 为 `0` ：

   访问 `http://localhost:8080/user/view`

   数据

   ```json
   {"email":"new@email.com","name":"new name","password":"new password"}
   ```

   响应

   ```json
   {
       "status": true,
       "message": null,
       "content": {
           "email": "new@email.com",
           "name": "new name",
           "password": "new password",
           "token": 0
       }
   }
   ```

**:star: 提示：如果你使用 Git 来管理代码，别忘了提交修改。**

# 本章小节

本章我们了解了 `JWT` 的定义、用途，以及 `JJWT` 在 Spring Boot 中的应用。在接下来的章节中，我们将会使用 `AOP` 切面编程思想，结合 `JWT` 实现服务器的用户鉴权功能。
