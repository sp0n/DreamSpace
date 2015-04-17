package controllers;

import play.*;
import play.mvc.*;
import java.sql.*;
import views.html.*;
import play.db.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("WEBSITE IS A GO"));
    }
	
	public static Result helloWeb() {
	
		ObjectNode result = Json.newObject();

		result.put("content", "Hello Web");

		return ok(result);
	}
	
	
	public static Result getNameAge() {
		
		ObjectNode result = Json.newObject();
		Connection conn = null;
		Statement stmt = null;
		
	    try{
		
			conn = DB.getConnection();
			stmt = conn.createStatement();
		
		
			String sql = "SELECT * FROM test";
			
			ResultSet rs = stmt.executeQuery(sql);
		
		    while(rs.next()){
				//Retrieve by column name

				String name  = rs.getString("name");
				int age = rs.getInt("age");				
				ObjectNode test = Json.newObject();
				test.put("Name", name);
				test.put("Age", age);
				
				
				result.put(name, age);
		   	}
		    rs.close();

			return ok(result);
		}catch(SQLException se){
			//Handle errors for JDBC
	        return internalServerError(se.toString());
		}catch(Exception e){
	    	//Handle errors for Class.forName
	        return internalServerError(e.toString());
	 	}finally{
			 //finally block used to close resources
			 try{
			    if(stmt!=null)
			       conn.close();
			 }catch(SQLException se){
			 }// do nothing
			 try{
			    if(conn!=null)
			       conn.close();
			 }catch(SQLException se){
			    return internalServerError(se.toString());
			 }//end finally try
	   	}//end try

	}
}
