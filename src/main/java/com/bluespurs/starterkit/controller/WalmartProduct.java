package com.bluespurs.starterkit.controller;

import java.util.ArrayList;
import java.util.List;

import com.bluespurs.starterkit.service.Facet;
import com.bluespurs.starterkit.service.Items;

public class WalmartProduct {
	
	private String query;
	private String sort; 
	private String responseGroup;
	private int totalResults;
	private int start;
	private int numItems;
	private ArrayList<Items> items;
	private ArrayList<Facet> facet;
	
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getResponseGroup() {
		return responseGroup;
	}
	public void setResponseGroup(String responseGroup) {
		this.responseGroup = responseGroup;
	}
	public int getTotalResults() {
		return totalResults;
	}
	public void setTotalResults(int totalResults) {
		this.totalResults = totalResults;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getNumItems() {
		return numItems;
	}
	public void setNumItems(int numItems) {
		this.numItems = numItems;
	}
	public ArrayList<Items> getItems() {
		return items;
	}
	public void setItems(ArrayList<Items> items) {
		this.items = items;
	}
	public ArrayList<Facet> getFacet() {
		return facet;
	}
	public void setFacet(ArrayList<Facet> facet) {
		this.facet = facet;
	}
	/*@Override
	public String toString() {
		{
		    "productName": "iPad Mini",
		    "bestPrice": "150.00",
		    "currency": "CAD",
		    "location": "Walmart"
		}
		String st = "{"+"\"productName\""+":"+"variable"+","+ "\"bestPrice\""+": "+ "}";
		return "";
		
	}*/
	
	
}
