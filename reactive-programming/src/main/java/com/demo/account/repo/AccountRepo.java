package com.demo.account.repo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import org.springframework.stereotype.Repository;

import com.demo.account.document.Account;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface AccountRepo extends ReactiveMongoRepository<Account, Integer> {

	@Tailable
	Flux<Account> findWithTailableCursorBy();

	Mono<Account> findOneByAccountNo(String accNumber);

}
