/*
 * $Id: ImageItemViewer.java,v 1.1 2005/06/30 14:06:38 gummi Exp $
 * Created on 15.3.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.gallery.presentation;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlGraphicImage;
import javax.faces.component.html.HtmlOutputText;
import com.idega.block.gallery.bean.ImageItemBean;
import com.idega.content.bean.ContentItemBean;
import com.idega.content.presentation.ContentItemViewer;
import com.idega.webface.convert.WFTimestampConverter;


/**
 * 
 *  Last modified: $Date: 2005/06/30 14:06:38 $ by $Author: gummi $
 * 
 * @author <a href="mailto:gummi@idega.com">Gudmundur Agust Saemundsson</a>
 * @version $Revision: 1.1 $
 */
public class ImageItemViewer extends ContentItemViewer {

	private final static String FIELD_IMAGE = ImageItemBean.FIELDNAME_IMAGE;
	private final static String FIELD_CREATION_DATE = ContentItemBean.FIELDNAME_CREATION_DATE;
	private final static String FIELD_DESCRIPTION = ImageItemBean.FIELDNAME_DESCRIPTION;
	
	private final static String[] FIELD_ARRAY = new String[] {FIELD_IMAGE,FIELD_CREATION_DATE,FIELD_DESCRIPTION};
	private final static String facetIdPrefix = "image_";
	private final static String styleClassPrefix = "image_";

	
	/**
	 * 
	 */
	public ImageItemViewer() {
		super();
		this.setStyleClass("image_item");
	}
	
	
	
	public String[] getViewerFieldNames(){
		return FIELD_ARRAY;
	}
	
	/**
	 * @return Returns the facetIdPrefix.
	 */
	protected String getFacetIdPrefix() {
		return facetIdPrefix;
	}

	/**
	 * @return Returns the styleClassPrefix.
	 */
	protected String getDefaultStyleClassPrefix() {
		return styleClassPrefix;
	}
	
	protected UIComponent createFieldComponent(String fieldName){
		if(FIELD_IMAGE.equals(fieldName)){
			return new HtmlGraphicImage();
		} else {
			return new HtmlOutputText();
		}
	}
	
	protected void initializeContent() {	
		super.initializeContent();
		((HtmlOutputText)getFieldViewerComponent(FIELD_CREATION_DATE)).setConverter(new WFTimestampConverter());
	}
	
}
