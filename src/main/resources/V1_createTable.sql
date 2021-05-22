drop table if exists customer;
drop table if exists product;
drop table if exists purchase_invoice;
drop table if exists purchase_transaction;
drop table if exists sale_invoice;
drop table if exists sale_transaction;
drop table if exists supplier;



create table customer (id varchar(36) not null, address varchar(255), created_date timestamp, first_name varchar(255), last_name varchar(255), modified_date timestamp, primary key (id));
create table product (id varchar(36) not null, created_date timestamp, modified_date timestamp, name varchar(255), price decimal(19,2), sale_price decimal(19,2), description varchar(255), primary key (id));
create table purchase_invoice (id varchar(36) not null, created_date timestamp, modified_date timestamp, supplier_id varchar(36), primary key (id));
create table purchase_transaction (id varchar(36) not null, created_date timestamp, description varchar(255), modified_date timestamp, price decimal(19,2), quantity bigint, invoice_id varchar(36), product_id varchar(36) not null, primary key (id));
create table sale_invoice (id varchar(36) not null, created_date timestamp, modified_date timestamp, customer_id varchar(36), primary key (id));
create table sale_transaction (id varchar(36) not null, created_date timestamp, description varchar(255), modified_date timestamp, price decimal(19,2), quantity bigint, invoice_id varchar(36), product_id varchar(36) not null, primary key (id));
create table supplier (id varchar(36) not null, address varchar(255), created_date timestamp, first_name varchar(255), last_name varchar(255), modified_date timestamp, primary key (id));
alter table purchase_invoice add constraint FKqtx4kjstn77n9v4wowt0mlxkx foreign key (supplier_id) references supplier;
alter table purchase_transaction add constraint FKk5ila2wwhg03dmjj09xc5pikb foreign key (invoice_id) references purchase_invoice;
alter table purchase_transaction add constraint FK850huaktm1ev5g3jefeb8qdat foreign key (product_id) references product;
alter table sale_invoice add constraint FKt1eli7jvci5frjgs50tba9p15 foreign key (customer_id) references customer;
alter table sale_transaction add constraint FK8aggg6jsmks0iklv5wq0i8wpd foreign key (invoice_id) references sale_invoice;
alter table sale_transaction add constraint FKwbltmowgsigtquwnn824c20a foreign key (product_id) references product;



CREATE OR REPLACE VIEW product_view AS
    (SELECT
        p.id AS id,

        p.name AS name,
        p.description AS description,
        p.price AS price,
        p.sale_price AS SALE_PRICE,
        p.created_date AS CREATED_DATE,

        it.quantity - (CASE
            WHEN s.quantity IS NOT NULL THEN s.quantity
            ELSE 0
        END) AS quantity
    FROM
        product p
            INNER JOIN
        (SELECT
            pt.product_id, (SUM(pt.quantity)) AS quantity
        FROM
            purchase_transaction pt
        GROUP BY pt.product_id) AS it ON p.id = it.product_id
            LEFT JOIN
        (SELECT
            st.product_id, (SUM(st.quantity)) AS quantity
        FROM
            sale_transaction st
        GROUP BY st.product_id) AS s ON p.id = s.product_id);