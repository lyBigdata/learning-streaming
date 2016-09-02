package sparkSql;

import java.io.Serializable;

/**
 * <pre>
 * User: liuyu
 * Date: 2016/9/2
 * Time: 11:16
 * </pre>
 *
 * @author liuyu
 */
public class MyBean implements Serializable {

    private static final long serialVersionUID = 42L;


    private String wifiMac;

    private String clientMac;


    private long firstAt;

    private long updateAt;


    public String getWifiMac() {
        return wifiMac;
    }

    public void setWifiMac(String wifiMac) {
        this.wifiMac = wifiMac;
    }

    public String getClientMac() {
        return clientMac;
    }

    public void setClientMac(String clientMac) {
        this.clientMac = clientMac;
    }

    public long getFirstAt() {
        return firstAt;
    }

    public void setFirstAt(long firstAt) {
        this.firstAt = firstAt;
    }

    public long getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(long updateAt) {
        this.updateAt = updateAt;
    }
}
