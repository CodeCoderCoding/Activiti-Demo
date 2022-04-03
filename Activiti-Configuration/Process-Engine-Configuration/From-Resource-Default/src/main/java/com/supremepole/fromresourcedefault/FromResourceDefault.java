package com.supremepole.fromresourcedefault;

import org.activiti.engine.ProcessEngineConfiguration;

/**
 * @ClassName com.supremepole.fromresourcedefault.FromResourceDefault
 * @Description
 * @Author yuzhihua
 * @Date 2022/4/3 13:00
 * @Version 1.0.0
 **/
public class FromResourceDefault {
    public static void main(String[] args) {
        //使用Activiti默认的方式创建ProcessEngineConfiguration
        ProcessEngineConfiguration config = ProcessEngineConfiguration.createProcessEngineConfigurationFromResourceDefault();
    }
}
