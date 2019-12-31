package com.preapm.sdk.zipkin.util;

/**
 * 
 * <pre>
 * TraceKeys
 * </pre>
 * 
 * @author 
 *
 * @since 2018年1月25日 下午4:18:52
 */
public class TraceKeys {
    public static final String CLIENT_SEND = "cs";
    public static final String CLIENT_RECV = "cr";
    public static final String SERVER_SEND = "ss";
    public static final String SERVER_RECV = "sr";
    public static final String WIRE_SEND = "ws";
    public static final String WIRE_RECV = "wr";

    public static final String CLIENT_ADDR = "ca";
    public static final String SERVER_ADDR = "sa";

    public static final String HTTP_HOST = "http.host";

    public static final String HTTP_METHOD = "http.method";
    
    public static final String HTTP_QUERY_STRING = "http.querystring";
    
    public static final String HTTP_CLIENT_IP = "http.client.ip";

    public static final String HTTP_PATH = "http.path";

    public static final String HTTP_URL = "http.url";

    public static final String HTTP_STATUS_CODE = "http.status_code";

    public static final String HTTP_REQUEST_SIZE = "http.request.size";

    public static final String HTTP_RESPONSE_SIZE = "http.response.size";

    public static final String SQL_QUERY = "sql.query";
    public static final String SQL_TIME = "sql.time";

    public static final String SUFFIX_BEGIN = ".begin";
    public static final String SUFFIX_END = ".end";
    public static final String SUFFIX_BACK = ".back";
    public static final String SUFFIX_ERROR = ".error";
    public static final String RESULT_FAILED = "failed";
    public static final String RESULT_SUCCESS = "success";
    
    public static final String TRACE_ID = "pre_traceId";
    
    public static final String SPAN_ID = "pre_spanId";
    
    public static final String PRE_NAME = "pre_name";

    private TraceKeys() {
    }
}
