package mqttwspring;

public abstract class MQTTConfig {
    protected final String broker = "localhost";
    protected final int qos = 2;
    protected Integer port = 1883;
    protected final String TCP = "tcp://";
    protected String userName = "TemperatureSensor";

    protected abstract void config();

}
