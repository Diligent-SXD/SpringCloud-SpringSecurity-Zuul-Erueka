# Zuul Auth Eureka Demo

本项目使用了SpringCloud、SpringSecurity、Zuul、Eureka、Redis、[JWT](https://jwt.io/introduction/) token等技术.

#### Modules

##### 1. **auth-service**
这个微服务是申请token并把token保存到redis等待下次验证。
- 用户 POST `{username,password}` 到 `/auth/login`.
- 该服务会校验用户账号密码，信息正确的话会生成JWTtoken并根据账号作为key把相关信息存到Redis中，最后在body中返回token给客户端。

##### 2. **student-service**
前端设置header:
- 把从授权中心拿到的token放在header上,key为`Authorization`，并在token前加上`Bearer `,在访问的时候一同发送到后台.

接口:
- POST`{pageNum,pageSize}`到`/student/list`该接口任何人都可以访问
- POST`{Student}`到`/student/add`该接口只能拥有ROLE_ADMIN权限的人才能访问
- DELETE`{id}`到`/student/delete/{id}`该接口只能拥有ROLE_ADMIN权限的人才能访问
- PUT`{id, Student}``/student/update/{id}`该接口只能拥有ROLE_ADMIN权限的人才能访问
 
##### 3. **gateway-service**
`Zuul`网管:
- 定义路由到 `auth-service` 和 `student-service`.

##### 4. **eureka-service**
`Eureka`注册中心:
- 把`auth-service`、`student-service` 和 `gateway-service`注册进Eureka，方便服务之间的调用.

##### 5. **api**
- 定义使用到的主要工具，实体类等等

##### 6. **main**
- 定义主要的依赖

#### 待解决问题

##### 1. 网关过滤问题

`问题`:
- 由于能力有限，如果把过滤token的过滤器放在网关的微服务，token符合条件但放行后无法访问student服务，一直报`401 Unauthorized`的错误，目前不知道原因。

`目前的解决方案`:
- 把过滤器放在student服务下面对token进行过滤，在网关对所有url进行放行。

##### 2. 代码规范
```
刚学，写的不好
```

#### 参考博客

- https://blog.csdn.net/zzxzzxhao/article/details/83381876
- https://blog.csdn.net/ech13an/article/details/80779973
- https://github.com/shuaicj/zuul-auth-example.git
- 等等
