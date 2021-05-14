package com.example.curiosityonline.sal.apiclients.adapters

import android.util.Base64
import com.google.gson.*
import com.google.gson.stream.*

internal class ByteArrayAdapter : TypeAdapter<ByteArray?>() {
    override fun write(writer: JsonWriter?, value: ByteArray?) {
        if (value == null) {
            writer?.nullValue()
        } else {
            writer?.value(Base64.encodeToString(value, Base64.DEFAULT))
        }
    }

    override fun read(reader: JsonReader?): ByteArray? {
        if (reader == null) {
            return null;
        }
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull()
            return null
        }
        return Base64.decode(reader.nextString(), Base64.DEFAULT)
    }
}