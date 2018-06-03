package com.stringsai.feedmanagement.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.stringsai.feedmanagement.data.SignRequest;
import com.stringsai.feedmanagement.data.User;

import reactor.core.publisher.Mono;

@Repository
public class UserRepository {
	
	private static final String FOLLOWING = "following";
	private static final String USERS_COLLECTION = "users";
	
	@Autowired
	private ReactiveMongoTemplate mongoTemplate;

	public Mono<SignRequest> save(Mono<SignRequest> r){
		return mongoTemplate.save(r);
	}
	
	public Mono<SignRequest> getByCredentials(Mono<SignRequest> req){
		return req.flatMap(r->{return mongoTemplate.find(Query.query(Criteria.where("_id").is(r.getUsername()).and("password").is(r.getPassword())), SignRequest.class).single();});
	}
	
	public Mono<User> update(Mono<User> user){
		return user.map(u->{
			Update update = new Update().set(FOLLOWING, u.getFollowing());
			Query query = Query.query(Criteria.where("_id").is(u.getUsername()));
			mongoTemplate.updateFirst(query, update, USERS_COLLECTION).subscribe();
			return u;
		});
	}
}
