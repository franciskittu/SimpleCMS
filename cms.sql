--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: css; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE css (
    id integer NOT NULL,
    nome character varying(30),
    descrizione character varying(255),
    file character varying(255) NOT NULL
);


ALTER TABLE public.css OWNER TO postgres;

--
-- Name: css_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE css_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.css_id_seq OWNER TO postgres;

--
-- Name: css_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE css_id_seq OWNED BY css.id;


--
-- Name: immagine; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE immagine (
    id integer NOT NULL,
    nome character varying(40) NOT NULL,
    dimensione integer NOT NULL,
    file character varying(255) NOT NULL,
    digest character varying(255) NOT NULL,
    data_upload timestamp without time zone NOT NULL,
    id_utente integer NOT NULL,
    tipo character varying(30)
);


ALTER TABLE public.immagine OWNER TO postgres;

--
-- Name: immagine_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE immagine_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.immagine_id_seq OWNER TO postgres;

--
-- Name: immagine_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE immagine_id_seq OWNED BY immagine.id;


--
-- Name: pagina; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE pagina (
    id integer NOT NULL,
    titolo character varying(30),
    body text,
    id_padre integer,
    id_sito integer,
    modello boolean DEFAULT false
);


ALTER TABLE public.pagina OWNER TO postgres;

--
-- Name: pagina_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE pagina_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.pagina_id_seq OWNER TO postgres;

--
-- Name: pagina_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE pagina_id_seq OWNED BY pagina.id;


--
-- Name: sito; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE sito (
    id integer NOT NULL,
    header text NOT NULL,
    footer text NOT NULL,
    id_utente integer NOT NULL,
    homepage integer,
    descrizione character varying(255),
    id_css integer
);


ALTER TABLE public.sito OWNER TO postgres;

--
-- Name: sito_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE sito_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.sito_id_seq OWNER TO postgres;

--
-- Name: sito_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE sito_id_seq OWNED BY sito.id;


--
-- Name: slide; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE slide (
    id integer NOT NULL,
    descrizione character varying(30),
    posizione smallint NOT NULL,
    file character varying(255) NOT NULL,
    nome character varying(50)
);


ALTER TABLE public.slide OWNER TO postgres;

--
-- Name: slide_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE slide_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.slide_id_seq OWNER TO postgres;

--
-- Name: slide_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE slide_id_seq OWNED BY slide.id;


--
-- Name: utente; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE utente (
    id integer NOT NULL,
    username character varying(20) NOT NULL,
    password character varying(40) NOT NULL,
    nome character varying(30),
    cognome character varying(30),
    email character varying(60) NOT NULL,
    data_di_nascita date NOT NULL,
    spazio_disp_img integer NOT NULL
);


ALTER TABLE public.utente OWNER TO postgres;

--
-- Name: utente_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE utente_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.utente_id_seq OWNER TO postgres;

