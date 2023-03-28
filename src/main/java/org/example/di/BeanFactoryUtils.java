package org.example.di;

import org.example.annotation.Inject;
import org.reflections.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.util.Set;

// BeanFactoryUtils Class => 재사용성
public class BeanFactoryUtils {
    // getConstructor[class type]
    public static Constructor<?> getInjectedConstructor(Class<?> clazz) {
        // Class type Object 를 들고 오는데? 단 조건은 Inject 가 붙어 있는 Class type 만 불러오겠다는 의미!
        Set<Constructor> injectedConstructors = ReflectionUtils.getAllConstructors(clazz, ReflectionUtils.withAnnotation(Inject.class));
        // 만약? value 가 없으면?
        if (injectedConstructors.isEmpty()) {
            return null;
        }
        // 만약 있다면 injectedConstructors 는 첫번째 element 만 반환해준다는 의미
        return injectedConstructors.iterator().next();
    }
}
