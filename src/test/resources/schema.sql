CREATE TABLE IF NOT EXISTS users
(
    uuid character varying COLLATE pg_catalog."default" NOT NULL,
    name character varying COLLATE pg_catalog."default",
    phone character varying COLLATE pg_catalog."default",
    email character varying COLLATE pg_catalog."default",
    CONSTRAINT users_pkey PRIMARY KEY (uuid)
);
