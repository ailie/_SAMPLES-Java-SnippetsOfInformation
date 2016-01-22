package com.example.ipc.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * ENSURING RELEASE OF LOW LEVEL RESOURCES:
 * https://hc.apache.org/httpcomponents-client-ga/tutorial/html/fundamentals.html
 * <p>
 * Please note that if response content is not fully-consumed (ie closed) the underlying
 * connection cannot be safely re-used and will be shut down and discarded by the
 * connection manager.
 * <p>
 * In order to ensure proper release of system resources one must close either (1) the
 * content stream associated with the entity or (2) the response itself; the difference
 * is that closing the former will attempt to keep the underlying connection alive by
 * consuming the entity content while the latter immediately shuts down and discards the
 * connection.
 * <p>
 * When working with streaming entities, one can use the EntityUtils#consume(HttpEntity)
 * method to ensure that the entity content has been fully consumed and the underlying
 * stream has been closed.
 * <p>
 * There can be situations, however, when only a small portion of the entire response
 * content needs to be retrieved and the performance penalty for consuming the remaining
 * content and making the connection reusable is too high, in which case one can
 * terminate the content stream by closing the response. The connection will not be
 * reused, but all level resources held by it will be correctly deallocated.
 */
public class ApacheHttpClient {

    public static void main(String[] args) throws IOException {
        String result;

        result = get("https://httpbin.org/get");
        result = post("https://httpbin.org/post");

        System.out.println(result);
    }

    private static String get(String link) throws MalformedURLException, IOException {
        return HttpClients.createDefault().execute(httpGET(link), responseHandler);
    }

    private static String post(String link)
            throws IOException, ClientProtocolException, UnsupportedEncodingException {
        CloseableHttpResponse response = HttpClients.createDefault()
                                                    .execute(httpPOST(link));
        return responseHandler.handleResponse(response);
    }

    private static HttpGet httpGET(String uri) {
        HttpGet req = new HttpGet(uri);
        return req;
    }

    private static HttpPost httpPOST(String uri) throws UnsupportedEncodingException {
        HttpPost req = new HttpPost(uri);

        List<NameValuePair> pairs = new ArrayList<NameValuePair>(2);

        pairs.add(new BasicNameValuePair("username", "VIP"));
        pairs.add(new BasicNameValuePair("password", "SECRET"));

        // this kind of Entity encodes parameters like this: param1=value1&param2=value2
        req.setEntity(new UrlEncodedFormEntity(pairs));

        return req;
    }

    /**
     * Using a Response Handler is the recommended way of executing HTTP requests and
     * processing HTTP responses. This approach enables the caller to concentrate on the
     * process of digesting HTTP responses and to delegate the task of system resource
     * deallocation to HttpClient. The use of an HTTP response handler guarantees that
     * the underlying HTTP connection will be released back to the connection manager
     * automatically in all cases.
     */
    private static ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

        @Override
        public String handleResponse(final HttpResponse response)
                throws ClientProtocolException, IOException {

            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                return entity != null ? EntityUtils.toString(entity) : null;
            } else
                throw new ClientProtocolException("Unexpected response status: " + status);
        }

    };
}
