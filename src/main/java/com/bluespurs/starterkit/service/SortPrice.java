package com.bluespurs.starterkit.service;


import java.util.Comparator;

public class SortPrice implements Comparator<Items> {
	
	@Override
	public int compare(Items o1, Items o2) {
		// TODO Auto-generated method stub
		if(o1.getSalePrice()> o2.getSalePrice()) {
		
			return 1; 
		} else {
			return -1;
		}
		//return 0;
	}
	
	

}
