alter table author add column border varchar(255) not null default '';

update author set border = '1px dotted #f00' where name = 'BetaFaceApi';
update author set border = '1px dotted #fff', name = 'MICO 1' where name = 'MICO';
update author set border = '1px dotted #00f' where name = 'Manual';

insert into author( color, name, border) values( '#0f0', 'MICO 2', '1px dotted #fff');