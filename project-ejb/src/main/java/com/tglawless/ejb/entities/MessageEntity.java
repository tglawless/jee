package com.tglawless.ejb.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

@Entity
@Table(schema="PROJECT", name="MESSAGES")
@NamedQueries({
	@NamedQuery(name=MessageEntity.FIND_ALL_QUERY, query="select m from MessageEntity m order by m.modifiedOn desc")
})
@SuppressWarnings("serial")
public class MessageEntity implements Serializable {
	
	public static final String FIND_ALL_QUERY = "findAllMessagesSortedByModifiedOn";
	
	@Id	
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="MESSAGE_ID")
	private long id;
	
	@Column(name="MESSAGE_TEXT")
	private String text;
	
	@Version
	@Column(name="MESSAGE_MODIFIED", insertable=false, updatable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedOn;
	
	public MessageEntity() { }
	
	public MessageEntity(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public long getId() {
		return id;
	}

	public Date getModifiedOn() {
		return modifiedOn;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MessageEntity other = (MessageEntity) obj;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MessageEntity [id=" + id + ", text=" + text + ", modifiedOn="
				+ modifiedOn + "]";
	}

}
