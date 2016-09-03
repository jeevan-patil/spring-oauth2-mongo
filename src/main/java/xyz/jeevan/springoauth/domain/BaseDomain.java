package xyz.jeevan.springoauth.domain;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 
 * @author jeevan
 * @date 03-Sep-2016 9:31:52 pm
 * @purpose Base domain object which all the other objects extend.
 *
 */
@Document
public class BaseDomain {
	@Id
	private String guid;

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
		if (null == this.guid || this.guid.isEmpty()) {
			this.guid = UUID.randomUUID().toString();
		}
	}
}
