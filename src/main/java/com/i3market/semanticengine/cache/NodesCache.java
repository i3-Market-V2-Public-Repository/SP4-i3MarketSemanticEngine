package com.i3market.semanticengine.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.i3market.semanticengine.common.domain.response.DataOfferingDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Log4j2
@Component
public class NodesCache {
    private LoadingCache<String, List<String>> loadingCache;
    public NodesCache(){
        super();
        loadingCache = CacheBuilder.newBuilder().expireAfterWrite(7200 , TimeUnit.MINUTES)
                .maximumSize(30).build(new CacheLoader<String,List<String>>() {
                    @Override
                    public List<String> load(String s) throws Exception {
                        return List.of();
                    }
                });
    }
    public void adduserValue(String name , List<String> list){
//        int attempt =0;
//        try {
//            attempt= loadingCache.get(name) +1;
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
        loadingCache.put(name, list);
    }
    public List<String> getValue(String username){
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
