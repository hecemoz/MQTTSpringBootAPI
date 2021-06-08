package mqttwspring;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.stereotype.Component;

@Component
public class MQTTPublisher extends MQTTConfig implements MqttCallback, MQTTPublisherBase {
    private String brokerUrl = null;
    final private String colon = ":";
    final private String clientId = "demoClient1";

    private MqttClient mqttClient = null;
    private MqttConnectOptions options = null;
    private MemoryPersistence persistence = null;
    private EngineTemperatureSensor sensor = null;

    private MQTTPublisher(){
        this.config();
    }


    public static MQTTPublisher getInstance(){
        return new MQTTPublisher();
    }


    @Override
    protected void config() {
        this.brokerUrl = this.TCP + this.broker + colon + this.port;
        this.persistence = new MemoryPersistence();
        this.options = new MqttConnectOptions();
        try{
            this.mqttClient = new MqttClient(brokerUrl,clientId,persistence);
            this.options.setCleanSession(true);
            this.options.setConnectionTimeout(2000);
            this.mqttClient.connect(this.options);
            this.mqttClient.setCallback(this);
            this.sensor = new EngineTemperatureSensor(mqttClient);
            MqttMessage mqttMessage = sensor.readEngineTemp();
            //this.mqttClient.publish("demoTopic", mqttMessage);
            mqttMessage.setQos(this.qos);
            mqttMessage.setRetained(true);
        } catch (MqttException me) {
            me.printStackTrace();
        }

    }

    @Override
    public void publishMessage(String topic, String message) {
        try{
            MqttMessage msg = new MqttMessage(message.getBytes());
            msg.setQos(this.qos);
            this.mqttClient.publish(topic, msg);

        } catch (MqttPersistenceException e) {
            e.printStackTrace();
        } catch (MqttException me) {
            me.printStackTrace();
        }
    }


    @Override
    public void connectionLost(Throwable throwable) {
        System.out.println("Connection Lost");
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        System.out.println("Delivery completed!");
    }

    @Override
    public void disconnect() {
        try{
            this.mqttClient.disconnect();
        } catch (MqttException me) {
            me.printStackTrace();
        }
    }
}
