package com.hixel.hixel.data.api;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.ArrayMap;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import retrofit2.Response;

/**
 * Fields network responses.
 *
 * @param <T> Any type.
 */
public class ApiResponse<T> {
    private static final Pattern LINK_PATTERN = Pattern
            .compile("<([^>]*)>[\\s]*;[\\s]*rel=\"([a-zA-Z0-9]+)\"");
    public final int code;
    @Nullable
    public final T body;
    @Nullable
    public final String errorMessage;
    @SuppressWarnings("WeakerAccess")
    @NonNull
    public final Map<String, String> links;

    public ApiResponse(Throwable error) {
        code = 500;
        body = null;
        errorMessage = error.getMessage();
        links = Collections.emptyMap();
    }

    public ApiResponse(Response<T> response) {
        code = response.code();

        if (response.isSuccessful()) {
            body = response.body();
            errorMessage = null;
        } else {
            String message = null;

            if (response.errorBody() != null) {
                try {
                    message = response.errorBody().string();
                } catch (IOException e) {
                    // TODO: Timber
                }
            }

            if (message == null || message.trim().length() == 0) {
                message = response.message();
            }
            errorMessage = message;
            body = null;
        }

        String linkHeader = response.headers().get("link");

        if (linkHeader == null) {
            links = Collections.emptyMap();
        } else {
            links = new ArrayMap<>();
            Matcher matcher = LINK_PATTERN.matcher(linkHeader);

            while (matcher.find()) {
                int count = matcher.groupCount();

                if (count == 2) {
                    links.put(matcher.group(2), matcher.group(1));
                }
            }
        }
    }

    public boolean isSuccessful() {
        return code >= 200 && code < 300;
    }
}
