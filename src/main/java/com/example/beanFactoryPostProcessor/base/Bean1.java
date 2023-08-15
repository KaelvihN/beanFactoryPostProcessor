package com.example.beanFactoryPostProcessor.base;

import lombok.extern.slf4j.Slf4j;

/**
 * @author : KaelvihN
 * @date : 2023/8/13 19:21
 */

@Slf4j
public class Bean1 {
    public Bean1() {
        log.info("我被Spring管理惹...By Bean1");
    }
}
