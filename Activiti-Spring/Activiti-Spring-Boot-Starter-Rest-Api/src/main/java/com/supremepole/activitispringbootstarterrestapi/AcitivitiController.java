package com.supremepole.activitispringbootstarterrestapi;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName AcitivitiController
 * @Description
 * @Author yuzhihua
 * @Date 2022/4/4 22:33
 * @Version 1.0.0
 **/
@RestController
public class AcitivitiController {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private IdentityService identityService;

    @RequestMapping("/activiti/repositoryService")
    @ResponseBody
    public String useRepositoryService() {
        return "调用流程存储服务，查询部署数量："
                + repositoryService.createDeploymentQuery().count();
    }

    @RequestMapping("/activiti/identityService")
    @ResponseBody
    public String useIdentityService() {
        return "调用用户服务，查询用户数量："
                + identityService.createUserQuery().count();
    }
}
