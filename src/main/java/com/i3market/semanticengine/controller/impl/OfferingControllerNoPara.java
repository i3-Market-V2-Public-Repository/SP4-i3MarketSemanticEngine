package com.i3market.semanticengine.controller.impl;

import com.i3market.semanticengine.common.domain.response.DataOfferingDto;
import com.i3market.semanticengine.common.domain.response.OfferingIdResponse;
import com.i3market.semanticengine.common.domain.response.ProviderIdResponse;
import com.i3market.semanticengine.service.impl.DataOfferingNoPaging;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import static com.i3market.semanticengine.common.constant.Constants.SEMANTIC_ENGINE_MAIN_PATH;

@RestController
@RequestMapping(SEMANTIC_ENGINE_MAIN_PATH)
@Tag(name = "No Query Para")
public class OfferingControllerNoPara {

    private DataOfferingNoPaging dataOfferingNoPaging;

    @Autowired
    public OfferingControllerNoPara(DataOfferingNoPaging dataOfferingNoPaging) {
        this.dataOfferingNoPaging = dataOfferingNoPaging;
    }

    @GetMapping("/offerings-list-P")
    @Operation(summary = "GET a list of all offering")
    ResponseEntity<Flux<OfferingIdResponse>> getOfferingList(){
       return ResponseEntity.ok(dataOfferingNoPaging.getOfferingList()) ;
    }
    @GetMapping("/offerings-list/on-active-P")
    @Operation(summary = "GET a list of all offering on active state")
    ResponseEntity<Flux<OfferingIdResponse>> getOfferingListonActive(){
        return ResponseEntity.ok(dataOfferingNoPaging.getOfferingListonActive());
    }

    @GetMapping("/offering/{id}/providerId-P")
    @Operation(summary = "GET an a list of offering for a provider")
    ResponseEntity<Flux<DataOfferingDto>> getOfferingByProvider(@PathVariable(name = "id") final String provider){
        return ResponseEntity.ok(dataOfferingNoPaging.getOfferingByProvider(provider));
    }
    @GetMapping("/offering-P/{category}")
    @Operation(summary = "GET a list of offerings by category")
    ResponseEntity<Flux<DataOfferingDto>> getOfferingByCategory(@PathVariable(name = "category") final String category){
        return  ResponseEntity.ok(dataOfferingNoPaging.getOfferingByCategory(category));
    }
    @GetMapping("/providers/{category}/category-P")
    @Operation(summary = "GET a list of provider by category")
    ResponseEntity<Flux<ProviderIdResponse>> getProviderListByCategory(@PathVariable(name = "category") final String category){
        return ResponseEntity.ok(dataOfferingNoPaging.getProviderListByCategory(category));
    }
    @GetMapping("/ActiveOfferingByCategory-P/{category}")
    @Operation(summary = "GET a list of offerings by category")
    ResponseEntity<Flux<DataOfferingDto>> getActiveOfferingByCategory(@PathVariable(name = "category") final String category){
        return  ResponseEntity.ok(dataOfferingNoPaging.getOfferingsByActiveAndCategory(category));
    }
    @GetMapping("/ActiveOfferingByProvider/{id}/providerId-P")
    @Operation(summary = "GET an a list of offering for a provider")
    ResponseEntity<Flux<DataOfferingDto>> getActiveOfferingByProvider(@PathVariable(name = "id") final String provider){
        return ResponseEntity.ok(dataOfferingNoPaging.getOfferingsByActiveAndProvider(provider));
    }

    @GetMapping("/textSearch/text-P/{text}")
    @Operation(summary = "Get Offering/Offerings based on keyword/text")
    ResponseEntity<Flux<DataOfferingDto>> getTextSearchMono(@PathVariable(name = "text") String text){
        return ResponseEntity.ok(dataOfferingNoPaging.getTextSearchReact(text));
    }
    @GetMapping("/getActiveOfferingByText/{text}/text-P")
    @Operation(summary = " Search all active offerings by text/keyword")
    public ResponseEntity<Flux<DataOfferingDto>> getActiveOfferingByText(@PathVariable(name = "text") String text ){
        return  ResponseEntity.ok(dataOfferingNoPaging.getActiveTextSearch(text));
    }
    @GetMapping("/offerings-list/on-SharedNetwork-P")
    @Operation(summary = "GET a list of all offering on Shared Network")
    ResponseEntity<Flux<OfferingIdResponse>> getOfferingListonSharedNetwork(){
        return ResponseEntity.ok(dataOfferingNoPaging.getOfferingListonSharedNetwork());
    }
}
