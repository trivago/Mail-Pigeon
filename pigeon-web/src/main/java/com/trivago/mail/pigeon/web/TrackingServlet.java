package com.trivago.mail.pigeon.web;

import com.trivago.mail.pigeon.storage.ConnectionFactory;
import com.trivago.mail.pigeon.storage.IndexTypes;
import com.trivago.mail.pigeon.storage.RelationTypes;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.index.IndexHits;


import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

public class TrackingServlet extends HttpServlet
{


	@Override
	public void init() throws ServletException
	{
		super.init();

	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		String userId = req.getParameter("user_id");
		String newsletterId = req.getParameter("newsletter_id");

		if (userId != null && newsletterId != null)
		{
			final GraphDatabaseService database = ConnectionFactory.getDatabase();

			Node newsletterNode = ConnectionFactory.getNewsletterIndex().get(IndexTypes.NEWSLETTER_ID, newsletterId).getSingle();
			Node userNode = ConnectionFactory.getUserIndex().get(IndexTypes.USER_ID, userId).getSingle();

			if (newsletterNode != null && userNode != null)
			{
				final Transaction transaction = database.beginTx();
				try
				{
					if (!userNode.hasRelationship(RelationTypes.OPENED, Direction.OUTGOING))
					{
						userNode.createRelationshipTo(newsletterNode, RelationTypes.OPENED);
					}
					else
					{
						boolean found = false;
						for(Relationship rel : userNode.getRelationships(RelationTypes.OPENED))
						{
							if (rel.getEndNode().equals(newsletterNode))
							{
								found = true;
							}
						}
						if (!found)
						{
							userNode.createRelationshipTo(newsletterNode, RelationTypes.OPENED);
						}
					}
					transaction.success();
				}
				catch (Exception e)
				{
					transaction.failure();
				}
				finally
				{
					transaction.finish();
				}
			}
		}

		// Write out the faked gif
		resp.setContentType("image/gif");
		byte[] emptyGif = new byte[]{
				0x47, 0x49, 0x46, 0x38, 0x39, 0x61, 0x01, 0x00, 0x01, 0x00, (byte) 0xf0, 0x01, 0x00, (byte) 0xff, (byte) 0xff,
				(byte) 0xff, 0x00, 0x00, 0x00, 0x21, (byte) 0xf9, 0x04, 0x01, 0x0a, 0x00, 0x00, 0x00, 0x2c, 0x00, 0x00, 0x00, 0x00,
				0x01, 0x00, 0x01, 0x00, 0x00, 0x02, 0x02, 0x44, 0x01, 0x00, 0x3b};
		resp.setContentLength(emptyGif.length);
		ServletOutputStream out = resp.getOutputStream();
		out.write(emptyGif);
	}
}
