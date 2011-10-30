package com.trivago.mail.pigeon.bean;

import com.trivago.mail.pigeon.storage.ConnectionFactory;
import org.apache.log4j.Logger;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.NotFoundException;
import org.neo4j.graphdb.Transaction;
import org.svenson.JSONProperty;

import java.lang.reflect.InvocationTargetException;

public abstract class AbstractBean {

	protected Node dataNode;

	protected static final Logger log = Logger.getLogger("com.trivago.mail.pigeon.bean");

	@JSONProperty(ignore = true)
	public Node getDataNode()
	{
		return dataNode;
	}

	protected void writeProperty(final String key, final Object value) {
		Transaction tx = ConnectionFactory.getDatabase().beginTx();
		try {
			dataNode.setProperty(key, value);
			tx.success();
		} catch (Exception e) {
			tx.failure();
		} finally {
			tx.finish();
		}
	}

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
				return null;
			}
			else
			{
				throw e;
			}
		}
	}

	protected <T> T getProperty(Class<T> type, String key)
	{
		return getProperty(type, key, true);
	}
	
	protected <T,W> T getWrappedProperty(Class<T> returnType, Class<W> argumentType, String key)
	{
		try {
			W data = getProperty(argumentType, key);
			return returnType.getConstructor(argumentType).newInstance(data);
		} catch (Exception e) {
			return null;
		}
	}
}
