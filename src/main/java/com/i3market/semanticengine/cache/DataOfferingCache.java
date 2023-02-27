package com.i3market.semanticengine.cache;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.i3market.semanticengine.common.domain.response.DataOfferingDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
@Log4j2
@Service
public class DataOfferingCache {
    private LoadingCache<String, DataOfferingDto> loadingCache;

    public DataOfferingCache() {
        super();
        loadingCache = CacheBuilder.newBuilder().expireAfterWrite(7200 , TimeUnit.MINUTES)
                .maximumSize(30).build(new CacheLoader<String,DataOfferingDto>() {
                    @Override
                    public DataOfferingDto load(String s) throws Exception {
                        return DataOfferingDto.builder().build();
                    }
                });
    }

    public void adduserValue(String name , DataOfferingDto dto){
//        int attempt =0;
//        try {
//            attempt= loadingCache.get(name) +1;
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
        loadingCache.put(name,dto);
    }

    public DataOfferingDto getValue(String username){
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
}
