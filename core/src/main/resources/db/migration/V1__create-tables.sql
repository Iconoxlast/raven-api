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
INSERT INTO publishers (publ_name) VALUES ('DC'),('Marvel');
create table characters(
    char_id bigint not null auto_increment PRIMARY KEY,
    char_publ_id bigint not null,
    char_page_name varchar(100) not null,
    char_latest_update datetime not null,
    UNIQUE KEY cver_uq (char_publ_id, char_page_name),
    FOREIGN KEY (char_publ_id) REFERENCES publishers(publ_id)
);
create table character_versions(
    cver_id bigint not null auto_increment PRIMARY KEY,
    cver_publ_id bigint not null,
    cver_page_name varchar(100) not null,
    cver_latest_update datetime not null,
    UNIQUE KEY cver_uq (cver_publ_id, cver_page_name),
    FOREIGN KEY (cver_publ_id) REFERENCES publishers(publ_id)

);
create table character_disambiguations(
    char_id bigint not null,
    cver_id bigint not null,
    PRIMARY KEY (char_id, cver_id),
    FOREIGN KEY (char_id) REFERENCES characters(char_id) ON DELETE cascade,
    FOREIGN KEY (cver_id) REFERENCES character_versions(cver_id) ON DELETE cascade
);
create table issues(
    iss_page_id bigint not null PRIMARY KEY,
    iss_publ_id bigint not null,
    iss_page_name varchar(100) not null,
    iss_publication_date date not null, 
    FOREIGN KEY (iss_publ_id) REFERENCES publishers(publ_id)
);
create table character_appearances(
    iss_page_id bigint not null,
    cver_id bigint not null,
    PRIMARY KEY (iss_page_id, cver_id),
    FOREIGN KEY (iss_page_id) REFERENCES issues(iss_page_id) ON DELETE cascade,
    FOREIGN KEY (cver_id) REFERENCES character_versions(cver_id) ON DELETE cascade
);