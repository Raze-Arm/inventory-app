create sequence hibernate_sequence start with 1 increment by 1
create table activity (id bigint not null, created timestamp, last_updated timestamp, entity varchar(255), expires varchar(255), ip varchar(255), parameter varchar(255), request_method varchar(255), response_status integer, url varchar(255), user_agent varchar(255), user_id bigint, primary key (id))
create table customer (id varchar(36) not null, address varchar(255), created_date timestamp, first_name varchar(255), last_name varchar(255), modified_date timestamp, primary key (id))
create table invoice_view (id varchar(255) not null, created_date timestamp, name varchar(255), type varchar(255), primary key (id))
create table product (id varchar(36) not null, created_date timestamp, description varchar(255), modified_date timestamp, name varchar(255), price decimal(19,2), sale_price decimal(19,2), primary key (id))
create table product_view (id varchar(255) not null, created_date timestamp, description varchar(255), name varchar(255), price decimal(19,2), quantity bigint, sale_price decimal(19,2), primary key (id))
create table purchase_invoice (id varchar(36) not null, created_date timestamp, modified_date timestamp, supplier_id varchar(36), primary key (id))
create table purchase_transaction (id varchar(36) not null, created_date timestamp, description varchar(255), modified_date timestamp, price decimal(19,2), quantity bigint, invoice_id varchar(36), product_id varchar(36) not null, primary key (id))
create table sale_invoice (id varchar(36) not null, created_date timestamp, modified_date timestamp, customer_id varchar(36), primary key (id))
create table sale_transaction (id varchar(36) not null, created_date timestamp, description varchar(255), modified_date timestamp, price decimal(19,2), quantity bigint, invoice_id varchar(36), product_id varchar(36) not null, primary key (id))
create table supplier (id varchar(36) not null, address varchar(255), created_date timestamp, first_name varchar(255), last_name varchar(255), modified_date timestamp, primary key (id))
create table transaction_view (id varchar(255) not null, created_date timestamp, price decimal(19,2), product_name varchar(255), quantity bigint, type varchar(255), primary key (id))
create table user_account (id bigint not null, creation_date timestamp, is_account_non_expired boolean, is_account_non_locked boolean, is_credentials_non_expired boolean, is_enabled boolean, modified_date timestamp, password varchar(255), user_roles varchar(255), username varchar(255), primary key (id))
create table user_account_user_permissions (user_account_id bigint not null, user_permissions varchar(255))
create table user_profile (id varchar(36) not null, created_date timestamp, first_name varchar(255), last_name varchar(255), modified_date timestamp, photo_path varchar(255), account_id bigint not null, primary key (id))
create table user_session (username varchar(255) not null, token clob, primary key (username))
create index IDXkiyy7m3fwm4vo5nil9ibp5846 on customer (first_name, last_name)
create index IDXjmivyxk9rmgysrmsqw15lqr5b on product (name)
create index IDXnejv48oro0mjt6v13jl7t3l8k on supplier (first_name, last_name)
create index IDXcastjbvpeeus0r8lbpehiu0e4 on user_account (username)
alter table user_account add constraint UK_castjbvpeeus0r8lbpehiu0e4 unique (username)
create index IDXshil01lken9uud5fvqe7g1t58 on user_profile (first_name, last_name)
alter table user_profile add constraint UK_k3d1y1iufa28c7v4vtxsqw9aa unique (account_id)
alter table activity add constraint FKb0e1g6c44ampoe1ondy9t6v8w foreign key (user_id) references user_account
alter table purchase_invoice add constraint FKqtx4kjstn77n9v4wowt0mlxkx foreign key (supplier_id) references supplier
alter table purchase_transaction add constraint FKk5ila2wwhg03dmjj09xc5pikb foreign key (invoice_id) references purchase_invoice
alter table purchase_transaction add constraint FK850huaktm1ev5g3jefeb8qdat foreign key (product_id) references product
alter table sale_invoice add constraint FKt1eli7jvci5frjgs50tba9p15 foreign key (customer_id) references customer
alter table sale_transaction add constraint FK8aggg6jsmks0iklv5wq0i8wpd foreign key (invoice_id) references sale_invoice
alter table sale_transaction add constraint FKwbltmowgsigtquwnn824c20a foreign key (product_id) references product
alter table user_account_user_permissions add constraint FKajmdd9jsygg62yohq6fe9ppbn foreign key (user_account_id) references user_account
alter table user_profile add constraint FKp581a3prvwt8w63lu5s4w9jub foreign key (account_id) references user_account
