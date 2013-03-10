package com.google.gson;

import java.io.IOException;

// FIXME Gson needs to be fixed!
public class JsonPrinter implements JsonFormatter {
	
	private static class FormattingVisitor implements JsonElementVisitor {
		private final Appendable writer;
		private final Escaper escaper;
		private final boolean serializeNulls;

		FormattingVisitor(Appendable writer, Escaper escaper,
				boolean serializeNulls) {
			this.writer = writer;
			this.escaper = escaper;
			this.serializeNulls = serializeNulls;
		}

		public void visitPrimitive(JsonPrimitive primitive) throws IOException {
			primitive.toString(writer, escaper);
		}

		public void visitNull() throws IOException {
			writer.append("null");
		}

		public void startArray(JsonArray array) throws IOException {
			writer.append('[');
		}

		public void visitArrayMember(JsonArray parent, JsonPrimitive member,
				boolean isFirst) throws IOException {
			if (!isFirst) {
				writer.append(',');
			}
			member.toString(writer, escaper);
		}

		public void visitArrayMember(JsonArray parent, JsonArray member,
				boolean isFirst) throws IOException {
			if (!isFirst) {
				writer.append(',');
			}
		}

		public void visitArrayMember(JsonArray parent, JsonObject member,
				boolean isFirst) throws IOException {
			if (!isFirst) {
				writer.append(',');
			}
		}

		public void visitNullArrayMember(JsonArray parent, boolean isFirst)
				throws IOException {
			if (!isFirst) {
				writer.append(',');
			}
		}

		public void endArray(JsonArray array) throws IOException {
			writer.append(']');
		}

		public void startObject(JsonObject object) throws IOException {
			writer.append('{');
		}

		public void visitObjectMember(JsonObject parent, String memberName,
				JsonPrimitive member, boolean isFirst) throws IOException {
			if (!isFirst) {
				writer.append(',');
			}
			writer.append('"');
			writer.append(memberName);
			writer.append("\":");
			member.toString(writer, escaper);
		}

		public void visitObjectMember(JsonObject parent, String memberName,
				JsonArray member, boolean isFirst) throws IOException {
			if (!isFirst) {
				writer.append(',');
			}
			writer.append('"');
			writer.append(memberName);
			writer.append("\":");
		}

		public void visitObjectMember(JsonObject parent, String memberName,
				JsonObject member, boolean isFirst) throws IOException {
			if (!isFirst) {
				writer.append(',');
			}
			writer.append('"');
			writer.append(memberName);
			writer.append("\":");
		}

		public void visitNullObjectMember(JsonObject parent, String memberName,
				boolean isFirst) throws IOException {
			if (serializeNulls) {
				visitObjectMember(parent, memberName, (JsonObject) null,
						isFirst);
			}
		}

		public void endObject(JsonObject object) throws IOException {
			writer.append('}');
		}
	}

	private final boolean escapeHtmlChars;

	public JsonPrinter() {
		this(true);
	}

	public JsonPrinter(boolean escapeHtmlChars) {
		this.escapeHtmlChars = escapeHtmlChars;
	}

	public void format(JsonElement root, Appendable writer,
			boolean serializeNulls) throws IOException {
		if (root == null) {
			return;
		}
		JsonElementVisitor visitor = new FormattingVisitor(writer, new Escaper(
				escapeHtmlChars), serializeNulls);
		JsonTreeNavigator navigator = new JsonTreeNavigator(visitor,
				serializeNulls);
		navigator.navigate(root);
	}
}
