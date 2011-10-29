package com.trivago.mail.pigeon.storage;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.index.IndexHits;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: mmueller
 * Date: 26.10.11
 * Time: 15:41
 * To change this template use File | Settings | File Templates.
 */
public abstract class Util
{
	public static <T> List<T> indexHitsToList(Class<T> clazz, IndexHits<Node> hitList)
	{
		List<T> list = new ArrayList<T>();
		for (Node node : hitList)
		{
			T s = null;
			try
			{
				s = clazz.getConstructor(new Class[] {node.getClass()}).newInstance(node);
				list.add(s);
			}
			catch (InstantiationException e)
			{
				e.printStackTrace();
			}
			catch (IllegalAccessException e)
			{
				e.printStackTrace();
			}
			catch (InvocationTargetException e)
			{
				e.printStackTrace();
			}
			catch (NoSuchMethodException e)
			{
				e.printStackTrace();
			}

		}
		return list;
	}

    public static long generateId()
    {
        return Math.round(new Date().getTime() * Math.random());
    }
}
