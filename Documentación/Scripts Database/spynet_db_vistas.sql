create view vista_auditorias_inspector_admin as 
select auditory_name, client_name, username, auditory_date_init, auditory_date_limit, auditory_state from auditory 
join auditory_access on auditory_id=aud_access_auditory_id
join user on user_id=aud_access_user_id
join client on client_id=auditory_client_id;

create view all_users as 
select user_id, username, user_rol, user_last_session, user_state from user;


create view all_observations as 
select observation_auditory_id, username, observation_title, observation_description, observation_datetime 
from observation join user where user_id=observation_user_id;


select * from vista_auditorias_inspector_admin;
select * from vista_auditorias_inspector_admin where username="auditor_ana";
select * from all_users;
select * from all_observations;