package com.i3market.semanticengine.service.impl;

import com.i3market.semanticengine.common.domain.CategoriesList;
import com.i3market.semanticengine.common.domain.request.RequestNewDataOffering;
import com.i3market.semanticengine.common.domain.response.DataOfferingDto;
import com.i3market.semanticengine.exception.InvalidInputException;
import com.i3market.semanticengine.exception.NotFoundException;
import com.i3market.semanticengine.mapper.Mapper;
import com.i3market.semanticengine.repository.DataOfferingRepository;
import com.i3market.semanticengine.service.DataOfferingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

@Log4j2
@Service
@RequiredArgsConstructor
public class DataOfferingServiceImpl implements DataOfferingService {

    @Value("${category-list.url}")
    private String categoryListUrl;

    private final DataOfferingRepository dataOfferingRepository;

    private final Mapper mapper;

    private final WebClient.Builder webClient;

    private final SimpleDateFormat simpleFormat = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss");

    @Override
    public Flux<CategoriesList> getCategoryList() {
        return webClient.build().get().uri(categoryListUrl).retrieve()
                .bodyToFlux(CategoriesList.class)
                .log(log.getName(), Level.FINE);
    }


    @Override
    public Mono<DataOfferingDto> createDataOffering(final RequestNewDataOffering dto) {

        final var entity = mapper.dtoToEntity(dto);
        entity.setCreatedAt(simpleFormat.format(new Date()));
        final var newEntity = dataOfferingRepository.save(entity);
        return newEntity.log(log.getName(), Level.FINE)
                .onErrorMap(RuntimeException.class,
                        err -> new InvalidInputException(HttpStatus.BAD_REQUEST, "Error"))
                .map(e -> mapper.entityToDto(e));
    }

    @Override
    public Mono<Void> deleteAllOffering() {
        return dataOfferingRepository.deleteAll();
    }

    @Override
    public Mono<DataOfferingDto> getDataOfferingById(final String id) {
        final var entity = dataOfferingRepository.findById(id);
        return entity.log(log.getName(), Level.FINE)
                .switchIfEmpty(Mono.error(new NotFoundException(HttpStatus.NOT_FOUND, "No data offering found for id : " + id)))
                .map(e -> mapper.entityToDto(e));
    }
}
