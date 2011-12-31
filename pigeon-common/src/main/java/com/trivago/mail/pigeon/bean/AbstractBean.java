package com.trivago.mail.pigeon.bean;

import com.trivago.mail.pigeon.storage.ConnectionFactory;
import org.apache.log4j.Logger;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.NotFoundException;
import org.neo4j.graphdb.Transaction;
import org.svenson.JSONProperty;

/**
 * Abstract Bean as parent for the other entity beans. Your new beans should
 * inherit from this one.
 *
 * @author Mario Mueller mario.mueller@trivago.com
 */
public abstract class AbstractBean
{

	/**
	 * The data node from neo4j
	 */
	protected Node dataNode;

	protected static final Logger log = Logger.getLogger("com.trivago.mail.pigeon.bean");

	/**
	 * The Node class is a wrapper from the neo4j libraries.
	 *
	 * @return returns the data node from neo4j
	 */
	@JSONProperty(ignore = true)
	public Node getDataNode()
	{
		return dataNode;
	}

	/**
	 * Wrapper for writing properties directly to the node. Do not use this within another transaction as it will fail.
	 *
	 * @param key name of the property
	 * @param value value of the property. Should be of type String, a boxed scalar or at least serializable.
	 */
	protected void writeProperty(final String key, final Object value)
	{
		Transaction tx = ConnectionFactory.getDatabase().beginTx();
		try
		{
			dataNode.setProperty(key, value);
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

	/**
	 * Gets a property from the Node. You must give a type that the value should be cast to. You can use ignoreMissing
	 * if you expect the property to be missing (e.g. after you migrated the data model and added or removed a property
	 * from the beans).
	 *
	 * @param type type of value the property value should be cast to.
	 * @param key name of the property
	 * @param ignoreMissing in a normal case, neo4j throws an exception for every missing property. set this true to ignore it
	 * @param <T> The class the property value should be cast to.
	 * @return an object of class T or null
	 * @throws NotFoundException
	 */
	protected <T> T getProperty(Class<T> type, String key, boolean ignoreMissing)
	{
		try
		{
			return type.cast(getDataNode().getProperty(key));
		}
		catch (NotFoundException e)
		{
			if (ignoreMissing)
			{
				try
				{
					return type.getConstructor(String.class).newInstance("");
				}
				catch (Exception e1)
				{
					return null;
				}
			}
			else
			{
				throw e;
			}
		}
	}

	/**
	 * Gets a property from the Node. You must give a type that the value should be cast to.
	 *
	 * This is a shortcut for getProperty(type, key, true).
	 *
	 * @see AbstractBean#getProperty(Class, String, boolean) 
	 * @param type type of value the property value should be cast to.
	 * @param key name of the property
	 * @param <T> The class the property value should be cast to.
	 * @return an object of class T or null
	 * @throws NotFoundException
	 */
	protected <T> T getProperty(Class<T> type, String key)
	{
		return getProperty(type, key, true);
	}

	/**
	 * This is a helper method for wrapped properties. The intended use case was a date, which was saved as a Long in the
	 * property and should be returned as Date. The property value must match a constructor of the wrapper class.
	 *
	 * <code>
	 *     Date myDate = bean.getWrappedProperty(Date.class, Long.class, "date");
	 * </code>
	 *
	 * @param returnType The class that wraps the node's property value
	 * @param argumentType The class that the property is cast to and that must match to constructor of the returnType
	 * @param key The name of the property
	 * @param <T> The return type (e.g. Date)
	 * @param <W> The constructor type (e.g. Long)
	 * @return A constructed wrapper instance of T with the property value cast to W as constructor paramter of T
	 */
	protected <T, W> T getWrappedProperty(Class<T> returnType, Class<W> argumentType, String key)
	{
		try
		{
			W data = getProperty(argumentType, key);
			try
			{
				return returnType.getConstructor(argumentType).newInstance(data);
			}
			catch (NullPointerException ex)
			{
				return null;
			}
		}
		catch (Exception e)
		{
			return null;
		}
	}
}
