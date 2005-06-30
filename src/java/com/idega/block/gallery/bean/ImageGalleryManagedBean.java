/*
 * $Id: ImageGalleryManagedBean.java,v 1.1 2005/06/30 14:06:38 gummi Exp $
 * Created on 15.3.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.gallery.bean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.webdav.lib.search.CompareOperator;
import org.apache.webdav.lib.search.SearchException;
import org.apache.webdav.lib.search.SearchExpression;
import org.apache.webdav.lib.search.SearchRequest;
import org.apache.webdav.lib.search.SearchScope;
import com.idega.block.gallery.presentation.ImageItemViewer;
import com.idega.business.IBOLookup;
import com.idega.content.bean.ContentItemBeanComparator;
import com.idega.content.bean.ContentListViewerManagedBean;
import com.idega.content.business.ContentSearch;
import com.idega.content.presentation.ContentItemViewer;
import com.idega.core.file.util.MimeTypeUtil;
import com.idega.core.search.business.Search;
import com.idega.core.search.business.SearchResult;
import com.idega.presentation.IWContext;
import com.idega.slide.business.IWSlideSession;
import com.idega.slide.util.IWSlideConstants;
import com.idega.util.IWTimestamp;


/**
 * 
 *  Last modified: $Date: 2005/06/30 14:06:38 $ by $Author: gummi $
 * 
 * @author <a href="mailto:gummi@idega.com">Gudmundur Agust Saemundsson</a>
 * @version $Revision: 1.1 $
 */
public class ImageGalleryManagedBean implements ContentListViewerManagedBean {

	
	private String resourcePath=null;

	private int numberOfDaysDisplayed = 30;
	private List categories = null;
	private String detailsViewerPath = null;
	
