package com.ide.api.utilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
@Component
public class AppRunner implements ApplicationRunner {
    private final DataSource dataSource;

    @Autowired
    public  AppRunner(DataSource dataSource){
        this.dataSource = dataSource;
    }

    @Override
    public void run(ApplicationArguments args) throws  Exception{
        System.out.println("Database URL: "+ dataSource.getConnection().getMetaData().getURL());
        System.out.println("Database URL: "+ dataSource.getConnection().getMetaData().getURL());
    }
}
