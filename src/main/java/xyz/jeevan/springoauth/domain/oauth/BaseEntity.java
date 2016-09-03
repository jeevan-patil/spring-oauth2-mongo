package xyz.jeevan.springoauth.domain.oauth;

import java.util.Date;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.util.Assert;

public class BaseEntity {
	private int version;

	@Id
	private String id;

	private Date timeCreated;

	public BaseEntity() {
		this(UUID.randomUUID());
	}

	public BaseEntity(UUID guid) {
		Assert.notNull(guid, "UUID is required");
		id = guid.toString();
		this.timeCreated = new Date();
	}

	public BaseEntity(String guid) {
		Assert.notNull(guid, "UUID is required");
		id = guid;
		this.timeCreated = new Date();
	}

	public String getId() {
		return id;
	}

	public int hashCode() {
		return getId().hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		BaseEntity that = (BaseEntity) o;

		if (!id.equals(that.id))
			return false;

		return true;
	}

	public int getVersion() {
		return version;
	}

	public Date getTimeCreated() {
		return timeCreated;
	}
}
