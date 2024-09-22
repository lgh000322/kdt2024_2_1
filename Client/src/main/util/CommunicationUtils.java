package main.util;

import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import main.dto.RequestData;
import main.dto.ResponseData;

public class CommunicationUtils {
	
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_TIME;


    // 서버랑 연결
    public ServerConnectUtils getConnection() {
        ServerConnectUtils serverConnectUtils = new ServerConnectUtils();
        serverConnectUtils.connect();
        return serverConnectUtils;
    }
    
    public void sendServer(String jsonSendStr, DataOutputStream dos) throws IOException {
        dos.writeUTF(jsonSendStr);
        dos.flush();
    }

 // 객체를 JSON으로 변환하는 메소드
    public String objectToJson(String messageType, Object object) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                .registerTypeAdapter(LocalTime.class, new LocalTimeTypeAdapter())
                .create();
        
        RequestData requestData = new RequestData();
        requestData.setMessageType(messageType);
        requestData.setData(object);
        
        return gson.toJson(requestData);
    }

    public <T> ResponseData<T> jsonToResponseData(String jsonReceivedStr, Type typeOfT) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                .registerTypeAdapter(LocalTime.class, new LocalTimeTypeAdapter())
                .create();
        
        Type responseType = TypeToken.getParameterized(ResponseData.class, typeOfT).getType();
        return gson.fromJson(jsonReceivedStr, responseType);
    }

 // LocalDate를 처리하기 위한 TypeAdapter
    private static class LocalDateTypeAdapter extends TypeAdapter<LocalDate> {
        @Override
        public void write(JsonWriter out, LocalDate value) throws IOException {
            if (value == null) {
                out.nullValue();
            } else {
                out.beginObject();
                out.name("year").value(value.getYear());
                out.name("month").value(value.getMonthValue());
                out.name("day").value(value.getDayOfMonth());
                out.endObject();
            }
        }

        @Override
        public LocalDate read(JsonReader in) throws IOException {
            if (in.peek() == com.google.gson.stream.JsonToken.NULL) {
                in.nextNull();
                return null;
            }

            in.beginObject();
            int year = 0, month = 0, day = 0;
            while (in.hasNext()) {
                switch (in.nextName()) {
                    case "year":
                        year = in.nextInt();
                        break;
                    case "month":
                        month = in.nextInt();
                        break;
                    case "day":
                        day = in.nextInt();
                        break;
                }
            }
            in.endObject();
            return LocalDate.of(year, month, day);
        }
    }

 // LocalTime을 처리하기 위한 TypeAdapter
    private static class LocalTimeTypeAdapter extends TypeAdapter<LocalTime> {
        @Override
        public void write(JsonWriter out, LocalTime value) throws IOException {
            if (value == null) {
                out.nullValue();
            } else {
                out.beginObject();
                out.name("hour").value(value.getHour());
                out.name("minute").value(value.getMinute());
                out.name("second").value(value.getSecond());
                out.name("nano").value(value.getNano());
                out.endObject();
            }
        }

        @Override
        public LocalTime read(JsonReader in) throws IOException {
            if (in.peek() == com.google.gson.stream.JsonToken.NULL) {
                in.nextNull();
                return null;
            }

            in.beginObject();
            int hour = 0, minute = 0, second = 0, nano = 0;
            while (in.hasNext()) {
                switch (in.nextName()) {
                    case "hour":
                        hour = in.nextInt();
                        break;
                    case "minute":
                        minute = in.nextInt();
                        break;
                    case "second":
                        second = in.nextInt();
                        break;
                    case "nano":
                        nano = in.nextInt();
                        break;
                }
            }
            in.endObject();
            return LocalTime.of(hour, minute, second, nano);
        }
    }
}
