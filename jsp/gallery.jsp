<?xml version="1.0"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
        xmlns:h="http://java.sun.com/jsf/html"
        xmlns:jsf="http://java.sun.com/jsf/core"
        xmlns:ws="http://xmlns.idega.com/com.idega.workspace"
        xmlns:wf="http://xmlns.idega.com/com.idega.webface"
        xmlns:gallery="http://xmlns.idega.com/com.idega.block.gallery"
version="1.2">

	<jsf:view>
		<ws:page>
			<h:form>
				<wf:wfblock id="image_gallery_block" title="Image Gallery">
					<gallery:GalleryViewer id="image_gallery" beanIdentifier="ImageGalleryManagedBean" resourcePath="/files/cms/gallery" />
				</wf:wfblock>
			</h:form>
		</ws:page>
	</jsf:view>
</jsp:root>