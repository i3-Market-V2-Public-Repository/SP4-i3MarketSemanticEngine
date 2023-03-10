package com.i3market.semanticengine;

import com.i3market.semanticengine.service.impl.DataOfferingServiceImpl;
import eu.i3market.seedsindex.DataCategory;
import eu.i3market.seedsindex.SearchEngineIndexRecord;
import eu.i3market.seedsindex.SeedsIndex;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * Author: Chi-Hung Le
 * Email: lchhung@gmail.com
 */

@Log4j2
@SpringBootApplication
@EnableMongoRepositories
public class SemanticEngineApplication   {


    private static  DataOfferingServiceImpl dataOfferingService ;



    public static void main(final String[] args) {



        final SpringApplication app = new SpringApplication(SemanticEngineApplication.class);
        setProfile(app);
        final Environment env = app.run(args).getEnvironment();

        logApplicationStartup(env);
        log.info("Value of node {}\n Value of Key {}",env.getProperty("app.node"),env.getProperty("app.key"));

        String node = env.getProperty("app.node");
        String key = env.getProperty("app.key");
        if( !String.valueOf(node).isEmpty() && !String.valueOf(key).isEmpty() ){
            System.out.println("if block");
            final List<String> location = getLocation();

            boolean loopValue = true;

                for(var loc : location){
                    log.info("Value pf location {}",loc);
                    if(loc.equalsIgnoreCase(node)){
                        log.info("Value of node {} \t Value found {}",node,loc);
                        loopValue =false;
                        break;
                    }

                }
                if(loopValue && key.length()>50){
                    log.info("could not find address in seedIndex , adding to network...");
                    SeedsIndex seedsIndex = new SeedsIndex("http://95.211.3.251" + ":8545",
                            key);

                    try {

                        seedsIndex.init();
                        seedsIndex.setMyIndexRecord(new URI(node), new DataCategory[]{
                                DataCategory.AGRICULTURE
                        });
                        final Collection<SearchEngineIndexRecord> byDataCategory = seedsIndex.findByDataCategory(null);
                        for(var se : byDataCategory){
                            location.add(String.valueOf(se.getLocation())) ;
                        }
                        location.stream().forEach(e-> System.out.println(e));

                    } catch (Exception e) {
                       log.info("Sorry , exception occurs cannot add node to network...");
                    }
                    finally {
                        seedsIndex.shutdown();
                    }
                }

        }
        else{
            System.out.println("Else block");
        }
//        getLocation().stream().forEach(e->System.out.println("location "+e));
    }

    final static void setProfile(final SpringApplication app) {
        final String envVar = "SEMANTIC-ENGINE";
        final String value = System.getenv().getOrDefault(envVar, "local");
        if (value.contains("test-env")) {
            app.setAdditionalProfiles("test-env");
        } else {
            app.setAdditionalProfiles(value);
        }
    }

    private static void logApplicationStartup(final Environment env) {
        String protocol = "http";
        if (env.getProperty("server.ssl.key-store") != null) {
            protocol = "https";
        }
        final String serverPort = env.getProperty("server.port");
        String contextPath = env.getProperty("server.servlet.context-path");
        if (StringUtils.isBlank(contextPath)) {
            contextPath = "/";
        }
        String hostAddress = "localhost";
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (final UnknownHostException e) {
            log.warn("The host name could not be determined, using `localhost` as fallback");
        }
        log.info("\n----------------------------------------------------------\n\t" +
                        "Application '{}' is running! Access URLs:\n\t" +
                        "Local: \t\t{}://localhost:{}{}swagger-ui.html\n\t" +
                        "External: \t{}://{}:{}{}swagger-ui.html\n\t" +
                        "Profile(s): \t{}\n----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                protocol,
                serverPort,
                contextPath,
                protocol,
                hostAddress, // This will return host address as 172.26.144.1
                serverPort,
                contextPath,
                env.getActiveProfiles());
    }

    @Bean
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }


    public static List<String> getLocation () {
        SeedsIndex seedsIndex = new SeedsIndex("http://95.211.3.250" + ":8545",
                "0x91ca5769686d3c0ba102f0999140c1946043ecdc1c3b33ee3fd2c80030e46c26");
        List<String> location= new ArrayList<>();
        try {

            seedsIndex.init();
            final Collection<SearchEngineIndexRecord> byDataCategory = seedsIndex.findByDataCategory(null);
            for(var se : byDataCategory){
                location.add(String.valueOf(se.getLocation())) ;
            }

            return location;

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            seedsIndex.shutdown();
        }
        return null;

    }


}