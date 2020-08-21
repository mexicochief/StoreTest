CREATE schema Store;
USE Store;
create table Users
(
    id         serial primary key,
    first_name varchar(30),
    last_name  varchar(30)
);

create table products
(
    id           serial primary key,
    product_name varchar(30),
    cost         decimal(4,2)
);

create table orders
(
    id         serial primary key,
    user_id    bigint unsigned,
    order_date date,
    sum        decimal(4,2),
    foreign key (user_id) references Users (id)
);

create table orderProduct
(
    order_id   bigint unsigned,
    product_id bigint unsigned,
    foreign key (order_id) references orders (id),
    foreign key (product_id) references products (id)
)
