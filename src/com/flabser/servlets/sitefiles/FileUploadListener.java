package com.flabser.servlets.sitefiles;

import org.apache.commons.fileupload.ProgressListener;

/**
 * This is a File Upload Listener that is used by Apache
 * Commons File Upload to monitor the progress of the 
 * uploaded file.
 * 
 * @author S.Tchaikovsky
 * 
 * Initial Creation Date: 06.11.2012
 */
public class FileUploadListener implements ProgressListener
{
    private volatile long 
    	bytesRead = 0L,
    	contentLength = 0L,
    	item = 0L;   
    
    public FileUploadListener() 
    {
    	super();
    }
    
    public void update(long aBytesRead, long aContentLength, int anItem) 
    {
        bytesRead = aBytesRead;
        contentLength = aContentLength;
        item = anItem;
    }
    
    public long getBytesRead() 
    {
        return bytesRead;
    }
    
    public long getContentLength() 
    {
        return contentLength;
    }
    
    public long getItem() 
    {
        return item;
    }
}

