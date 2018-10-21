package com.business.exchange.service;

import org.springframework.stereotype.Service;

@Service
public interface BusinessService {

    String create();

    String assign();

    String ownRank();

    String inflowRank();

    String history();

}