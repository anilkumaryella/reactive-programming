package com.demo.account.service;

import java.time.Duration;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.demo.account.common.DataException;
import com.demo.account.document.Account;
import com.demo.account.repo.AccountRepo;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AccountService {

	@Inject
	private AccountRepo cbsAccountRepo;

	private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);

	public Mono<Account> getAccount(String accNo) {
		return cbsAccountRepo.findOneByAccountNo(accNo);
	}

	public Flux<Account> getAllAccounts() throws DataException {

		return cbsAccountRepo.findWithTailableCursorBy().delayElements(Duration.ofMillis(2500));
	}

	public Flux<Account> getCbsAccount() throws DataException {

		try {
			Flux<Account> list = cbsAccountRepo.findAll();
			LOGGER.info("list of accounts from DB ..[{}]", list);
			if (list == null) {
				throw new DataException("01", "NO Account Exists !");
			}

			return list;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			if (e instanceof DataException) {
				throw e;
			}
			throw new DataException("11", "FAILURE");
		}

	}

	public Mono<Account> saveCbsAccount(Account cbsAccount) {
		Mono<Account> acc = cbsAccountRepo.save(cbsAccount);
		acc.block();
		return acc;
	}

}
