package com.stringsai.feedmanagement.db;

import java.util.Date;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.stringsai.feedmanagement.data.Feed;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class FeedRepository {
	
	private static final String POSTED_BY = "postedBy";
	private static final String POSTED_ON = "postedOn";
	private static final long MILLIS_IN_A_DAY = 1000*60*60*24;
	
	@Autowired
	private ReactiveMongoTemplate mongoTemplate;

	public Mono<Feed> save(Mono<Feed> feed){
		return mongoTemplate.save(feed);
	}
	
	public Flux<Feed> getByPosedUser(Set<String> users){
		Query q = Query.query(Criteria.where(POSTED_BY).in(users.toArray())).addCriteria(Criteria.where(POSTED_ON).gt(new Date().getTime()-MILLIS_IN_A_DAY)).with(Sort.by(Direction.DESC, POSTED_ON));
		return mongoTemplate.find(q, Feed.class);
	}

}
