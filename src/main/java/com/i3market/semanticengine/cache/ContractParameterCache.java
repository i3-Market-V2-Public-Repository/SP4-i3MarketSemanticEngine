package com.i3market.semanticengine.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.i3market.semanticengine.common.domain.response.ContractParametersResponse;
import com.i3market.semanticengine.common.domain.response.DataOfferingDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Log4j2
@Component
public class ContractParameterCache {
    private LoadingCache<String, ContractParametersResponse> loadingCache;

    public ContractParameterCache() {
        super();
        loadingCache = CacheBuilder.newBuilder().expireAfterWrite(7200 , TimeUnit.MINUTES)
                .maximumSize(30).build(new CacheLoader<String,ContractParametersResponse>() {
                    @Override
                    public ContractParametersResponse load(String s) throws Exception {
                        return ContractParametersResponse.builder().build();
                    }
                });
    }

    public void adduserValue(String name , ContractParametersResponse dto){
//        int attempt =0;
//        try {
//            attempt= loadingCache.get(name) +1;
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
        loadingCache.put(name,dto);
    }

    public ContractParametersResponse getValue(String username){
        try {
            return loadingCache.get(username);
        } catch (ExecutionException e) {
            log.info("inside catch block");
            e.printStackTrace();
        }
        return null;
    }
    public void clear(String username){
        loadingCache.refresh(username);
    }
    public void clearAll(){
        loadingCache.invalidateAll();
    }
}
