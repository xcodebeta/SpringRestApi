package com.bluespurs.starterkit.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bluespurs.starterkit.service.Items;
import com.bluespurs.starterkit.service.SortPrice;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

@RestController
public class HelloWorldController {
    public static final String INTRO = "The Bluespurs Interview Starter Kit is running properly.";
    public static final Logger log = LoggerFactory.getLogger(HelloWorldController.class);

    /**
     * The index page returns a simple String message to indicate if everything is working properly.
     * The method is mapped to "/" as a GET request.
     * @throws UnsupportedEncodingException 
     */
    
    		
    //@RequestMapping("/product/search?name=ipad")
    /*public String helloWorld() {
        log.info("Visiting index page");
       
        return INTRO;
    }*/
    
    public static ArrayList<Items> getWalmartData(String commonURl) {
    	
    	String REQUEST_URL = commonURl;
    	WalmartProduct obj=null;	
    	BufferedReader in = null;
    	Gson gson = null;
    	String jsonString="";
    	  try {
    		  
    		  URL url = new URL(REQUEST_URL);
    			 in = new BufferedReader(new InputStreamReader(url.openStream()));
    			String inputLine;
    			while ((inputLine = in.readLine()) != null) {
    				jsonString = inputLine;
    			}
    			
    	        gson = new GsonBuilder().create();
    			obj = gson.fromJson(jsonString, WalmartProduct.class);
    			
    			in.close();
    		} catch (IOException e) {
    	  		e.getMessage();
    				e.printStackTrace();
    		} catch (Exception e) {
    				e.getMessage();
    				e.printStackTrace();
    		} finally {
    			
    		}
    	ArrayList<Items> al= obj.getItems();
   	   	Collections.sort(obj.getItems(), new SortPrice());	
   	   	return al;
    }
    

    @RequestMapping(value="/product/search", method=RequestMethod.GET, params="name")
    @ResponseBody
    public String byParameter(@RequestParam("name") String productName) throws UnsupportedEncodingException {
        
         	String walmartUrl = "http://api.walmartlabs.com/v1/search?apiKey=rm25tyum3p9jm9x9x7zxshfa&query="+productName;
         	
         	ArrayList<Items> sortedList = getWalmartData(walmartUrl);
    		
         	JsonObject walmartData = new JsonObject();
         	
         	walmartData.addProperty("productName", sortedList.get(0).getName());
         	walmartData.addProperty("bestPrice", sortedList.get(0).getSalePrice());
         	walmartData.addProperty("currency", "CAD");
         	walmartData.addProperty("location", "Walmart");
         	
         	
    		return walmartData.toString();
         	
    	
    }
}
