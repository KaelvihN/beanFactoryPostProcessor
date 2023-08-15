package com.example.beanFactoryPostProcessor.component;

import lombok.extern.slf4j.Slf4j;

/**
 * @author : KaelvihN
 * @date : 2023/8/14 15:13
 */

@Slf4j
public class Bean4 {
    public Bean4() {
        log.info("我被Spring管理惹...By Bean4");
    }
}
