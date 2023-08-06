package com.pyonsnalcolor.batch.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pyonsnalcolor.exception.PyonsnalcolorBatchException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Map;
import java.util.function.Supplier;

import static com.pyonsnalcolor.exception.model.BatchErrorCode.*;

public class BatchExceptionUtil {

    public static <T> T handleException(Supplier<T> supplier) {
        try {
            return supplier.get();
        } catch (IllegalArgumentException e) {
            throw new PyonsnalcolorBatchException(INVALID_ACCESS, e);
        } catch (Exception e) {
            throw new PyonsnalcolorBatchException(BATCH_UNAVAILABLE, e);
        }
    }

    public static Document getDocumentByUrlWithTimeout(String url, int timeOut) {
        try {
            return Jsoup.connect(url).timeout(timeOut).get();
        } catch (IOException e) {
            throw new PyonsnalcolorBatchException(IO_EXCEPTION, e);
        }
    }

    public static Document getDocumentByConnection(Connection connection, int timeOut) {
        try {
            return connection.timeout(timeOut).get();
        } catch (IOException e) {
            throw new PyonsnalcolorBatchException(IO_EXCEPTION, e);
        }
    }

    public static Map<String, Object> getPageDataMap(ObjectMapper objectMapper, Object data) {
        try {
            return objectMapper.readValue((String) data, Map.class);
        } catch (JsonProcessingException e) {
            throw new PyonsnalcolorBatchException(BATCH_UNAVAILABLE, e);
        }
    }
}