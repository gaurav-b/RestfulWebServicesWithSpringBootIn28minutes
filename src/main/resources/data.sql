insert into user_details(id,birth_date,name)
	values(10001, current_date(), 'Ranga');
insert into user_details(id,birth_date,name)
	values(10002, current_date(), 'Ravi');
insert into user_details(id,birth_date,name)
	values(10003, current_date(), 'Sathish');

	
insert into post(id, description, user_id) 
	values(20001, 'i want to learn AWS', 10001);
insert into post(id, description, user_id) 
	values(20002, 'i want to learn DvOps', 10001);
insert into post(id, description, user_id) 
	values(20003, 'i want to get AWS certified', 10002);
insert into post(id, description, user_id) 
	values(20004, 'i want to learn Multi cloud', 10002);