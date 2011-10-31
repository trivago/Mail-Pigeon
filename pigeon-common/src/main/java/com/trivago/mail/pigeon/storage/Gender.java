package com.trivago.mail.pigeon.storage;

public enum Gender
{
	MALE("male"),
	FEMALE("female"),
	COMPANY("company");


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
}
