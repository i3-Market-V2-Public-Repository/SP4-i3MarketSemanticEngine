package com.i3market.semanticengine.mapper;

import com.i3market.semanticengine.common.domain.DataProviderDto;
import com.i3market.semanticengine.common.domain.DataProviderEntity;
import com.i3market.semanticengine.common.domain.entity.DataOffering;
import com.i3market.semanticengine.common.domain.request.RequestNewDataOffering;
import com.i3market.semanticengine.common.domain.response.DataOfferingDto;

public interface Mapper {

    DataProviderEntity dtoToEntity(DataProviderDto dto);

    DataProviderDto entityToDto(DataProviderEntity entity);

    DataProviderEntity updateDtoToEntity(DataProviderEntity dto);

    DataOffering dtoToEntity(RequestNewDataOffering dto);

    DataOfferingDto entityToDto(DataOffering entity);
}
