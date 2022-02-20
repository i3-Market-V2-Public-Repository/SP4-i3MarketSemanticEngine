package com.i3market.semanticengine.mapper.impl;

import com.i3market.semanticengine.common.domain.DataProviderDto;
import com.i3market.semanticengine.common.domain.DataProviderEntity;
import com.i3market.semanticengine.common.domain.OrganizationDto;
import com.i3market.semanticengine.common.domain.OrganizationEntity;
import com.i3market.semanticengine.common.domain.entity.*;
import com.i3market.semanticengine.common.domain.request.*;
import com.i3market.semanticengine.common.domain.response.*;
import com.i3market.semanticengine.mapper.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Component
public class MapperImpl implements Mapper {
    @Override
    public DataProviderEntity dtoToEntity(final DataProviderDto dto) {

        System.out.println(isNull(dto.getOrganization()));

        if (isNull(dto.getOrganization())) {
            return DataProviderEntity.builder()
                    .providerId(dto.getProviderId())
                    .name(dto.getName())
                    .description(dto.getDescription())
                    .build();
        }

        final List<OrganizationEntity> organizationEntities = dto.getOrganization()
                .stream()
                .map(e -> dtoToEntity(e))
                .collect(Collectors.toList());

        return DataProviderEntity.builder()
                .providerId(dto.getProviderId())
                .name(dto.getName())
                .description(dto.getDescription())
                .organization(organizationEntities)
                .build();
    }

    @Override
    public DataProviderDto entityToDto(final DataProviderEntity entity) {
        if (isNull(entity.getOrganization())) {
            return DataProviderDto.builder()
                    .providerId(entity.getProviderId())
                    .name(entity.getName())
                    .createdAt(entity.getCreatedAt())
                    .updatedAt(entity.getUpdatedAt())
                    .description(entity.getDescription())
                    .build();
        }

        final List<OrganizationDto> organizationDtos = entity.getOrganization()
                .stream()
                .map(e -> entityToDto(e))
                .collect(Collectors.toList());
        return DataProviderDto.builder()
                .providerId(entity.getProviderId())
                .name(entity.getName())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .description(entity.getDescription())
                .organization(organizationDtos)
                .build();
    }

    @Override
    public DataProviderEntity updateDtoToEntity(final DataProviderEntity dto) {

        return DataProviderEntity.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .organization(dto.getOrganization())
                .build();
    }

    private OrganizationEntity dtoToEntity(final OrganizationDto dto) {
        return OrganizationEntity.builder()
                .organizationId(dto.getOrganizationId())
                .name(dto.getName())
                .description(dto.getDescription())
                .address(dto.getAddress())
                .contactPoint(dto.getContactPoint())
                .build();
    }

    private OrganizationDto entityToDto(final OrganizationEntity entity) {
        return OrganizationDto.builder()
                .organizationId(entity.getOrganizationId())
                .name(entity.getName())
                .description(entity.getDescription())
                .address(entity.getAddress())
                .contactPoint(entity.getContactPoint())
                .build();
    }

    //OFFERING
    @Override
    public DataOffering dtoToEntity(final RequestNewDataOffering request) {
        return DataOffering.builder()
                .provider(request.getProvider())
                .marketId(request.getMarketId())
                .owner(request.getOwner())
                .dataOfferingTitle(request.getDataOfferingTitle())
                .dataOfferingDescription(request.getDataOfferingDescription())
                .category(request.getCategory())
                .status(request.getStatus())
                .dataOfferingExpirationTime(request.getDataOfferingExpirationTime())
                .contractParameters(requestToEntity(request.getContractParameters()))
                .hasPricingModel(requestToEntity(request.getHasPricingModel()))
                .hasDataset(requestToEntity(request.getHasDataset()))
                .build();
    }

