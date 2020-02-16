package com.javaer.myblog.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
@Component
public class LogAspect {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private class ReqeustLog {
        private String url;
        private String ip;
        private String classMethod;
        private Object[] args;

        public ReqeustLog(String url, String ip, String classMethod, Object[] args) {
            this.url = url;
            this.ip = ip;
            this.classMethod = classMethod;
            this.args = args;
        }

        @Override
        public String toString() {
            return "ReqeustLog{" + "url='" + url + '\'' + ", ip='" + ip + '\'' + ", classMethod='" + classMethod + '\'' + ", args=" + Arrays.toString(args) + '}';
        }
    }
    //定义一个切面，针对web下的所有类的所有方法
    @Pointcut("execution(* com.javaer.myblog.web.*.*(..))")
    public void log(){

    }

    //切面之前
    @Before("log()")
    public void doBefore(JoinPoint joinPoint){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String classMethod = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        ReqeustLog reqeustLog = new ReqeustLog(request.getRequestURL().toString(), request.getRemoteAddr(), classMethod, joinPoint.getArgs());
        logger.info("Rquest  ----- {}", reqeustLog);
    }

    //切面之后
    @After("log()")
    public void doAfter(){

    }
    //返回之后
    @AfterReturning(returning = "result",pointcut = "log()")
    public void doAfterReturn(Object result){
        logger.info("Return ------ {}",result );
    }

}
