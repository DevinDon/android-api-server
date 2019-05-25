# Spring Boot 入门实战（七）：无状态鉴权与 AOP

# 项目信息

项目代码已托管至 [GitHub, https://github.com/DevinDon/android-api-server](https://github.com/DevinDon/android-api-server) ，[点击此处](https://github.com/DevinDon/android-api-server/tree/c5f0e56f4183f233f747fae932b7cd5e8f6237e0) :point_left: 查看本章节内容。

在上一章中我们讲到了 `JWT` 的生成与解析，并实现了工具类 `JWTUtil` 。在本章中，我们将使用 `AOP` 切面编程来实现 `JWT` 身份鉴权，并将解析信息注入控制层 `Controller` ，实现代码解耦。

# 基础概念

## 什么是 AOP

AOP，Aspect Oriented Programming，面向切面编程（面向过程编程）。

下面我们将通过一个小栗子来简单了解 **AOP** ，并在与 **OOP** 的对比中了解这两种编程思想的差异。

## AOP 和 OOP 的差异

在介绍 AOP 与 OOP 的差异之前，我们先来回顾下 OOP 的特性。

### OOP 的典型实现

OOP，Object Oriented Programing，面向对象编程。

我们来看一个例子：**（一个人，会吃饭唱歌睡觉）**

```java
public class Human {

  public String name;

  public void eat(String food) {
    System.out.print("I will eat " + food + " now.")
  }

  public void sing(String song) {
    System.out.print("I want sing " + song);
  }

  public void sleep() {
    System.out.print("I'm going to sleep.");
  }

}

new Human().eat("fish"); // I will eat fish now.
```

这是一段典型的 OOP 代码，这段代码定义了一个 `Human 人`，这个人有一个行为（方法）是 `eat 吃`，以及一个特征（属性）是 `name 姓名`。

如果我们想让这个人在吃东西前后洗洗手，我们可能需要把 `eat()` 改成这样：**（一个人，在吃饭前后要洗手）**

```java
public class Human {

  /* ... */

  public void eat(String food) {
    System.out.print("Wash my hands first!");
    System.out.print("I will eat " + food + " now.");
    System.out.print("Wash my hands again!");
  }

  /* ... */

}

new Human().eat("fish");
// Wash my hands first!
// I will eat fish now.
// Wash my hands again!
```

看起来没有什么问题，但是却加入了与 `eat()` 这个行为无关的代码，增加了程序的无关耦合度。

但是如果这个人有洁癖，不管干什么之前都要洗手，那我们就需要把**所有行为**都加上这段无关的代码，像这样：**（一个人，在干什么事前后都要洗手）**

```java
public class Human {

  public String name;

  public void eat(String food) {
    System.out.print("Wash my hands first!");
    System.out.print("I will eat " + food + " now.");
    System.out.print("Wash my hands again!");
  }

  public void sing(String song) {
    System.out.print("Wash my hands first!");
    System.out.print("I want sing " + song);
    System.out.print("Wash my hands again!");
  }

  public void sleep() {
    System.out.print("Wash my hands first!");
    System.out.print("I'm going to sleep.");
    System.out.print("Wash my hands again!");
  }

}


```

天啦噜！我们给 `Human` 类添加了相当多的**无关**且**重复**的代码，极大的影响了程序的可读性；如果说这个人现在的洁癖行为由**洗手**变成了**洗脚**，那么我们需要改动相当多的地方，这样极大的影响到了程序的可维护性。

这是由 OOP 编程思想特性引发的弊端。

为了解决这类问题，有一群古老而神圣的程序员们提出了一种全新的编程思想：AOP 面向切面编程。

### AOP 的创新实现

虽然同为编程思想，但 AOP 与 OOP 有着显著的差异。OOP 侧重于描述对象，即声明该对象的行为（方法）和特征（属性），属于静态描述；而 AOP 侧重于描述执行的过程，即对象执行某个行为（方法）的流程，属于动态描述。

回到刚刚的问题，我们使用 AOP 面向过程编程的思路改写这段代码：

类 `Human` 处于包 `com.example.human` 中。

```java
package com.example.human;

public class Human {

  public String name;

  public void eat(String food) {
    System.out.print("I will eat " + food + " now.")
  }

  public void sing(String song) {
    System.out.print("I want sing " + song);
  }

  public void sleep() {
    System.out.print("I'm going to sleep.");
  }

}
```

创建切面类，并绑定 `pointcut` 到 `Human` 上：

```java
package com.example.aspect;

public class HumanAspect {

  @Pointcut("execution (* com.example.human..*(..))")
  public void pointcut() {

  }

  @Before("pointcut()")
  public void before() {
    System.out.print("Wash my hands first!");
  }

  @After("pointcut()")
  public void after() {
    System.out.print("Wash my hands again!");
  }

}
```

该切面类定义了两个方法，`before()` 和 `after()` ，这些方法绑定在了 `com.example.human` 包中的所有方法上。其中，`before()` 会在绑定方法之前执行，`after()` 会在绑定方法之后执行。

我们将与 `eat()` 行为无关的代码提取到了 `before()` 和 `after()` 中，实现了复用，减少了重复代码；并通过一个新类 `NewHuman` 将原先的行为（方法）进行了包装，实现了解耦，降低了代码的耦合度。这样一来，如果 `Human` 有了新的癖好，只需要更改 `before()` 和 `after()` 中的代码即可，极大的增加了可维护性。

这就是一个使用 AOP 编程思想的简单实例，即将程序的运行过程进行编程（面向过程编程）。

下面我们将会详细了解 `AspectJ` 的工作方式及用法。

## 切入类 `@Aspect`

`Aspect` 注解作用于类上，用于将类标注为切入类。

```java
@Aspect
public class DemoAspect {

  /* ... */

}
```

## 切入点 `@Pointcut`

`Pointcut` 注解作用于切入类的方法上，用于标注使用该切入点的切入方法作用于哪些包、类或方法。

因为切入点表达式的语法过于灵活强大，所以仅列举几种**常用**的切入点表达书：

### 指定包、类、方法及参数

```java
@Aspect
public class DemoAspect {

  // 切入点指定为:
  // com.example.aop.service.CalcService.add(double[] args) 方法
  // 不接受重载方法, 即不接受类似于 add(float[] args) 的重载方法
  @Pointcut("execution(* com.example.aop.service.CalcService.add(double[]))")
  public void pointcutWithSpecificMethodAndParam() { }

  /* ... */

}
```

### 指定包、类及方法

指定包、类及方法，但不限制参数，即接受重载方法：

```java
@Aspect
public class DemoAspect {

  // 切入点指定为:
  // com.example.aop.service.CalcService.add 方法
  // (..) 表明不限参数类型, 即接受所有重载方法
  @Pointcut("execution(* com.example.aop.service.CalcService.add(..))")
  public void pointcutWithSpecificMethod() { }

  /* ... */

}
```

### 指定包和类

指定包和类，包括其子包中类的方法：

```java
@Aspect
public class DemoAspect {

  // 切入点指定为:
  // com.example.aop.service.CalcService 类中的方法
  // .*(..) 表明不限方法, 不限参数类型
  @Pointcut("execution(* com.example.aop.service.CalcService.*(..))")
  public void pointcutWithSpecificClass() { }

  /* ... */

}
```

### 仅指定包

仅指定包，包括其子包中类的方法：

```java
@Aspect
public class DemoAspect {

  // 切入点指定为:
  // com.example.aop.service 包中的方法, 包括包中的所有子包和子类中的方法
  // ..*(..) 表明不限子包、类、方法以及参数类型
  @Pointcut("execution(* com.example.aop.service..*(..))")
  public void pointcutWithSpecificPackage() { }

  /* ... */

}
```

### 指定注解

含有指定注解的方法：

```java
@Aspect
public class DemoAspect {

  // 切入点指定为：
  // 被 com.example.aspect.Target 注解的方法
  @Pointcut("@annotation(com.example.aspect.Target)")
  public void pointcutWithAnnotation() { }

  /* ... */

}
```

### 匹配相似方法

匹配所有名称相同的方法：

```java
@Aspect
public class DemoAspect {

  // 切入点指定为:
  // 所有前缀为 method 的方法, 不限参数类型
  @Pointcut("execution(* ..method*(..))")
  public void pointcutWithSpecificPackage() { }

  /* ... */

}
```

### 匹配方法类型

匹配方法类型：`public`, `protected` 或 `private`：

```java
@Aspect
public class DemoAspect {

  // 切入点指定为:
  // 所有前缀为 method 的 public 方法, 不限参数类型
  @Pointcut("execution(public * ..method*(..))")
  public void pointcutWithSpecificPackage() { }

  /* ... */

}
```

## 通知 `Advice`

### 前置通知 Before

需要声明切入点，在切入方法之前执行。如果前置通知抛出异常，则不会执行切入方法。

示例代码如下：

```java
@Aspect
public class DemoAspect {

  /* ... */

  @Pointcut("execution (* com.example.aop.service..*(..))")
  public void pointcut() { }

  // Before 注解声明切入点为 pointcut()
  @Before("pointcut()")
  public void before() {
    System.out.print("Aspect: Before");
  }

  // 前置通知抛出异常, 切入点指定的切入方法将不会执行
  @Before("pointcut()")
  public void beforeWithException() throws Exception {
    System.out.print("Aspect: Before but throws exception");
    throw new Exception("Aspect: Before but throws exception");
  }

  // 通过注解获取参数
  @Before(value = "pointcut()", argNames = "arg")
  public void beforeWithArgs(JoinPoint point, Object arg) {
    System.out.print("Aspect: Before with arg " + arg);
  }

  // 通过 JoinPoint 获取参数
  @Before("pointcut()")
  public void beforeWithJoinPoint(JoinPoint point) {
    System.out.print("Aspect: Before with args " + point.getArgs());
  }

  /* ... */

}
```

### 后置通知 After

需要声明切入点，在切入方法执行之后执行。

```java
@Aspect
public class DemoAspect {

  /* ... */

  @Pointcut("execution (* com.example.aop.service..*(..))")
  public void pointcut() { }

  // After 注解声明切入点为 pointcut()
  @After("pointcut()")
  public void after() {
    System.out.print("Aspect: After");
  }

  /* ... */

}
```

### 返回通知 AfterReturning

需要声明切入点，在切入方法执行完毕之后执行，可以获取切入方法的返回值。

```java
@Aspect
public class DemoAspect {

  /* ... */

  @Pointcut("execution (* com.example.aop.service..*(..))")
  public void pointcutByReturning() { }

  @AfterReturning("pointcutWithReturning")

  // AfterRunturning 注解声明切入点为 pointcutByReturning()
  @AfterReturning("pointcutWithReturning")
  public void afterReturning() {
    System.out.println("ASPECT: Return");
  }

  // 将返回值注入 (Inject) 为参数 returned
  @AfterReturning(value = "pointcutWithReturned()", returning = "returned")
  public void afterReturningWithReturning(Object returned) {
    System.out.println("ASPECT: Return\tRETURN: " + returned);
  }

  /* ... */

}
```

**注意：**

1. `returning` 参数名与 `afterReturningWithReturning()` 方法中的参数名相同，即都为 `returned`；

2. `AfterRetruning` 含有返回值时，不能和 **`Around` 环绕通知**使用同一个 `pointcut`，否则返回值会为 `null` 并报错。

### 环绕通知 Around

需要声明切入点，伴随方法的整个生命周期。

```java
@Aspect
public class DemoAspect {

  /* ... */

  @Pointcut("execution (* com.example.aop.service..*(..))")
  public void pointcut() { }

  // Around 注解声明切入点为 pointcut()
  @Around("pointcut()")
  public void around() {
    System.out.println("ASPECT: Around");
  }

  // 如果被环绕的方法有返回值, 需要通过 ProceedingJoinPoint 返回该值
  @Around("pointcut()")
  public Object around(ProceedingJoinPoint point) throws Throwable {
    System.out.println("ASPECT: Around\tARGS: " + point.getArgs().toString());
    Object result = point.proceed();
    System.out.println("ASPECT: Around\tRETURN: " + result);
    return result;
  }

}
```

**注意：**

1. 环绕通知 `Around` 使用 `ProceedingJoinPoint` 而非 `JoinPoint`；
2. 如果被环绕的方法有返回值，那么环绕方法需要返回 `ProceedingJoinPoint` 对象的 `proceed()` 值。

### 异常通知 AfterThrowing

需要声明切入点，在切入方法发生异常后执行。如果切入方法不抛出异常，则不执行。

```java
@Aspect
public class DemoAspect {

  /* ... */

  @Pointcut("execution (* com.example.aop.service..*(..))")
  public void pointcut() { }

  // AfterThrowing 注解声明切入点为 pointcut()
  @AfterThrowing("pointcut()")
  public void afterThrowing() {
    System.out.println("ASPECT: Throw");
  }

}
```

## 切入点信息 `JoinPoint`

### 获取切入方法参数

获取切入方法的参数，可用于所有的通知方法，声明如下：

```java
Object[] getArgs();
```

使用代码如下：

```java
@Before("pointcut()")
public void before(JoinPoint point) {
  System.out.println("ASPECT: Before\tARGS: " + point.getArgs().toString());
}
```

### 获取切入方法名称

通过获取切入方法的声明，能够获取切入方法名称在内的相关定义，可用于所有的通知方法，声明如下：

```java
Signature getSignature();
```

使用代码如下：

```java
@After("pointcut()")
public void after(JoinPoint point) {
  System.out.println("SIGN: " + point.getSignature().toLongString());
  Sysout.out.println("NAME: " + point.getSignature().getName());
}
```

### 获取切入方法返回值

获取切入方法的返回值，可用于 `环绕通知 Around` 和 `返回通知 AfterReturning`。

在环绕通知中：

```java
@Around("pointcut()")
public Object around(ProceedingJoinPoint point) throws Throwable {
  Object returned = point.proceed();
  System.out.println("ASPECT: Around\tRETURN: " + returned);
  return result;
}
```

在返回通知中：

```java
@AfterReturning(value = "pointcutWithReturned()", returning = "returned")
public void afterReturning(JoinPoint point, Object returned) {
  System.out.println("ASPECT: Return\tRETURN: " + returned);
}
```

## 优先级 `@Order`

如果一个切面中有两个相同的通知，如有两个前置通知 `Before`，那么如何确定两个前置通知的执行顺序？

为了解决执行顺序的问题，我们需要使用 `Order` 来定义通知的优先级，数值越小优先级越高，如：

```java
@Aspect
public class DemoAspect {

  /* ... */

  @Pointcut("execution (* com.example.aop.service..*(..))")
  public void pointcut() { }

  // First
  @Order(1)
  @Before("pointcut()")
  public void before1() {
    System.out.print("1");
  }

  // Second
  @Order(10)
  @Before("pointcut()")
  public void before2() {
    System.out.print("2");
  }
  // Third
  @Order(11)
  @Before("pointcut()")
  public void before3() {
    System.out.print("3");
  }

}
```

上述通知的执行顺序为：`before1() -> before2() -> before3()`。

# 设计思路

## 认证功能

通过 `JWT` 实现身份认证需要如下几个步骤：

1. 客户端登录，服务器向客户端发送 `JWT Token` ；
2. 客户端将 `JWT Token` 放入请求头中，向服务器发送请求；
3. 服务器端解析请求头中的 `JWT Token` ，确认用户身份。

我们将这些公用的方法提取到切面类中，即可实现无关代码的解耦。

## 日志功能

通过 `@Before` 注解实现日志功能，记录访问时间、路径、来源等信息。

# 代码实现

## 创建 `@AuthTarget`

我们使用 `@AuthTarget` 注解来标记需要认证的控制器 `Controller` （将在下一章讲到）。

在目录 `src/main/java/red/don/api/android/aspect` 下创建注解 [`AuthTarget`](https://github.com/DevinDon/android-api-server/blob/c5f0e56f4183f233f747fae932b7cd5e8f6237e0/src/main/java/red/don/api/android/aspect/AuthTarget.java) :point_left: ：

```java
package red.don.api.android.aspect;

public @interface AuthTarget {

}
```

对的，就这么简单。

## 创建 `@AuthUser`

我们使用 `@AuthUser` 注解来标记需要注入认证用户信息的参数，来区别普通参数。

在目录 `src/main/java/red/don/api/android/aspect` 下创建注解 [`AuthUser`](https://github.com/DevinDon/android-api-server/blob/c5f0e56f4183f233f747fae932b7cd5e8f6237e0/src/main/java/red/don/api/android/aspect/AuthUser.java) :point_left: ：

```java
package red.don.api.android.aspect;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthUser {

}
```

其中，`@Target(ElementType.PARAMETER)` 表明了该注解应用于方法参数上，`@Retention(RetentionPolicy.RUNTIME)` 表明在运行期保留，以便于 `AOP` 识别。

## 实现 `AuthAspect`

在目录 `src/main/java/red/don/api/android/aspect` 下创建类 [`AuthAspect`](https://github.com/DevinDon/android-api-server/blob/c5f0e56f4183f233f747fae932b7cd5e8f6237e0/src/main/java/red/don/api/android/aspect/AuthAspect.java) :point_left: ，并实现如下方法：

### 切入点 `pointcut`

```java
@Pointcut("@annotation(red.don.api.android.aspect.AuthTarget)")
public void pointcut() {

}
```

该切入点将会绑定在被 `red.don.api.android.aspect.AuthTarget` 注解的方法上。

### 环绕通知 `around`

该方法会从请求头中的 `Authorization` 字段自动获取 `JWT Token` 并进行解析验证：

```java
@Around("pointcut()")
public Object around(ProceedingJoinPoint point) throws Throwable {
  HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
  HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
    .getResponse();
  UserEntity auth = null;
  UserEntity user = null;
  if (request.getHeader("Authorization") != null) {
    auth = JWTUtil.parse(request.getHeader("Authorization").substring(7));
    user = mapper.selectOne("email", auth.getEmail());
  }
  if (user == null || user.getToken() == 0 || user.getToken() > auth.getToken()) {
    response.setStatus(401);
    return new Response<String>(false, "401 Unauthorized", null);
  } else {
    Object[] args = point.getArgs();
    /** Param with `AuthUser` target will be injected. */
    Annotation[][] annotations = ((MethodSignature) point.getSignature()).getMethod().getParameterAnnotations();
    for (int i = 0; i < args.length; i++) {
      if (args[i] instanceof UserEntity) {
        for (Annotation annotation : annotations[i]) {
          if (annotation instanceof AuthUser) {
            args[i] = user;
          }
        }
      }
    }
    return point.proceed(args);
  }
}
```

当验证通过时，该方法会将从 `JWT Token` 解析出来的用户填入控制器参数中。

当验证失败时，该方法会向客户端返回 `401 Unauthorized` 错误。

完整代码请参阅 [`GitHub, src/main/java/red/don/api/android/aspect/AuthAspect.java`](https://github.com/DevinDon/android-api-server/blob/c5f0e56f4183f233f747fae932b7cd5e8f6237e0/src/main/java/red/don/api/android/aspect/AuthAspect.java) :point_left: 。

## 实现 `LogAspect`

在目录 `src/main/java/red/don/api/android/aspect` 下创建类 [`LogAspect`](https://github.com/DevinDon/android-api-server/blob/c5f0e56f4183f233f747fae932b7cd5e8f6237e0/src/main/java/red/don/api/android/aspect/LogAspect.java) :point_left: ，并实现如下方法：

### 属性定义

```java
private Logger logger = LoggerFactory.getLogger(Application.class);
```

定义 `slf4j` 的 `Logger` 实例，用于记录日志。

### 切入点 `Pointcut`

```java
@Pointcut("execution(* red.don.api.android.controller..*(..))")
public void pointcut() {

}
```

该切入点为控制层中的所有方法。

### 前置通知 `before`

```java
@Before("pointcut()")
public void before(JoinPoint point) {
  var request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
  logger.info("METHOD: " + request.getMethod() + ", URI: " + request.getRequestURI() + " REMOTE: " + request.getRemoteAddr());
}
```

该通知记录了客户端的请求方式与路径。

完整代码请参阅 [`GitHub, src/main/java/red/don/api/android/aspect/LogAspect.java`](https://github.com/DevinDon/android-api-server/blob/c5f0e56f4183f233f747fae932b7cd5e8f6237e0/src/main/java/red/don/api/android/aspect/LogAspect.java) :point_left: 。

# 代码重构

在《**Spring Boot 入门实战（五）：四层结构之控制层**》中，我们设计的控制器无法记录登录状态，每次请求都需要客户端发送用户名与密码进行身份认证，既不简洁也不优雅。在本章中，我们将会重构控制层，使用 `JWT` 来记录登录状态，并通过 `AOP` 来实现自动认证与鉴权。

整体思路如下：

1. 为需要用户登录才能操作的方法加上 `AuthTarget` 注解；
2. 移除用户登录参数 `UserEntity` 的注解；
3. 根据接口特性设置访问方式 `POST` 、`GET` 、`DELETE` 等方式；
4. 其他优化。

## 重构 `CheckController`

### 签到 `check`

```java
@AuthTarget
@PostMapping("/in")
public Response<String> check(@AuthUser UserEntity user) {
  return new Response<>(service.check(user));
}
```

### 查看 `view`

访问方式：`POST -> GET`

```java
@AuthTarget
@GetMapping("/view/{year}/{month}/{day}")
public Response<String> view(@AuthUser UserEntity user, @PathVariable("year") int year, @PathVariable("month") int month, @PathVariable("day") int day) {
  return new Response<>(service.view(user, year, month, day));
}
```

完整代码请参阅 [`GitHub, src/main/java/red/don/api/android/controller/CheckController.java`](https://github.com/DevinDon/android-api-server/blob/c5f0e56f4183f233f747fae932b7cd5e8f6237e0/src/main/java/red/don/api/android/controller/CheckController.java) :point_left: ，或[查看重构对比](https://github.com/DevinDon/android-api-server/commit/f34ee914555686be0f99f38d75b545e5e1c72e06#diff-564ebb78588700dc8c14cd177f4884e8) :point_left: 。

## 重构 `SignController`

### 注销 `signOut`

```java
@AuthTarget
@PostMapping("/out")
public Response<String> signOut(@AuthUser UserEntity user) {
  return new Response<>(service.signOut(user));
}
```

完整代码请参阅 [`GitHub, src/main/java/red/don/api/android/controller/SignController.java`](https://github.com/DevinDon/android-api-server/blob/c5f0e56f4183f233f747fae932b7cd5e8f6237e0/src/main/java/red/don/api/android/controller/SignController.java) :point_left: ，或[查看重构对比](https://github.com/DevinDon/android-api-server/commit/8bf501ce8daceb5a49bfe0fcef5aca6b4e0d7bcb#diff-fb4e0c919e2da5ccfd24d1da56430b19) :point_left: 。

## 重构 `UserController`

### 删除 `delete`

访问方式：`POST -> DELETE`

```java
@AuthTarget
@DeleteMapping("/delete")
public Response<String> delete(@AuthUser UserEntity user) {
  return new Response<>(service.delete(user.getEmail()));
}
```

### 修改 `modify`

访问方式：`POST -> PUT`

```java
@AuthTarget
@PutMapping("/modify")
public Response<String> modify(@AuthUser UserEntity user, @RequestBody UserEntity modified) {
  return new Response<>(service.modify(user.getEmail(), modified));
}
```

### 查看 `view`

访问方式：`POST -> GET`

```java
@AuthTarget
@GetMapping("/view")
public Response<UserEntity> view(@AuthUser UserEntity user) {
  UserEntity content = service.view(user.getEmail());
  return new Response<UserEntity>(content != null, content);
}
```

完整代码请参阅 [`GitHub, src/main/java/red/don/api/android/controller/UserController.java`](https://github.com/DevinDon/android-api-server/blob/c5f0e56f4183f233f747fae932b7cd5e8f6237e0/src/main/java/red/don/api/android/controller/UserController.java) :point_left: ，或[查看重构对比](https://github.com/DevinDon/android-api-server/commit/8a695a0aa72bd2564a32f7c4a65cda56b2139961#diff-039c95c6b467e650427d1ea19baa1a9f) :point_left: 。

# 单元测试

由于 `Controller` 测试涉及到了过多的测试知识，故不进行详解，仅列出测试代码。

如果想详细了解相关知识，试试这个 [传送门](https://cn.bing.com/search?q=Spring+Boot+Controller+%E6%B5%8B%E8%AF%95&qs=n&form=QBRE&sp=-1&pq=spring+boot+controller%E6%B5%8B%E8%AF%95&sc=0-24&sk=&cvid=28C5FDD23F8440CFBDA98D70FC1D52E2) :point_left: 。

## 测试 `CheckController`

在测试目录 `src/test/java/red/don/api/android/controller` 下创建测试类 [`CheckControllerTest`](https://github.com/DevinDon/android-api-server/blob/c5f0e56f4183f233f747fae932b7cd5e8f6237e0/src/test/java/red/don/api/android/controller/CheckControllerTest.java) :point_left: ：

### 属性定义

```java
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
private UserEntity user = new UserEntity("email", "name", "password");
private ObjectMapper json = new ObjectMapper();
private String json401;
```

### 准备 `before`

```java
@Before
public void before() throws Exception {
  json401 = json.writeValueAsString(new Response<String>(false, "401 Unauthorized", null));
  mvc = MockMvcBuilders.webAppContextSetup(context).build();
  userMapper.deleteAll();
  assertEquals("table `user` should be empty", 0, userMapper.countAll());
  checkMapper.deleteAll();
  assertEquals("table `check` should be empty", 0, checkMapper.countAll());
  assertTrue("insert should be success", userMapper.insert(user));
}
```

### 收尾 `after`

```java
@After
public void after() {
  userMapper.deleteAll();
  assertEquals("table `user` should be empty", 0, userMapper.countAll());
  checkMapper.deleteAll();
  assertEquals("table `check` should be empty", 0, checkMapper.countAll());
}
```

### 测试 `/check/in`

未登录测试：

```java
@Test
public void checkInWithoutAuthed() throws Exception {
  var responseWithoutAuthed = mvc.perform(MockMvcRequestBuilders.post("/check/in")).andReturn().getResponse();
  assertEquals("HTTP status code should be 401", 401, responseWithoutAuthed.getStatus());
  assertEquals("HTTP content should be " + json401, json401, responseWithoutAuthed.getContentAsString());
}
```

已登录测试：

```java
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
```

### 测试 `/check/view`

未登录测试：

```java
@Test
public void checkViewWithoutAuthed() throws Exception {
  var responseWithoutAuthed = mvc.perform(MockMvcRequestBuilders.get("/check/view/2012/12/21")).andReturn()
    .getResponse();
  assertEquals("HTTP status code should be 401", 401, responseWithoutAuthed.getStatus());
  assertEquals("HTTP content should be " + json401, json401, responseWithoutAuthed.getContentAsString());
}
```

已登录测试：

```java
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
```

完整代码请参阅 [`GitHub, src/test/java/red/don/api/android/controller/CheckControllerTest.java`](https://github.com/DevinDon/android-api-server/blob/c5f0e56f4183f233f747fae932b7cd5e8f6237e0/src/test/java/red/don/api/android/controller/CheckControllerTest.java) :point_left: 。

## 测试 `SignController`

在测试目录 `src/test/java/red/don/api/android/controller` 下创建测试类 [`SignControllerTest`](https://github.com/DevinDon/android-api-server/blob/c5f0e56f4183f233f747fae932b7cd5e8f6237e0/src/test/java/red/don/api/android/controller/SignControllerTest.java) :point_left: ：

### 属性定义

```java
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
```

### 准备 `before`

```java
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
```

### 收尾 `after`

```java
@After
public void after() {
  userMapper.deleteAll();
  assertEquals("table `user` should be empty", 0, userMapper.countAll());
}
```

### 测试 `/sign/in`

```java
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
```

### 测试 `/sign/out`

```java
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
```

### 测试 `sign/up`

```java
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
```

完整代码请参阅 [`GitHub, src/test/java/red/don/api/android/controller/SignControllerTest.java`](https://github.com/DevinDon/android-api-server/blob/c5f0e56f4183f233f747fae932b7cd5e8f6237e0/src/test/java/red/don/api/android/controller/SignControllerTest.java) :point_left: 。

## 测试 `UserController`

在测试目录 `src/test/java/red/don/api/android/controller` 下创建测试类 [`UserControllerTest`](https://github.com/DevinDon/android-api-server/blob/c5f0e56f4183f233f747fae932b7cd5e8f6237e0/src/test/java/red/don/api/android/controller/UserControllerTest.java) :point_left: ：

### 属性定义

```java
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
```

### 准备 `before`

```java
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
```

### 收尾 `after`

```java
@After
public void after() {
  userMapper.deleteAll();
  assertEquals("table `user` should be empty", 0, userMapper.countAll());
}
```

### 测试 `/user/delete`

未登录测试：

```java
@Test
public void userDeleteWithoutAuthed() throws Exception {
  var response = mvc.perform(MockMvcRequestBuilders.delete("/user/delete")).andReturn().getResponse();
  assertEquals("HTTP status code should be 401", 401, response.getStatus());
  assertEquals("HTTP content should be " + json401, json401, response.getContentAsString());
}
```

已登录测试：

```java
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
```

### 测试 `/user/modify`

未登录测试：

```java
@Test
public void userModifyWithoutAuthed() throws Exception {
  var response = mvc.perform(MockMvcRequestBuilders.put("/user/modify")
                             .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(jsonNobody)).andReturn().getResponse();
  assertEquals("HTTP status code should be 401", 401, response.getStatus());
  assertEquals("HTTP content should be " + json401, json401, response.getContentAsString());
}
```

已登录测试：

```java
@Test
public void userModify() throws Exception {
  var responseTrue = mvc.perform(MockMvcRequestBuilders.put("/user/modify").header("Authorization", "Bearer " + token)
                                 .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(jsonUser)).andReturn().getResponse();
  assertEquals("HTTP status code should be 200", 200, responseTrue.getStatus());
  String jsonTrue = json.writeValueAsString(new Response<>(true));
  assertEquals("HTTP content should be " + jsonTrue, jsonTrue, responseTrue.getContentAsString());
}
```

### 测试 `user/view`

未登录测试：

```java
@Test
public void userViewWithoutAuthed() throws Exception {
  var response = mvc.perform(MockMvcRequestBuilders.get("/user/view")).andReturn().getResponse();
  assertEquals("HTTP status code should be 401", 401, response.getStatus());
  assertEquals("HTTP content should be " + json401, json401, response.getContentAsString());
}
```

已登录测试：

```java
@Test
public void userView() throws Exception {
  var responseTrue = mvc.perform(MockMvcRequestBuilders.get("/user/view").header("Authorization", "Bearer " + token)
                                 .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(jsonUser)).andReturn().getResponse();
  assertEquals("HTTP status code should be 200", 200, responseTrue.getStatus());
  String jsonTrue = json
    .writeValueAsString(new Response<UserEntity>(true, null, userMapper.selectOne("email", user.getEmail())));
  assertEquals("HTTP content should be " + jsonTrue, jsonTrue, responseTrue.getContentAsString());
}
```

完整代码请参阅 [`GitHub, src/test/java/red/don/api/android/controller/UserControllerTest.java`](https://github.com/DevinDon/android-api-server/blob/c5f0e56f4183f233f747fae932b7cd5e8f6237e0/src/test/java/red/don/api/android/controller/UserControllerTest.java) :point_left: 。

## 执行测试

**:warning: 警告：该测试会清空数据库，请在测试数据库上执行测试！**

**:warning: 警告：该测试会清空数据库，请在测试数据库上执行测试！**

**:warning: 警告：该测试会清空数据库，请在测试数据库上执行测试！**

执行测试，确保每个控制器均可正常工作。

## 运行服务器

现在，按 `F5` 启动服务器，顺便可以使用 `Postman` 进行一次模拟测试。

**:star: 提示：如果你使用 Git 来管理代码，别忘了提交修改。**

# 本章小节

本章我们深入了解了 `AOP` 设计理念在 Spring Boot 框架中的应用，并用其重构了控制层的鉴权部分。至此，本系列教程的开发部分已经全部完成。

在最后一章，我们将会简单了解 Spring Boot 打包与 Docker 容器部署的相关知识。此部分并非课本内容，需要一定的 Linux 运维基础，有兴趣的同学简单翻阅即可。

点击此处进入[《第八章：服务打包与部署》](8-服务打包与部署.md#项目信息)
