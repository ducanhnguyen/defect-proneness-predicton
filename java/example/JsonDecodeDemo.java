package example;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

class JsonDecodeDemo {

	public static void main(String[] args) {

		JSONParser parser = new JSONParser();
		String s = "{\"product\":{\"4228\":[{\"when\":1002748091,\"what\":\"JDT\",\"who\":29},{\"when\":1002781200,\"what\":\"Platform\",\"who\":20}]}}";

		try {
			JSONObject obj = (JSONObject) parser.parse(s);
			System.out.println(obj.size());
			System.out.println(obj.get("product"));
		} catch (ParseException pe) {

			System.out.println("position: " + pe.getPosition());
			System.out.println(pe);
		}
	}
}
