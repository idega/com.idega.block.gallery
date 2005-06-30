/*
 * $Id: GalleryViewer.java,v 1.1 2005/06/30 14:06:38 gummi Exp $
 * Created on 17.3.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.gallery.presentation;

import com.idega.content.presentation.ContentItemListViewer;


/**
 * 
 *  Last modified: $Date: 2005/06/30 14:06:38 $ by $Author: gummi $
 * 
 * @author <a href="mailto:gummi@idega.com">Gudmundur Agust Saemundsson</a>
 * @version $Revision: 1.1 $
 */
public class GalleryViewer extends ContentItemListViewer {

	/**
	 * 
	 */
	public GalleryViewer() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param managedBeanId
	 */
	public GalleryViewer(String managedBeanId) {
		super(managedBeanId);
		this.setStyleClass("image_gallery");
	}
	
	protected String[] getToolbarActions(){
		return null;
	}
	
}
