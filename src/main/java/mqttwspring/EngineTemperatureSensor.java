package mqttwspring;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Random;
import java.util.concurrent.Callable;

public class EngineTemperatureSensor implements Callable<Void> {
    private MqttClient client;
    public String TOPIC;
    private Random rnd = new Random();

    public EngineTemperatureSensor(MqttClient client) {
        this.client = client;
    }

    @Override
    public Void call() throws Exception {
        if (!client.isConnected())
            return null;
        MqttMessage msg = readEngineTemp();
        msg.setQos(0);
        msg.setRetained(true);
        client.publish(TOPIC, msg);
        return null;
    }

    public MqttMessage readEngineTemp() {
        double temp = 80 + rnd.nextDouble() * 20.0;
        byte[] payload = String.format("T:%04.2f", temp).getBytes();
        return new MqttMessage(payload);
    }
}
