package com.flabser.script.mail;

public class _Memo {
	public String msubject = "";
	public String body = "";
	
	
	
	_Memo(String subject, String bodyTitle, String b, boolean considerLang){
		msubject = getSubject(subject);
		body = "<b><font color=\"#000080\" size=\"4\" face=\"Default Serif\">" + bodyTitle + "</font></b><hr>";
		body += "<table cellspacing=\"0\" cellpadding=\"4\" border=\"0\" style=\"padding:10px; font-size:12px; font-family:Arial;\">";
		body += "<tr>";
		body += "<td style=\"border-bottom:1px solid #CCC;\" valign=\"top\" colspan=\"2\">";
		body += b + "<br/>";
		body += "</td></tr><tr>";
		body += "<td colspan=\"2\"></td>";
		body += "</tr></table>";
       /* if (doc != null) {
		    body += "<p><font size=\"2\" face=\"Arial\">Для просмотра перейдите по <a href=\"" + doc.getFullURL() + "\">ссылке...</a></p></font>";
        }*/
	}
	
		
	private String getSubject(String subject){
		
		return subject;
	}
	
}
