delimiter $$
create procedure get_auditorias_by_user(in p_username varchar(45))
begin
    declare v_rol varchar(20);

    select user_rol into v_rol from user where username = p_username limit 1;

    if v_rol = 'auditor' then
        select auditory_name, client_name, auditory_date_init, auditory_date_limit, auditory_state
        from vista_auditorias_inspector_admin
        where username = p_username;
    elseif v_rol = 'inspector' or v_rol = 'admin' then
        select * from vista_auditorias_inspector_admin;
    else
        select 'Rol no válido o usuario no encontrado' as mensaje;
    end if;
end $$
delimiter ;


delimiter $$
create procedure get_users_by_role(in p_username varchar(45))
begin
    declare v_rol varchar(20);

    select user_rol into v_rol from user where username = p_username limit 1;

    if v_rol = 'admin' then
        select * from all_users;
    elseif v_rol = 'inspector' then
        select * from all_users where user_rol = 'auditor';
    else
        select 'Rol no válido para esta consulta' as mensaje;
    end if;
end $$
delimiter ;




delimiter $$
create procedure get_observations_by_auditory_id(in p_auditory_id int)
begin
    select * from all_observations where observation_auditory_id=p_auditory_id;
end $$
delimiter ; 



delimiter $$

create procedure get_findings_by_auditory_id(in p_auditory_id int)
begin
    select * from all_findings where finding_auditory_id = p_auditory_id;
end $$

delimiter ;

'''
select * from user;

select * from all_users;
call get_auditorias_by_user("admin_1");

call get_auditorias_by_user("auditor_ana");

call get_users_by_role("inspector_diego");

call get_users_by_role("auditor_ana")

call get_observations_by_auditory_id("3")

call get_findings_by_auditory_id("1");'''