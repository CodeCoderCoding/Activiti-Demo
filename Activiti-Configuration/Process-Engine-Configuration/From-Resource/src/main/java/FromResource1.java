
import org.activiti.engine.ProcessEngineConfiguration;

/**
 * @ClassName FromResource1
 * @Description
 * @Author yuzhihua
 * @Date 2022/4/3 13:20
 * @Version 1.0.0
 **/
public class FromResource1 {
    public static void main(String[] args) {
        // 指定配置文件创建ProcessEngineConfiguration
        ProcessEngineConfiguration config = ProcessEngineConfiguration
                .createProcessEngineConfigurationFromResource("my-activiti1.xml");

    }
}