	/**
	 * 
	 */
	public ImageGalleryManagedBean() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.idega.content.bean.ContentListViewerManagedBean#getContentItems()
	 */
	public List getContentItems() {
		try {
			List l = loadAllImagesInFolder(resourcePath);
//			ContentItemBeanComparator c = new ContentItemBeanComparator();
//			c.setReverseOrder(true);
//			Collections.sort(l,c);
			return l;
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ArrayList();
	}
	
	
	
	/**
	 * Loads all xml files in the given folder
	 * @param folder
	 * @return List containing ArticleItemBean
	 * @throws XmlException
	 * @throws IOException
	 */
	public List loadAllImagesInFolder(String folder) throws IOException{
		List list = new ArrayList();		
			
		IWContext iwc = IWContext.getInstance();		
		
		IWTimestamp oldest = null;
		
		try {
			String scope = folder;
			IWSlideSession session = (IWSlideSession)IBOLookup.getSessionInstance(iwc,IWSlideSession.class);
			if(scope != null){
				if(scope.startsWith(session.getWebdavServerURI())){
					scope = scope.substring(session.getWebdavServerURI().length());
				}
				if(scope.startsWith("/")){
					scope = scope.substring(1);
				}
			}
			ContentSearch searchBusiness = new ContentSearch(iwc.getIWMainApplication());
			Search search = searchBusiness.createSearch(getSearchRequest(scope, oldest,categories));
			Collection results = search.getSearchResults();
			
			if(results!=null){				
				for (Iterator iter = results.iterator(); iter.hasNext();) {
					SearchResult result = (SearchResult) iter.next();
					try {
						System.out.println("Attempting to load "+result.getSearchResultURI());
						ImageItemBean image = new ImageItemBean();
						image.load(result.getSearchResultURI());
						list.add(image);
					}catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		catch (SearchException e1) {
			e1.printStackTrace();
		}
		
		return list;
	}

	/**
	 * @param folder
	 * @param localeString
	 * @param oldest
	 * @param categoryList
	 * @throws SearchException
	 */
	public SearchRequest getSearchRequest(String scope, IWTimestamp oldest, List categoryList) throws SearchException {
		SearchRequest s = new SearchRequest();
		s.addSelection(IWSlideConstants.PROPERTY_CREATION_DATE);
		s.addSelection(IWSlideConstants.PROPERTY_CATEGORY);
		s.addScope(new SearchScope(scope));
		SearchExpression expression = null;
		
		
		//MIME_TYPE
//		MimeTypeUtil mUtil = MimeTypeUtil.getInstance();
//		String[] mimeTypes = mUtil.getMimeTypesInCategory(MimeTypeUtil.MIME_TYPE_CATEGORY_IMAGE);
//		List mimeTypeExpressions = new ArrayList();
//		if(mimeTypes != null){
//			for (int i = 0; i<mimeTypes.length;i++) {
//				mimeTypeExpressions.add(s.compare(CompareOperator.LIKE,IWSlideConstants.PROPERTY_CONTENT_TYPE,mimeTypes[i]));
//			}
//			Iterator expr = mimeTypeExpressions.iterator();
//			if(expr.hasNext()){
//				SearchExpression mimeTypeExpression = (SearchExpression)expr.next();
//				while(expr.hasNext()){
//					mimeTypeExpression = s.or(mimeTypeExpression,(SearchExpression)expr.next());
//				}
//				expression = mimeTypeExpression;
//			}
//		}
		
		SearchExpression nameExpression = s.not(s.compare(CompareOperator.LIKE,IWSlideConstants.PROPERTY_DISPLAY_NAME,".*"));
		SearchExpression mimetypeExpression = s.compare(CompareOperator.LIKE,IWSlideConstants.PROPERTY_CONTENT_TYPE,"image/*");
		
		expression = s.and(mimetypeExpression,nameExpression);
		
		
		
		//CREATION_DATE
		SearchExpression creationDateExpression = null;		
		if(oldest != null){
			creationDateExpression = s.compare(CompareOperator.GTE, IWSlideConstants.PROPERTY_CREATION_DATE,oldest.getDate());
			if(expression==null){
				expression = creationDateExpression;
			} else {
				expression = s.and(expression,creationDateExpression);
			}
		}
		
		//CATEGORIES
		List categoryExpressions = new ArrayList();
		if(categoryList != null){
			for (Iterator iter = categoryList.iterator(); iter.hasNext();) {
				String categoryName = (String) iter.next();
				categoryExpressions.add(s.compare(CompareOperator.LIKE,IWSlideConstants.PROPERTY_CATEGORY,"%"+categoryName+"%"));
			}
			Iterator expr = categoryExpressions.iterator();
			if(expr.hasNext()){
				SearchExpression categoryExpression = (SearchExpression)expr.next();
				while(expr.hasNext()){
					categoryExpression = s.or(categoryExpression,(SearchExpression)expr.next());
				}
				if(expression==null){
					expression = categoryExpression;
				} else {
					expression = s.and(expression,categoryExpression);
				}
			}
		}
		
		
		if(expression!=null){
			s.setWhereExpression(expression);
		}
		System.out.println("------------------------");
		System.out.println(s.asString());
		System.out.println("------------------------");
		return s;
	}

	/* (non-Javadoc)
	 * @see com.idega.content.bean.ContentListViewerManagedBean#getContentViewer()
	 */
	public ContentItemViewer getContentViewer() {
		return new ImageItemViewer();
	}

	/* (non-Javadoc)
	 * @see com.idega.content.bean.ContentListViewerManagedBean#getAttachmentViewers()
	 */
	public List getAttachmentViewers() {
		return null;
	}


	/* (non-Javadoc)
	 * @see com.idega.content.bean.ContentListViewerManagedBean#setResourcePath(java.lang.String)
	 */
	public void setResourcePath(String path) {
		resourcePath=path;
	}

	/* (non-Javadoc)
	 * @see com.idega.content.bean.ContentListViewerManagedBean#setDetailsViewerPath(java.lang.String)
	 */
	public void setDetailsViewerPath(String path) {
		detailsViewerPath = path;
	}
	
	/**
	 * @return Returns the categories.
	 */
	public List getCategories() {
		return categories;
	}
	/**
	 * @param categories The categories to set.
	 */
	public void setCategories(List categories) {
		this.categories = categories;
	}


	/* (non-Javadoc)
	 * @see com.idega.content.bean.ContentListViewerManagedBean#getIWActionURIHandlerIdentifier()
	 */
	public String getIWActionURIHandlerIdentifier() {
		return null;
	}
}
