package com.zjw.utils;

import com.zjw.config.StaticConfiguration;
import com.zjw.domain.Option;
import com.zjw.service.OptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/26 13:31
 */
@Component
public class OptionUtils {

    private static OptionService optionService;

    @Autowired
    OptionUtils(OptionService optionService) {
        OptionUtils.optionService = optionService;
    }

    public static void recordCurrentOption(String option) {
        Option option1 = new Option(0, StaticConfiguration.getEmploy().getLoginName() + ":" + StaticConfiguration.getEmploy().getName(),
                option, new Date());
        OptionUtils.optionService.insertOption(option1);
    }
}
