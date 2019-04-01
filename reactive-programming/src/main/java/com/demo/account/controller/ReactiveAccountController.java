package com.demo.account.controller;

import java.time.Duration;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demo.account.common.DataException;
import com.demo.account.common.Versions;
import com.demo.account.document.Account;
import com.demo.account.document.Response_VO;
import com.demo.account.repo.AccountRepo;
import com.demo.account.service.AccountService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/app/", produces = Versions.V1_0)

public class ReactiveAccountController {

	@Inject
	private AccountService cbsService;

	@Inject
	private AccountRepo accountRepo;

	private static final Logger LOGGER = LoggerFactory.getLogger(ReactiveAccountController.class);

	@RequestMapping(value = "/listallacc", method = RequestMethod.GET)
	public Flux<Account> listacc() throws DataException {
		LOGGER.info("list all account with status A");
		return cbsService.getCbsAccount();
	}

	@RequestMapping(value = "/fetchacc", method = RequestMethod.GET)
	public Mono<Account> fetchacc(@RequestParam String accNo) throws DataException {

		return cbsService.getAccount(accNo);
	}

	@RequestMapping(value = "/hotlistallacc", method = RequestMethod.GET, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Account> livelistacc() throws DataException {
		LOGGER.info("stream list all account");

		return accountRepo.findWithTailableCursorBy().delayElements(Duration.ofMillis(2500));
	}

	@RequestMapping(value = "/regacc", method = RequestMethod.POST)
	public Mono<Response_VO> saveacc(@RequestBody Account cbsAccount) throws DataException {
		LOGGER.info("Save account [{}]", cbsAccount);
		Response_VO response = new Response_VO("11", "Failed");
		if (cbsService.saveCbsAccount(cbsAccount) != null) {
			response.setResponseCode("00");
			response.setResponseMsg("SUCCESS !");
		}
		return Mono.just(response);
	}

}
