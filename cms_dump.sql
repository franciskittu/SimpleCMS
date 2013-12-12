--
-- PostgreSQL database dump
--

-- Dumped from database version 9.1.9
-- Dumped by pg_dump version 9.1.9
-- Started on 2013-12-12 18:47:00 CET

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

SET search_path = public, pg_catalog;

ALTER TABLE ONLY public.sito DROP CONSTRAINT sito_id_utente_fkey;
ALTER TABLE ONLY public.pagina DROP CONSTRAINT pagina_id_padre_fkey;
ALTER TABLE ONLY public.pagina DROP CONSTRAINT pagina_homepage_fkey;
ALTER TABLE ONLY public.pagina DROP CONSTRAINT pagina_css_fkey;
ALTER TABLE ONLY public.immagine DROP CONSTRAINT immagine_id_utente_fkey;
ALTER TABLE ONLY public.utente DROP CONSTRAINT utente_username_key;
ALTER TABLE ONLY public.utente DROP CONSTRAINT utente_pkey;
ALTER TABLE ONLY public.tab DROP CONSTRAINT tab_pkey;
ALTER TABLE ONLY public.sito DROP CONSTRAINT sito_pkey;
ALTER TABLE ONLY public.pagina DROP CONSTRAINT pagina_pkey;
ALTER TABLE ONLY public.immagine DROP CONSTRAINT immagine_pkey;
ALTER TABLE ONLY public.css DROP CONSTRAINT css_pkey;
ALTER TABLE public.utente ALTER COLUMN id DROP DEFAULT;
ALTER TABLE public.tab ALTER COLUMN chiave DROP DEFAULT;
ALTER TABLE public.sito ALTER COLUMN id DROP DEFAULT;
ALTER TABLE public.pagina ALTER COLUMN id DROP DEFAULT;
ALTER TABLE public.immagine ALTER COLUMN id DROP DEFAULT;
ALTER TABLE public.css ALTER COLUMN id DROP DEFAULT;
DROP SEQUENCE public.utente_id_seq;
DROP TABLE public.utente;
DROP SEQUENCE public.tab_chiave_seq;
DROP TABLE public.tab;
DROP SEQUENCE public.sito_id_seq;
DROP TABLE public.sito;
DROP SEQUENCE public.pagina_id_seq;
DROP TABLE public.pagina;
DROP SEQUENCE public.immagine_id_seq;
DROP TABLE public.immagine;
DROP SEQUENCE public.css_id_seq;
DROP TABLE public.css;
DROP EXTENSION plpgsql;
DROP SCHEMA public;
--
-- TOC entry 5 (class 2615 OID 2200)
-- Name: public; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA public;


ALTER SCHEMA public OWNER TO postgres;

--
-- TOC entry 1928 (class 0 OID 0)
-- Dependencies: 5
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: postgres
--

COMMENT ON SCHEMA public IS 'standard public schema';


--
-- TOC entry 173 (class 3079 OID 11645)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 1930 (class 0 OID 0)
-- Dependencies: 173
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 170 (class 1259 OID 16475)
-- Dependencies: 5
-- Name: css; Type: TABLE; Schema: public; Owner: cms_user; Tablespace: 
--

CREATE TABLE css (
    id integer NOT NULL,
    nome character varying(30),
    descrizione character varying(255),
    file character varying(255) NOT NULL
);


ALTER TABLE public.css OWNER TO cms_user;

--
-- TOC entry 169 (class 1259 OID 16473)
-- Dependencies: 5 170
-- Name: css_id_seq; Type: SEQUENCE; Schema: public; Owner: cms_user
--

CREATE SEQUENCE css_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.css_id_seq OWNER TO cms_user;

--
-- TOC entry 1931 (class 0 OID 0)
-- Dependencies: 169
-- Name: css_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: cms_user
--

ALTER SEQUENCE css_id_seq OWNED BY css.id;


--
-- TOC entry 166 (class 1259 OID 16443)
-- Dependencies: 5
-- Name: immagine; Type: TABLE; Schema: public; Owner: cms_user; Tablespace: 
--

CREATE TABLE immagine (
    id integer NOT NULL,
    nome character varying(40) NOT NULL,
    dimensione integer NOT NULL,
    tipo character varying(9) NOT NULL,
    file character varying(255) NOT NULL,
    digest character varying(255) NOT NULL,
    data_upload date NOT NULL,
    id_utente integer NOT NULL
);


ALTER TABLE public.immagine OWNER TO cms_user;

--
-- TOC entry 165 (class 1259 OID 16441)
-- Dependencies: 5 166
-- Name: immagine_id_seq; Type: SEQUENCE; Schema: public; Owner: cms_user
--

