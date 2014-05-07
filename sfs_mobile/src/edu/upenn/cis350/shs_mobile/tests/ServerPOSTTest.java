package edu.upenn.cis350.shs_mobile.tests;

//import static org.junit.Assert.*;

//import org.junit.Before;
//import org.junit.Test;

import edu.upenn.cis350.shs_mobile.ServerPOST;

public class ServerPOSTTest {

	
	//@Test
	public void testLogin() {
		ServerPOST post = new ServerPOST("auth.php");
		post.addField("pennkey", "molisani");
		post.addField("password", "mpassword");
		post.addField("birthday", "1994-01-31");
		System.out.println(post.execute().toString());
	}
	
	

}
