package com.basic.myspringboot.runner;

import com.basic.myspringboot.config.MyEnvironment;
import com.basic.myspringboot.property.MyPropProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class MyPropRunner implements ApplicationRunner {
    @Value("${myprop.username}")
    private String userName;

    @Value("${myprop.port}")
    private int port;

    @Autowired
    private Environment environment;

    @Autowired
    private MyPropProperties properties;

    @Autowired
    private MyEnvironment myEnvironment;

    private Logger logger = LoggerFactory.getLogger(MyPropRunner.class);

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("Logger 구현체 => " + logger.getClass().getName());

        logger.debug("${myprop.username} = {}", userName);
        logger.debug("${myprop.port} = {}", port);

        logger.info("MyPropProperties getUserName() = {}", properties.getUserName());
        logger.info("MyPropProperties getPort() = {}", properties.getPort());

        logger.info("현재 활성화된 MyEnvironment Bean = {}", myEnvironment);
    }//run
}//class
