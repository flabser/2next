package com.flabser.script;

import java.util.*;
import com.flabser.script.constants._DocumentModeType;

public class _Document {
    public boolean isValid;
    public boolean isNewDoc;
    protected String currentUserID;
   
    private _Session session;


    public _Document(_Database db) {
        session = db.getParent();
        currentUserID = session.getCurrentUserID();
       
      
    }

   /* public _Document(BaseDocument document) {
        this.doc = document;
        isNewDoc = doc.isNewDoc();
        isValid = doc.isValid;
        currentUserID = document.getCurrentUserID();
    }

    public _Document(BaseDocument document, _Session ses) {
        this(document);
        this.session = ses;
        currentUserID = session.getCurrentUserID();
        //  if (document.db != null) {
        //  this.db = new _Database(document.db, "", ses);
        //  }
    }

    public _Document(BaseDocument document, String user) {
        this(document);
        this.currentUserID = user;
    }*/

    public _DocumentModeType getEditMode() {
		return null;
      /*  switch (doc.editMode) {
            case kz.flabs.dataengine.Const.EDITMODE_READONLY:
                return _DocumentModeType.READ_ONLY;
            case kz.flabs.dataengine.Const.EDITMODE_EDIT:
                return _DocumentModeType.EDIT;
            default:
                return _DocumentModeType.NO_ACCESS;
        }*/
    }

    public String getAuthorID() {
		return currentUserID;
       
    }

    public Date getRegDate() {
		return null;
       
    }

    

	/*	public String toString(){
        return doc.toString();
	}*/


}
