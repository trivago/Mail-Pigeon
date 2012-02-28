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

import org.neo4j.graphdb.RelationshipType;


public enum RelationTypes implements RelationshipType
{
	// Newsletter <-> User
	RECIEVED,
	DELIVERED_TO,
	OPENED,

	// Root Node <-> all node types
	NEWSLETTER_REFERENCE,
	USER_REFERENCE,
	GROUP_REFERENCE,
	SENDER_REFERENCE,
	BOUNCE_REFERENCE,
	CAMPAIGN_REFERENCE,
	MAIL_TEMPLATE_REFERENCE,
	TAG_REFERENCE,

	// User <-> Group
	BELONGS_TO_GROUP,

	// Sender <-> Newsletter
	SENT_EMAIL,

	// Bounce <-> Newsletter (via userId vertex property)
	BOUNCED_MAIL,
	BOUNCED_USER,

	// Newsletter <-> Campaign
	PART_OF_CAMPAIGN,

	// Newsletter <-> Template
	USES_TEMPLATE,

	// Tag -> ANYTHING (ALWAYS FROM TAG TO TARGET!!)
	TAGGED
}
