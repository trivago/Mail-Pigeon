package com.trivago.mail.pigeon.web.webservice;

import com.trivago.mail.pigeon.bean.Recipient;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.index.IndexHits;
import org.svenson.JSON;
import org.svenson.util.JSONBeanUtil;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.xml.ws.http.HTTPException;
import java.util.ArrayList;
import java.util.List;

@Path("/recipient")
public class RecipientEndpoint {

	@Path("/")
	@GET
	@Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
	public String list()
	{
		final IndexHits<Node> recipientList = Recipient.getAll();
		ArrayList<Recipient> recipientArrayList = new ArrayList<Recipient>();
		for (Node n : recipientList)
		{
			Recipient r = new Recipient(n);
			recipientArrayList.add(r);
		}
		List<String> ignoredProps = new ArrayList<String>();
		ignoredProps.add("dataNode");
		JSON.defaultJSON().setIgnoredProperties(ignoredProps);
		return JSON.defaultJSON().forValue( recipientArrayList );
	}
	
	@Path("/{id}")
	@GET
	@Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
	public String getOne(@PathParam("id") Long id)
	{
		Recipient r = new Recipient(id);
		if (r.getDataNode() == null)
		{
			throw new HTTPException(404);
		}
		List<String> ignoredProps = new ArrayList<String>();
		ignoredProps.add("dataNode");
		JSON.defaultJSON().setIgnoredProperties(ignoredProps);
		return JSON.defaultJSON().forValue( r );
	}

	@Path("/{id}")
	@POST
	@Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
	public String createOrUpdate(@PathParam("id") Long id)
	{
		return "";
	}

}
