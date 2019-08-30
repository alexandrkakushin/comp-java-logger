package ru.ak.model;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

public class Response implements Serializable {

	private static final long serialVersionUID = -7004999904619722613L;
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

	@XmlElement
	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	@XmlElement
	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	@XmlElement
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}