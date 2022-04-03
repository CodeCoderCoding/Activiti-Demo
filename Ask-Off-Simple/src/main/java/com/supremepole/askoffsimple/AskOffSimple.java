package com.supremepole.askoffsimple;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

/**
 * @ClassName AskOffSimple
 * @Description
 * @Author yuzhihua
 * @Date 2022/3/29 22:19
 * @Version 1.0.0
 **/
public class AskOffSimple {
    public static void main(String[] args) {
        // 新建流程引擎
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        // 存储服务
        RepositoryService repositoryService = engine.getRepositoryService();
        // 运行时服务
        RuntimeService runtimeService = engine.getRuntimeService();
        // 任务服务
        TaskService taskService =engine.getTaskService();
        // 部署服务
        repositoryService.createDeployment().addClasspathResource("MyFirstProcess.bpmn").deploy();
        // process的id属性
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("myProcess");

        // 普通员工完成请假的任务
        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();//查询流程任务
        System.out.println("当前流程节点：" + task.getName());
        taskService.complete(task.getId());

        // 经理审批任务
        task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        System.out.println("当前流程节点：" + task.getName());
        taskService.complete(task.getId());

        // 流程结束
        task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        System.out.println("流程结束：" + task);
        // 关闭流程引擎
        engine.close();
    }
}
