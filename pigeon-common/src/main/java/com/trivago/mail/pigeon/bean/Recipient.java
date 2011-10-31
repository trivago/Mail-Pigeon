package com.trivago.mail.pigeon.bean;

import com.trivago.mail.pigeon.storage.ConnectionFactory;
import com.trivago.mail.pigeon.storage.Gender;
import com.trivago.mail.pigeon.storage.IndexTypes;
import com.trivago.mail.pigeon.storage.RelationTypes;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.IndexHits;
import org.svenson.JSONProperty;

import java.util.Date;


public class Recipient extends AbstractBean
{
	public static final String ID = "user_id";
	public static final String FIRSTNAME = "firstname";
	public static final String LASTNAME = "lastname";
	public static final String GENDER = "gender";
	public static final String TITLE = "title";
	public static final String BIRTHDAY = "birthday";
	public static final String COUNTRY = "country";
	public static final String LANGUAGE = "language";
	public static final String CITY = "city";
	public static final String EXTERNAL_ID = "external_id";
	public static final String EMAIL = "email";
	public static final String DATE = "date";

	public Recipient(final Node underlayingNode)
	{
		this.dataNode = underlayingNode;
	}

	public Recipient(final long userId)
	{
		dataNode = ConnectionFactory.getUserIndex().get(IndexTypes.USER_ID, userId).getSingle();
	}

	public Recipient(final long userId,
					 final String firstname,
					 final String lastname,
					 final String email)
	{
		Transaction tx = ConnectionFactory.getDatabase().beginTx();
		try
		{
			dataNode = ConnectionFactory.getDatabase().createNode();
			dataNode.setProperty(ID, userId);
			dataNode.setProperty("type", getClass().getName());
			dataNode.setProperty(FIRSTNAME, firstname);
			dataNode.setProperty(LASTNAME, lastname);
			dataNode.setProperty(EMAIL, email);
			ConnectionFactory.getUserIndex().add(this.dataNode, IndexTypes.USER_ID, userId);
			ConnectionFactory.getUserIndex().add(this.dataNode, IndexTypes.TYPE, getClass().getName());
			ConnectionFactory.getDatabase().getReferenceNode().createRelationshipTo(dataNode, RelationTypes.USER_REFERENCE);
			tx.success();
		}
		catch (Exception e)
		{
			tx.failure();
		}
		finally
		{
			tx.finish();
		}
	}

	public long getId()
	{
		return getProperty(Long.class, ID, false);
	}

	public String getFirstname()
	{
		return getProperty(String.class, FIRSTNAME);
	}

	public String getLastname()
	{
		return getProperty(String.class, LASTNAME);
	}

	public String getEmail()
	{
		return getProperty(String.class, EMAIL);
	}

	public void setFirstname(String name)
	{
		writeProperty(FIRSTNAME, name);
	}

	public void setEmail(String email)
	{
		writeProperty(EMAIL, email);
	}

	public Gender getGender()
	{
		return getProperty(Gender.class, GENDER);
	}

	public void setGender(final Gender gender)
	{
		writeProperty(GENDER, gender);
	}

	public String getTitle()
	{
		return getProperty(String.class, TITLE);
	}

	public void setTitle(final String title)
	{
		writeProperty(TITLE, title);
	}

	public String getCity()
	{
		return getProperty(String.class, CITY);
	}

	public void setCity(final String city)
	{
		writeProperty(CITY, city);
	}

	public String getCountry()
	{
		return getProperty(String.class, COUNTRY);
	}

	public void setCountry(final String country)
	{
		writeProperty(COUNTRY, country);
	}

	public Date getBrithday()
	{
		return getWrappedProperty(Date.class, Long.class, BIRTHDAY);
	}

	public void setBirthday(final Date birthday)
	{
		writeProperty(BIRTHDAY, birthday.getTime());
	}

	public String getLanguage()
	{
		return getProperty(String.class, LANGUAGE);
	}

	public void setLanguage(final String language)
	{
		writeProperty(LANGUAGE, language);
	}

	public String getExternalId()
	{
		return getProperty(String.class, EXTERNAL_ID);
	}

	public void setExternalId(final String externalId)
	{
		writeProperty(EXTERNAL_ID, externalId);
	}

	public Relationship addRecievedNewsletter(Mail mail)
	{
		Transaction tx = ConnectionFactory.getDatabase().beginTx();
		Relationship relation = null;
		try
		{
			Node newsletterNode = mail.getDataNode();
			relation = dataNode.createRelationshipTo(newsletterNode, RelationTypes.RECIEVED);
			relation.setProperty(DATE, new Date().getTime());
			tx.success();
		}
		catch (Exception e)
		{
			tx.failure();
		}
		finally
		{
			tx.finish();
		}
		return relation;
	}

	@JSONProperty(ignore = true)
	public Iterable<Relationship> getReceivedNewsletters()
	{
		return dataNode.getRelationships(RelationTypes.RECIEVED);
	}

	@JSONProperty(ignore = true)
	public static IndexHits<Node> getAll()
	{
		return ConnectionFactory.getUserIndex().get("type", Recipient.class.getName());
	}
}
