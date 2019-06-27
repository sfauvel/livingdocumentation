package org.dojo.livingdoc.demo;

import org.dojo.livingdoc.Configuration;
import org.dojo.livingdoc.annotation.ClassDemo;

import java.lang.reflect.Field;
import java.sql.Connection;


/**
 * Execute some code to retrieve information.
 *
 * Reflexion is used to get all attributes to document.
 */
@ClassDemo
public class ExecuteDoc {
    public static void main(String[] args) throws IllegalAccessException {
        Configuration configuration = new Configuration();

        System.out.println("Default Configuration");
        for (Field field : Configuration.class.getDeclaredFields()) {
            field.setAccessible(true);
            System.out.println(field.getName() + ":" + field.get(configuration));
        }
    }

}
