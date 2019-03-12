package com.org.prototypeExercise.myPractise;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * Created by Administrator on 2019/3/12.
 */
public class myPractise {

    public static void main(String[] args) {
        Person person = initPerson("张三");

        Student student = (Student) getPrototype(person,Student.class);

        System.out.println("student's name is : " + student.getName());

        person = initPerson("李四");

        student = (Student) getPrototype(person,Student.class);

        System.out.println("student's name is : " + student.getName());

    }

    public static Person initPerson(String name){
        Person person = new Person();
        person.setName(name);
        person.setSex(1);
        person.setAge(23);
        person.setHeight("170cm");
        person.setWeight("130kg");
        return person;
    }

    public static Object getPrototype(Object clazzOrigin,Class clazzTarget){

        try {
            Method[] methods = clazzOrigin.getClass().getMethods();
            Object instance = clazzTarget.newInstance();
            for (Method m : methods) {
                if (m.getName().equals("getClass")){
                    continue;
                }
                //未来优化成，如果抛出MethodNotFoundException，就终止线程，并打印Exception
                if (m.getName().startsWith("get")) {
                    //获取返回值类型
                    Class<?> returnType = m.getReturnType();
                    //执行方法，得到返回值
                    Object obj = m.invoke(clazzOrigin, null);
                    //得到目标方法名
                    String targetName = m.getName().replace("get","set");
                    //得到目标方法
                    Method targetMethod = instance.getClass().getMethod(targetName, returnType);
                    //类型转换
                    obj = convertType(returnType,obj.toString());
                    //执行set方法
                    targetMethod.invoke(instance, obj);

                }
            }
            return instance;
        } catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public static Object convertType(Class<?> returnType,String obj){
//        if (returnType.equals(String.class)){
//            return (String) obj;
//        }
        if (returnType.equals(int.class)){
            return Integer.parseInt(obj);
        }
        if (returnType.equals(double.class)){
            return Double.parseDouble(obj);
        }

        return obj;
    }
}