CREATE SEQUENCE immagine_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.immagine_id_seq OWNER TO cms_user;

--
-- TOC entry 1932 (class 0 OID 0)
-- Dependencies: 165
-- Name: immagine_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: cms_user
--

ALTER SEQUENCE immagine_id_seq OWNED BY immagine.id;


--
-- TOC entry 172 (class 1259 OID 16486)
-- Dependencies: 5
-- Name: pagina; Type: TABLE; Schema: public; Owner: cms_user; Tablespace: 
--

CREATE TABLE pagina (
    id integer NOT NULL,
    titolo character varying(30),
    body text,
    css integer,
    id_padre integer,
    homepage integer
);


ALTER TABLE public.pagina OWNER TO cms_user;

--
-- TOC entry 171 (class 1259 OID 16484)
-- Dependencies: 5 172
-- Name: pagina_id_seq; Type: SEQUENCE; Schema: public; Owner: cms_user
--

CREATE SEQUENCE pagina_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.pagina_id_seq OWNER TO cms_user;

--
-- TOC entry 1933 (class 0 OID 0)
-- Dependencies: 171
-- Name: pagina_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: cms_user
--

ALTER SEQUENCE pagina_id_seq OWNED BY pagina.id;


--
-- TOC entry 168 (class 1259 OID 16459)
-- Dependencies: 5
-- Name: sito; Type: TABLE; Schema: public; Owner: cms_user; Tablespace: 
--

CREATE TABLE sito (
    id integer NOT NULL,
    header text NOT NULL,
    footer text NOT NULL,
    id_utente integer NOT NULL
);


ALTER TABLE public.sito OWNER TO cms_user;

--
-- TOC entry 167 (class 1259 OID 16457)
-- Dependencies: 168 5
-- Name: sito_id_seq; Type: SEQUENCE; Schema: public; Owner: cms_user
--

CREATE SEQUENCE sito_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.sito_id_seq OWNER TO cms_user;

--
-- TOC entry 1934 (class 0 OID 0)
-- Dependencies: 167
-- Name: sito_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: cms_user
--

ALTER SEQUENCE sito_id_seq OWNED BY sito.id;


--
-- TOC entry 162 (class 1259 OID 16425)
-- Dependencies: 5
-- Name: tab; Type: TABLE; Schema: public; Owner: cms_user; Tablespace: 
--

CREATE TABLE tab (
    chiave integer NOT NULL,
    nome character varying(30),
    cognome character varying(30)
);


ALTER TABLE public.tab OWNER TO cms_user;

--
-- TOC entry 161 (class 1259 OID 16423)
-- Dependencies: 5 162
-- Name: tab_chiave_seq; Type: SEQUENCE; Schema: public; Owner: cms_user
--

CREATE SEQUENCE tab_chiave_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.tab_chiave_seq OWNER TO cms_user;

--
-- TOC entry 1935 (class 0 OID 0)
-- Dependencies: 161
-- Name: tab_chiave_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: cms_user
--

ALTER SEQUENCE tab_chiave_seq OWNED BY tab.chiave;


--
-- TOC entry 164 (class 1259 OID 16433)
-- Dependencies: 5
-- Name: utente; Type: TABLE; Schema: public; Owner: cms_user; Tablespace: 
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


ALTER TABLE public.utente OWNER TO cms_user;

--
-- TOC entry 163 (class 1259 OID 16431)
-- Dependencies: 164 5
-- Name: utente_id_seq; Type: SEQUENCE; Schema: public; Owner: cms_user
--

CREATE SEQUENCE utente_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.utente_id_seq OWNER TO cms_user;

--
-- TOC entry 1936 (class 0 OID 0)
-- Dependencies: 163
-- Name: utente_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: cms_user
--

ALTER SEQUENCE utente_id_seq OWNED BY utente.id;


--
-- TOC entry 1890 (class 2604 OID 16478)
-- Dependencies: 170 169 170
-- Name: id; Type: DEFAULT; Schema: public; Owner: cms_user
--

ALTER TABLE ONLY css ALTER COLUMN id SET DEFAULT nextval('css_id_seq'::regclass);


--
-- TOC entry 1888 (class 2604 OID 16446)
-- Dependencies: 165 166 166
-- Name: id; Type: DEFAULT; Schema: public; Owner: cms_user
--

ALTER TABLE ONLY immagine ALTER COLUMN id SET DEFAULT nextval('immagine_id_seq'::regclass);


