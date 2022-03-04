package com.i3market.semanticengine.mapper;

import com.i3market.semanticengine.common.domain.entity.DataOffering;
import com.i3market.semanticengine.common.domain.entity.DataProvider;
import com.i3market.semanticengine.common.domain.request.RequestDataOffering;
import com.i3market.semanticengine.common.domain.request.RequestDataProvider;
import com.i3market.semanticengine.common.domain.response.ContractParametersResponse;
import com.i3market.semanticengine.common.domain.response.DataOfferingDto;
import com.i3market.semanticengine.common.domain.response.DataProviderDto;
import com.i3market.semanticengine.common.domain.response.ProviderIdResponse;

public interface Mapper {

    DataProvider requestToEntity(final RequestDataProvider dto);

    DataProviderDto entityToDto(final DataProvider entity);

    DataProvider updateDtoToEntity(final DataProvider dto);

    DataOffering requestToEntity(final RequestDataOffering dto);

    DataOfferingDto entityToDto(final DataOffering entity);

    DataOffering dtoToEntity(final DataOfferingDto dto);

    ContractParametersResponse contractParameterDto(final DataOffering entity);

    ProviderIdResponse providerIdDto(final DataOfferingDto entity);


}
