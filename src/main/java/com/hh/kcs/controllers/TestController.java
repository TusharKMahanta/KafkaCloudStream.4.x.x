package com.hh.kcs.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/test")
public class TestController {
    @RequestMapping(method = RequestMethod.GET,value = "/{message}")
    public @ResponseBody ResponseEntity<Map<String,String>> test(@PathVariable String message){
        Map<String,String> responseBody=new HashMap<>();
        responseBody.put("message","Hello World:: "+ message);
        ResponseEntity<Map<String,String>> response=new ResponseEntity<>(responseBody, HttpStatus.ACCEPTED);
        return response;
    }
}
