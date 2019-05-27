# API 接口

需要认证的接口（带有请求头 `Authorization` ）若未通过认证，则返回 `401 Unauthorized` 响应：

```json
{
  "status": false,
  "message": "401 Unauthorized",
  "content": null
}
```

## Check 签到接口

### 今日签到 `/check/in`

请求方式：`POST`

请求头：`Authorization: Bearer Token`

请求数据：`无`

返回数据：`JSON, Response<String>`

```json
{
  "status": true, // boolean, 登录成功或失败
  "message": null,
  "content": null
}
```

### 查看签到 `/check/view/${year}/${month}/${day}`

请求方式：`GET`

请求头：`Authorization: Bearer Token`

请求数据：`无`

返回数据：`JSON, Response<>`

```json
{
  "status": true, // boolean, year-month-day 当天是否签到
  "message": null,
  "content": null
}
```

备注：使用路径参数，分别为年、月、日

## Sign 认证接口

### 登录 `/sign/in`

请求方式：`POST`

请求头：`无`

请求数据：`JSON, UserEntity`

```json
{
  "email": "email@email.com",
  "password": "password"
}
```

返回数据：`JSON, Response<String>`

```json
{
  "status": true, // boolean, 登录成功或失败
  "message": null,
  "content": "JWT Token" // JWT Token, 失败返回 null
}
```

### 注销 `/sign/out`

请求方式：`POST`

请求头：`Authorization: Bearer Token`

请求数据：`无`

返回数据：`JSON, Response<>`

```json
{
  "status": true, // boolean, 注销成功或失败
  "message": null,
  "content": null
}
```

### 注册 `/sign/up`

请求方式：`POST`

请求头：`无`

请求数据：`JSON, UserEntity`

```json
{
  "email": "email@email.com",
  "password": "password"
}
```

返回数据：`JSON, Response<UserEntity>`

```json
{
  "status": true, // boolean, 注册成功或失败
  "message": null,
  "content": { // UserEntity, 注册失败返回 null
    "email": "email@email.com",
    "name": "name",
    "password": "password"
  }
}
```

## User 用户接口

### 删除用户 `/user/delete`

请求方式：`DELETE`

请求头：`Authorization: Bearer Token`

请求数据：`无`

返回数据：`JSON, Response<>`

```json
{
  "status": true, // boolean, 删除成功或失败
  "message": null,
  "content": null
}
```

### 修改用户 `/user/modify`

请求方式：`PUT`

请求头：`Authorization: Bearer Token`

请求数据：`JSON, UserEntity`

```json
"content": { // UserEntity, 修改后的信息
  "email": "new@email.com",
  "name": "new name",
  "password": "new password"
}
```

返回数据：`JSON, Response<>`

```json
{
  "status": true, // boolean, 修改成功或失败
  "message": null,
  "content": { // UserEntity, 修改后的用户信息；修改失败返回 null
    "email": "email@email.com",
    "name": "name",
    "password": "password"
  }
}
```

### 查看用户 `/user/view`

请求方式：`DELETE`

请求头：`Authorization: Bearer Token`

请求数据：`无`

返回数据：`JSON, Response<UserEntity>`

```json
{
  "status": true, // boolean, 查看成功或失败
  "message": null,
  "content": { // UserEntity, 用户信息；查看失败返回 null
    "email": "email@email.com",
    "name": "name",
    "password": "password"
  }
}
```

# 教程

[教程请查看序章](0-序章.md)。
