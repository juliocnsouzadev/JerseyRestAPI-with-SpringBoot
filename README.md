# Specifications
We have a Product Entity with One to Many relationship with Image entity. Product also has a Many to One relationship with itself (Many Products to one Parent Product).
Excute the command mvn spring-boot:run to start the application.
The data is stored in an embedded database HSQL. The tables creation is done by the application running a script on the datasource creation time.


## Webservices

## Products
* **create (http post method)**

*path: /ws/produts/create*
accepts application.json
sample:
```javascript
{  
   "id":1,
   "name":"child",
   "description":"child product",
   "parentProduct":{  
      "id":1,
      "name":"parent",
      "description":"parent product"
   }
}
```

success status code = 201
fail status code = 412 (json syntax failiture)


* **update (http put method)**

*path: /ws/produts/{id}*
{id} is the id of the product to update that must be passed as a path parameter
accepts application.json
sample:
```javascript
{  
   "id":1,
   "name":"child",
   "description":"child product",
   "parentProduct":{  
      "id":1,
      "name":"parent",
      "description":"parent product"
   }
}
```
success status code = 200
fail status code = 412 (json syntax or number format in path parameter failiture)
fail status code = 410 (finding the product with de provide failiture)

* **delete (http delete method)**

*path: /ws/produts/{id}*
{id} is the id of the product to update that must be passed as a path parameter
success status code = 200
fail status code = 412 (number format in path parameter failiture)

* **get all excluding relationships (http get method)**

*path: /ws/produts/all*
produces application.json
sample:
```javascript
[  
   {  
      "id":1,
      "name":"prod one",
      "description":"First Product"
   },
   {  
      "id":2,
      "name":"prod two",
      "description":"Second Product"
   }
]
```
success status code = 200
fail status code = 410 (in case the result set is null)

* **get  by id excluding relationships (http get method)**

*path: /ws/produts/{id}*
{id} is the id of the product to update that must be passed as a path parameter
produces application.json
sample:
```javascript
   {  
      "id":2,
      "name":"prod two",
      "description":"Second Product"
   }
```

success status code = 200
fail status code = 410 (in case the result is null)

* **get  all including specified relationships (http get method)**

*path: /ws/produts/all/{relationship}*
{relationship} is the wanted relationships with product that must be passed as a path parameter. The options are: PARENT, IMAGES, BOTH, NONE;
produces application.json
sample passing PARENT:
```javascript
[  
   {  
      "id":1,
      "name":"prod one",
      "description":"First Product"
   },
   {  
      "id":2,
      "name":"prod two",
      "description":"Second Product",
      "parentProduct":{  
         "id":1,
         "name":"prod one",
         "description":"First Product"
      }
   }
]
```
success status code = 200
fail status code = 410 (in case the result set is null)

* **get  by id including specified relationships (http get method)**

*path: /ws/produts/{id}/{relationship}*
{id} is the id of the product to update that must be passed as a path parameter
{relationship} is the wanted relationships with product that must be passed as a path parameter. The options are: PARENT, IMAGES, BOTH, NONE;
produces application.json
sample passing 2 and PARENT:
```javascript

   {  
      "id":2,
      "name":"prod two",
      "description":"Second Product",
      "parentProduct":{  
         "id":1,
         "name":"prod one",
         "description":"First Product"
      }
   }
```

success status code = 200
fail status code = 410 (in case the result is null)

* **get  children by parent id (http get method)**

*path: /ws/produts/children/{id}*
{id} is the id of the product to update that must be passed as a path parameter
sample:
```javascript
[  
   {  
      "id":2,
      "name":"prod two",
      "description":"Second Product",
      "parentProduct":{  
         "id":1,
         "name":"prod one",
         "description":"First Product"
      }
   }
]
```

## Images
* **create (http post method)**

*path: /ws/produts/images/create*
accepts application.json
sample:
```javascript
{  
   "id":1,
   "product":{  
      "id":1,
      "name":"parent",
      "description":"parent product"
   }
}
```

success status code = 201
fail status code = 412 (json syntax failiture)

* **update (http put method)**

*path: /ws/produts/images/{id}*
{id} is the id of the image to update that must be passed as a path parameter
accepts application.json
sample:
```javascript
{  
   "id":1,
   "product":{  
      "id":1,
      "name":"parent",
      "description":"parent product"
   }
}
```

success status code = 200
fail status code = 412 (json syntax or number format in path parameter failiture)
fail status code = 410 (finding the image with de provide failiture)

* **delete (http delete method)**

*path: /ws/produts/images/{id}*
{id} is the id of the image to update that must be passed as a path parameter
success status code = 200
fail status code = 412 (number format in path parameter failiture)

* **get all excluding relationships (http get method)**

*path: /ws/produts/images/all*
produces application.json
sample:
```javascript
[  
   {  
      "id":1
   },
   {  
      "id":2
   }
]
```

success status code = 200
fail status code = 410 (in case the result set is null)

* **get  by id excluding relationships (http get method)**

*path: /ws/produts/images/{id}*
{id} is the id of the image to update that must be passed as a path parameter
produces application.json
sample:
```javascript
   {  
      "id":1
   }
```

success status code = 200
fail status code = 410 (in case the result is null)
fail status code = 412 (number format in path parameter failiture)

* **get  all including specified relationships (http get method)**

*path: /ws/produts/images/all/{relationship}*
{relationship} is the wanted relationships with image that must be passed as a path parameter. The options are: PRODUCT, NONE;
produces application.json
sample passing PRODUCT:
```javascript
[  
   {  
      "id":1,
      "product":{  
         "id":1,
         "name":"prod one",
         "description":"First Product"
      }
   },
   {  
      "id":2,
      "product":{  
         "id":2,
         "name":"prod two",
         "description":"Second Product",
         "parentProduct":{  
            "id":1,
            "name":"prod one",
            "description":"First Product"
         }
      }
   }
]
```

success status code = 200
fail status code = 410 (in case the result set is null)

* **get  by id including specified relationships (http get method)**

*path: /ws/produts/images/{id}/{relationship}*
{id} is the id of the image to update that must be passed as a path parameter
{relationship} is the wanted relationships with image that must be passed as a path parameter. The options are: PRODUCT, NONE;
produces application.json
sample passing 2 and PRODUCT:
```javascript
{  
   "id":1,
   "product":{  
      "id":1,
      "name":"prod one",
      "description":"First Product"
   }
}
```

success status code = 200
fail status code = 410 (in case the result is null)
fail status code = 412 (number format in path parameter failiture)

* **get  set by product id (http get method)**

*path: /ws/produts/images/forProduct/{id}*
{id} is the id of the image to update that must be passed as a path parameter
produces application.json
sample:
```javascript
[  
   {  
      "id":1,
      "product":{  
         "id":1,
         "name":"prod one",
         "description":"First Product"
      }
   }
]
```

success status code = 200
fail status code = 410 (in case the result is null)
fail status code = 412 (number format in path parameter failiture)