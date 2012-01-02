package com.trivago.mail.pigeon.importer;

import com.trivago.mail.pigeon.bean.Recipient;
import com.trivago.mail.pigeon.bean.RecipientGroup;
import com.trivago.mail.pigeon.storage.ConnectionFactory;
import com.trivago.mail.pigeon.storage.Gender;
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
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Csv
{
	private File file;

	private long groupId;

	private static final Logger log = Logger.getLogger(Csv.class);

	private List<String> allowedHeadlines = Arrays.asList("id", "title", "firstname", "lastname", "email", "active", "gender", "birthday", "city", "country", "language");

	private List<String> mandatoryHeadLines = Arrays.asList("firstname", "lastname", "email", "active");

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
		ConnectionFactory.getDatabase();
		csvReader.readHeaders();
		try
		{
			String[] headers = csvReader.getHeaders();
			if (!Arrays.asList(headers).containsAll(mandatoryHeadLines))
			{
				throw new RuntimeException("Cannot parse csv, expecting headers: [" + mandatoryHeadLines.toString() + "]");
			}

			while (csvReader.readRecord())
			{

				Recipient recipient;
				long userId;
				if (!csvReader.get("id").equals(""))
				{

					userId = Long.parseLong(csvReader.get("id"));

					log.info("ID " + userId + " given, try to load node");

					Node userNode = ConnectionFactory.getUserIndex().get(IndexTypes.USER_ID, userId).getSingle();

					// If we have a user and we do not want to update => just skip it
					if (userNode != null && !forceUpdate)
					{
						log.info("Found usernode and update is not forced, skipping entry");
						continue;
					}

					// user does not exist, create it


				}
				else
				{
					userId = Util.generateId();

				}

				Gender gender = Gender.UNKNOWN;
				if (!csvReader.get("gender").equals(""))
				{
					String genderString = csvReader.get("gender");
					if (genderString.equalsIgnoreCase("male"))
					{
						gender = Gender.MALE;
					}
					else if (csvReader.get("gender").equalsIgnoreCase("female"))
					{
						gender = Gender.FEMALE;
					}
					else if (csvReader.get("gender").equalsIgnoreCase("company"))
					{
						gender = Gender.COMPANY;
					}
				}

				Date birthDay = null;
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
				if (!csvReader.get("birthday").equalsIgnoreCase(""))
				{
					birthDay = simpleDateFormat.parse(csvReader.get("birthday"));
				}

				recipient = new Recipient(
						userId,
						csvReader.get("firstname"),
						csvReader.get("lastname"),
						csvReader.get("email"),
						Boolean.getBoolean(csvReader.get("active")),
						gender,
						birthDay,
						csvReader.get("title"),
						csvReader.get("city"),
						csvReader.get("country"),
						csvReader.get("language"),
						csvReader.get("external_id")
				);

				log.debug("Created new user with id " + recipient.getId());

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