--
-- Name: utente_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE utente_id_seq OWNED BY utente.id;


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY css ALTER COLUMN id SET DEFAULT nextval('css_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY immagine ALTER COLUMN id SET DEFAULT nextval('immagine_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY pagina ALTER COLUMN id SET DEFAULT nextval('pagina_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sito ALTER COLUMN id SET DEFAULT nextval('sito_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY slide ALTER COLUMN id SET DEFAULT nextval('slide_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY utente ALTER COLUMN id SET DEFAULT nextval('utente_id_seq'::regclass);


--
-- Data for Name: css; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO css (id, nome, descrizione, file) VALUES (1, 'n', 'd', 'f');
INSERT INTO css (id, nome, descrizione, file) VALUES (2, 'n', 'd', 'f');
INSERT INTO css (id, nome, descrizione, file) VALUES (3, 'stiloso', 'un bellissimo css', 'css/stylish.css');
INSERT INTO css (id, nome, descrizione, file) VALUES (4, '', '', 'css/.css');
INSERT INTO css (id, nome, descrizione, file) VALUES (5, 'GNU', '', 'css/.css');
INSERT INTO css (id, nome, descrizione, file) VALUES (6, 'GNU', '', 'css/.css');
INSERT INTO css (id, nome, descrizione, file) VALUES (7, 'fogliodistile', 'ottimo!', 'css/ciccu.css');
INSERT INTO css (id, nome, descrizione, file) VALUES (8, 'fogliodistile', 'ottimo!', 'css/fogliodistile.css');
INSERT INTO css (id, nome, descrizione, file) VALUES (9, 'fogliodistile', 'ottimo!', 'css/fogliodistile.css');


--
-- Name: css_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('css_id_seq', 9, true);


--
-- Data for Name: immagine; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO immagine (id, nome, dimensione, file, digest, data_upload, id_utente, tipo) VALUES (4, 'puttana', 75323, 'images/-51-75-738281840-54107-15-99-2212353-78-1125377-68-38.jpg', '-51-75-738281840-54107-15-99-2212353-78-1125377-68-38', '2013-12-16 16:19:20.344', 2, NULL);
INSERT INTO immagine (id, nome, dimensione, file, digest, data_upload, id_utente, tipo) VALUES (5, 'CAAAAAH', 42370, 'images/-61-248-76-5651-268484-9741-12560-7139843612416-17.jpg', '-61-248-76-5651-268484-9741-12560-7139843612416-17', '2013-12-16 16:24:01.84', 2, NULL);
INSERT INTO immagine (id, nome, dimensione, file, digest, data_upload, id_utente, tipo) VALUES (6, 'cernia', 75185, 'images/84988739583114644539-4656-61109-24-66-1197260127.jpg', '84988739583114644539-4656-61109-24-66-1197260127', '2013-12-16 16:27:54.878', 2, NULL);
INSERT INTO immagine (id, nome, dimensione, file, digest, data_upload, id_utente, tipo) VALUES (7, 'lavatrice', 21147, 'images/111-621-40-69-101-22-41962-74-41126115-6210310469-8977.jpg', '111-621-40-69-101-22-41962-74-41126115-6210310469-8977', '2013-12-16 23:14:53.985', 2, NULL);
INSERT INTO immagine (id, nome, dimensione, file, digest, data_upload, id_utente, tipo) VALUES (8, 'finoaquituttobene', 4878, 'images/104-40-27753611414-20-4471126-8015-65-283-3119112-18.jpg', '104-40-27753611414-20-4471126-8015-65-283-3119112-18', '2013-12-16 23:29:16.607', 2, NULL);
INSERT INTO immagine (id, nome, dimensione, file, digest, data_upload, id_utente, tipo) VALUES (9, 'nevermind', 548121, 'images/978389-96-71661633190-125-68-1411688-5834-49748.jpg', '978389-96-71661633190-125-68-1411688-5834-49748', '2014-01-04 12:50:04.012', 2, 'image/jpeg');


--
-- Name: immagine_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('immagine_id_seq', 9, true);


--
-- Data for Name: pagina; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO pagina (id, titolo, body, id_padre, id_sito, modello) VALUES (3, 'homepage', '<div>questo è il corpo della homepage</div>', NULL, 5, false);
INSERT INTO pagina (id, titolo, body, id_padre, id_sito, modello) VALUES (4, 'pagina1', '<div>questo è il corpo della pagina1</div>', 3, 5, false);
INSERT INTO pagina (id, titolo, body, id_padre, id_sito, modello) VALUES (5, 'pagina2', '<div>questo è il corpo della pagina2</div>', 3, 5, false);
INSERT INTO pagina (id, titolo, body, id_padre, id_sito, modello) VALUES (6, 'pagina3', '<div>questo è il corpo della pagina3 che è foglia della pagina 1</div>', 4, 5, false);


--
-- Name: pagina_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('pagina_id_seq', 6, true);


--
-- Data for Name: sito; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO sito (id, header, footer, id_utente, homepage, descrizione, id_css) VALUES (5, '<div><h1>Questa &egrave; l\intestazione</h1></div>', '<div><h2>Questo è il pi&egrave; di pagina</h2></div>', 2, 3, NULL, NULL);


--
-- Name: sito_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('sito_id_seq', 5, true);


--
-- Data for Name: slide; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO slide (id, descrizione, posizione, file, nome) VALUES (1, 'kurt in concerto', 1, 'slides/-103-114-128-45-88401276688-116-5392-4966119-34-92106-39.jpg', 'kurtd cobain');
INSERT INTO slide (id, descrizione, posizione, file, nome) VALUES (2, 'nirvana in water', 2, 'slides/-69-84-5727-30-6168-18050-2263-55-107-46-32-84-11852-64.jpg', 'nirvana');


--
-- Name: slide_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('slide_id_seq', 2, true);


--
-- Data for Name: utente; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO utente (id, username, password, nome, cognome, email, data_di_nascita, spazio_disp_img) VALUES (2, 'franciskittu', 'password', 'francesco', NULL, 'franciskittu@gmail.com', '1991-06-14', 2232976);


--
-- Name: utente_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('utente_id_seq', 2, true);


--
-- Name: css_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY css
    ADD CONSTRAINT css_pkey PRIMARY KEY (id);


--
-- Name: immagine_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY immagine
    ADD CONSTRAINT immagine_pkey PRIMARY KEY (id);


--
-- Name: pagina_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY pagina
    ADD CONSTRAINT pagina_pkey PRIMARY KEY (id);


--
-- Name: sito_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY sito
    ADD CONSTRAINT sito_pkey PRIMARY KEY (id);


--
-- Name: slide_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY slide
    ADD CONSTRAINT slide_pkey PRIMARY KEY (id);


--
-- Name: utente_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY utente
    ADD CONSTRAINT utente_pkey PRIMARY KEY (id);


--
-- Name: utente_username_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY utente
    ADD CONSTRAINT utente_username_key UNIQUE (username);


--
-- Name: immagine_id_utente_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY immagine
    ADD CONSTRAINT immagine_id_utente_fkey FOREIGN KEY (id_utente) REFERENCES utente(id);


--
-- Name: pagina_id_padre_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY pagina
    ADD CONSTRAINT pagina_id_padre_fkey FOREIGN KEY (id_padre) REFERENCES pagina(id) ON UPDATE CASCADE;


--
-- Name: pagina_id_sito_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY pagina
    ADD CONSTRAINT pagina_id_sito_fkey FOREIGN KEY (id_sito) REFERENCES sito(id) ON DELETE SET NULL;


--
-- Name: sito_homepage_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sito
    ADD CONSTRAINT sito_homepage_fkey FOREIGN KEY (homepage) REFERENCES pagina(id);


--
-- Name: sito_id_css_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sito
    ADD CONSTRAINT sito_id_css_fkey FOREIGN KEY (id_css) REFERENCES css(id);


--
-- Name: sito_id_utente_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sito
    ADD CONSTRAINT sito_id_utente_fkey FOREIGN KEY (id_utente) REFERENCES utente(id) ON DELETE CASCADE;


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- Name: css; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE css FROM PUBLIC;
REVOKE ALL ON TABLE css FROM postgres;
GRANT ALL ON TABLE css TO postgres;
GRANT ALL ON TABLE css TO cms_user;


--
-- Name: css_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE css_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE css_id_seq FROM postgres;
GRANT ALL ON SEQUENCE css_id_seq TO postgres;
GRANT ALL ON SEQUENCE css_id_seq TO cms_user;


--
-- Name: immagine; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE immagine FROM PUBLIC;
REVOKE ALL ON TABLE immagine FROM postgres;
GRANT ALL ON TABLE immagine TO postgres;
GRANT ALL ON TABLE immagine TO cms_user;


--
-- Name: immagine_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE immagine_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE immagine_id_seq FROM postgres;
GRANT ALL ON SEQUENCE immagine_id_seq TO postgres;
GRANT ALL ON SEQUENCE immagine_id_seq TO cms_user;


--
-- Name: pagina; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE pagina FROM PUBLIC;
REVOKE ALL ON TABLE pagina FROM postgres;
GRANT ALL ON TABLE pagina TO postgres;
GRANT ALL ON TABLE pagina TO cms_user;


--
-- Name: pagina_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE pagina_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE pagina_id_seq FROM postgres;
GRANT ALL ON SEQUENCE pagina_id_seq TO postgres;
GRANT ALL ON SEQUENCE pagina_id_seq TO cms_user;


--
-- Name: sito; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE sito FROM PUBLIC;
REVOKE ALL ON TABLE sito FROM postgres;
GRANT ALL ON TABLE sito TO postgres;
GRANT ALL ON TABLE sito TO cms_user;


--
-- Name: sito_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE sito_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE sito_id_seq FROM postgres;
GRANT ALL ON SEQUENCE sito_id_seq TO postgres;
GRANT ALL ON SEQUENCE sito_id_seq TO cms_user;


--
-- Name: slide; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE slide FROM PUBLIC;
REVOKE ALL ON TABLE slide FROM postgres;
GRANT ALL ON TABLE slide TO postgres;
GRANT ALL ON TABLE slide TO cms_user;


--
-- Name: slide_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE slide_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE slide_id_seq FROM postgres;
GRANT ALL ON SEQUENCE slide_id_seq TO postgres;
GRANT ALL ON SEQUENCE slide_id_seq TO cms_user;


--
-- Name: utente; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE utente FROM PUBLIC;
REVOKE ALL ON TABLE utente FROM postgres;
GRANT ALL ON TABLE utente TO postgres;
GRANT ALL ON TABLE utente TO cms_user;


--
-- Name: utente_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE utente_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE utente_id_seq FROM postgres;
GRANT ALL ON SEQUENCE utente_id_seq TO postgres;
GRANT ALL ON SEQUENCE utente_id_seq TO cms_user;


--
-- PostgreSQL database dump complete
--

