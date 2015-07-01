package com.flabser.rule;


import org.w3c.dom.Node;
import com.flabser.appenv.AppEnv;
import com.flabser.rule.constants.RunMode;
import com.flabser.users.UserRoleType;
import com.flabser.util.XMLUtil;

public class Role {
	public String name = "unknown";
	public String appID;
	public String description;
	public RunMode isOn = RunMode.ON; 
	public boolean isValid;
	public boolean active = false;


	Role(Node node, String appID){
		try{
			this.appID = appID;
			name = XMLUtil.getTextContent(node,"@id", false);
			description = XMLUtil.getTextContent(node,".", false);

			if (XMLUtil.getTextContent(node,"@mode", false).equalsIgnoreCase("off")){                    
				isOn = RunMode.OFF;	
				isValid = false;
			}

            if (XMLUtil.getTextContent(node, "@default", false).equalsIgnoreCase("on")) {
                active = true;
            }

			isValid = true;
		} catch(Exception e) {  
			AppEnv.logger.errorLogEntry(this.getClass().getSimpleName(),e);						
		}
	}

}
