package com.trivago.mail.pigeon.importer;

import com.trivago.mail.pigeon.bean.Recipient;
import com.trivago.mail.pigeon.bean.RecipientGroup;
import com.trivago.mail.pigeon.storage.ConnectionFactory;
import com.trivago.mail.pigeon.storage.IndexTypes;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import java.io.*;
import java.util.Date;

public class Csv
{
	private File file;

	public Csv(File file)
	{
		this.file = file;
	}

	public void importData(final boolean forceUpdate) throws IOException
	{
		BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

		String line;
		int rowCount = 1;
		int invalidCount = 0;
		final GraphDatabaseService database = ConnectionFactory.getDatabase();
		final RecipientGroup defaultGroup = new RecipientGroup(1);

		Transaction tx = database.beginTx();
		try
		{
			while ((line = bufferedReader.readLine()) != null)
			{
				String[] parts = line.split(";");

				// part 0: user_id => if empty, use neo4j id as id
				// part 1: username => Real Name, unique name, you decide
				// part 2: email address,
				// part 4: group_id => if empty, the default group is used.
				if (!validateRow(parts))
				{
					++invalidCount;
					continue;
				}

				Recipient recipient;

				if (!parts[0].equals(""))
				{
					int userId = Integer.parseInt(parts[0]);
					Node userNode = ConnectionFactory.getUserIndex().get(IndexTypes.USER_ID, userId).getSingle();

					// If we have a user and we do not want to update => just skip it
					if (userNode != null && !forceUpdate)
					{
						continue;
					}

					recipient = new Recipient(userNode);
				}
				else
				{
					long rndUserId = Math.round(new Date().getTime() * Math.random());
					recipient = new Recipient(rndUserId, parts[1], parts[2]);
				}

				RecipientGroup group;
				if (parts[4].equals(""))
				{
					group = defaultGroup;
				}
				else
				{
					Node loadedGroupNode = ConnectionFactory.getGroupIndex().get(IndexTypes.GROUP_ID, Long.parseLong(parts[4])).getSingle();

					// groups must exist before import
					if (loadedGroupNode == null)
					{
						++invalidCount;
						continue;
					}
					else
					{
						group = new RecipientGroup(loadedGroupNode);
					}
				}
				group.addRecipient(recipient);
				++rowCount;
			}
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

	private boolean validateRow(String[] row)
	{
		if (row[1].equals(""))
		{
			return false;
		}

		if (row[2].equals(""))
		{
			return false;
		}
		return true;
	}
}
