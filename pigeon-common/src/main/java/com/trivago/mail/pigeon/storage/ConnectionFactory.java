package com.trivago.mail.pigeon.storage;

import com.trivago.mail.pigeon.configuration.Settings;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.index.Index;
import org.neo4j.kernel.AbstractGraphDatabase;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.neo4j.server.WrappingNeoServerBootstrapper;

public class ConnectionFactory
{

	private static AbstractGraphDatabase graphDb = new EmbeddedGraphDatabase(Settings.create().getConfiguration().getString("neo4j.path"));

	private static Index<Node> newsletterIndex;

	private static Index<Node> userIndex;

	private static Index<Node> groupIndex;

	private static Index<Node> senderIndex;

	private static Index<Node> bounceIndex;

	private static Index<Node> campaignIndex;

	private static Index<Node> templateIndex;

	private static Index<Node> tagIndex;

	public static final long DEFAULT_BOUNCE_NODE = 1337L;

	private static boolean notStarted = true;

	/**
	 * The wrapper for the graph db to be exposed as server
	 */
	private static WrappingNeoServerBootstrapper srv;

	static
	{
		newsletterIndex = graphDb.index().forNodes("newsletter");
		userIndex = graphDb.index().forNodes("user");
		groupIndex = graphDb.index().forNodes("group");
		senderIndex = graphDb.index().forNodes("sender");
		senderIndex = graphDb.index().forNodes("bounce");
		campaignIndex = graphDb.index().forNodes("campaign");
		templateIndex = graphDb.index().forNodes("template");
        
		if (notStarted)
		{
			srv = new WrappingNeoServerBootstrapper(graphDb);
			srv.start();
			registerShutdownHook();
			notStarted = false;
		}
	}

	private ConnectionFactory()
	{
	}

	/**
	 * Singleton like getter for the graph db.
	 *
	 * @return the graphdb instance
	 */
	public static GraphDatabaseService getDatabase()
	{
		return graphDb;
	}

	public static Index<Node> getNewsletterIndex()
	{
		return newsletterIndex;
	}

	public static Index<Node> getUserIndex()
	{
		return userIndex;
	}

	public static Index<Node> getGroupIndex()
	{
		return groupIndex;
	}

	public static Index<Node> getSenderIndex()
	{
		return senderIndex;
	}

	public static Index<Node> getBounceIndex()
	{
		return bounceIndex;
	}

	public static Index<Node> getCampaignIndex()
	{
		return campaignIndex;
	}

	public static Index<Node> getTemplateIndex()
	{
		return templateIndex;
	}

	public static Index<Node> getTagIndex()
	{
		return tagIndex;
	}

	private static void shutdown()
	{
		srv.stop();
		graphDb.shutdown();
	}

	private static void registerShutdownHook()
	{
		// Registers a shutdown hook for the Neo4j and index service instances
		// so that it shuts down nicely when the VM exits (even if you
		// "Ctrl-C" the running example before it's completed)
		Runtime.getRuntime().addShutdownHook(new Thread()
		{
			@Override
			public void run()
			{
				shutdown();
			}
		});
	}
}
