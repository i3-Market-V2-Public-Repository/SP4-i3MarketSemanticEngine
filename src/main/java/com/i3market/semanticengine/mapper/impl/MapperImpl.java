package com.i3market.semanticengine.mapper.impl;

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
    public DataProvider requestToEntity(final RequestDataProvider request) {

        if (isNull(request.getOrganization())) {
            return DataProvider.builder()
                    .providerId(request.getProviderId().toLowerCase())
                    .name(request.getName())
                    .description(request.getDescription())
                    .build();
        }

        final List<OrganizationEntity> organizationEntities = request.getOrganization()
                .stream()
                .map(this::requestToEntity)
                .collect(Collectors.toList());

        return DataProvider.builder()
                .providerId(request.getProviderId().toLowerCase())
                .name(request.getName())
                .description(request.getDescription())
                .organization(organizationEntities)
                .build();
    }

    @Override
    public DataProviderDto entityToDto(final DataProvider entity) {
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
                .map(this::entityToDto)
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
    public DataProvider updateDtoToEntity(final DataProvider dto) {
        return DataProvider.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .organization(dto.getOrganization())
                .build();
    }

    private OrganizationEntity requestToEntity(final RequestOrganization dto) {
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
    public DataOffering requestToEntity(final RequestDataOffering request) {
        return DataOffering.builder()
                .provider(request.getProvider().toLowerCase())
                .marketId(request.getMarketId())
                .owner(request.getOwner())
                .providerDid(request.getProviderDid())
                .marketDid(request.getMarketDid())
                .ownerDid(request.getOwnerDid())
                .active(request.isActive())
                .ownerConsentForm(request.getOwnerConsentForm())
                .inSharedNetwork(request.isInSharedNetwork())
                .personalData(request.isPersonalData())
                .dataOfferingTitle(request.getDataOfferingTitle())
                .dataOfferingDescription(request.getDataOfferingDescription())
                .category(request.getCategory().strip().toLowerCase())
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
                .parallelStream().map(this::requestToEntity)
                .collect(Collectors.toList());

        final var datsetInforlationList = request.getDatasetInformation()
                .parallelStream().map(this::requestToEntity)
                .collect(Collectors.toList());

        return Dataset.builder()
                .title(request.getTitle())
                .keyword(request.getKeyword())
                .dataset(request.getDataset())
                .description(request.getDescription())
                .issued(request.getIssued())
                .modified(request.getModified())
                .language(request.getLanguage())
                .spatial(request.getSpatial())
                .accrualPeriodicity(request.getAccrualPeriodicity())
                .temporal(request.getTemporal())
                .temporalResolution(request.getTemporalResolution())
                .theme(request.getTheme())
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
                .dataStream(request.isDataStream())
                .accessService(requestToEntity(request.getAccessService()))
                .dataExchangeSpec(requestToEntity(request.getDataExchangeSpec()))
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
    private DataExchangeSpec requestToEntity(final RequestDataExchangeSpec requestDataExchangeSpec){
        return DataExchangeSpec.builder()
                .encAlg(requestDataExchangeSpec.getEncAlg())
                .signingAlg(requestDataExchangeSpec.getSigningAlg())
                .hashAlg(requestDataExchangeSpec.getHashAlg())
                .ledgerContractAddress(requestDataExchangeSpec.getLedgerContractAddress())
                .ledgerSignerAddress(requestDataExchangeSpec.getLedgerSignerAddress())
                .pooToPorDelay(requestDataExchangeSpec.getPooToPorDelay())
                .pooToPopDelay(requestDataExchangeSpec.getPooToPopDelay())
                .pooToSecretDelay(requestDataExchangeSpec.getPooToSecretDelay())
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
                .dataOfferingId(entity.getId())
                .provider(entity.getProvider())
                .marketId(entity.getMarketId())
                .owner(entity.getOwner())
                .providerDid(entity.getProviderDid())
                .marketDid(entity.getMarketDid())
                .ownerDid(entity.getOwnerDid())
                .active(entity.isActive())
                .ownerConsentForm(entity.getOwnerConsentForm())
                .inSharedNetwork(entity.isInSharedNetwork())
                .personalData(entity.isPersonalData())
                .dataOfferingTitle(entity.getDataOfferingTitle())
                .dataOfferingDescription(entity.getDataOfferingDescription())
                .category(entity.getCategory())
                .status(entity.getStatus())
                .version(entity.getVersion())
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
                .parallelStream().map(this::entityToDto)
                .collect(Collectors.toList());

        final var datasetInformation = entity.getDatasetInformation()
                .parallelStream().map(this::entityToDto)
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
                .temporalResolution(entity.getTemporalResolution())
                .temporal(entity.getTemporal())
                .theme(entity.getTheme())
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
                .dataStream(entity.isDataStream())
                .accessService(entityToDto(entity.getAccessService()))
                .dataExchangeSpec(entityToDto(entity.getDataExchangeSpec()))
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

    private ResponseDataExchangeSpec entityToDto(final DataExchangeSpec responseDataExchangeSpec){
        return ResponseDataExchangeSpec.builder()
                .encAlg(responseDataExchangeSpec.getEncAlg())
                .signingAlg(responseDataExchangeSpec.getSigningAlg())
                .hashAlg(responseDataExchangeSpec.getHashAlg())
                .ledgerContractAddress(responseDataExchangeSpec.getLedgerContractAddress())
                .ledgerSignerAddress(responseDataExchangeSpec.getLedgerSignerAddress())
                .pooToPorDelay(responseDataExchangeSpec.getPooToPorDelay())
                .pooToPopDelay(responseDataExchangeSpec.getPooToPopDelay())
                .pooToSecretDelay(responseDataExchangeSpec.getPooToSecretDelay())
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


    ///////////////////////////

    @Override
    public DataOffering dtoToEntity(final DataOfferingDto dto) {
        return DataOffering.builder()
                .owner(dto.getOwner())
                //.providerDid(dto.getProviderDid())
                .active(dto.isActive())
                .ownerConsentForm(dto.getOwnerConsentForm())
                .inSharedNetwork(dto.isInSharedNetwork())
                .personalData(dto.isPersonalData())
                .dataOfferingTitle(dto.getDataOfferingTitle())
                .dataOfferingDescription(dto.getDataOfferingDescription())
                .category(dto.getCategory().strip().toLowerCase())
                .status(dto.getStatus())
//                .version(dto.getVersion())
                .dataOfferingExpirationTime(dto.getDataOfferingExpirationTime())
                .contractParameters(requestToEntity(dto.getContractParameters()))
                .hasPricingModel(requestToEntity(dto.getHasPricingModel()))
                .hasDataset(requestToEntity(dto.getHasDataset()))
                .build();
    }

    @Override
    public ContractParametersResponse contractParameterDto(final DataOffering entity) {
        return ContractParametersResponse.builder()
                .offeringId(entity.getId())
                .version(entity.getVersion())
                .provider(entity.getProvider())
                .providerDid(entity.getProviderDid())
                .active(entity.isActive())
                .dataStream(entity.getHasDataset().getDistribution().get(0).isDataStream())
                .personalData(entity.isPersonalData())
                .category(entity.getCategory())
                .dataOfferingTitle(entity.getDataOfferingTitle())
                .contractParameters(entityToDto(entity.getContractParameters()))
                .hasPricingModel(entityToDto(entity.getHasPricingModel()))
                .dataExchangeSpec(entityToDto(entity.getHasDataset().getDistribution().get(0).getDataExchangeSpec()))
                .build();
    }

    @Override
    public ProviderIdResponse providerIdDto(final DataOfferingDto entity) {
        return ProviderIdResponse.builder()
                .provider(entity.getProvider()).build();
    }

    private ContractParameters requestToEntity(final ContractParametersDto dto) {
        return ContractParameters.builder()
                .interestOfProvider(dto.getInterestOfProvider())
                .interestDescription(dto.getInterestDescription())
                .hasGoverningJurisdiction(dto.getHasGoverningJurisdiction())
                .purpose(dto.getPurpose())
                .purposeDescription(dto.getPurposeDescription())
                .hasIntendedUse(requestToEntity(dto.getHasIntendedUse()))
                .hasLicenseGrant(requestToEntity(dto.getHasLicenseGrant()))
                .build();
    }

    private IntendedUse requestToEntity(final IntendedUseDto dto) {
        return IntendedUse.builder()
                .processData(dto.isProcessData())
                .editData(dto.isEditData())
                .shareDataWithThirdParty(dto.isShareDataWithThirdParty())
                .build();
    }

    private LicenseGrant requestToEntity(final LicenseGrantDto dto) {
        return LicenseGrant.builder()
                .copyData(dto.isCopyData())
                .transferable(dto.isTransferable())
                .exclusiveness(dto.isExclusiveness())
                .revocable(dto.isRevocable())
                .build();
    }

    private PricingModel requestToEntity(final PricingModelDto dto) {
        return PricingModel.builder()
                .pricingModelName(dto.getPricingModelName())
                .basicPrice(dto.getBasicPrice())
                .currency(dto.getCurrency())
                .hasPaymentOnSubscription(requestToEntity(dto.getHasPaymentOnSubscription()))
                .hasPaymentOnApi(requestToEntity(dto.getHasPaymentOnApi()))
                .hasPaymentOnUnit(requestToEntity(dto.getHasPaymentOnUnit()))
                .hasPaymentOnSize(requestToEntity(dto.getHasPaymentOnSize()))
                .hasFreePrice(requestToEntity(dto.getHasFreePrice()))
                .build();
    }

    private PaymentOnSubscription requestToEntity(final PaymentOnSubscriptionDto dto) {
        return PaymentOnSubscription.builder()
                .paymentOnSubscriptionName(dto.getPaymentOnSubscriptionName())
                .paymentType(dto.getPaymentType())
                .timeDuration(dto.getTimeDuration())
                .description(dto.getDescription())
                .repeat(dto.getRepeat())
                .hasSubscriptionPrice(dto.getHasSubscriptionPrice())
                .build();
    }

    private PaymentOnApi requestToEntity(final PaymentOnApiDto dto) {
        return PaymentOnApi.builder()
                .paymentOnApiName(dto.getPaymentOnApiName())
                .description(dto.getDescription())
                .numberOfObject(dto.getNumberOfObject())
                .hasApiPrice(dto.getHasApiPrice())
                .build();
    }

    private PaymentOnUnit requestToEntity(final PaymentOnUnitDto dto) {
        return PaymentOnUnit.builder()
                .paymentOnUnitName(dto.getPaymentOnUnitName())
                .description(dto.getDescription())
                .dataUnit(dto.getDataUnit())
                .hasUnitPrice(dto.getHasUnitPrice())
                .build();
    }

    private PaymentOnSize requestToEntity(final PaymentOnSizeDto dto) {
        return PaymentOnSize.builder()
                .paymentOnSizeName(dto.getPaymentOnSizeName())
                .description(dto.getDescription())
                .dataSize(dto.getDataSize())
                .hasSizePrice(dto.getHasSizePrice())
                .build();
    }

    private FreePrice requestToEntity(final FreePriceDto dto) {
        return FreePrice.builder()
                .hasPriceFree(dto.isHasPriceFree())
                .build();
    }

    private Dataset requestToEntity(final DatasetDto dto) {
        final var distributionList = dto.getDistribution()
                .parallelStream().map(this::requestToEntity)
                .collect(Collectors.toList());

        final var datsetInforlationList = dto.getDatasetInformation()
                .parallelStream().map(this::requestToEntity)
                .collect(Collectors.toList());

        return Dataset.builder()
                .title(dto.getTitle())
                .keyword(dto.getKeyword())
                .dataset(dto.getDataset())
                .description(dto.getDescription())
                .issued(dto.getIssued())
                .modified(dto.getModified())
                .temporal(dto.getTemporal())
                .language(dto.getLanguage())
                .spatial(dto.getSpatial())
                .accrualPeriodicity(dto.getAccrualPeriodicity())
                .temporalResolution(dto.getTemporalResolution())
                .theme(dto.getTheme())
                .distribution(distributionList)
                .datasetInformation(datsetInforlationList)
                .build();
    }

    private Distribution requestToEntity(final DistributionDto dto) {
        return Distribution.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .license(dto.getLicense())
                .accessRights(dto.getAccessRights())
                .downloadType(dto.getDownloadType())
                .conformsTo(dto.getConformsTo())
                .mediaType(dto.getMediaType())
                .packageFormat(dto.getPackageFormat())
                .accessService(requestToEntity(dto.getAccessService()))
                .build();
    }

    private AccessService requestToEntity(final AccessServiceDto dto) {
        return AccessService.builder()
                .conformsTo(dto.getConformsTo())
                .endpointDescription(dto.getEndpointDescription())
                .endpointURL(dto.getEndpointURL())
                .servesDataset(dto.getServesDataset())
                .serviceSpecs(dto.getServiceSpecs())
                .build();
    }

    private DatasetInformation requestToEntity(final DatasetInformationDto dto) {
        return DatasetInformation.builder()
                .measurementType(dto.getMeasurementType())
                .measurementChannelType(dto.getMeasurementChannelType())
                .sensorId(dto.getSensorId())
                .deviceId(dto.getDeviceId())
                .cppType(dto.getCppType())
                .sensorType(dto.getSensorType())
                .build();
    }
}
