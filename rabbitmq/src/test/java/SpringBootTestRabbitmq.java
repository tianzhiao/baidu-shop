import com.baidu.rabbitmq.SpringBootApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName SpringBootTestRabbitmq
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/12
 * @Version V1.09999999999999999
 **/
@SpringBootTest(classes = SpringBootApplication.class)
@RunWith(SpringRunner.class)
public class SpringBootTestRabbitmq {


    @Autowired
    AmqpTemplate amqpTemplate;

    @Test
    public void test(){

        amqpTemplate.convertAndSend("exchange_test","l.l","你好世界");

        try {
            /**
             * 多少时间拆能访问
             */
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

}
