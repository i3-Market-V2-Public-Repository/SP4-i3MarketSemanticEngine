package com.i3market.semanticengine.service.impl;

import com.i3market.semanticengine.common.domain.DataProviderDto;
import com.i3market.semanticengine.common.domain.DataProviderEntity;
import com.i3market.semanticengine.exception.InvalidInputException;
import com.i3market.semanticengine.exception.NotFoundException;
import com.i3market.semanticengine.mapper.Mapper;
import com.i3market.semanticengine.repository.DataProviderRepository;
import com.i3market.semanticengine.service.DataProviderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

import static java.util.Objects.isNull;

@Log4j2
@Service
@RequiredArgsConstructor
public class DataProviderServiceImpl implements DataProviderService {

    private final DataProviderRepository providerRepository;

    private final Mapper mapper;
    

    private final SimpleDateFormat simpleFormat = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss");

    @Override
    public Mono<DataProviderDto> createDataProvider(final DataProviderDto dto) {
        System.out.println(dto.getProviderId());
        if (isNull(dto.getProviderId()) || isNull(dto.getName())) {
            throw new InvalidInputException(HttpStatus.BAD_REQUEST, "ProviderId and Provider name must be provided");
        }
        final DataProviderEntity entity = mapper.dtoToEntity(dto);
        entity.setCreatedAt(simpleFormat.format(new Date()));
        final Mono<DataProviderEntity> newEntity = providerRepository.save(entity);

        return newEntity.log(log.getName(), Level.FINE)
                .onErrorMap(
                        DuplicateKeyException.class,
                        ex -> new InvalidInputException(HttpStatus.CONFLICT, "Duplicated data providerId " + dto.getProviderId()))
                .map(e -> mapper.entityToDto(e));
    }

    @Override
    public Mono<DataProviderDto> getDataProviderByProviderId(final String providerId) {
        final Mono<DataProviderEntity> getProvider = providerRepository.findByProviderId(providerId);

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
    public Mono<Void> deleteProviderByProviderId(final String providerId) {
        return providerRepository.findByProviderId(providerId).log(log.getName(), Level.FINE)
                .map(e -> providerRepository.delete(e)).flatMap(e -> e);
    }

    @Override
    public Mono<DataProviderDto> updatedDataProvider(final DataProviderDto dataProviderDto) {
        final String providerId = dataProviderDto.getProviderId();
        final Mono<DataProviderEntity> currentEntity = providerRepository.findByProviderId(providerId);
        final DataProviderEntity updateEntity = mapper.dtoToEntity(dataProviderDto);
        return currentEntity
                .switchIfEmpty(Mono.error(new NotFoundException(HttpStatus.NOT_FOUND, "Data providerId " + providerId + " does not exist")))
                .log(log.getName(), Level.FINE)
                .map(e -> providerTransform(updateEntity, e))
                .flatMap(providerRepository::save)
                .map(e -> mapper.entityToDto(e));
    }

    @Override
    public Flux<DataProviderDto> providerList(final int page, final int size) {
        return providerRepository.findAll()
                .log(log.getName(), Level.FINE)
                .map(e -> mapper.entityToDto(e))
                .skip(page * size).take(size);
    }


    private DataProviderEntity providerTransform(final DataProviderEntity updateEntity,
                                                 final DataProviderEntity currentEntity) {
        return currentEntity.toBuilder()
                .name(updateEntity.getName())
                .organization(updateEntity.getOrganization())
                .description(updateEntity.getDescription())
                .updatedAt(simpleFormat.format(new Date()))
                .build();
    }
}
