create table request_log(

    reqs_id bigint not null auto_increment PRIMARY KEY,
    reqs_method varchar(10) not null,
    reqs_uri varchar(200) not null,
    reqs_client_ip varchar(100) not null,
    reqs_status int not null,
    reqs_user_agent varchar(200) not null,
    reqs_locale varchar(50) not null,
    reqs_response_time int not null,
    reqs_timestamp datetime not null

);
create table publishers(

    publ_id bigint not null auto_increment PRIMARY KEY,
    publ_name varchar(20) not null

);
create table characters(

    char_id bigint not null auto_increment PRIMARY KEY,
    char_publ_id bigint not null,
    char_page_name varchar(100) not null,
    char_latest_update datetime not null,
    FOREIGN KEY (char_publ_id) REFERENCES publishers(publ_id)

);
create table character_disambiguations(

    char_id bigint not null,
    cver_id bigint not null,
    FOREIGN KEY (char_id) REFERENCES characters(char_id),
    FOREIGN KEY (cver_id) REFERENCES character_versions(cver_id)
);
create table character_versions(

    cver_id bigint not null auto_increment PRIMARY KEY,
    cver_char_id bigint not null,
    cver_page_name varchar(100) not null UNIQUE,
    char_latest_update datetime not null,
    FOREIGN KEY (cver_char_id) REFERENCES characters(char_id)

);
create table issues(

    iss_page_id bigint not null PRIMARY KEY,
    iss_publ_id bigint not null,
    iss_page_name varchar(100) not null UNIQUE,
    iss_publication_date date not null, 
    FOREIGN KEY (iss_publ_id) REFERENCES publishers(publ_id)

);
create table character_appearances(

    iss_page_name varchar(100) not null,
    cver_id bigint not null,
    FOREIGN KEY (iss_page_name) REFERENCES issues(iss_page_name),
    FOREIGN KEY (cver_id) REFERENCES character_versions(cver_id)

);