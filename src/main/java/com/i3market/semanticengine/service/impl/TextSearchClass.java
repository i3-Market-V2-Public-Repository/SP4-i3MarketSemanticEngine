package com.i3market.semanticengine.service.impl;

import com.i3market.semanticengine.common.domain.entity.DataOffering;
import com.i3market.semanticengine.common.domain.response.DataOfferingDto;
import com.i3market.semanticengine.exception.NotFoundException;
import com.i3market.semanticengine.mapper.Mapper;
import com.i3market.semanticengine.repository.DataOfferingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 */

@Service
public class TextSearchClass {
    private DataOfferingRepository dataOfferingRepository;
    @Autowired
    private final ReactiveMongoTemplate mongoTemplate2;

    private final Mapper mapper;

    public TextSearchClass(DataOfferingRepository dataOfferingRepository, ReactiveMongoTemplate mongoTemplate2, Mapper mapper) {
        this.dataOfferingRepository = dataOfferingRepository;
        this.mongoTemplate2 = mongoTemplate2;
        this.mapper = mapper;
    }


    public Flux<DataOfferingDto> getTextSearchReact(String text , int page , int size ){
        TextCriteria textCriteria = TextCriteria.forDefaultLanguage()
                .matchingAny(text);
        Query query = TextQuery.queryText(textCriteria)
                .sortByScore();
        final Flux<DataOffering> data_offering_t = mongoTemplate2.find(query, DataOffering.class);
       return  data_offering_t.map(e-> mapper.entityToDto(e))
               .skip(page*size)
               .take(size)
               .switchIfEmpty(Mono.error(new NotFoundException(HttpStatus.NOT_FOUND, "Sorry! no offering found with keyword: " + text)));


    }

}
