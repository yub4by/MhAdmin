package com.yppah;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * IDEA找不到或无法加载主类（SpringBoot项目）解决
 * https://blog.csdn.net/qq_42673507/article/details/104528239
 * 终端运行mvn clean compile后重启项目即可
 */
@SpringBootApplication
public class VueadminJavaApplication {

    public static void main(String[] args) {
        SpringApplication.run(VueadminJavaApplication.class, args);
    }

}
