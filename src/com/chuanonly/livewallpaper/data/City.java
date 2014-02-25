package com.chuanonly.livewallpaper.data;

public class City
{
	public String code;
	public String name;
	public String parentName;
	public String enName;
	public String enProvice;
	 public City(String name, String province, String code, String enName, String enProvice)
	{
		this.name = name;
		this.parentName = province;
		this.code = code;
		this.enName = enName;
		this.enProvice = enProvice;
	}
}
