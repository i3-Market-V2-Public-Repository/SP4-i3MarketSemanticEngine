# i3-Market Semantics Engine

## 1. Introduction
The semantic engine is a data microservice, which can interact with other services of the i3-Market for data offering registration. The semantic engine also provides necessary information such as contract parameters, pricing models, etc for other services and users. The repository of the semantic engine on the i3-Market gitlab can be found [here]https://github.com/i3-Market-V2-Public-Repository/SP4-I3MarketSemanticEngineMongodb) . 

## 2. Objectives

The semantic engine provides necessary API endpoints in relation to the data offering purpose of the i3Market. Details of each API endpoint can be found in the following sections.

## i3-Market Semantic Models
You can find the main Semantic Data Models files for i3-Market in github project at
https://github.com/i3-Market-V2-Public-Repository/SemanticsDataModels
and the specific files for last version at
https://github.com/i3-Market-V2-Public-Repository/SemanticsDataModels/tree/public/Version-2
and definitions for API Template description of Data Offering
Data Offerings description schema definitions in the API template.pdf

##   How to build the semantic engine docker images and containers


The Semantic engine 's repository is stored in Gitlab click here . The semantic engine is developed by Java Spring Boot with interface to metadata registry in MongoDB

a) Install your Docker engine.

b) Create docker image of the semantic engine. Before creating a docker image, it is required to build a ".jar" file of the semantic engine at first.

c) Try the following to create your docker image and run a container for the semantic engine


docker build --no-cache -t registry.gitlab.com/i3-market/code/wp4/semantic-engine:latest .
d) To run with the docker, use the below docker compose

```
 services:
  semantic-engine-service:
    container_name: "semantic-engine-service"
    image: registry.gitlab.com/i3-market/code/wp4/semantic-engine:${SEMANTIC_ENGINE_VERSION}
    ports:
      - "8082:8082"
    restart: always
    environment:
      - "SPRING_PROFILES_ACTIVE=test-env"
```
```
  mongodb:
    image: 'mongo:latest'
    container_name: "semantic-engine-db"
    command: mongod --port 27018
    ports:
      - '27018:27018'
    restart: always
    environment:
      MONGO_INITDB_ROOT_USERNAME: root
      MONGO_INITDB_ROOT_PASSWORD: YOUR_PASSWORD

```

## 3. Use cases of the semantic engine

- User registration with the semantic engine:

> ```POST /api/registration/```

Example of user registration is shown below:

```
{
    "providerId": "uiot-provider",
    "name": "My name",
    "description": "This is UIoT data Provider",
    "organization": {
       {
       "organizationId": "NUIGP1",
       "name": "National University of Ireland Galway",
       "description": "Education Organisation",
       "address": "University Road, Galway",
       "contactPoint": "chihung.le@nuigalway.ie"
       }
    }
}

```


- Data offering registration with the semantic engine

> ```POST /api/registration/data-offering```

A sample of a data offering can be found [here](hhttps://github.com/i3-Market-V2-Public-Repository/SP4-I3MarketSemanticEngineMongodb/blob/public/dataOffering.json in a JSON data format.

- Update an existing data offering:

> ```PATCH /api/registration/update-offering```

- Get a list of providers using category:

>```GET /api/registration/providers{category}/category```

- Get a list of all providers:

>```GET/api/registration/provider-list```

- Get a number of total offerings for a specific data provider. This number would be helpful for loading pages in a Web UI Application:

>```GET/api/registration/offerings/{providerId}/totalOfferings```

- Get a number of total offerings for a specific data provider at a specific category. This number would be helpful for loading pages in a Web UI Application:

>```GET/api/registration/offerings/category={category}/providerId={providerId}```

- Get a list of offeringId:

>```GET/api/registration/offering-list```

- Get a list of data offering by data profiderId:

>```GET/api/registration/offering/{id}/providerId```

- Get a list of data offering by data offeringId:

>```GET/api/registration/offering/{id}/offeringId```

- Get a list of data offering by category. This API will getch data from all node, e.g. in this case where have four nodes 244, 249, 250, and 251:

>```GET/api/registration/offering/{category}```

- Download a template of the data offering in the JSON (JavaScript Object Notation) format. This might be helpfull in some case, e.g. convert from this JSON file into a Java Class:
>```GET/api/registration/offering/offering-template```

- Get contract parameters object of a data offering by data offeringId:

>```GET/api/registration/contract-parameter/{offeringId}/offeringId```

- Get a list of category from distributed storage at its original API endpoint  :

>```GET/api/registration/categories-list```

- Delete a data offering by data offeringid. This will delete all information of a data offering from the database:

>```DELETE/api/registration/delete-offering/{offeringId}```


## 4. Access to the semantic engine API endpoint from the Backplane and the SDK-RI

- If you would like to get accessed to the semantic engine through the Backplane, please refer to the Backplane specification defined in this developer portal.

- Similarly, you would like to get accessed to the semantic engine through the SDK-RI, please refer to the SDK-RI specification defined in this developer portal.

## 5. Contact us if you have any issue with the semantic engine

Mirza Fardeenbaig   mirza.fardeenbaig@insight-centre.org



(Achille: <achille.zappa@insight-centre.org>)



## 6. License

This is free and unencumbered software released into the public domain.

Anyone is free to copy, modify, publish, use, compile, sell, or
distribute this software, either in source code form or as a compiled
binary, for any purpose, commercial or non-commercial, and by any
means.

In jurisdictions that recognize copyright laws, the author or authors
of this software dedicate any and all copyright interest in the
software to the public domain. We make this dedication for the benefit
of the public at large and to the detriment of our heirs and
successors. We intend this dedication to be an overt act of
relinquishment in perpetuity of all present and future rights to this
software under copyright law.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
OTHER DEALINGS IN THE SOFTWARE.

 Licensed under the EUPL
For more information, please refer to LICENSE file.

