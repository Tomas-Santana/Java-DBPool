package PropertyHandler;

import java.io.FileInputStream;
import java.util.Properties;
import java.text.MessageFormat;

public class PropertyHandler {
    private String dir;
    public PropertyHandler(String filename) {
        dir = filename;
    }
    public String getProp(String key, String defaultValue) {
        try {
            Properties prop = new Properties();
            FileInputStream ip = new FileInputStream(dir);
            prop.load(ip);

            String value = prop.getProperty(key);
            if (value == null) {
                return defaultValue;
            }
            return value;

        } catch (Exception e) {
            System.out.println(e);
            return "";
        }
    }
    public String getProp(String key, Object[] args, String defaultValue) {
        String value = this.getProp(key, defaultValue);
        value = MessageFormat.format(value, args);
        return value;
    }


}