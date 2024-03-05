package com.hh.kcs.tests;

import com.hh.kcs.common.BaseIntegrationTest;
import com.hh.kcs.models.MessageModel;
import com.hh.kcs.repositories.MessageRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CouchbaseDocumentTest extends BaseIntegrationTest {

    @Autowired
    private MessageRepository messageRepository;
    @Test
    public void testCouchbaseDocument(){
        MessageModel messageModel=new MessageModel();
        String id="couchbaseId";
        messageModel.setId(id);
        messageModel.setMessage("Hello Couchbase document");
        messageRepository.save(messageModel);
        MessageModel messageModel1= messageRepository.findById(id).get();
        Assertions.assertEquals(messageModel1.getMessage(),messageModel.getMessage());
    }
}
