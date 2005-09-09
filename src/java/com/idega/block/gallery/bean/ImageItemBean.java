package com.idega.block.gallery.bean;


import java.util.Enumeration;
import com.idega.content.bean.ContentItem;
import com.idega.content.bean.ContentItemBean;
import com.idega.presentation.IWContext;
import com.idega.slide.util.WebdavExtendedResource;

/*
 * $Id: ImageItemBean.java,v 1.2 2005/09/09 22:08:14 gimmi Exp $
 * Created on 15.3.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
/**
 * 
 *  Last modified: $Date: 2005/09/09 22:08:14 $ by $Author: gimmi $
 * 
 * @author <a href="mailto:gummi@idega.com">Gudmundur Agust Saemundsson</a>
 * @version $Revision: 1.2 $
 */
public class ImageItemBean extends ContentItemBean implements ContentItem {
	
	public final static String FIELDNAME_DESCRIPTION = "description";
	public final static String FIELDNAME_IMAGE = "image";
	private final static String[] FIELDNAME_ARRAY = new String[] {FIELDNAME_IMAGE,ContentItemBean.FIELDNAME_CREATION_DATE,FIELDNAME_DESCRIPTION};

	String contextURI = null;
	
	/**
	 * 
	 */
	public ImageItemBean() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.idega.content.bean.ContentItem#getContentFieldNames()
	 */
	public String[] getContentFieldNames() {
		return FIELDNAME_ARRAY;
	}

	/* (non-Javadoc)
	 * @see com.idega.content.bean.ContentItem#getContentItemPrefix()
	 */
	public String getContentItemPrefix() {
		return "image_";
	}
	
	public String getDescription(){
		return (String) getValue(FIELDNAME_DESCRIPTION);
	}
	
	public void setDescription(String description){
		setValue(FIELDNAME_DESCRIPTION,description);
	}
	
	public Object getValue(String fieldName){
		if(FIELDNAME_IMAGE.equals(fieldName)){
			String url = getResourcePath();
			if(contextURI==null){
				IWContext iwc = IWContext.getInstance();
				contextURI = iwc.getIWMainApplication().getApplicationContextURI();
			}
			
			if(contextURI.length() > 1){
				if(url!=null && url.startsWith(contextURI)){
					url = url.substring(contextURI.length());
				}
			}
			return url;
		}
		return super.getValue(fieldName);
	}
	
	public void setValue(String fieldName, Object value){
		if(FIELDNAME_IMAGE.equals(fieldName)){
			setResourcePath((String)value);
		}
		super.setValue(fieldName,value);
	}
	
	/**
	 * @param webdavResource
	 */
	public boolean load(WebdavExtendedResource webdavResource) throws Exception {
		Enumeration e = webdavResource.propfindMethod(FIELDNAME_DESCRIPTION);
		if(e.hasMoreElements()){
			setDescription((String)e.nextElement());
		}
		
		return true;
	}
	
	public String[] getToolbarActions(){
		return new String[] {"preview"};
	}

	public void setDatasource(String datasource) {
	}
	
}
