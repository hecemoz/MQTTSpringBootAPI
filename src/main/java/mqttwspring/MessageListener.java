package mqttwspring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageListener implements Runnable {

    @Autowired
    MQTTSubscriberBase subscriber;


    @Override
    public void run() {
        subscriber.subscribeMessage("demoTopic");
    }

}
