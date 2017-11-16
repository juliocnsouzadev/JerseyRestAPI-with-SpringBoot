 CREATE TABLE product (
                id INTEGER,
                name VARCHAR(100),
                description  VARCHAR(255),
                parent_product_id INTEGER,
                PRIMARY KEY (id),
                CONSTRAINT FK_parent_product FOREIGN KEY (parent_product_id) REFERENCES product(id)
                );

CREATE TABLE image (
                id INTEGER,
                product_id INTEGER,
                PRIMARY KEY (id),
                CONSTRAINT FK_product FOREIGN KEY (product_id) REFERENCES product(id)
                );
