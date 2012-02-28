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

public enum Gender
{
	MALE("male"),
	FEMALE("female"),
	COMPANY("company"),
	UNKNOWN("unknown");


	private String textValue;

	Gender(String gender)
	{
		this.textValue = gender;
	}

	@Override
	public String toString()
	{
		return textValue;
	}

	public static Gender fromString(String identifier)
	{
		if (identifier.equalsIgnoreCase("male"))
		{
			return Gender.MALE;
		}
		else if (identifier.equalsIgnoreCase("female"))
		{
			return Gender.FEMALE;
		}
		else if (identifier.equalsIgnoreCase("company"))
		{
			return Gender.COMPANY;
		}
		else
		{
			return Gender.UNKNOWN;
		}

	}
}
