package com.preapm.sdk.zipkin.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Enumeration;

/**
 * 
 * <pre>
 * InetAddressUtils
 * </pre>
 * 
 * @author 
 *
 * @since 2018年1月25日 下午4:18:47
 */
public class InetAddressUtils {

    private static InetAddress localHostLANAddress() throws UnknownHostException {
        try {
            InetAddress candidateAddress = null;
            for (Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces(); ifaces
                    .hasMoreElements();) {
                NetworkInterface iface = ifaces.nextElement();
                for (Enumeration<InetAddress> inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements();) {
                    InetAddress inetAddr = inetAddrs.nextElement();
                    if (!inetAddr.isLoopbackAddress()) {
                        if (inetAddr.isSiteLocalAddress()) {
                            return inetAddr;
                        } else if (candidateAddress == null) {
                            candidateAddress = inetAddr;
                        }
                    }
                }
            }
            if (candidateAddress != null) {
                return candidateAddress;
            }

            InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
            if (jdkSuppliedAddress == null) {
                throw new UnknownHostException("The JDK InetAddress.getLocalHost() method unexpectedly returned null.");
            }
            return jdkSuppliedAddress;
        } catch (Exception e) {
            UnknownHostException unknownHostException = new UnknownHostException(
                    "Failed to determine LAN address: " + e);
            unknownHostException.initCause(e);
            throw unknownHostException;
        }
    }

    public static int localIpv4() {
        try {
            return ByteBuffer.wrap(localHostLANAddress().getAddress()).getInt();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return ipToInt("127.0.0.1");
        }
    }

    public static int ipToInt(String ip) {
        try {
            int index = ip.indexOf(":");
            String ips[] = ip.substring(0, index <= 0 ? ip.length() : index).split("\\.");
            int r = 0;
            for (int i = 0, len = ips.length; i < len; i++) {
                r |= (Integer.parseInt(ips[i]) << (24 - i * 8));
            }
            return r;
        } catch (Exception e) {
            e.printStackTrace();
            return 127 << 24 | 1;
        }

    }
}