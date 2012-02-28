/**
 * Copyright (C) 2011-2012 trivago GmbH <mario.mueller@trivago.com>, <christian.krause@trivago.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.trivago.mail.pigeon.storage;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.index.IndexHits;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
				s = clazz.getConstructor(new Class[]{node.getClass()}).newInstance(node);
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
