/*Create database*/
create database stock_market_share_db;

/*Create tables*/
create table public.security_info
(
    id       integer primary key not null default nextval('security_info_id_seq'::regclass),
    username character varying(255),
    password character varying(255),
    user_id  integer,
    foreign key (user_id) references public.users (id)
        match simple on update no action on delete cascade
);

create table public.stock
(
    id       integer primary key    not null default nextval('stock_id_seq'::regclass),
    symbol   character varying(255) not null,
    name     character varying(255),
    currency character varying(255),
    exchange character varying(255),
    mic_code character varying(255),
    country  character varying(255),
    type     character varying(255),
    date     date                   not null
);

create table public.userfavoritestock
(
    id       integer primary key not null default nextval('userfavoritestock_id_seq'::regclass),
    user_id  integer,
    stock_id integer,
    foreign key (stock_id) references public.stock (id)
        match simple on update no action on delete cascade,
    foreign key (user_id) references public.users (id)
        match simple on update no action on delete cascade
);

create table public.users
(
    id        integer primary key    not null default nextval('users_id_seq'::regclass),
    firstname character varying(255),
    lastname  character varying(255),
    email     character varying(255) not null,
    age       integer,
    role      character varying(255)
);
create unique index unique_email_constraint on users using btree (email);

create table public.role
(
    "user" character varying(255),
    admin  character varying(255)
);

create table public.token
(
    id        integer primary key not null default nextval('token_id_seq'::regclass),
    body      character varying(255),
    createdat date
);


