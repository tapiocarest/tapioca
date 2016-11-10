package rest.tapioca.mainframe.basic_connector;

import com.l7tech.policy.assertion.ext.message.CustomJsonData;
import com.l7tech.policy.assertion.ext.message.InvalidDataException;

public class TapiocaJsonData implements CustomJsonData {
	private final String data;

	public TapiocaJsonData(String data) {
		this.data = data;
	}

	@Override
	public String getJsonData() {
		return data;
	}

	@Override
	public Object getJsonObject() throws InvalidDataException {
		return null;
	}
}