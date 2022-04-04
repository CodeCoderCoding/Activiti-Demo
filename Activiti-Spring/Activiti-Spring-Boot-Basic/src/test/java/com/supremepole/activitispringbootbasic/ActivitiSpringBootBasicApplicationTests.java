package com.supremepole.activitispringbootbasic;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.spring.boot.DataSourceProcessEngineAutoConfiguration;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

@SpringBootTest
public class ActivitiSpringBootBasicApplicationTests {

    @Test
    public void processEngineWithBasicDataSource() throws Exception {
        AnnotationConfigApplicationContext context = this.context(
                DataSourceAutoConfiguration.class, DataSourceProcessEngineAutoConfiguration.DataSourceProcessEngineConfiguration.class);
        Assert.assertNotNull("the processEngine should not be null!", context.getBean(ProcessEngine.class));
    }

    @Test
    public void launchProcessDefinition() throws Exception {
        AnnotationConfigApplicationContext applicationContext = this.context(
                DataSourceAutoConfiguration.class, DataSourceProcessEngineAutoConfiguration.DataSourceProcessEngineConfiguration.class);
        RepositoryService repositoryService = applicationContext.getBean(RepositoryService.class);
        Assert.assertNotNull("we should have a default repositoryService included", repositoryService);
        Assert.assertEquals(1, repositoryService.createProcessDefinitionQuery().count());
        List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("waiter")
                .list();
        Assert.assertNotNull(processDefinitionList);
        Assert.assertTrue(!processDefinitionList.isEmpty());
        ProcessDefinition processDefinition = processDefinitionList.iterator().next();
        Assert.assertEquals("waiter", processDefinition.getKey());
    }

    private AnnotationConfigApplicationContext context(Class<?>... clzz) {
        AnnotationConfigApplicationContext annotationConfigApplicationContext
                = new AnnotationConfigApplicationContext();
        annotationConfigApplicationContext.register(clzz);
        annotationConfigApplicationContext.refresh();
        return annotationConfigApplicationContext;
    }

}
