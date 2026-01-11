create database if not exists cafeteria_db;
--
use cafeteria_db;
--
create table if not exists cliente (
id_cliente int primary key auto_increment,
nombre varchar(50) not null,
apellidos varchar(150),
email varchar(150) not null unique,
fecha_registro date not null default (current_date),
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
fecha_pedido date not null default (current_date),
total decimal(10, 2) not null,
tipo_pago varchar(30) not null,
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
--
delimiter ||
create function existe_cliente(f_email varchar(150))
returns bit
begin
	declare i int;
    set i = 0;
    while (i < (select max(id_cliente) from cliente)) do
    if ((select email from cliente
		where id_cliente = (i+1)) like f_email)
	then return 1;
    end if;
    set i = i+1;
    end while;
    return 0;
end; ||
delimiter ;
--
delimiter ||
create function existe_empleado(f_codigo varchar(50))
returns bit
begin
	declare i int;
    set i = 0;
    while (i < (select max(id_empleado) from empleado)) do
    if ((select codigo_empleado from empleado
		where id_empleado = (i+1)) like f_codigo)
	then return 1;
    end if;
    set i = i+1;
    end while;
    return 0;
end; ||
delimiter ;
--
delimiter ||
create function existe_producto(f_codigo varchar(20))
returns bit
begin
	declare i int;
    set i = 0;
    while (i < (select max(id_producto) from producto)) do
    if ((select codio from producto
		where id_producto = (i+1)) like f_codigo)
	then return 1;
    end if;
    set i = i+1;
    end while;
    return 0;
end; ||
delimiter ;