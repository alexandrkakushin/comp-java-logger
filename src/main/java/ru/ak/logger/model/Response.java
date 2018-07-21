package ru.ak.logger.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class Response {

	private Object object;
	private boolean error;
	private String description;

	public Response() {
	}

	public Response(Object object, boolean error, String description) {
		this.object = object;
		this.error = error;
		this.description = description;
	}

	public Object getResult() {
		return object;
	}

	public void setResult(Object object) {
		this.object = object;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}