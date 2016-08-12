package com.businesshaps.oi;


public class JsonOutput extends DataObject {
	private int totalItems;
	private int itemsPerPage;
	private int pages;
	private int pageIndex;
	private String sort;
	private Object[] items;
	private String[] ids;
	private DataObject[] children;
	
	
	public DataObject[] getChildren() {
		return children;
	}
	public String[] getIds() {
		return ids;
	}
	public void setIds(String[] ids) {
		this.ids = ids;
	}
	public void setChildren(DataObject[] children) {
		this.children = children;
	}
	public void setPages() {
		this.pages = this.getItemsPerPage()==0?1:(int) Math.ceil(new Float(this.getTotalItems())/new Float(this.getItemsPerPage()));
	}
	public int getItemsPerPage() {
		return itemsPerPage;
	}
	public void setItemsPerPage(int itemsPerPage) {

		this.itemsPerPage = itemsPerPage;
		this.setPages();
	}
	public int getPages() {
		this.setPages();
		return this.pages;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public int getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(int pageIndex) {
		
		this.pageIndex = pageIndex;
		this.setPages();
	}
	public int getTotalItems() {
		return totalItems;
	}
	public void setTotalItems(int totalItems) {
		this.totalItems = totalItems;
		this.setPages();
	}
	public Object[] getItems() {
		return items;
	}
	public void setItems(Object[] items) {
		this.items = items;
	}
	
}
