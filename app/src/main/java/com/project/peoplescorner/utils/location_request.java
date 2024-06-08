package com.project.peoplescorner.utils;

public class location_request 
{
	public static double Latitude;
	public static double Longitude;
	
	public static double GetLatitude()
	{	
		return Latitude;
	}
	public static void SetLatitide(double Rlatitude)
	{
		Latitude=Rlatitude;
	}
	
	public static double GetLongitude()
	{	
		return Longitude;
	}
	public static void SetLongitude(double Rlongitude)
	{
		Longitude=Rlongitude;
	}
}
