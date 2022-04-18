package com.supremepole.askoff;

import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.cmd.NeedsActiveTaskCmd;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntityManagerImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.*;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AskOffApplicationTests {

    @Autowired
    private ProcessEngine processEngine;
    @Autowired
    private IdentityService identityService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private ManagementService managementService;

    /**
     * 获取流程引擎及各个Service
     */
//    @Before
//    public void before(){
//        ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration
//                .createStandaloneProcessEngineConfiguration();
//        processEngineConfiguration.setJdbcDriver("com.mysql.cj.jdbc.Driver");
//        processEngineConfiguration.setJdbcUrl("jdbc:mysql://localhost:3306/activiti-demo-ask-off?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8&nullCatalogMeansCurrent=true");
//        processEngineConfiguration.setJdbcUsername("root");
//        processEngineConfiguration.setJdbcPassword("123456");
//        //如果表不存在，则自动创建表
//        processEngineConfiguration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
//        processEngine = processEngineConfiguration.buildProcessEngine();
//
//        System.out.println(processEngine.toString());
//
//        repositoryService = processEngine.getRepositoryService();
//        identityService = processEngine.getIdentityService();
//        runtimeService = processEngine.getRuntimeService();
//        taskService = processEngine.getTaskService();
//        historyService = processEngine.getHistoryService();
//
//        managementService = processEngine.getManagementService();
//
//        assertNotNull(processEngine);
//    }


    /**
     * 初始化审批人 act_id_user: deptmen， hrmen
     */
    @Test
    public void initUser(){
        User deptmen = identityService.newUser("deptmen");
        deptmen.setFirstName("部门领导");
        identityService.saveUser(deptmen);

        User hrmen = identityService.newUser("hrmen");
        hrmen.setFirstName("人事领导");
        identityService.saveUser(hrmen);

//        assertEquals(2,identityService.createUserQuery().count());
    }

    /**
     * 初始化组 act_id_group: deptLeader, hr
     */
    @Test
    public void initGroup(){
        Group deptLeader = identityService.newGroup("deptLeader");
        deptLeader.setName("deptLeader");
        //扩展字段
        deptLeader.setType("assignment");
        identityService.saveGroup(deptLeader);

        Group hr = identityService.newGroup("hr");
        hr.setName("hr");
        hr.setType("assignment");
        identityService.saveGroup(hr);

        assertEquals(2,identityService.createGroupQuery().count());
    }

    /**
     * 初始化人员、组的关系
     */
    @Test
    public void initMemberShip(){
        identityService.createMembership("deptmen","deptLeader");
        identityService.createMembership("hrmen","hr");
    }

    /**
     * 部署流程定义
     */
    @Test
    public void deployTest(){
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("leave.bpmn")
                .deploy();
        assertNotNull(deployment);
    }

    /**
     * 发起流程
     */
    @Test
    public void submitApplyTest(){
        String applyUserId = "startmen";
        //设置流程启动发起人,在流程开始之前设置，会自动在表ACT_HI_PROCINST 中的START_USER_ID_中设置用户ID
        identityService.setAuthenticatedUserId(applyUserId);
        runtimeService.startProcessInstanceByKey("leave");
    }

    /**
     * 获取待办并通过
     */
    @Test
    public void getTaskTodo(){
        //根据当前人id查询待办任务
        List<Task> taskList = taskService.createTaskQuery()
                .processDefinitionKey("leave")
                .taskAssignee("deptmen")
                .active().list();
        //根据当前人未签收的任务
        List<Task> taskList1 = taskService.createTaskQuery()
                .processDefinitionKey("leave")
                .taskCandidateUser("deptmen")
                .active().list();

        List<Task> list = new ArrayList<>();
        list.addAll(taskList);
        list.addAll(taskList1);
        System.out.println("-------"+list.get(0).toString()+"----"+list.get(0).getName());
        //assertEquals(1,list.size());

        Task task = list.get(0);

        //审批流程
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processDefinitionKey("leave")
                .singleResult();
        // 添加批注
        identityService.setAuthenticatedUserId("deptmen");
        taskService.addComment(task.getId(), processInstance.getId(), "deptmen【同意】了");
        Map<String, Object> variables = new HashMap<>();
        variables.put("deptLeaderApproved", true);
        // 只有签收任务，act_hi_taskinst 表的 assignee 字段才不为 null
        taskService.claim(task.getId(), "deptmen");
        taskService.complete(task.getId(), variables);

    }

    /**
     * 获取已完成的流程
     */
    @Test
    public void getCompileTask(){
        List<HistoricTaskInstance> taskInstanceList = historyService.createHistoricTaskInstanceQuery()
                .processDefinitionKey("leave")
                .taskAssignee("deptmen")
                .finished().list();
        for (HistoricTaskInstance instance  :taskInstanceList) {
            System.out.println(instance.getProcessInstanceId()+"--"+instance.getName()+"--"+instance.getAssignee());
        }
    }

    /**
     * 获取审批意见
     */
    @Test
    public void getHistoryComment(){
        //获取流程实例对象
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processDefinitionKey("leave").singleResult();
        //获取历史活动集合
        List<HistoricActivityInstance> historicActivityInstanceList = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstance.getId())
                .activityType("userTask")
                .finished()
                .list();

        for (HistoricActivityInstance historicActivityInstance:historicActivityInstanceList ) {
            List<Comment> commentList = taskService.getTaskComments(historicActivityInstance.getTaskId(), "comment");
            for (int i = 0; i < commentList.size(); i++) {
                System.out.println(commentList.get(i).getProcessInstanceId()+"---"+ commentList.get(i).getUserId() +"批复内容：" + commentList.get(i).getFullMessage());
            }
        }
    }

}
