package com.vivanet.sudents;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.vivanet.sudents.bean.StudentInfo;
import com.vivanet.sudents.bean.StudentResponse;

public class StudentInfoHandler implements RequestHandler<StudentInfo, StudentResponse> {

    private DynamoDB dynamoDb;
    private String DYNAMODB_TABLE_NAME = "StudentInfo";
    private Regions REGION = Regions.US_EAST_1;

    public StudentResponse handleRequest(StudentInfo studentInfo, Context context) {
 
        this.initDynamoDbClient();

        persistData(studentInfo);

        StudentResponse studentResponse = new StudentResponse();
        studentResponse.setMessage("Saved Successfully!!!");
        return studentResponse;
    }

    private void initDynamoDbClient() {
        AmazonDynamoDBClient client = new AmazonDynamoDBClient();
        client.setRegion(Region.getRegion(REGION));
        this.dynamoDb = new DynamoDB(client);
    }

    private PutItemOutcome persistData(StudentInfo studentInfo) throws ConditionalCheckFailedException {

        return this.dynamoDb.getTable(DYNAMODB_TABLE_NAME)
          .putItem(
            new PutItemSpec().withItem(new Item()
            .withString("studentName", studentInfo.getStudentName())
            .withString("studentEmail", studentInfo.getStudentEmail())            
            ));
              
    }
}