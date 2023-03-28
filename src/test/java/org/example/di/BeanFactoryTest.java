package org.example.di;

import org.example.annotation.Controller;
import org.example.annotation.Service;
import org.example.controller.UserController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

// BeanFactoryTest Class
class BeanFactoryTest {
    // Filed add => Reflections, BeanFactory
    private Reflections reflections;
    private BeanFactory beanFactory;

    // setUp method add => test method call 이전 작업을 call[test]
    @BeforeEach
    void setUp() {
        // reflections 초기화 작업 => reflections 를 사용하기 위함
        reflections = new Reflections("org.example");
        // TopDown 방식 => Controller, Service 의 parameter 들을 받아서 Annotation Object type 들을 return 해주겠다는 의미임 => getTypesAnnotatedWith method
        // Top 부분 => UserController, UserService 의 values 를 받아옴
        Set<Class<?>> preInstantiatedClazz = getTypesAnnotatedWith(Controller.class, Service.class);
        // beanFactory 는 new BeanFactory Constructor 의 parameter 에게 value 를 전달해줌
        beanFactory = new BeanFactory(preInstantiatedClazz);
    }

    // Down 부분 => getTypesAnnotatedWith method
    // annotation type 이 여러개 들어 올 수 있기에 작성한 method
    private Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation>... annotations) {
        // BeanFactory 초기화 작업 => HashSet Constructor[Inherit]
        Set<Class<?>> beans = new HashSet<>();
        // Annotation's Class 들을 beans 에 추가해주는 작업
        for (Class<? extends Annotation> annotation : annotations) {
            // addAll method => reflections 의 annotation type add
            beans.addAll(reflections.getTypesAnnotatedWith(annotation));
        }
        // return beans
        return beans;
    }

    // ↑ ↑ ↑ ↑ [setUp Test 작업]
    
    // Test method 로 Test 작업 실시
    @Test
    void diTest() {
        // bean 을 꺼내오는 작업! => Class type Object
        UserController userController = beanFactory.getBean(UserController.class);

        // userController 가 not null 이면? 즉, 무조건 값이 null 이 될 수 없기에 test
        assertThat(userController).isNotNull();
        // userController 에 Service 도 not null 이면? 즉, 무조건 값이 null 이 될 수 없기에 test
        assertThat(userController.getUserService()).isNotNull();

        // 1. di FrameWork 를 add 하지 않아서 1차 error 가 남!
        // 2. di FrameWork 작업 후
    }
}