package com.i3market.semanticengine.service.impl;

import com.i3market.semanticengine.common.domain.entity.DataOffering;
import com.i3market.semanticengine.common.domain.entity.DataProvider;
import com.i3market.semanticengine.common.domain.request.RequestDataProvider;
import com.i3market.semanticengine.common.domain.response.DataProviderDto;
import com.i3market.semanticengine.common.domain.response.ProviderIdResponse;
import com.i3market.semanticengine.exception.ConflictException;
import com.i3market.semanticengine.exception.InvalidInputException;
import com.i3market.semanticengine.exception.NotFoundException;
import com.i3market.semanticengine.mapper.Mapper;
import com.i3market.semanticengine.repository.DataOfferingRepository;
import com.i3market.semanticengine.repository.DataProviderRepository;
import com.i3market.semanticengine.service.DataProviderService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Log4j2
@Service
@RequiredArgsConstructor
public class DataProviderServiceImpl implements DataProviderService {

    private final DataProviderRepository providerRepository;

    private final DataOfferingRepository dataOfferingRepository;

    private final Mapper mapper;


    private final SimpleDateFormat simpleFormat = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss");

    @Override
    public Mono<DataProviderDto> createDataProvider(final RequestDataProvider dto) {
        if (isNull(dto.getProviderId()) || isNull(dto.getName())) {
            throw new InvalidInputException(HttpStatus.BAD_REQUEST, "ProviderId and Provider name must be provided");
        }
        final DataProvider entity = mapper.requestToEntity(dto);
        entity.setCreatedAt(Instant.now());
        final Mono<DataProvider> newEntity = providerRepository.save(entity);

        return newEntity.log(log.getName(), Level.FINE)
                .onErrorMap(
                        DuplicateKeyException.class,
                        ex -> new InvalidInputException(HttpStatus.CONFLICT, "Duplicated data providerId " + dto.getProviderId()))
                .map(e -> mapper.entityToDto(e));
    }

    @Override
    public Mono<DataProviderDto> getDataProviderByProviderId(final String providerId) {
        final Mono<DataProvider> getProvider = providerRepository.findByProviderId(providerId.toLowerCase());
        return getProvider
                .switchIfEmpty(Mono.error(new NotFoundException(HttpStatus.NOT_FOUND, "Data providerId " + providerId + " does not exist")))
                .log(log.getName(), Level.FINE)
                .map(e -> mapper.entityToDto(e));
    }

    @Override
    public Mono<Void> deleteAllProvider() {
        return providerRepository.deleteAll();
    }

    @Override
    @SneakyThrows
    public Mono<Void> deleteProviderByProviderId(final String providerId) {
        final var getOfferings = dataOfferingRepository.findByProvider(providerId).take(1L).collectList().toFuture().get().size();
        if (getOfferings > 0) {
            throw new ConflictException(HttpStatus.CONFLICT, "We are sorry. You can not delete this provider as there are offerings associated with this providerId: " + providerId);
        }
        return providerRepository.findByProviderId(providerId.toLowerCase())
                .log(log.getName(), Level.FINE)
                .map(e -> providerRepository.delete(e))
                .flatMap(e -> e);
    }

    @Override
    public Mono<DataProviderDto> updatedDataProvider(final RequestDataProvider dataProviderDto) {
        final String providerId = dataProviderDto.getProviderId();
        final Mono<DataProvider> currentEntity = providerRepository.findByProviderId(providerId);
        final DataProvider updateEntity = mapper.requestToEntity(dataProviderDto);
        return currentEntity
                .switchIfEmpty(Mono.error(new NotFoundException(HttpStatus.NOT_FOUND, "Data providerId " + providerId + " does not exist")))
                .log(log.getName(), Level.FINE)
                .map(e -> providerTransform(updateEntity, e).toBuilder().updatedAt(Instant.now()).build())
                .flatMap(providerRepository::save)
                .map(e -> mapper.entityToDto(e));
    }

    @Override
    public Flux<ProviderIdResponse> providerList(final int page, final int size) {

        final Flux<ProviderIdResponse> providerRepo = providerRepository.findAll()
                .log(log.getName(), Level.FINE)
                .skip(page * size).take(size)
                .map(e -> ProviderIdResponse.builder().provider(e.getProviderId()).build())
                .switchIfEmpty(Mono.error(new NotFoundException(HttpStatus.NOT_FOUND, "Sorry! there is no provider found")));
        return providerRepo;


    }

    private DataProvider providerTransform(final DataProvider updateEntity, final DataProvider currentEntity) {
        return currentEntity.toBuilder()
                .name(updateEntity.getName())
                .organization(updateEntity.getOrganization())
                .description(updateEntity.getDescription())
                .build();
    }
}