--
-- TOC entry 1891 (class 2604 OID 16489)
-- Dependencies: 172 171 172
-- Name: id; Type: DEFAULT; Schema: public; Owner: cms_user
--

ALTER TABLE ONLY pagina ALTER COLUMN id SET DEFAULT nextval('pagina_id_seq'::regclass);


--
-- TOC entry 1889 (class 2604 OID 16462)
-- Dependencies: 168 167 168
-- Name: id; Type: DEFAULT; Schema: public; Owner: cms_user
--

ALTER TABLE ONLY sito ALTER COLUMN id SET DEFAULT nextval('sito_id_seq'::regclass);


--
-- TOC entry 1886 (class 2604 OID 16428)
-- Dependencies: 162 161 162
-- Name: chiave; Type: DEFAULT; Schema: public; Owner: cms_user
--

ALTER TABLE ONLY tab ALTER COLUMN chiave SET DEFAULT nextval('tab_chiave_seq'::regclass);


--
-- TOC entry 1887 (class 2604 OID 16436)
-- Dependencies: 164 163 164
-- Name: id; Type: DEFAULT; Schema: public; Owner: cms_user
--

ALTER TABLE ONLY utente ALTER COLUMN id SET DEFAULT nextval('utente_id_seq'::regclass);


--
-- TOC entry 1920 (class 0 OID 16475)
-- Dependencies: 170 1923
-- Data for Name: css; Type: TABLE DATA; Schema: public; Owner: cms_user
--

COPY css (id, nome, descrizione, file) FROM stdin;
\.


--
-- TOC entry 1937 (class 0 OID 0)
-- Dependencies: 169
-- Name: css_id_seq; Type: SEQUENCE SET; Schema: public; Owner: cms_user
--

SELECT pg_catalog.setval('css_id_seq', 1, false);


--
-- TOC entry 1916 (class 0 OID 16443)
-- Dependencies: 166 1923
-- Data for Name: immagine; Type: TABLE DATA; Schema: public; Owner: cms_user
--

COPY immagine (id, nome, dimensione, tipo, file, digest, data_upload, id_utente) FROM stdin;
\.


--
-- TOC entry 1938 (class 0 OID 0)
-- Dependencies: 165
-- Name: immagine_id_seq; Type: SEQUENCE SET; Schema: public; Owner: cms_user
--

SELECT pg_catalog.setval('immagine_id_seq', 1, false);


--
-- TOC entry 1922 (class 0 OID 16486)
-- Dependencies: 172 1923
-- Data for Name: pagina; Type: TABLE DATA; Schema: public; Owner: cms_user
--

COPY pagina (id, titolo, body, css, id_padre, homepage) FROM stdin;
\.


--
-- TOC entry 1939 (class 0 OID 0)
-- Dependencies: 171
-- Name: pagina_id_seq; Type: SEQUENCE SET; Schema: public; Owner: cms_user
--

SELECT pg_catalog.setval('pagina_id_seq', 1, false);


--
-- TOC entry 1918 (class 0 OID 16459)
-- Dependencies: 168 1923
-- Data for Name: sito; Type: TABLE DATA; Schema: public; Owner: cms_user
--

COPY sito (id, header, footer, id_utente) FROM stdin;
\.


--
-- TOC entry 1940 (class 0 OID 0)
-- Dependencies: 167
-- Name: sito_id_seq; Type: SEQUENCE SET; Schema: public; Owner: cms_user
--

SELECT pg_catalog.setval('sito_id_seq', 1, false);


--
-- TOC entry 1912 (class 0 OID 16425)
-- Dependencies: 162 1923
-- Data for Name: tab; Type: TABLE DATA; Schema: public; Owner: cms_user
--

COPY tab (chiave, nome, cognome) FROM stdin;
1	francesco	proietti
\.


--
-- TOC entry 1941 (class 0 OID 0)
-- Dependencies: 161
-- Name: tab_chiave_seq; Type: SEQUENCE SET; Schema: public; Owner: cms_user
--

SELECT pg_catalog.setval('tab_chiave_seq', 1, true);


--
-- TOC entry 1914 (class 0 OID 16433)
-- Dependencies: 164 1923
-- Data for Name: utente; Type: TABLE DATA; Schema: public; Owner: cms_user
--

COPY utente (id, username, password, nome, cognome, email, data_di_nascita, spazio_disp_img) FROM stdin;
\.


--
-- TOC entry 1942 (class 0 OID 0)
-- Dependencies: 163
-- Name: utente_id_seq; Type: SEQUENCE SET; Schema: public; Owner: cms_user
--

SELECT pg_catalog.setval('utente_id_seq', 1, false);


