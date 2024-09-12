package org.example.server.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDate;

public class LocalDateTypeAdapter extends TypeAdapter<LocalDate> {
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
