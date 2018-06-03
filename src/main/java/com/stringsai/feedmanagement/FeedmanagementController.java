package com.stringsai.feedmanagement;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.WebSession;

import com.stringsai.feedmanagement.data.Feed;
import com.stringsai.feedmanagement.data.SignRequest;
import com.stringsai.feedmanagement.data.User;
import com.stringsai.feedmanagement.service.FeedmanagementService;

import reactor.core.publisher.Mono;

@RestController
public class FeedmanagementController {
	
	private static final Log LOG = LogFactory.getLog(FeedmanagementController.class);
	private static final String USER = "user";
	
	@Autowired
	private FeedmanagementService service;
	
	@PostMapping("fms/user/add")
	public Mono<ResponseEntity<User>> signUp(@RequestBody SignRequest r, WebSession session){
		return service.save(Mono.just(r))
				.map(u->{
					User user = session.getAttribute(USER);
					if(user == null) {
						session.getAttributes().put(USER, u);
					}else {
						LOG.error(u+" is signed in. Sign out first.");
						throw new Error("Someone is already signed in. signout first.");
					}
					LOG.info(u +" signed up successfully.");
					return u;
				})
				.map(u->{return new ResponseEntity<>(u, HttpStatus.ACCEPTED);})
				.timeout(Duration.ofMillis(5000), Mono.just(new ResponseEntity<User>(HttpStatus.REQUEST_TIMEOUT)))
				.onErrorReturn(new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE));
	}
	
	@PostMapping("fms/signin")
	public Mono<ResponseEntity<User>> signIn(@RequestBody SignRequest r, WebSession session){
		return service.getByCredentials(Mono.just(r))
				.map(u->{
					User user = session.getAttribute(USER);
					if(user == null) {
						session.getAttributes().put(USER, u);
					}else {
						LOG.error(u+" is signed in. Sign out first.");
						throw new Error("Someone is already signed in");
					}
					LOG.info(u +" signed in successfully.");
					return u;
				})
				.map(u->{return new ResponseEntity<>(u, HttpStatus.ACCEPTED);})
				.timeout(Duration.ofMillis(5000), Mono.just(new ResponseEntity<User>(HttpStatus.REQUEST_TIMEOUT)))
				.onErrorReturn(new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE));
	}
	
	@PostMapping("fms/signout")
	public Mono<ResponseEntity<String>> signOut(WebSession session){
		return Mono.just(session.getAttributes().remove(USER) != null ? new ResponseEntity<String>("you have been logged out seccessfully", HttpStatus.OK) : new ResponseEntity<String>("seems like no one is signed in", HttpStatus.NOT_ACCEPTABLE));
	}
	
	@PostMapping("fms/user/follow")
	public Mono<ResponseEntity<String>> follow(@RequestBody User followUser, WebSession session){
		return service.updateUser(Mono.just(followUser).map(f->{
			User u = session.getAttribute(USER);
			if(u == null) {
				LOG.error("Not signed in. signin first.");
				throw new Error("Not signed in. signin first.");
			}
			LOG.info(u+" is now following "+f);
			u.addFollowing(f.getUsername());
			return u;
		}))
		.map(u->{return new ResponseEntity<String>("Successfully following "+followUser, HttpStatus.OK);})
		.onErrorReturn(new ResponseEntity<String>("Unable to follow. Contact admin.", HttpStatus.NOT_ACCEPTABLE));
	}
	
	@PostMapping("fms/post")
	public Mono<ResponseEntity<String>> postFeed(@RequestBody String feed, WebSession session){
		return service.saveFeed(Mono.just(feed).map(f->{
			Feed uf = new Feed(f);
			User user = session.getAttribute(USER);
			if(user == null) {
				LOG.error("Not signed in. signin first.");
				throw new Error("Not signed in. signin first.");
			}
			uf.setPostedBy(user.getUsername());
			return uf;
		}))
		.map(f->{return new ResponseEntity<String>("Successfully posted", HttpStatus.OK);})
		.onErrorReturn(new ResponseEntity<String>("Unable to post. Contact admin.", HttpStatus.NOT_ACCEPTABLE));
	}
	
	@GetMapping("fms/home")
	public Mono<ResponseEntity<List<Feed>>> getFeeds(WebSession session){
		User user = session.getAttribute(USER);
		if(user == null) {
			LOG.error("Not signed in. signin first.");
			return Mono.just(new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE));
		}
		LOG.info("fetching feed for "+user);
		return service.getByPosedUser(user.getFollowing()).collectList().map(l->{
			Collections.sort(l, service.BUZZWORD_COMPARATOR);
			return l;
		})
		.map(feeds->{return new ResponseEntity<>(feeds, HttpStatus.OK);});
	}

}
