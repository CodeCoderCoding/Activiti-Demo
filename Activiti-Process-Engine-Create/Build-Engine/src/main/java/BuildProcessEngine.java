import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;

/**
 * @ClassName BuildProcessEngine
 * @Description
 * @Author yuzhihua
 * @Date 2022/4/3 13:34
 * @Version 1.0.0
 **/
public class BuildProcessEngine {
    public static void main(String[] args) {
        // 读取配置
        ProcessEngineConfiguration config = ProcessEngineConfiguration
                .createProcessEngineConfigurationFromResource("build_engine.xml");
        // 创建ProcessEngine
        ProcessEngine engine = config.buildProcessEngine();
    }
}