--
-- TOC entry 1903 (class 2606 OID 16483)
-- Dependencies: 170 170 1924
-- Name: css_pkey; Type: CONSTRAINT; Schema: public; Owner: cms_user; Tablespace: 
--

ALTER TABLE ONLY css
    ADD CONSTRAINT css_pkey PRIMARY KEY (id);


--
-- TOC entry 1899 (class 2606 OID 16451)
-- Dependencies: 166 166 1924
-- Name: immagine_pkey; Type: CONSTRAINT; Schema: public; Owner: cms_user; Tablespace: 
--

ALTER TABLE ONLY immagine
    ADD CONSTRAINT immagine_pkey PRIMARY KEY (id);


--
-- TOC entry 1905 (class 2606 OID 16494)
-- Dependencies: 172 172 1924
-- Name: pagina_pkey; Type: CONSTRAINT; Schema: public; Owner: cms_user; Tablespace: 
--

ALTER TABLE ONLY pagina
    ADD CONSTRAINT pagina_pkey PRIMARY KEY (id);


--
-- TOC entry 1901 (class 2606 OID 16467)
-- Dependencies: 168 168 1924
-- Name: sito_pkey; Type: CONSTRAINT; Schema: public; Owner: cms_user; Tablespace: 
--

ALTER TABLE ONLY sito
    ADD CONSTRAINT sito_pkey PRIMARY KEY (id);


--
-- TOC entry 1893 (class 2606 OID 16430)
-- Dependencies: 162 162 1924
-- Name: tab_pkey; Type: CONSTRAINT; Schema: public; Owner: cms_user; Tablespace: 
--

ALTER TABLE ONLY tab
    ADD CONSTRAINT tab_pkey PRIMARY KEY (chiave);


--
-- TOC entry 1895 (class 2606 OID 16438)
-- Dependencies: 164 164 1924
-- Name: utente_pkey; Type: CONSTRAINT; Schema: public; Owner: cms_user; Tablespace: 
--

ALTER TABLE ONLY utente
    ADD CONSTRAINT utente_pkey PRIMARY KEY (id);


--
-- TOC entry 1897 (class 2606 OID 16440)
-- Dependencies: 164 164 1924
-- Name: utente_username_key; Type: CONSTRAINT; Schema: public; Owner: cms_user; Tablespace: 
--

ALTER TABLE ONLY utente
    ADD CONSTRAINT utente_username_key UNIQUE (username);


--
-- TOC entry 1906 (class 2606 OID 16452)
-- Dependencies: 1894 166 164 1924
-- Name: immagine_id_utente_fkey; Type: FK CONSTRAINT; Schema: public; Owner: cms_user
--

ALTER TABLE ONLY immagine
    ADD CONSTRAINT immagine_id_utente_fkey FOREIGN KEY (id_utente) REFERENCES utente(id);


--
-- TOC entry 1908 (class 2606 OID 16495)
-- Dependencies: 1902 170 172 1924
-- Name: pagina_css_fkey; Type: FK CONSTRAINT; Schema: public; Owner: cms_user
--

ALTER TABLE ONLY pagina
    ADD CONSTRAINT pagina_css_fkey FOREIGN KEY (css) REFERENCES css(id);


--
-- TOC entry 1910 (class 2606 OID 16505)
-- Dependencies: 172 1904 172 1924
-- Name: pagina_homepage_fkey; Type: FK CONSTRAINT; Schema: public; Owner: cms_user
--

ALTER TABLE ONLY pagina
    ADD CONSTRAINT pagina_homepage_fkey FOREIGN KEY (homepage) REFERENCES pagina(id);


--
-- TOC entry 1909 (class 2606 OID 16500)
-- Dependencies: 172 1904 172 1924
-- Name: pagina_id_padre_fkey; Type: FK CONSTRAINT; Schema: public; Owner: cms_user
--

ALTER TABLE ONLY pagina
    ADD CONSTRAINT pagina_id_padre_fkey FOREIGN KEY (id_padre) REFERENCES pagina(id);


--
-- TOC entry 1907 (class 2606 OID 16468)
-- Dependencies: 1894 164 168 1924
-- Name: sito_id_utente_fkey; Type: FK CONSTRAINT; Schema: public; Owner: cms_user
--

ALTER TABLE ONLY sito
    ADD CONSTRAINT sito_id_utente_fkey FOREIGN KEY (id_utente) REFERENCES utente(id);


--
-- TOC entry 1929 (class 0 OID 0)
-- Dependencies: 5
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2013-12-12 18:47:01 CET

--
-- PostgreSQL database dump complete
--