    private ContractParameters requestToEntity(final RequestContractParameters request) {
        return ContractParameters.builder()
                .interestOfProvider(request.getInterestOfProvider())
                .interestDescription(request.getInterestDescription())
                .hasGoverningJurisdiction(request.getHasGoverningJurisdiction())
                .purpose(request.getPurpose())
                .purposeDescription(request.getPurposeDescription())
                .hasIntendedUse(requestToEntity(request.getHasIntendedUse()))
                .hasLicenseGrant(requestToEntity(request.getHasLicenseGrant()))
                .build();
    }

    private IntendedUse requestToEntity(final RequestIntendedUse request) {
        return IntendedUse.builder()
                .processData(request.isProcessData())
                .editData(request.isEditData())
                .shareDataWithThirdParty(request.isShareDataWithThirdParty())
                .build();
    }

    private LicenseGrant requestToEntity(final RequestLicenseGrant request) {
        return LicenseGrant.builder()
                .copyData(request.isCopyData())
                .transferable(request.isTransferable())
                .exclusiveness(request.isExclusiveness())
                .revocable(request.isRevocable())
                .build();
    }

    private PricingModel requestToEntity(final RequestPricingModel request) {
        return PricingModel.builder()
                .pricingModelName(request.getPricingModelName())
                .basicPrice(request.getBasicPrice())
                .currency(request.getCurrency())
                .hasPaymentOnSubscription(requestToEntity(request.getHasPaymentOnSubscription()))
                .hasPaymentOnApi(requestToEntity(request.getHasPaymentOnApi()))
                .hasPaymentOnUnit(requestToEntity(request.getHasPaymentOnUnit()))
                .hasPaymentOnSize(requestToEntity(request.getHasPaymentOnSize()))
                .hasFreePrice(requestToEntity(request.getHasFreePrice()))
                .build();
    }

    private PaymentOnSubscription requestToEntity(final RequestPaymentOnSubscription request) {
        return PaymentOnSubscription.builder()
                .paymentOnSubscriptionName(request.getPaymentOnSubscriptionName())
                .paymentType(request.getPaymentType())
                .timeDuration(request.getTimeDuration())
                .description(request.getDescription())
                .repeat(request.getRepeat())
                .hasSubscriptionPrice(request.getHasSubscriptionPrice())
                .build();
    }

    private PaymentOnApi requestToEntity(final RequestPaymentOnApi request) {
        return PaymentOnApi.builder()
                .paymentOnApiName(request.getPaymentOnApiName())
                .description(request.getDescription())
                .numberOfObject(request.getNumberOfObject())
                .hasApiPrice(request.getHasApiPrice())
                .build();
    }

    private PaymentOnUnit requestToEntity(final RequestPaymentOnUnit request) {
        return PaymentOnUnit.builder()
                .paymentOnUnitName(request.getPaymentOnUnitName())
                .description(request.getDescription())
                .dataUnit(request.getDataUnit())
                .hasUnitPrice(request.getHasUnitPrice())
                .build();
    }

    private PaymentOnSize requestToEntity(final RequestPaymentOnSize request) {
        return PaymentOnSize.builder()
                .paymentOnSizeName(request.getPaymentOnSizeName())
                .description(request.getDescription())
                .dataSize(request.getDataSize())
                .hasSizePrice(request.getHasSizePrice())
                .build();
    }

    private FreePrice requestToEntity(final RequestFreePrice request) {
        return FreePrice.builder()
                .hasPriceFree(request.isHasPriceFree())
                .build();
    }

    private Dataset requestToEntity(final RequestDataset request) {
        final var distributionList = request.getDistribution()
                .parallelStream().map(e -> requestToEntity(e))
                .collect(Collectors.toList());

        final var datsetInforlationList = request.getDatasetInformation()
                .parallelStream().map(e -> requestToEntity(e))
                .collect(Collectors.toList());

        return Dataset.builder()
                .title(request.getTitle())
                .keyword(request.getKeyword())
                .dataset(request.getDataset())
                .description(request.getDescription())
                .issued(request.getIssued())
                .modified(request.getModified())
                .temporal(request.getTemporal())
                .language(request.getLanguage())
                .spatial(request.getSpatial())
                .accrualPeriodicity(request.getAccrualPeriodicity())
                .temporal(request.getTemporal())
                .distribution(distributionList)
                .datasetInformation(datsetInforlationList)
                .build();
    }

