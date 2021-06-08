package mqttwspring;

public interface MQTTSubscriberBase {
    public void subscribeMessage(String topic);
    public void disconnect();
}
