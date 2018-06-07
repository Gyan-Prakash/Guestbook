package controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import com.amazonaws.services.dynamodbv2.util.TableUtils.TableNeverTransitionedToStateException;
import config.AWSConfig;
import model.DynamoDBModel;

@Controller
public class DynamoDBController {
	
	AmazonDynamoDB dynamoDBClient;
	DynamoDB dynamoDB;

	String tableName = "Microservices-Lab";//"<Enter the DynamoDb Table Name >";

	@RequestMapping("/")
	public String person(Model model)
	{
		DynamoDBModel t = new DynamoDBModel();
	
		model.addAttribute("DynamoDBModel", t);
		return "Login-Signup";	
	}
	
	@RequestMapping(value="/registerform",method = RequestMethod.POST)
	public String registerUse(@ModelAttribute DynamoDBModel foo)
	{
		
		//Implement  Dynamo Db Table creation  code if table doesn't exist
		//If table exist then use the same table.
		
		AWSConfig dynamoDBConfigObject = new AWSConfig();
		dynamoDBClient = dynamoDBConfigObject.amazonDynamoDB();
		dynamoDB = new DynamoDB(dynamoDBClient);
	
		//TODO Auto-generated catch block
			
			 CreateTableRequest createTableRequest = new CreateTableRequest().withTableName(tableName)
		                .withKeySchema(new KeySchemaElement().withAttributeName("Email").withKeyType(KeyType.HASH))
		                .withKeySchema(new KeySchemaElement().withAttributeName("Password").withKeyType(KeyType.RANGE))
		                .withAttributeDefinitions(new AttributeDefinition().withAttributeName("Email").withAttributeType(ScalarAttributeType.S))
		                .withAttributeDefinitions(new AttributeDefinition().withAttributeName("Password").withAttributeType(ScalarAttributeType.S))
		                .withProvisionedThroughput(new ProvisionedThroughput().withReadCapacityUnits(1L).withWriteCapacityUnits(1L));
			 
				TableUtils.createTableIfNotExists(dynamoDBClient, createTableRequest);
			
				try {
					TableUtils.waitUntilActive(dynamoDBClient, tableName);
				} catch (TableNeverTransitionedToStateException | InterruptedException e) {
					// TODO Auto-generated catch block
					System.out.println("Table is not ready for use! "+foo.getEmail()+"  pass :  " +foo.getPassword());
					e.printStackTrace();
				}
				
				System.out.println("Table is ready for use! "+foo.getEmail()+"  pass :  " +foo.getPassword());
				insertRecord(foo.getEmail(),foo.getPassword());
		
		return "S3upload";		
		
	}
	@RequestMapping(value="/loginForm",method = RequestMethod.POST)
	public String login(@ModelAttribute DynamoDBModel dynamoDBModel )
	{
		
		//Implement  Dynamo Db Table creation  code if table doesn't exist
		//If table exist then use the same table.
		
		AWSConfig dynamoDBConfigObject = new AWSConfig();
		dynamoDBClient = dynamoDBConfigObject.amazonDynamoDB();
		dynamoDB = new DynamoDB(dynamoDBClient);
		
		DynamoDBModel email = new DynamoDBModel();
		DynamoDBMapper mapper = new DynamoDBMapper(dynamoDBClient);
		email.setEmail(dynamoDBModel.getEmail());
		
		DynamoDBQueryExpression<DynamoDBModel> queryExpression = new DynamoDBQueryExpression<DynamoDBModel>()
		    .withHashKeyValues(email);

		List<DynamoDBModel> itemList = mapper.query(DynamoDBModel.class, queryExpression);
		if(itemList.get(0).getEmail().contains(dynamoDBModel.getEmail()))
		{
			System.out.println(itemList.get(0).getEmail());
			if(itemList.get(0).getPassword().contains(dynamoDBModel.getPassword()))
			{
				return "S3upload";
			}
			else
			{
				return "LoginError";
			}
			
		}
		else
		{
			return "LoginError";
		}
			 		
	}


	public String insertRecord(String name, String pwd)
	{
		 DynamoDBMapper mapper = new DynamoDBMapper(dynamoDBClient);

	        DynamoDBModel DynamoDBModelitem = new DynamoDBModel();
	        DynamoDBModelitem.setEmail(name);
	        
	        DynamoDBModelitem.setPassword(pwd);
	        mapper.save(DynamoDBModelitem);
	                
	        String ans = DynamoDBModelitem.getEmail() + DynamoDBModelitem.getPassword() ;
	        return ans;	
	}

}

