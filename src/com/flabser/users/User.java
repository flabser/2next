package com.flabser.users;

import org.apache.catalina.realm.RealmBase;
import com.flabser.dataengine.Const;
import com.flabser.dataengine.DatabaseFactory;
import com.flabser.dataengine.IDatabase;
import com.flabser.dataengine.IDeployer;
import com.flabser.dataengine.pool.DatabasePoolException;
import com.flabser.dataengine.system.IApplicationDatabase;
import com.flabser.dataengine.system.ISystemDatabase;
import com.flabser.exception.WebFormValueException;
import com.flabser.exception.WebFormValueExceptionType;
import com.flabser.util.Util;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class User implements Const {
	public int docID;
	public boolean isValid = false;
	public HashMap<String, ApplicationProfile> enabledApps = new HashMap<String, ApplicationProfile>();
	private HashSet<UserRole> roles = new HashSet<UserRole>();
	private HashSet<UserGroup> groups = new HashSet<UserGroup>();
	public boolean isAuthorized;
	public String lastURL;

	private transient ISystemDatabase sysDatabase;
	private String userID;
	private String userName;
	private Date primaryRegDate;
	private Date regDate;
	private String password;
	private String passwordHash = "";
	private String email = "";
	private int isSupervisor;
	private int hash;
	private String verifyCode;
	private UserStatusType status = UserStatusType.UNKNOWN;
	private UserSession session;

	public User() {
		this.sysDatabase = DatabaseFactory.getSysDatabase();
		userID = "anonymous";
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		try {
			this.userID = userID;
		} catch (Exception e) {

		}
	}

	public HashSet<String> getAllUserGroups() {
		HashSet<String> userGroups = new HashSet<String>();
		if (userID.equals(sysUser)) {
			userGroups = supervisorGroupAsSet;
			userGroups.addAll(observerGroupAsList);
		}
		try {
			// userGroups.addAll(appUser.getAllUserGroups());
		} catch (Exception e) {
			userGroups.add(userID);
		}
		return userGroups;
	}

	public void fill(ResultSet rs) throws SQLException {
		try {
			docID = rs.getInt("DOCID");
			userName = rs.getString("USERNAME");
			primaryRegDate = rs.getTimestamp("PRIMARYREGDATE");
			regDate = rs.getTimestamp("REGDATE");
			userID = rs.getString("USERID");
			setEmail(rs.getString("EMAIL"));
			isSupervisor = rs.getInt("ISSUPERVISOR");
			password = rs.getString("PWD");
			passwordHash = rs.getString("PWDHASH");
			setHash(rs.getInt("LOGINHASH"));
			verifyCode = rs.getString("VERIFYCODE");
			status = UserStatusType.getType(rs.getInt("STATUS"));
			isValid = true;
		} catch (Exception e) {
			isValid = false;
		}
	}

	public String getPassword() {
		return password;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPassword(String password) throws WebFormValueException {
		if (!("".equalsIgnoreCase(password))) {
			if (Util.pwdIsCorrect(password)) {
				this.password = password;
			} else {
				throw new WebFormValueException(WebFormValueExceptionType.FORMDATA_INCORRECT, "password");
			}
		}
	}

	public void setPasswordHash(String password) throws WebFormValueException {
		if (!("".equalsIgnoreCase(password))) {
			if (Util.pwdIsCorrect(password)) {
				this.passwordHash = password.hashCode() + "";
				//this.passwordHash = getMD5Hash(password);
				this.passwordHash = RealmBase.Digest(password, "MD5", "UTF-8");
			} else {
				throw new WebFormValueException(WebFormValueExceptionType.FORMDATA_INCORRECT, "password");
			}
		}
	}

	public String getCurrentUserID() {
		return userID;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) throws WebFormValueException {
		if (email != null) {
			if (email.equalsIgnoreCase("")) {
				this.email = "";
			} else if (Util.addrIsCorrect(email)) {
				this.email = email;
			} else {
				throw new WebFormValueException(WebFormValueExceptionType.FORMDATA_INCORRECT, "email");
			}
		}
	}

	public boolean isSupervisor() {
		if (isSupervisor == 1) {
			return true;
		} else {
			return false;
		}
	}

	public int getIsSupervisor() {
		return isSupervisor;
	}

	public void setAdmin(boolean isAdmin) {
		if (isAdmin) {
			this.isSupervisor = 1;
		} else {
			isSupervisor = 0;
		}
	}

	public boolean hasRole(String roleName) {
		return true;
	}

	public boolean hasGroup(String groupName) {
		return true;
	}

	public void addApplication(ApplicationProfile ap) {
		enabledApps.put(ap.appName, ap);
	}

	public void setHash(int hash) {
		this.hash = hash;
	}

	public int getHash() {
		return hash;
	}

	public void fillFieldsToSaveLight(HashMap<String, String[]> fields) throws WebFormValueException {
		/*
		 * setUserID(getWebFormValue("userid", fields, userID)[0]);
		 * setEmail(getWebFormValue("email", fields, email)[0]);
		 * setInstMsgAddress(getWebFormValue("instmsgaddress", fields,
		 * instMsgAddress)[0]); setPassword(getWebFormValue("pwd", fields,
		 * password)[0]); setPasswordHash(getWebFormValue("pwd", fields,
		 * password)[0]); String p_eds = getWebFormValue("p_eds", fields,
		 * "")[0]; for (String key : fields.keySet()) { String formSesID =
		 * getWebFormValue("formsesid", fields, "")[0]; if
		 * (!"".equalsIgnoreCase(formSesID)) { HashMap<String, BlobFile>
		 * uploadedFiles = new RuntimeObjUtil().getUploadedFiles(fields); if
		 * (uploadedFiles.size() > 0) { for (Map.Entry<String, BlobFile>
		 * file_entry : uploadedFiles.entrySet()) { try { FileInputStream ksfis
		 * = new FileInputStream(new File(file_entry.getValue().path)); final
		 * InputStream ksbufin = new BufferedInputStream(ksfis);
		 * Security.addProvider(new IolaProvider()); final KeyStore ks =
		 * KeyStore.getInstance("PKCS12", IolaProvider.PROVIDER_NAME); //final
		 * KeyStore ks = KeyStore.getInstance("PKCS12"); ks.load(ksbufin,
		 * p_eds.toCharArray()); KeyStore.ProtectionParameter protParam = new
		 * KeyStore.PasswordProtection(p_eds.toCharArray()); Enumeration<String>
		 * aliases = ks.aliases(); String alias = ""; while
		 * (aliases.hasMoreElements()) { alias = aliases.nextElement(); } if
		 * (!"".equalsIgnoreCase(alias)) { //KeyStore.PrivateKeyEntry entry =
		 * (KeyStore.PrivateKeyEntry)ks.getEntry(alias, protParam);
		 * //Certificate cert = entry.getCertificate(); Certificate cert =
		 * ks.getCertificate(alias); publicKey =
		 * Base64.encodeBase64String(cert.getEncoded());
		 * setPublicKey(publicKey); } } catch (IOException e) {
		 * e.printStackTrace(); } catch (NoSuchAlgorithmException e) {
		 * e.printStackTrace(); } catch (KeyStoreException e) {
		 * e.printStackTrace(); } catch (CertificateEncodingException e) {
		 * e.printStackTrace(); } catch (CertificateException e) {
		 * e.printStackTrace(); } catch (NoSuchProviderException e) {
		 * e.printStackTrace(); } } } }
		 * 
		 * 
		 * } this.appUser.setReplacers(fields);
		 */
	}

	public boolean save() throws InstantiationException, IllegalAccessException, ClassNotFoundException,
			DatabasePoolException, SQLException {
		int result = 0;
		if (docID == 0) {
			result = sysDatabase.insert(this);
		} else {
			result = sysDatabase.update(this);
		}

		if (result < 0) {
			return false;
		} else {
			docID = result;
			for (ApplicationProfile appProfile : enabledApps.values()) {
				IApplicationDatabase appDb = sysDatabase.getApplicationDatabase();
				int res = appDb.createDatabase(appProfile.dbHost, appProfile.getDbName(), appProfile.owner,
						appProfile.dbPwd);
				if (res == 0 || res == 1) {
					Class cls = Class.forName(appProfile.getImpl());
					IDatabase dataBase = (IDatabase) cls.newInstance();
					IDeployer ad = dataBase.getDeployer();
					ad.init(appProfile);
					ad.deploy();
				} else {
					return false;
				}
			}
		}
		return true;

	}

	public String toString() {
		return "userID=" + userID + ", email=" + email;
	}

	public String toXML() {
		return "<userid>" + userID + "</userid>";
	}

	public String usersByKeytoXML() {
		return "<userid>" + userID + "</userid>" + "<key>" + docID + "</key>" + "<email>" + email + "</email>";

	}

	public void setSession(UserSession session) {
		this.session = session;
	}

	public UserSession getSession() {
		return session;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	public Date getPrimaryRegDate() {
		return primaryRegDate;
	}

	public Date getRegDate() {
		if (regDate == null) regDate = new Date();
		return regDate;
	}

	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}

	public UserStatusType getStatus() {
		return status;
	}

	public void setStatus(UserStatusType status) {
		this.status = status;
	}
}
