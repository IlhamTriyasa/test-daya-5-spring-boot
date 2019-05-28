package com.test.dayalima.test.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class LocalIP {
    public static final String LocalComputerName(){
        InetAddress ip = null;
        try {
            ip = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            return "";
        }
        return ip.getCanonicalHostName();
    }

    public static final String LocalIPAddress(){
        InetAddress ip = null;
        try {
            ip = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            return "";
        }
        return ip.getHostAddress();
    }
}
