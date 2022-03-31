package com.i3market.semanticengine.common.domain.entity;

import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
public class Dataset implements Serializable {

    String title;

    String keyword;

    String dataset;

    String description;

    String issued;

    String modified;

    String temporal;

    String language;

    String spatial;

    String accrualPeriodicity;

    String temporalResolution;

    List<String> theme = new ArrayList<>();

    List<Distribution> distribution = new ArrayList<>();

    List<DatasetInformation> datasetInformation = new ArrayList<>();

}
