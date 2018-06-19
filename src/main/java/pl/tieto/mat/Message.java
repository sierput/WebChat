package pl.tieto.mat;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

public class Message {
	private String content;
	private String sender;
	private String date;

	public Message(String content, String sender) {
		super();
		this.content = content;
		this.sender = sender;
		this.date = new Date().toString();
	}

	public Message() {
	};

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}
