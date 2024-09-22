package org.example.server.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalTime;

public class LocalTimeTypeAdapter extends TypeAdapter<LocalTime> {
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
