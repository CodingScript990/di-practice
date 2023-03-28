package org.example.di;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

// BeanFactory Class
public class BeanFactory {
    // Field add => preInstantiatedClazz[Instance Variable], beans[HashMap Type Variable]
    private final Set<Class<?>> preInstantiatedClazz;
    // key 로 Class type Generic 과 Object value 를 Instance variable 로 반환을 해주는 variable 임
    private Map<Class<?>, Object> beans = new HashMap<>();

    // BeanFactory Constructor => parameter[preInstantiatedClazz]
    public BeanFactory(Set<Class<?>> preInstantiatedClazz) {
        this.preInstantiatedClazz = preInstantiatedClazz;
        // 초기화 method 를 생성 => beans 를 초기화 해주는 작업
        initialize();
    }

    // initialize method => beans 초기화 작업
    private void initialize() {
        // topDown 방식 작업 => Class 을 Instance type 으로 해주기 위한 작업
        for (Class<?> clazz : preInstantiatedClazz) {
            // Object type => Instance add
            Object instance = createInstance(clazz);
            // beans add => class type object[clazz], instance type object[instance]
            beans.put(clazz, instance);
        }
    }

    // createInstance method => clazz
    // 1. UserController => Recursion function[재귀함수]
    // 2. UserService
    private Object createInstance(Class<?> clazz) {
        // 1. Constructor => Instance 를 생성하기 위해서는 Constructor 가 있어야함
        // topDown 방식 작업 => class type object 를 Constructor 로 조회하는 작업
        Constructor<?> constructor = findConstructor(clazz);

        // 2. Parameter => parameter 정보도 알아야함!
        List<Object> parameters = new ArrayList<>();
        // 1. 번에서 조회한 values 를 가져와서 parameter 의 value 도 true 인지 check
        for (Class<?> typeClass : constructor.getParameterTypes()) {
            // UserService
            // constructor 에서 문제없이 들고왔다면, parameter 를 add 해줌[Instance Object add]
            parameters.add(getParameterByClass(typeClass));
        }
        // 3. Instance => Constructor 조회, Parameter 정보도 check 후 완벽하면 add 하겠다는 의미!
        // try-catch[예외처리]
        try {
            return constructor.newInstance(parameters.toArray());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    // Constructor => findConstructor method
    private Constructor<?> findConstructor(Class<?> clazz) {
        // getConstructor class 반환
        return getConstructor(clazz);
    }

    // getConstructor method
    private Constructor getConstructor(Class<?> clazz) {
        // constructor 를 들고 오는데, inject 가 붙은 constructor 만 데려온다는 의미
        Constructor<?> constructor = BeanFactoryUtils.getInjectedConstructor(clazz);
        // 만약 Object value 가 있으면, constructor 를 반환해주고!
        if (Objects.nonNull(constructor)) {
            return constructor;
        }
        // constructor 가 없다면 class type object 에 1번째 constructor 를 생성해줌
        return clazz.getConstructors()[0];
    }

    // getParameterByClass method
    private Object getParameterByClass(Class<?> typeClass) {
        // instanceBean add
        Object instanceBean = getBean(typeClass);
        // 만약? 해당 object 가 nonNull 이면 instance value 를 반환해줌
        if (Objects.nonNull(instanceBean)) {
            return instanceBean;
        }
        // createInstance method 를 이용하여 typeClass 에게 값을 던져줌
        // UserService 가 생성되지 않으면 생성을 위해 return 한다는 의미
        return createInstance(typeClass);
    }

    // getBean Generic Type method => Object type[requiredType]
    // Generic :  클래스 내부에서 지정하는 것이 아닌 외부에서 사용자에 의해 지정되는 것
    // Type : 필요에 의해 지정할 수 있도록 하는 일반(Generic) 타입을 원한다는 것
    /**
     * Generic Type Method 장점
     * 1. 제네릭을 사용하면 잘못된 타입이 들어올 수 있는 것을 컴파일 단계에서 방지할 수 있음
     * 2. 클래스 외부에서 타입을 지정해주기 때문에 따로 타입을 체크하고 변환해줄 필요가 없음 즉, 관리하기가 편함
     * 3. 비슷한 기능을 지원하는 경우 코드의 재사용성이 높아짐
     */
    public <T> T getBean(Class<T> requiredType) {
        // requiredType 의 value 를 Generic Type 으로 얻어오겠다는 의미
        return (T) beans.get(requiredType);
    }
}
