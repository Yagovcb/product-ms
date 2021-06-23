drop table if exists product;

create table product (
    id varchar AUTO_INCREMENT PRIMARY KEY,
    name varchar(250) not null,
    descricao varchar(250) not null,
    preco double precision not null
);
