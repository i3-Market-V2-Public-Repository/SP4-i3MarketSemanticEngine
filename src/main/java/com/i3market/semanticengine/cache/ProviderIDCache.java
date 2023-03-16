package com.i3market.semanticengine.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.i3market.semanticengine.common.domain.entity.OfferingIdRes;
import com.i3market.semanticengine.common.domain.response.ProviderIdResponse;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Component
public class ProviderIDCache {
    private LoadingCache<Pair<String , String>, List<ProviderIdResponse>> loadingCache;

    public ProviderIDCache(){
        super();
        loadingCache = CacheBuilder.newBuilder().expireAfterWrite(7200 , TimeUnit.MINUTES)
                .maximumSize(30).build(new CacheLoader<Pair<String, String>, List<ProviderIdResponse>>() {
                    @Override
                    public List<ProviderIdResponse> load(Pair<String, String> stringStringPair) throws Exception {
                        return List.of();
                    }
                });
    }
    public void addValue(Pair<String , String> pair , List<ProviderIdResponse> name){
        loadingCache.put(pair,name);
    }

    public List<ProviderIdResponse> getValue(Pair<String , String> pair ){
        try {
            return loadingCache.get(pair);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public long CacheTimeOut(){
        final long size = loadingCache.size();
        return size;
    }
    public void clearAll(){
        loadingCache.invalidateAll();
    }
}
