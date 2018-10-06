package com.hixel.hixel.data.api;

import android.support.annotation.Nullable;
import com.google.gson.reflect.TypeToken;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class EnvelopeConverterFactory extends Converter.Factory {
    @Nullable
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {

        Type envelopedType = TypeToken.getParameterized(Envelope.class, type).getType();

        Converter<ResponseBody, Envelope<?>> delegate = retrofit.nextResponseBodyConverter(this, envelopedType, annotations);

        return body -> {
            Envelope<?> envelope = delegate.convert(body);

            return envelope;
        };
    }
}