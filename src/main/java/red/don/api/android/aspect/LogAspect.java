package red.don.api.android.aspect;

import java.lang.annotation.Annotation;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import red.don.api.android.Application;
import red.don.api.android.entity.LogEntity;
import red.don.api.android.entity.UserEntity;
import red.don.api.android.mapper.LogMapper;
import red.don.api.android.service.LogService;

@Aspect
@Component
public class LogAspect {

  @Autowired
  private LogMapper mapper;
  @Autowired
  private LogService service;

  private Logger logger = LoggerFactory.getLogger(Application.class);

  @Pointcut("execution(* red.don.api.android.controller..*(..))")
  public void pointcut() {

  }

  @Before("pointcut()")
  public void before(JoinPoint point) {
    var request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    UserEntity user = null;
    Object[] args = point.getArgs();
    /** Param with `AuthUser` target will be injected. */
    Annotation[][] annotations = ((MethodSignature) point.getSignature()).getMethod().getParameterAnnotations();
    for (int i = 0; i < args.length; i++) {
      if (args[i] instanceof UserEntity) {
        for (Annotation annotation : annotations[i]) {
          if (annotation instanceof AuthUser) {
            user = (UserEntity) args[i];
          }
        }
      }
    }
    LogEntity log = new LogEntity(request.getMethod(), request.getRequestURI(), user == null ? null : user.getEmail(),
        request.getRemoteAddr(), System.currentTimeMillis());
    logger.info(log.toString());
    service.access();
    mapper.insert(log);
  }

}
