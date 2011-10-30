package com.trivago.mail.pigeon.storage;

import com.trivago.mail.pigeon.configuration.Settings;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.index.Index;
import org.neo4j.kernel.EmbeddedGraphDatabase;

public class ConnectionFactory
{

	private static GraphDatabaseService graphDb;

	private static Index<Node> newsletterIndex;

	private static Index<Node> userIndex;

	private static Index<Node> groupIndex;
	
	private static Index<Node> senderIndex;

	private static Index<Node> bounceIndex;

	private static Index<Node> campaignIndex;

	private static Index<Node> templateIndex;

	private static Index<Node> tagIndex;

	public static final long DEFAULT_BOUNCE_NODE = 1337L;

	public static GraphDatabaseService getDatabase()
	{
		if (graphDb == null)
		{
			graphDb = new EmbeddedGraphDatabase(Settings.create().getConfiguration().getString("neo4j.path"));
			newsletterIndex = graphDb.index().forNodes("newsletter");
			userIndex = graphDb.index().forNodes("user");
			groupIndex = graphDb.index().forNodes("group");
			senderIndex = graphDb.index().forNodes("sender");
			senderIndex = graphDb.index().forNodes("bounce");
			campaignIndex = graphDb.index().forNodes("campaign");
            templateIndex = graphDb.index().forNodes("template");

			registerShutdownHook();
		}
		return graphDb;
	}

	public static Index<Node> getNewsletterIndex()
	{
		if (graphDb == null)
		{
			getDatabase();
		}
		return newsletterIndex;
	}

	public static Index<Node> getUserIndex()
	{
		if (graphDb == null)
		{
			getDatabase();
		}
		return userIndex;
	}

	public static Index<Node> getGroupIndex()
	{
		if (graphDb == null)
		{
			getDatabase();
		}
		return groupIndex;
	}

	public static Index<Node> getSenderIndex()
	{
		if (graphDb == null)
		{
			getDatabase();
		}
		return senderIndex;
	}

	public static Index<Node> getBounceIndex()
	{
		if (graphDb == null)
		{
			getDatabase();
		}
		return bounceIndex;
	}

	public static Index<Node> getCampaignIndex()
	{
		if (graphDb == null)
		{
			getDatabase();
		}
		return campaignIndex;
	}

    public static Index<Node> getTemplateIndex()
	{
		if (graphDb == null)
		{
			getDatabase();
		}
		return templateIndex;
	}

	public static Index<Node> getTagIndex()
	{
		if (graphDb == null)
		{
			getDatabase();
		}
		return tagIndex;
	}

	private static void shutdown()
    {
        graphDb.shutdown();
    }

	private static void registerShutdownHook()
    {
        // Registers a shutdown hook for the Neo4j and index service instances
        // so that it shuts down nicely when the VM exits (even if you
        // "Ctrl-C" the running example before it's completed)
        Runtime.getRuntime().addShutdownHook( new Thread()
        {
            @Override
            public void run()
            {
                shutdown();
            }
        } );
    }
}
