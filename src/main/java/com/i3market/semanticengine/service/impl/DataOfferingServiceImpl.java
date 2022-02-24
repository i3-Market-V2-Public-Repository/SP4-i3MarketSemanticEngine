package com.i3market.semanticengine.service.impl;

import com.i3market.semanticengine.common.domain.CategoriesList;
import com.i3market.semanticengine.common.domain.entity.DataOffering;
import com.i3market.semanticengine.common.domain.request.RequestNewDataOffering;
import com.i3market.semanticengine.common.domain.response.*;
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
import java.util.Comparator;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.stream.Collectors;

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

        final var entity = mapper.requestToEntity(dto);
        entity.setCreatedAt(simpleFormat.format(new Date()));
        final var newEntity = dataOfferingRepository.save(entity);
        return newEntity.log(log.getName(), Level.FINE)
                .onErrorMap(RuntimeException.class,
                        err -> new InvalidInputException(HttpStatus.BAD_REQUEST, "Error"))
                .map(mapper::entityToDto);
    }

    @Override
    public Mono<Void> deleteAllOffering() {
        return dataOfferingRepository.deleteAll();
    }

    @Override
    public Mono<DataOfferingDto> getDataOfferingById(final String id) {
        final var entity = dataOfferingRepository.findById(id);
        return entity.log(log.getName(), Level.FINE)
                .switchIfEmpty(Mono.error(new NotFoundException(HttpStatus.NOT_FOUND, "We are sorry! No data offering found for id : " + id)))
                .map(mapper::entityToDto);
    }

    @Override
    public Flux<OfferingIdResponse> getOfferingList(final int page, final int size) {
        return dataOfferingRepository.findAll()
                .log(log.getName(), Level.FINE)
                .skip(page * size).take(size)
                .sort(compareOfferingTime.reversed())
                .sort(compareOfferingTitle)
                .switchIfEmpty(Mono.error(new NotFoundException(HttpStatus.NOT_FOUND, "We are sorry! No data offering found.")))
                .map(e -> OfferingIdResponse.builder().offering(e.getId()).build());
    }

    @Override
    public Mono<DataOfferingDto> updateDataOffering(final DataOfferingDto body) {
        final var currentEntity = dataOfferingRepository.findById(body.getOfferingId());
        final var updateEntity = mapper.dtoToEntity(body);
        return currentEntity.log(log.getName(), Level.FINE)
                .switchIfEmpty(Mono.error(new NotFoundException(HttpStatus.NOT_FOUND, "Were are sorry! Data Offering id" + body.getOfferingId() + " does not exist")))
                .map(e -> offeringTransform(updateEntity, e).toBuilder().updatedAt(simpleFormat.format(new Date())).build())
                .flatMap(dataOfferingRepository::save)
                .map(mapper::entityToDto);
    }

    @Override
    public Flux<DataOfferingDto> getOfferingByProvider(final String provider, final int page, final int size) {
        return dataOfferingRepository.findByProvider(provider)
                .sort(compareOfferingTime.reversed())
                .sort(compareOfferingTitle)
                .skip(page * size).take(size)
                .log(log.getName(), Level.FINE)
                .switchIfEmpty(Mono.error(new NotFoundException(HttpStatus.NOT_FOUND, "Were are sorry! No data offering found for provider: " + provider)))
                .map(mapper::entityToDto);
    }

    @Override
    public Flux<DataOfferingDto> getOfferingByCategory(final String category, final int page, final int size) {
        return dataOfferingRepository.findByCategory(category)
                .sort(compareOfferingTime.reversed())
                .sort(compareOfferingTitle)
                .skip(page * size).take(size)
                .log(log.getName(), Level.FINE)
                .switchIfEmpty(Mono.error(new NotFoundException(HttpStatus.NOT_FOUND, "Were are sorry! No data offering found for category: " + category)))
                .map(mapper::entityToDto);
    }

    @Override
    public Mono<ContractParametersResponse> getContractParameterByOfferingId(final String offeringId) {
        return dataOfferingRepository.findById(offeringId)
                .log(log.getName(), Level.FINE)
                .switchIfEmpty(Mono.error(new NotFoundException(HttpStatus.NOT_FOUND, "We are sorry! No contract parameter found:")))
                .map(mapper::contractParameterDto);
    }

    @Override
    public Flux<ProviderIdResponse> getProviderListByCategory(final String category, final int page, final int size) {
        return dataOfferingRepository.findByCategory(category).skip(page * size).take(size)
                .sort(compareByProvider)
                .map(mapper::providerIdDto);
    }

    @Override
    public Mono<TotalOfferingResponse> getOfferingByProviderIdAndCategorySorted(final String inputProviderId, final String inputCategory,
                                                                                final int page, final int size, final String orderBy, final String sortBy)
            throws ExecutionException, InterruptedException {
        final String providerId = inputProviderId.strip();
        final String category = inputCategory.strip();
        if (providerId.isEmpty() || providerId.isBlank() || providerId.length() == 0) {
            throw new InvalidInputException(HttpStatus.BAD_REQUEST, "You have inputted an invalid providerId");
        } else if (category.isBlank() || category.isEmpty() || category.length() == 0) {
            throw new InvalidInputException(HttpStatus.BAD_REQUEST, "You have inputted an invalid category");
        }

        final var getAllOffering = dataOfferingRepository.findAll().sort(compareOfferingTime.reversed()).sort(compareOfferingTitle);

        final var total = getAllOffering.collectList().toFuture().get().size();

        final var result = getAllOffering.skip(page * size).take(size).collectList().toFuture()
                .get().stream().map(mapper::entityToDto)
                .collect(Collectors.toList());

        return Mono.just(TotalOfferingResponse.builder().totalOffering(total).result(result).build());
    }

    @Override
    public Mono<Void> deleteDataOfferingById(String offeringId) {
        return dataOfferingRepository.findById(offeringId)
                .map(dataOfferingRepository::delete)
                .flatMap(e -> e);
    }

    private DataOffering offeringTransform(final DataOffering updateEntity, final DataOffering currentEntity) {
        return currentEntity.toBuilder()
                .owner(updateEntity.getOwner())
                .dataOfferingTitle(updateEntity.getDataOfferingTitle())
                .dataOfferingDescription(updateEntity.getDataOfferingDescription())
                .category(updateEntity.getCategory())
                .status(updateEntity.getStatus())
                .dataOfferingExpirationTime(updateEntity.getDataOfferingExpirationTime())
                .contractParameters(updateEntity.getContractParameters())
                .hasPricingModel(updateEntity.getHasPricingModel())
                .hasDataset(updateEntity.getHasDataset())
                .build();
    }

    final Comparator<DataOffering> compareOfferingTime = Comparator.comparing(DataOffering::getCreatedAt);
    final Comparator<DataOffering> compareOfferingTitle = Comparator.comparing(DataOffering::getDataOfferingTitle);
    final Comparator<DataOffering> compareByProvider = Comparator.comparing(DataOffering::getProvider);
}
