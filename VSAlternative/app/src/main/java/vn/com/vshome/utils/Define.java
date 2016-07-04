package vn.com.vshome.utils;

/**
 * Created by anlab on 7/4/16.
 */
public class Define {

    public enum NetworkType {
        NotDetermine,
        LocalNetwork,
        DnsNetwork
    }

    public static NetworkType NETWORK_TYPE = NetworkType.NotDetermine;
    public static String IP_ADDRESS = "";
    public static String DNS_ADDRESS = "";
    public static int IP_PORT = 0;
    public static int DNS_PORT = 0;
}
