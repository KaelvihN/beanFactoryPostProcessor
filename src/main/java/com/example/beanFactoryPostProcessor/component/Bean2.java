package com.example.beanFactoryPostProcessor.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author : KaelvihN
 * @date : 2023/8/13 19:21
 */

@Slf4j
@Component
public class Bean2 {
    public Bean2() {
        log.info("我被Spring管理惹...By Bean2");
    }
}
