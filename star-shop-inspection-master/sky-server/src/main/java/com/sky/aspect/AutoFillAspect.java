package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * 自定义切面类，实现公共字段自动填充
 */
@Aspect
@Component
@Slf4j
public class AutoFillAspect {

    /**
     * 切入点
     */
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut() {}

    /**
     *  通知：增强功能
     *  前置通知
     */
    @Before(("autoFillPointCut()"))
    public void autoFill(JoinPoint joinPoint) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        // 获取操作类型和参数
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        AutoFill annotation = signature.getMethod().getAnnotation(AutoFill.class);
        OperationType type = annotation.value();

        Object[] args = joinPoint.getArgs();
        if (args==null || args.length==0) {
            return;
        }
        Object arg = args[0];
        // 获取时间和操作人id
        LocalDateTime now = LocalDateTime.now();
        Long empId = BaseContext.getCurrentId();
        // 通过反射填充
        Class clazz = arg.getClass();
        Method setUpdateUser = clazz.getMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
        Method setUpdateTime = clazz.getMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
        setUpdateUser.invoke(arg, empId);
        setUpdateTime.invoke(arg, now);
        if (type == OperationType.INSERT) {
            Method setCreateUser = clazz.getMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
            Method setCreateTime = clazz.getMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
            setCreateTime.invoke(arg, now);
            setCreateUser.invoke(arg, empId);
        }
    }
}
