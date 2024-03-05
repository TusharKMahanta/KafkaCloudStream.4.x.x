package com.hh.kcs.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.couchbase.core.mapping.Document;

@Document
@Data
@NoArgsConstructor
public class MessageModel {
    @Id
    private String id;
    @Version
    private  Long documentVersion;
    private String message;

}
