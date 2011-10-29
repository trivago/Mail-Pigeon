package com.trivago.mail.pigeon.importer;

import com.trivago.mail.pigeon.bean.Recipient;
import com.trivago.mail.pigeon.bean.RecipientGroup;
import com.trivago.mail.pigeon.storage.ConnectionFactory;
import com.trivago.mail.pigeon.storage.IndexTypes;
import com.trivago.mail.pigeon.storage.Util;
import org.apache.log4j.Logger;
import org.jumpmind.symmetric.csv.CsvReader;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;

public class Csv
{
	private File file;

	private long groupId;

	private static final Logger log = Logger.getLogger(Csv.class);

	public Csv(File file, long groupId)
	{
		this.file = file;
		this.groupId = groupId;
	}

	public Csv(File file)
	{
		this.file = file;
	}

	public void importData(final boolean forceUpdate) throws IOException
	{
		BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

		CsvReader csvReader = new CsvReader(new FileReader(file));
		csvReader.setDelimiter(';');

		int rowCount = 1;
		int invalidCount = 0;
		final GraphDatabaseService database = ConnectionFactory.getDatabase();

		try
		{

			while (csvReader.readRecord())
			{
				String[] parts = csvReader.getValues();

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

					long userId = Long.parseLong(parts[0]);

					log.info("ID " + userId + " given, try to load node");

					Node userNode = ConnectionFactory.getUserIndex().get(IndexTypes.USER_ID, userId).getSingle();

					// If we have a user and we do not want to update => just skip it
					if (userNode != null && !forceUpdate)
					{
						log.info("Found usernode and update is not forced, skipping entry");
						continue;
					}

					// user does not exist, create it

					recipient = new Recipient(userId, parts[1], parts[2]);
					log.debug("Created new user with id " + recipient.getId());
				}
				else
				{
					long rndUserId = Util.generateId();
					recipient = new Recipient(rndUserId, parts[1], parts[2]);
				}


				Node loadedGroupNode = ConnectionFactory.getGroupIndex().get(IndexTypes.GROUP_ID, this.groupId).getSingle();
				RecipientGroup group;


				if (loadedGroupNode == null)
				{
					++invalidCount;
					continue;
				}
				else
				{
					group = new RecipientGroup(loadedGroupNode);
				}

				group.addRecipient(recipient);
				log.debug("Added " + recipient.getId() + " to " + " Group " + group.getId());
				++rowCount;
			}

			log.info("Total Objects: " + rowCount);
			log.info("Invalid Objects: " + invalidCount);
		}
		catch (Exception e)
		{
			log.error("Exception while importing", e);
		}
	}

	private boolean validateRow(String[] row)
	{
		return !row[1].equals("") && !row[2].equals("");
	}
}
