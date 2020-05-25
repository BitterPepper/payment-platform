package com.evolut.payment.integration.util;

import com.evolut.payment.bootstrap.RestAppBootstrap;
import com.evolut.payment.utils.GSONHelper;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.util.StringContentProvider;

import static org.eclipse.jetty.http.HttpStatus.INTERNAL_SERVER_ERROR_500;
import static org.eclipse.jetty.http.HttpStatus.OK_200;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DriverIT {
    public static final String ACC_GROUP_URI = "account";
    public static final String TRN_GROUP_URI = "transaction";

    private RestAppBootstrap restAppBootstrap;

    private HttpClient httpClient;
    private static final String APPLICATION_URL = "http://localhost:4567";

    public void init() throws Exception {
        restAppBootstrap = new RestAppBootstrap();
        restAppBootstrap.init();

        httpClient = new HttpClient();
        httpClient.start();
    }

    public void destroy() throws Exception {
        restAppBootstrap.destroy();
    }

    public <T> T sendCorrectGetReq(String uriSuffix, Class<T> clazz) throws Exception {
        ContentResponse response = httpClient.GET(APPLICATION_URL + "/" + uriSuffix);
        assertEquals(OK_200, response.getStatus());
        return GSONHelper.fromJson(response.getContentAsString(), clazz);
    }

    public void sendIncorrectGetReq(String uriSuffix) throws Exception {
        ContentResponse response = httpClient.GET(APPLICATION_URL + "/" + uriSuffix);
        assertEquals(INTERNAL_SERVER_ERROR_500, response.getStatus());
    }

    public <T> T sendCorrectPostReq(String uriSuffix, String requestBody, Class<T> clazz) throws Exception {
        ContentResponse response = sendPostReq(uriSuffix, requestBody);
        assertEquals(OK_200, response.getStatus());
        return GSONHelper.fromJson(response.getContentAsString(), clazz);
    }

    public void sendIncorrectPostReq(String uriSuffix, String requestBody) throws Exception {
        ContentResponse response = sendPostReq(uriSuffix, requestBody);
        assertEquals(INTERNAL_SERVER_ERROR_500, response.getStatus());
    }

    public ContentResponse sendPostReq(String uriSuffix, String requestBody) throws Exception {
        return httpClient.POST(APPLICATION_URL + "/" + uriSuffix)
                .content(new StringContentProvider(requestBody))
                .send();
    }

    public String buildAccCreateReq(String serial, String owner, String balance) {
        return String.format("{\"serial\": \"%s\", \"owner\": \"%s\", \"balance\": \"%s\"}",
                serial, owner, balance);
    }

    public String buildTrnCreateReq(String accFromId, String accToId, String amount) {
        return String.format("{\"accFromId\": \"%s\", \"accToId\": \"%s\", \"amount\": \"%s\"}",
                accFromId, accToId, amount);
    }

}
