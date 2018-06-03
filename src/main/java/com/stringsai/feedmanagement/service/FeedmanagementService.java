package com.stringsai.feedmanagement.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.synchronoss.cloud.nio.multipart.util.IOUtils;

import com.stringsai.feedmanagement.data.Feed;
import com.stringsai.feedmanagement.data.SignRequest;
import com.stringsai.feedmanagement.data.User;
import com.stringsai.feedmanagement.db.FeedRepository;
import com.stringsai.feedmanagement.db.UserRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class FeedmanagementService {
	
	private static final String BUZZFILE_NAME = "buzzwords";
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private FeedRepository feedRepository;
	static Set<String> buzzwords;
	public BuzzWordComparator BUZZWORD_COMPARATOR = new BuzzWordComparator();

	public FeedmanagementService() {
		loadBuzzwords();
	}
	
	public Mono<User> save(Mono<SignRequest> request){
		return userRepository.save(request).map(r->{return r.convertToUser();});
	}
	
	public Mono<User> getByCredentials(Mono<SignRequest> request){
		return userRepository.getByCredentials(request).map(r->{return r.convertToUser();});
	}
	
	public Mono<User> updateUser(Mono<User> user){
		return userRepository.update(user);
	}
	
	public Mono<Feed> saveFeed(Mono<Feed> feed){
		return feedRepository.save(feed);
	}
	
	public Flux<Feed> getByPosedUser(Set<String> users){
		return feedRepository.getByPosedUser(users);
	}
	
	private void loadBuzzwords() {
		try {
			FeedmanagementService.buzzwords = new HashSet<>();
			InputStream in = this.getClass().getClassLoader().getResourceAsStream(BUZZFILE_NAME);
			String st = IOUtils.inputStreamAsString(in, "UTF-8");
			Collections.addAll(FeedmanagementService.buzzwords, st.split(","));
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addBuzzword(String buzzword) throws Exception {
		URL url = this.getClass().getClassLoader().getResource(BUZZFILE_NAME);
		BufferedWriter writer = new BufferedWriter(new FileWriter(url.getFile()));
	    writer.write(","+buzzword);
	    writer.close();
	    FeedmanagementService.buzzwords.add(buzzword);
	}
	
	class BuzzWordComparator implements Comparator<Feed>{

		@Override
		public int compare(Feed o1, Feed o2) {
			int buzz1=0; int buzz2 = 0;
			for(String s :FeedmanagementService.buzzwords) {
				buzz1 += o1.getMessage().trim().contains(s.trim()) ? 1 : 0;
				buzz2 += o2.getMessage().trim().contains(s.trim()) ? 1 : 0;
			}
			return buzz1 > buzz2 ? -1 : (buzz1 == buzz2 ? 0 : 1);
		}
		
	}

}
