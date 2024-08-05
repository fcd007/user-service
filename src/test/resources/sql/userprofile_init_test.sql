insert into user (id, first_name,last_name, email, password, roles) values (89, 'Lucas','Santos','lucas@test.com.br', '{bcrypt}$2a$10$hPbciUrbIEZw6J5Utie.HOjh2h2XeaEm4w3KU5aYeuBD3VpdReFmS', 'USER');
insert into user (id, first_name,last_name, email, password, roles) values (90, 'Leticia','Pausini','leticia@test.com.br', '{bcrypt}$2a$10$hPbciUrbIEZw6J5Utie.HOjh2h2XeaEm4w3KU5aYeuBD3VpdReFmS', 'USER');
insert into profile (id, name, description, created_at, last_updated_on) values (999, 'admin','profile role admin', current_date, current_date);
insert into user_profile (id, user_id, profile_id) values (5,89,999);
insert into user_profile (id, user_id, profile_id) values (6,90,999);