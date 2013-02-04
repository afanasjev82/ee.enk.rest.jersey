package ee.enk.rest.jersey.auth;

import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;

public class CredentialStorage extends Hashtable<String, String>{

	private static final long serialVersionUID = 8075960087998404310L;

	public CredentialStorage() {
		super();
		Properties prop = new Properties();
	    InputStream in = this.getClass().getResourceAsStream("/cred.properties");
	    try {
			prop.load(in);
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
		Iterator<Object> it = prop.keySet().iterator();
		while(it.hasNext()){
			String username = (String) it.next();
			this.put(username,prop.getProperty(username));	
		}

		System.out.println("CredentialStorage initialized.");
	}

}
