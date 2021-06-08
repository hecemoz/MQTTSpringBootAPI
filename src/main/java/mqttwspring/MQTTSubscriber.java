package mqttwspring;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Component
public class MQTTSubscriber extends MQTTConfig implements MqttCallback, MQTTSubscriberBase {
    private String brokerUrl = null;
    final private String colon = ":";
    final private String clientId = "demoClient2";

    private MqttClient mqttClient = null;
    private MqttConnectOptions connectionOptions = null;
    private MemoryPersistence persistence = null;


    public MQTTSubscriber() {
        this.config();
    }


    @Override
    protected void config() {
        this.brokerUrl = this.TCP + this.broker + colon + this.port;
        this.persistence = new MemoryPersistence();
        this.connectionOptions = new MqttConnectOptions();
        try {
            this.mqttClient = new MqttClient(brokerUrl, clientId, persistence);
            this.connectionOptions.setCleanSession(true);
            this.connectionOptions.setConnectionTimeout(2000);
            this.mqttClient.connect(this.connectionOptions);
            this.mqttClient.setCallback(this);

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
    

    @Override
    public void subscribeMessage(String topic2) {
        try {
            CountDownLatch receivedSignal = new CountDownLatch(10);
            mqttClient.subscribe("demoTopic", (topic, msg) -> {
                byte[] payload = msg.getPayload();
                String m = new String(payload);
                receivedSignal.countDown();

                this.mqttClient.subscribe(topic2, this.qos);


            });
            receivedSignal.await(1, TimeUnit.MINUTES);
        } catch (MqttException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void disconnect() {
        try {
            this.mqttClient.disconnect();
        } catch (MqttException me) {
            me.printStackTrace();
        }
    }

    @Override
    public void connectionLost(Throwable throwable) {
        System.out.println("Connection Lost");
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        String time = new Timestamp(System.currentTimeMillis()).toString();
        System.out.println();
        System.out.println("*************************************************************************************************");
        System.out.println("Message arrived at time: " + time + "  Message: " + new String(message.getPayload()) + " from " + userName);
        userName = "testUser";
        System.out.println("*************************************************************************************************");
        System.out.println();
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}
