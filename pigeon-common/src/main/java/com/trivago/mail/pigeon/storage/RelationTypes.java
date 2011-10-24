package com.trivago.mail.pigeon.storage;

import org.neo4j.graphdb.RelationshipType;


public enum RelationTypes implements RelationshipType
{
	// Newsletter <-> User
	RECIEVED,
	DELIVERED_TO,

	// Root Node <-> all node types
	NEWSLETTER_REFERENCE,
	USER_REFERENCE,
	GROUP_REFERENCE,

	// User <-> Group
	BELONGS_TO_GROUP
}
