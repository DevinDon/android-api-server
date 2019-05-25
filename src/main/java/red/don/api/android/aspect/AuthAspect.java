package red.don.api.android.aspect;

import java.lang.annotation.Annotation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import red.don.api.android.entity.Response;
import red.don.api.android.entity.UserEntity;
import red.don.api.android.mapper.UserMapper;
import red.don.api.android.util.JWTUtil;

@Aspect
@Component
public class AuthAspect {

  @Autowired
  private UserMapper mapper;

  @Pointcut("@annotation(red.don.api.android.aspect.AuthTarget)")
  public void pointcut() {

  }

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

}
