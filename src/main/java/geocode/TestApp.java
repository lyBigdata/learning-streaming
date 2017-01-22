package geocode;

import java.io.FileInputStream;

/**
 * <pre>
 * User: liuyu
 * Date: 2017/1/14
 * Time: 20:23
 * </pre>
 *
 * @author liuyu
 */
public class TestApp {
    public static void main(String[] args) throws Exception{
        ReverseGeoCode reverseGeoCode = new ReverseGeoCode(new FileInputStream("C:/Users/ly/Desktop/latlp.info.2017_01_22_10_23_13/latlp.info.2017_01_22_10_23_13.txt"));
        long before = System.currentTimeMillis();
        System.out.println(reverseGeoCode.nearestPlace(22.872495714500083,108.37897636591241));
        System.out.println(System.currentTimeMillis() - before);
    }
}
