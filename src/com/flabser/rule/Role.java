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
	private UserRoleType roleProvider = UserRoleType.PROVIDED_BY_SELF;
	
	@Deprecated
	public Role(String name, String description){
		appID = "System";
		this.name = name;
		this.description = description;
	}
	
	public Role(String name, String appID, String description){
		this.appID = appID;
		this.name = name;
		this.description = description;
	}

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

	public void setRuleProvider(UserRoleType roleProvider){
		this.roleProvider = roleProvider;
	}
	
	public String toXML(){
		return "<ison>" + isOn + "</ison><name>" + name + "</name><app>" + appID + "</app>" +
					"<description>" + description + "</description><ruleprovider>" + roleProvider + "</ruleprovider>";
	}
}
