create table serveur
(
	serveur_id serial not null
		constraint serveur_pk
			primary key,
	nom varchar,
	prenom varchar
);

alter table serveur owner to postgres;

create table plat
(
	plat_id serial not null
		constraint plat_pk
			primary key,
	nom varchar,
	prix_unitaire double precision
);

alter table plat owner to postgres;

create table tables
(
	tables_id serial not null
		constraint table_pk
			primary key,
	nom varchar,
	nbconvive integer
);

alter table tables owner to postgres;

create table facture
(
	facture_id serial not null
		constraint facture_pk
			primary key,
	serveur_idx integer
		constraint facture_serveur_serveur_id_fk
			references serveur,
	tables_idx integer
		constraint facture_table_table_id_fk
			references tables
);

alter table facture owner to postgres;

create table facture_plat
(
	plat_idx integer
		constraint facture_plat_plat_plat_id_fk
			references plat,
	facture_idx integer
		constraint facture_plat_facture_facture_id_fk
			references facture
);

alter table facture_plat owner to postgres;

