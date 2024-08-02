package com.ide.api.utilities;

import com.ide.api.dto.LikeCountDTO;
import com.ide.api.service.LikeIllustrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;

@Component
public class AppRunner implements ApplicationRunner {
    private final DataSource dataSource;
    //private final LikeIllustrationService likeIllustrationService;

    @Autowired
    public  AppRunner(
            DataSource dataSource
    ){
        this.dataSource = dataSource;
        //this.likeIllustrationService = likeIllustrationService;
    }

    @Override
    public void run(ApplicationArguments args) throws  Exception{
        System.out.println("Database URL: "+ dataSource.getConnection().getMetaData().getURL());
        System.out.println("Database URL: "+ dataSource.getConnection().getMetaData().getURL());

//        List<LikeCountDTO> likeCountDTOs = this.likeIllustrationService.countByMention(44);
//        System.out.println(likeCountDTOs.get(0).getMention()+": "+likeCountDTOs.get(0).getCount());
//        System.out.println(likeCountDTOs.get(1).getMention()+": "+likeCountDTOs.get(1).getCount());
    }
}
