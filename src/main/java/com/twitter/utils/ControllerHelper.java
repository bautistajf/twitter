package com.twitter.utils;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;

@Log4j2
@SuppressWarnings({"squid:S106","squid:S1444","squid:ClassVariableVisibilityCheck"})
public class ControllerHelper {

    public static String hostname = "unknownHostname";
    public static String serviceName = "unknownService";
    public static String version = "unknownVersion";

    public static final String X_PROCESSTIME = "X-ProcessTime";
    public static final String X_INIT_TIMESTAMP = "X-Timestamp";
    public static final String X_REQUESTHOST = "X-RequestHost";
    public static final String X_SERVICENAME= "X-ServiceName";
    public static final String X_VERSION = "X-Version";

    public static final String X_PROCESSTIME_DESC = "Audit data: process time (in ms)";
    public static final String X_INIT_TIMESTAMP_DESC = "Audit data: Init timestamp";
    public static final String X_REQUESTHOST_DESC = "Audit data: request host";
    public static final String X_SERVICENAME_DESC = "Audit data: internal sericeName";
    public static final String X_VERSION_DESC = "Audit data: internal Version";


    public static final String HEADER_TOTAL_COUNT = "X-Total-Count";

    public static final String PATH = "/1.0/tweets";


    private ControllerHelper() {
        throw new IllegalAccessError("Utility class");
    }

    static {
        try {
            InetAddress addr;
            addr = InetAddress.getLocalHost();
            hostname = addr.getHostName();
        } catch (UnknownHostException ex) {
            System.out.println("Hostname can not be resolved");
        }
    }


    /**
     * @param timestamp
     * @return
     */
    public static HttpHeaders getHeaders(LocalDateTime timestamp) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(X_INIT_TIMESTAMP, timestamp.toString());
        httpHeaders.set(X_REQUESTHOST, hostname);
        httpHeaders.set(X_SERVICENAME, serviceName);
        httpHeaders.set(X_VERSION, version);

        return httpHeaders;
    }

    /**
     * @param timestamp
     * @param httpHeaders
     */
    public static void setProcessTime(LocalDateTime timestamp, HttpHeaders httpHeaders) {
        httpHeaders.set(X_PROCESSTIME, Long.toString(ChronoUnit.MILLIS.between(timestamp, LocalDateTime.now())));
    }



}
