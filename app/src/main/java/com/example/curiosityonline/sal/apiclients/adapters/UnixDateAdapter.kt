package com.example.curiosityonline.sal.apiclients.adapters

import java.util.*
import com.google.gson.*
import com.google.gson.stream.*

internal class UnixDateAdapter : TypeAdapter<Date?>() {
	override fun write(writer: JsonWriter?, value: Date?) {
		if (value == null) {
			writer?.nullValue()
		} else {
			writer?.value(value.time)
		}
	}

	override fun read(reader: JsonReader?): Date? {
		if (reader == null) {
			return null;
		}
		if (reader.peek() == JsonToken.NULL) {
			reader.nextNull()
			return null
		}
		return Date(reader.nextLong())
	}
}
