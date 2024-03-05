package com.hh.kcs.repositories;

import com.hh.kcs.models.MessageModel;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepository extends CrudRepository<MessageModel,String> {
}
