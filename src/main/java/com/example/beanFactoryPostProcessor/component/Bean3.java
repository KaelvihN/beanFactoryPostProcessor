package com.example.beanFactoryPostProcessor.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

/**
 * @author : KaelvihN
 * @date : 2023/8/14 15:12
 */

@Controller
@Slf4j
public class Bean3 {
    public Bean3() {
        log.info("我被Spring管理惹...By Bean3");
    }
}