    private Distribution requestToEntity(final RequestDistribution request) {
        return Distribution.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .license(request.getLicense())
                .accessRights(request.getAccessRights())
                .downloadType(request.getDownloadType())
                .conformsTo(request.getConformsTo())
                .mediaType(request.getMediaType())
                .packageFormat(request.getPackageFormat())
                .accessService(requestToEntity(request.getAccessService()))
                .build();
    }

    private AccessService requestToEntity(final RequestAccessService request) {
        return AccessService.builder()
                .conformsTo(request.getConformsTo())
                .endpointDescription(request.getEndpointDescription())
                .endpointURL(request.getEndpointURL())
                .servesDataset(request.getServesDataset())
                .serviceSpecs(request.getServiceSpecs())
                .build();
    }

    private DatasetInformation requestToEntity(final RequestDatasetInformation request) {
        return DatasetInformation.builder()
                .measurementType(request.getMeasurementType())
                .measurementChannelType(request.getMeasurementChannelType())
                .sensorId(request.getSensorId())
                .deviceId(request.getDeviceId())
                .cppType(request.getCppType())
                .sensorType(request.getSensorType())
                .build();
    }


    @Override
    public DataOfferingDto entityToDto(final DataOffering entity) {
        return DataOfferingDto.builder()
                .context(OfferingGeneralContext.builder().build())
                .id(entity.getId())
                .provider(entity.getProvider())
                .marketId(entity.getMarketId())
                .owner(entity.getOwner())
                .dataOfferingTitle(entity.getDataOfferingTitle())
                .dataOfferingDescription(entity.getDataOfferingDescription())
                .category(entity.getCategory())
                .status(entity.getStatus())
                .dataOfferingExpirationTime(entity.getDataOfferingExpirationTime())
                .contractParameters(entityToDto(entity.getContractParameters()))
                .hasPricingModel(entityToDto(entity.getHasPricingModel()))
                .hasDataset(entityToDto(entity.getHasDataset()))
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    private ContractParametersDto entityToDto(final ContractParameters entity) {
        return ContractParametersDto.builder()
                .interestOfProvider(entity.getInterestOfProvider())
                .interestDescription(entity.getInterestDescription())
                .hasGoverningJurisdiction(entity.getHasGoverningJurisdiction())
                .purpose(entity.getPurpose())
                .purposeDescription(entity.getPurposeDescription())
                .hasIntendedUse(entityToDto(entity.getHasIntendedUse()))
                .hasLicenseGrant(entityToDto(entity.getHasLicenseGrant()))
                .build();
    }

    private IntendedUseDto entityToDto(final IntendedUse entity) {
        return IntendedUseDto.builder()
                .processData(entity.isProcessData())
                .shareDataWithThirdParty(entity.isShareDataWithThirdParty())
                .editData(entity.isEditData())
                .build();
    }

    private LicenseGrantDto entityToDto(final LicenseGrant entity) {
        return LicenseGrantDto.builder()
                .copyData(entity.isCopyData())
                .transferable(entity.isTransferable())
                .exclusiveness(entity.isExclusiveness())
                .revocable(entity.isRevocable())
                .build();
    }

    private PricingModelDto entityToDto(final PricingModel entity) {
        return PricingModelDto.builder()
                .pricingModelName(entity.getPricingModelName())
                .basicPrice(entity.getBasicPrice())
                .currency(entity.getCurrency())
                .hasPaymentOnSubscription(entityToDto(entity.getHasPaymentOnSubscription()))
                .hasPaymentOnApi(entityToDto(entity.getHasPaymentOnApi()))
                .hasPaymentOnUnit(entityToDto(entity.getHasPaymentOnUnit()))
                .hasPaymentOnSize(entityToDto(entity.getHasPaymentOnSize()))
                .hasFreePrice(entityToDto(entity.getHasFreePrice()))
                .build();
    }

    private PaymentOnSubscriptionDto entityToDto(final PaymentOnSubscription entity) {
        return PaymentOnSubscriptionDto.builder()
                .paymentOnSubscriptionName(entity.getPaymentOnSubscriptionName())
                .paymentType(entity.getPaymentType())
                .timeDuration(entity.getTimeDuration())
                .description(entity.getDescription())
                .repeat(entity.getRepeat())
                .hasSubscriptionPrice(entity.getHasSubscriptionPrice())
                .build();
    }

    private PaymentOnApiDto entityToDto(final PaymentOnApi entity) {
        return PaymentOnApiDto.builder()
                .paymentOnApiName(entity.getPaymentOnApiName())
                .description(entity.getDescription())
                .numberOfObject(entity.getNumberOfObject())
                .hasApiPrice(entity.getHasApiPrice())
                .build();
    }

    private PaymentOnUnitDto entityToDto(final PaymentOnUnit entity) {
        return PaymentOnUnitDto.builder()
                .paymentOnUnitName(entity.getPaymentOnUnitName())
                .description(entity.getDescription())
                .dataUnit(entity.getDataUnit())
                .hasUnitPrice(entity.getHasUnitPrice())
                .build();
    }

    private PaymentOnSizeDto entityToDto(final PaymentOnSize entity) {
        return PaymentOnSizeDto.builder()
                .paymentOnSizeName(entity.getPaymentOnSizeName())
                .description(entity.getDescription())
                .dataSize(entity.getDataSize())
                .hasSizePrice(entity.getHasSizePrice())
                .build();
    }

    private FreePriceDto entityToDto(final FreePrice entity) {
        return FreePriceDto.builder()
                .hasPriceFree(entity.isHasPriceFree())
                .build();
    }

    private DatasetDto entityToDto(final Dataset entity) {

        final var distributionList = entity.getDistribution()
                .parallelStream().map(e -> entityToDto(e))
                .collect(Collectors.toList());

        final var datasetInformation = entity.getDatasetInformation()
                .parallelStream().map(e -> entityToDto(e))
                .collect(Collectors.toList());

        return DatasetDto.builder()
                .title(entity.getTitle())
                .keyword(entity.getKeyword())
                .dataset(entity.getDataset())
                .description(entity.getDescription())
                .issued(entity.getIssued())
                .modified(entity.getModified())
                .temporal(entity.getTemporal())
                .language(entity.getLanguage())
                .spatial(entity.getSpatial())
                .accrualPeriodicity(entity.getAccrualPeriodicity())
                .temporal(entity.getTemporal())
                .distribution(distributionList)
                .datasetInformation(datasetInformation)
                .build();
    }

    private DistributionDto entityToDto(final Distribution entity) {
        return DistributionDto.builder()
                .title(entity.getTitle())
                .description(entity.getDescription())
                .license(entity.getLicense())
                .accessRights(entity.getAccessRights())
                .downloadType(entity.getDownloadType())
                .conformsTo(entity.getConformsTo())
                .mediaType(entity.getMediaType())
                .packageFormat(entity.getPackageFormat())
                .accessService(entityToDto(entity.getAccessService()))
                .build();
    }

    private AccessServiceDto entityToDto(final AccessService entity) {
        return AccessServiceDto.builder()
                .conformsTo(entity.getConformsTo())
                .endpointDescription(entity.getEndpointDescription())
                .endpointURL(entity.getEndpointURL())
                .servesDataset(entity.getServesDataset())
                .serviceSpecs(entity.getServiceSpecs())
                .build();
    }

    private DatasetInformationDto entityToDto(final DatasetInformation entity) {
        return DatasetInformationDto.builder()
                .measurementType(entity.getMeasurementType())
                .measurementChannelType(entity.getMeasurementChannelType())
                .sensorId(entity.getSensorId())
                .deviceId(entity.getDeviceId())
                .cppType(entity.getCppType())
                .sensorType(entity.getSensorType())
                .build();
    }
}
