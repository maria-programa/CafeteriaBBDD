create database if not exists cafeteria_db;
--
use cafeteria_db;
--
create table if not exists cliente (
id_cliente int primary key auto_increment,
nombre varchar(50) not null,
apellidos varchar(150),
email varchar(150) not null unique,
fecha_registro date not null,
telefono varchar(20)
);
--
create table if not exists empleado (
id_empleado int primary key auto_increment,
codigo_empleado varchar(50) not null,
nombre varchar(50) not null,
apellidos varchar(150) not null,
dni varchar(10) unique not null,
fecha_contratacion date not null
);
--
create table if not exists producto (
id_producto int primary key auto_increment,
codigo varchar(20) unique not null,
nombre varchar(100) not null,
categoria varchar(50) not null,
precio decimal(10, 2) not null
);
--
create table if not exists pedido (
id_pedido int primary key auto_increment,
id_cliente int not null,
id_empleado int not null,
fecha_pedido date not null,
total decimal(10, 2) not null,
foreign key (id_cliente) references cliente(id_cliente),
foreign key (id_empleado) references empleado(id_empleado)
);
--
create table if not exists detalle_pedido (
id_detalle int primary key auto_increment,
id_pedido int not null,
id_producto int not null,
cantidad int not null,
subtotal decimal(10, 2) not null,
foreign key (id_pedido) references pedido(id_pedido),
foreign key (id_producto) references producto(id_producto)
);