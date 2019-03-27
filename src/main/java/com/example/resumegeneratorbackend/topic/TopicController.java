package com.example.resumegeneratorbackend.topic;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TopicController {
	@Autowired
	private TopicService TocicService;
	
	@RequestMapping("/topics")
	public List<Topic> getAllTopics(){
		return TocicService.getAllTopics();
	}
	@RequestMapping("/topics/{id}")
	public Topic getTopic(@PathVariable String id) {
		
		return TocicService.getTopic(id);
	}
	@RequestMapping(method=RequestMethod.POST,  value="/topics")
	public void addtopic(@RequestBody Topic topic) {
		TocicService.addTopic(topic);
		
	}
	@RequestMapping(method=RequestMethod.PUT,  value="/topics/{id}")
	public void uppdatetopic(@RequestBody Topic topic, @PathVariable String id) {
		TocicService.uppdateTopic(id,topic);
		
	}
	@RequestMapping(method=RequestMethod.DELETE,  value="/topics/{id}")
	public void deleteTopic(@PathVariable String id) {
		TocicService.deleteTopic(id);
		
		
		
	}

}
