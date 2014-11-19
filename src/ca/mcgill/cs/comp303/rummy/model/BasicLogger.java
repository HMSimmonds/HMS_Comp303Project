package ca.mcgill.cs.comp303.rummy.model;

import java.io.Serializable;

public class BasicLogger
{
	public void stateChanged(String pMessage)
	{
		System.out.println(pMessage);
	}
}
