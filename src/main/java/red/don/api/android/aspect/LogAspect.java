package red.don.api.android.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import red.don.api.android.Application;

@Aspect
@Component
public class LogAspect {

  private Logger logger = LoggerFactory.getLogger(Application.class);

  @Pointcut("execution(* red.don.api.android.controller..*(..))")
  public void pointcut() {

  }

  @Before("pointcut()")
  public void before(JoinPoint point) {
    var request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    logger.info(
        "METHOD: " + request.getMethod() + ", URI: " + request.getRequestURI() + " REMOTE: " + request.getRemoteAddr());
  }

}
