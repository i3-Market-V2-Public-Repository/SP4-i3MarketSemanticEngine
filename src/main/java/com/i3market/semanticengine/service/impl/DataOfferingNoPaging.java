package com.i3market.semanticengine.service.impl;

import com.i3market.semanticengine.common.domain.CategoriesList;
import com.i3market.semanticengine.common.domain.entity.DataOffering;
import com.i3market.semanticengine.common.domain.response.DataOfferingDto;
import com.i3market.semanticengine.common.domain.response.OfferingIdResponse;
import com.i3market.semanticengine.common.domain.response.ProviderIdResponse;
import com.i3market.semanticengine.exception.NotFoundException;
import com.i3market.semanticengine.mapper.Mapper;
import com.i3market.semanticengine.repository.DataOfferingRepository;
import com.i3market.semanticengine.repository.DataProviderRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.logging.Level;

@Service
@Log4j2
public class DataOfferingNoPaging {
    private DataOfferingRepository dataOfferingRepository;

    private  DataProviderRepository dataProviderRepository;

    private  Mapper mapper;

    private  ReactiveMongoTemplate mongoTemplate2;

    @Autowired
    public DataOfferingNoPaging(DataOfferingRepository dataOfferingRepository, DataProviderRepository dataProviderRepository, Mapper mapper, ReactiveMongoTemplate mongoTemplate2) {
        this.dataOfferingRepository = dataOfferingRepository;
        this.dataProviderRepository = dataProviderRepository;
        this.mapper = mapper;
        this.mongoTemplate2 = mongoTemplate2;
    }

    // list
    public Flux<OfferingIdResponse> getOfferingList() {
        return dataOfferingRepository.findAll()
                .log(log.getName(), Level.FINE)
                .map(mapper::entityToDto)
//                .skip(page * size).take(size)
                .sort(compareOfferingTime.reversed())
                .switchIfEmpty(Mono.error(new NotFoundException(HttpStatus.NOT_FOUND, "We are sorry! No data offering found.")))
                .map(e -> OfferingIdResponse.builder().offering(e.getDataOfferingId()).build());
    }

    // active list
    public Flux<OfferingIdResponse> getOfferingListonActive() {
        return dataOfferingRepository.findAll()
                .log(log.getName(), Level.FINE)
                .map(mapper::entityToDto)
                .filter(e-> e.isActive() == true)
//                .skip(page * size).take(size)
                .sort(compareOfferingTime.reversed())
                .switchIfEmpty(Mono.error(new NotFoundException(HttpStatus.NOT_FOUND, "We are sorry! No data offering found.")))
                .map(e -> OfferingIdResponse.builder().offering(e.getDataOfferingId()).build());
    }
    // by provider id

    public Flux<DataOfferingDto> getOfferingByProvider(final String provider) {
        return dataOfferingRepository.findByProvider(provider.toLowerCase())
                .map(mapper::entityToDto)
                .sort(compareOfferingTime.reversed())
                .sort(compareOfferingTitle)
//                .skip(page * size).take(size)
                .log(log.getName(), Level.FINE)
                .switchIfEmpty(Mono.error(new NotFoundException(HttpStatus.NOT_FOUND, "Were are sorry! No data offering found for provider: " + provider)));
    }
// by category
    public Flux<DataOfferingDto> getOfferingByCategory(final String category) {
        return dataOfferingRepository.findByCategory(category.strip().toLowerCase())
                .map(mapper::entityToDto)
                .sort(compareOfferingTime.reversed())
                .sort(compareOfferingTitle)
//                .skip(page * size).take(size)
                .log(log.getName(), Level.FINE)
                .switchIfEmpty(Mono.error(new NotFoundException(HttpStatus.NOT_FOUND, "Were are sorry! No data offering found for category: " + category)));
    }
    //provider by category

