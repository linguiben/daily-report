package com.webbora.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @desc: TODO
 * @author: Jupiter.Lin
 * @date: 2025/4/16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    private int id;
    private String name;
    private int age;
}
