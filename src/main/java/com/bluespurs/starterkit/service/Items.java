package com.bluespurs.starterkit.service;

import java.util.ArrayList;

public class Items {

	private int itemId;
	private int parentItemId;
	private String name;
	private double msrp;
	private double salePrice;
	private String upc;
	
	private String categoryPath;
	private String shortDescription;
	private int standardShipRate;
	private boolean marketplace;
	private String modelNumber;
	private String productUrl;
	
	private String customerRating;
	private int numReviews;
	private String customerRatingImage;
	private String categoryNode;
	private boolean bundle;
	private String stock;
	private String addToCartUrl;
	private String affiliateAddToCartUrl;
	private String offerType;
	private boolean shippingPassEligible;
	private boolean availableOnline;
	private Gift gift;
	private ArrayList<Image> image;
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	public int getParentItemId() {
		return parentItemId;
	}
	public void setParentItemId(int parentItemId) {
		this.parentItemId = parentItemId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getMsrp() {
		return msrp;
	}
	public void setMsrp(double msrp) {
		this.msrp = msrp;
	}
	public double getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(double salePrice) {
		this.salePrice = salePrice;
	}
	public String getUpc() {
		return upc;
	}
	public void setUpc(String upc) {
		this.upc = upc;
	}
	public String getCategoryPath() {
		return categoryPath;
	}
	public void setCategoryPath(String categoryPath) {
		this.categoryPath = categoryPath;
	}
	public String getShortDescription() {
		return shortDescription;
	}
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}
	public int getStandardShipRate() {
		return standardShipRate;
	}
	public void setStandardShipRate(int standardShipRate) {
		this.standardShipRate = standardShipRate;
	}
	public boolean isMarketplace() {
		return marketplace;
	}
	public void setMarketplace(boolean marketplace) {
		this.marketplace = marketplace;
	}
	public String getModelNumber() {
		return modelNumber;
	}
	public void setModelNumber(String modelNumber) {
		this.modelNumber = modelNumber;
	}
	public String getProductUrl() {
		return productUrl;
	}
	public void setProductUrl(String productUrl) {
		this.productUrl = productUrl;
	}
	public String getCustomerRating() {
		return customerRating;
	}
	public void setCustomerRating(String customerRating) {
		this.customerRating = customerRating;
	}
	public int getNumReviews() {
		return numReviews;
	}
	public void setNumReviews(int numReviews) {
		this.numReviews = numReviews;
	}
	public String getCustomerRatingImage() {
		return customerRatingImage;
	}
	public void setCustomerRatingImage(String customerRatingImage) {
		this.customerRatingImage = customerRatingImage;
	}
	public String getCategoryNode() {
		return categoryNode;
	}
	public void setCategoryNode(String categoryNode) {
		this.categoryNode = categoryNode;
	}
	public boolean isBundle() {
		return bundle;
	}
	public void setBundle(boolean bundle) {
		this.bundle = bundle;
	}
	public String getStock() {
		return stock;
	}
	public void setStock(String stock) {
		this.stock = stock;
	}
	public String getAddToCartUrl() {
		return addToCartUrl;
	}
	public void setAddToCartUrl(String addToCartUrl) {
		this.addToCartUrl = addToCartUrl;
	}
	public String getAffiliateAddToCartUrl() {
		return affiliateAddToCartUrl;
	}
	public void setAffiliateAddToCartUrl(String affiliateAddToCartUrl) {
		this.affiliateAddToCartUrl = affiliateAddToCartUrl;
	}
	public String getOfferType() {
		return offerType;
	}
	public void setOfferType(String offerType) {
		this.offerType = offerType;
	}
	public boolean isShippingPassEligible() {
		return shippingPassEligible;
	}
	public void setShippingPassEligible(boolean shippingPassEligible) {
		this.shippingPassEligible = shippingPassEligible;
	}
	public boolean isAvailableOnline() {
		return availableOnline;
	}
	public void setAvailableOnline(boolean availableOnline) {
		this.availableOnline = availableOnline;
	}
	public Gift getGift() {
		return gift;
	}
	public void setGift(Gift gift) {
		this.gift = gift;
	}
	public ArrayList<Image> getImage() {
		return image;
	}
	public void setImage(ArrayList<Image> image) {
		this.image = image;
	}
	
	
	/*public String getJsonData(ArrayList<Items> al) {
		
	}*/
	
}
