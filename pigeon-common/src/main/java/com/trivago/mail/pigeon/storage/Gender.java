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
