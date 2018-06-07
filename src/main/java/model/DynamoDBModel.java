package model;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "Microservices-Lab")
public class DynamoDBModel {

    private String Email;
	private String Password;
	
	@DynamoDBHashKey(attributeName="Email")
	public String getEmail() {
		return Email;
	}
	public void setEmail(String Email) {
		this.Email = Email;
	}
	
	
	@DynamoDBRangeKey(attributeName="Password") 
	public String getPassword() {
		return Password;
	}
	public void setPassword(String Password) {
		this.Password = Password;
	}
	
	
	
	
	
    
   
    
  
    
   	
}