    public Flux<ProviderIdResponse> getProviderListByCategory(final String category) {
        return dataOfferingRepository.findByCategory(category)
//                .skip(page * size).take(size)
                .map(mapper::entityToDto)
                .sort(compareByProvider).map(mapper::providerIdDto);
    }

    // active by category
    public Flux<DataOfferingDto> getOfferingsByActiveAndCategory(final String category){
        return  dataOfferingRepository.findByCategoryAndActiveTrue(category.toLowerCase())
                .map(mapper::entityToDto)
                .sort(compareOfferingTime.reversed())
                .sort(compareOfferingTitle)
//                .skip(page * size).take(size)
                .log(log.getName(), Level.FINE)
                .switchIfEmpty(Mono.error(new NotFoundException(HttpStatus.NOT_FOUND, "Were are sorry! No data offering found for category: " + category)));
    }

    //active by provider
    public Flux<DataOfferingDto> getOfferingsByActiveAndProvider(final String provider ){
        return  dataOfferingRepository.findByProviderAndActiveTrue(provider.toLowerCase())
                .map(mapper::entityToDto)
                .sort(compareOfferingTime.reversed())
                .sort(compareOfferingTitle)
//                .skip(page * size).take(size)
                .log(log.getName(), Level.FINE)
                .switchIfEmpty(Mono.error(new NotFoundException(HttpStatus.NOT_FOUND, "Were are sorry! No data offering found for provider: " + provider)));
    }


    // text search
    public Flux<DataOfferingDto> getTextSearchReact(String text  ){
        TextCriteria textCriteria = TextCriteria.forDefaultLanguage()
                .matchingAny(text);
        Query query = TextQuery.queryText(textCriteria)
                .sortByScore();
        final Flux<DataOffering> data_offering_t = mongoTemplate2.find(query, DataOffering.class);
        return  data_offering_t.map(e-> mapper.entityToDto(e))
//                .skip(page*size)
//                .take(size)
                .switchIfEmpty(Mono.error(new NotFoundException(HttpStatus.NOT_FOUND, "Sorry! no offering found with keyword: " + text)));


    }
    // active text search

    public Flux<DataOfferingDto> getActiveTextSearch(String text ){
        TextCriteria textCriteria = TextCriteria.forDefaultLanguage()
                .matchingAny(text);
        Query query = TextQuery.queryText(textCriteria)
                .sortByScore().addCriteria(Criteria.where("active").is(true));
        final Flux<DataOffering> data_offering_t = mongoTemplate2.find(query, DataOffering.class);
        return  data_offering_t.map(e-> mapper.entityToDto(e))
//                .skip(page*size)
//                .take(size)
                .switchIfEmpty(Mono.error(new NotFoundException(HttpStatus.NOT_FOUND, "Sorry! no offering found with keyword: " + text)));
    }
// on shared network
    public Flux<OfferingIdResponse> getOfferingListonSharedNetwork() {
        return dataOfferingRepository.findAll()
                .log(log.getName(), Level.FINE)
                .map(mapper::entityToDto)
                .filter(e-> e.isInSharedNetwork()==true)
//                .skip(page * size).take(size)
                .sort(compareOfferingTime.reversed())
                .switchIfEmpty(Mono.error(new NotFoundException(HttpStatus.NOT_FOUND, "We are sorry! No data offering found.")))
                .map(e -> OfferingIdResponse.builder().offering(e.getDataOfferingId()).build());
    }
    //get offering by active and provider





    final Comparator<CategoriesList> comparingCategoriesList = Comparator.comparing(e-> e.getName());
    final Comparator<DataOfferingDto> compareOfferingTime = Comparator.comparing(DataOfferingDto::getCreatedAt);
    final Comparator<DataOfferingDto> compareOfferingTitle = Comparator.comparing(DataOfferingDto::getDataOfferingTitle);
    final Comparator<DataOfferingDto> compareByProvider = Comparator.comparing(DataOfferingDto::getProvider);
}
