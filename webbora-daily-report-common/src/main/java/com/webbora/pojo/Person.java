package com.webbora.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @desc: TODO
 * @author: Jupiter.Lin
 * @date: 2025/4/16
 */
@Data
@NoArgsConstructor
public class Person {
    private int id;
    private String name;
    private int age;

    // 构造函数、getter和setter方法
    public Person(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
