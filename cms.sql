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
    tipo character varying(30),
    thumb character varying(255),
    cover boolean DEFAULT false
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
    id_css integer,
    data_creazione date DEFAULT ('now'::text)::date,
    nome character varying(50) NOT NULL
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
    password character varying(255) NOT NULL,
    nome character varying(30) NOT NULL,
    cognome character varying(30) NOT NULL,
    email character varying(60) NOT NULL,
    spazio_disp_img integer NOT NULL,
    codice_attivazione character varying(255),
    attivato boolean DEFAULT false,
    admin boolean DEFAULT false
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

INSERT INTO css (id, nome, descrizione, file) VALUES (11, 'orange', 'stile arancione', 'css/orange.css');
INSERT INTO css (id, nome, descrizione, file) VALUES (12, 'green', 'stile verde', 'css/green.css');
INSERT INTO css (id, nome, descrizione, file) VALUES (13, 'cyan', 'stile ciano', 'css/cyan.css');


--
-- Name: css_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('css_id_seq', 13, true);


--
-- Data for Name: immagine; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO immagine (id, nome, dimensione, file, digest, data_upload, id_utente, tipo, thumb, cover) VALUES (49, 'bacialassio_450ingr', 26766, 'images/-4625112-6222-10214050-1076112-5120-1082839-51-14.jpg', '-4625112-6222-10214050-1076112-5120-1082839-51-14', '2014-02-18 16:49:02.046', 24, 'image/jpeg', 'images/thumbs/-4625112-6222-10214050-1076112-5120-1082839-51-14.jpg', false);
INSERT INTO immagine (id, nome, dimensione, file, digest, data_upload, id_utente, tipo, thumb, cover) VALUES (50, 'baciok_seq1', 39378, 'images/-1113746-818735-5191-21-231-68118120-33-74-120-28-79.jpg', '-1113746-818735-5191-21-231-68118120-33-74-120-28-79', '2014-02-18 16:49:10.005', 24, 'image/jpeg', 'images/thumbs/-1113746-818735-5191-21-231-68118120-33-74-120-28-79.jpg', false);
INSERT INTO immagine (id, nome, dimensione, file, digest, data_upload, id_utente, tipo, thumb, cover) VALUES (51, 'baciok_seq2', 27833, 'images/-126103-54342299109-65-9194-115-113-84110-63-62115-57-75.jpg', '-126103-54342299109-65-9194-115-113-84110-63-62115-57-75', '2014-02-18 16:49:23.163', 24, 'image/jpeg', 'images/thumbs/-126103-54342299109-65-9194-115-113-84110-63-62115-57-75.jpg', false);
INSERT INTO immagine (id, nome, dimensione, file, digest, data_upload, id_utente, tipo, thumb, cover) VALUES (48, 'dolciumi', 291829, 'images/-1077-86-3462-789252-81462-105-64-71-87-65105-68104-50.jpg', '-1077-86-3462-789252-81462-105-64-71-87-65105-68104-50', '2014-02-18 16:48:51.557', 24, 'image/jpeg', 'images/thumbs/-1077-86-3462-789252-81462-105-64-71-87-65105-68104-50.jpg', true);
INSERT INTO immagine (id, nome, dimensione, file, digest, data_upload, id_utente, tipo, thumb, cover) VALUES (52, 'logo_dolci', 76839, 'images/-30-55-521993-104-100-54-123-26-1255-10010941-44-111-19-9329.gif', '-30-55-521993-104-100-54-123-26-1255-10010941-44-111-19-9329', '2014-02-18 17:08:47.33', 24, 'image/gif', 'images/thumbs/-30-55-521993-104-100-54-123-26-1255-10010941-44-111-19-9329.gif', false);


--
-- Name: immagine_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('immagine_id_seq', 52, true);


--
-- Data for Name: pagina; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO pagina (id, titolo, body, id_padre, id_sito, modello) VALUES (50, 'Frutta', '<h1><b>FRUTTA</b></h1>

<h2><b>Lo sapevi che ...?</b></h2>

<p>Con la parola frutta si raggruppano comunemente vari tipi di frutto commestibili compresi alcuni che non sono propriamente frutti, come le pomacee, ed escludendone altri come i pomodori principalmente in base al tipo di uso che se ne fa nell&#39;alimentazione.</p>
', 47, 20, false);
INSERT INTO pagina (id, titolo, body, id_padre, id_sito, modello) VALUES (51, 'Torta di Frutta', '<h1>TORTA DI FRUTTA</h1>

<p>&nbsp;</p>

<p><img alt=\"logo_dolci&quot;\" src=\"images/-30-55-521993-104-100-54-123-26-1255-10010941-44-111-19-9329.gif\" /></p>
', 50, 20, false);
INSERT INTO pagina (id, titolo, body, id_padre, id_sito, modello) VALUES (47, 'Homepage', '<p>Ciao sono Rita Rinaldi ed ho una pasticceria nel cuore di Rieti!</p><p>Ho creato questo sito per condividere tutte le mie ricette con il mondo!</p>', NULL, 20, false);
INSERT INTO pagina (id, titolo, body, id_padre, id_sito, modello) VALUES (48, 'Baci di Alassio', '<h1>BACI DI ALASSIO</h1>

<p>I Baci di Alassio prendono il nome dalla cittadina ligure alla quale appartengono come storia e prima preparazione.<br />
I Baci di Alassio sono dei dolcetti dal cuore di cioccolata e preparati con semplicit&agrave; ma dal gusto morbido e sempre attuale.</p>

<p><img alt=\"bacialassio_450ingr&quot;\" src=\"images/-4625112-6222-10214050-1076112-5120-1082839-51-14.jpg\" /></p>

<p><b><i>Per i baci</i></b></p>

<ul>
	<li>Nocciole farina 500 gr</li>
	<li>Uova 3 albumi</li>
	<li>Zucchero semolato 250 gr</li>
	<li>Cacao in polvere amaro 40 gr</li>
	<li>Miele acacia o millefiori 30 gr</li>
</ul>

<p><b><i>Per la ganache</i></b></p>

<ul>
	<li>Panna 100 gr</li>
	<li>Cioccolato fondente 150 gr</li>
</ul>

<h2>Preparazione</h2>

<p><img alt=\"baciok_seq1&quot;\" src=\"images/-1113746-818735-5191-21-231-68118120-33-74-120-28-79.jpg\" /></p>

<p>Tostate in forno caldo a 180&deg; le nocciole sparse su una teglia per circa 5 minuti, cos&igrave; che rilascino il loro olio; fatele raffreddare e poi trasferitele nel mixer insieme al cacao e allo zucchero semolato (1); azionare fino a ottenere una polvere il pi&ugrave; liscia possibile. (2) Montate a neve molto ferma gli albumi con un pizzico di sale. Trasferite la farina ottenuta in una terrina e aggiungete gli albumi in un paio di riprese mescolando delicatamente con una spatola; in ultimo il miele (3).</p>
', 49, 20, true);
INSERT INTO pagina (id, titolo, body, id_padre, id_sito, modello) VALUES (49, 'Cioccolato', '<h1><b>CIOCCOLATO</b></h1><h2><b>Lo sapevi che ...?</b></h2><p>Il cioccolato (o cioccolata, specie se fuso) &egrave; un alimento derivato dai semi dell&#39;albero del cacao, molto diffuso e ampiamente consumato nel mondo intero. Nella produzione artigianale di qualit&agrave;, il cioccolato &egrave; preparato utilizzando la pasta di cacao come realizzata ed imballata nei paesi origine, con l&#39;aggiunta di ingredienti ed aromi. Nella produzione industriale o comunque di minor pregio qualitativo, &egrave; preparato miscelando il burro di cacao (la parte grassa dei semi di cacao) con polvere di semi di cacao, zucchero e altri ingredienti facoltativi, quali il latte, le mandorle, le nocciole o altri aromi.</p><p>Oltre a ci&ograve;, il cioccolato &egrave; anche un ingrediente di svariati dolciumi, tra cui gelati, torte, biscotti e budini.</p><p>Alcuni studi sembrano confermare che il consumo frequente di cioccolato possa condurre ad una particolare forma di dipendenza detta, per analogia con l&#39;alcolismo, cioccolismo.[1] Altri studi dimostrano come l&#39;assunzione di cioccolato stimoli il rilascio di endorfine, in grado di aumentare il buon umore.</p><p>Il termine &quot;cioccolata&quot; viene utilizzato come sinonimo di &quot;cioccolato&quot; oppure per indicare una bevanda liquida a base di polvere di semi di cacao, nell&#39;uso occidentale contemporaneo invariabilmente con l&#39;aggiunta di zucchero (al contrario di come veniva consumato il cacao in bevande salate e speziate nelle culture precolombiane).</p><p>La cioccolata &egrave; un fluido non newtoniano.</p>', 47, 20, true);


--
-- Name: pagina_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('pagina_id_seq', 51, true);


--
-- Data for Name: sito; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO sito (id, header, footer, id_utente, homepage, id_css, data_creazione, nome) VALUES (20, '<p><img alt=\"dolciumi&quot;\" height=\"506\" src=\"images/-1077-86-3462-789252-81462-105-64-71-87-65105-68104-50.jpg\" width=\"970\" /></p>', '<p>info: rita_rinaldi@gmail.com</p>', 24, 47, 12, '2014-02-18', 'Ricettario di Rita');


--
-- Name: sito_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('sito_id_seq', 20, true);


--
-- Data for Name: slide; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Name: slide_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('slide_id_seq', 2, true);


--
-- Data for Name: utente; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO utente (id, username, password, nome, cognome, email, spazio_disp_img, codice_attivazione, attivato, admin) VALUES (24, 'rita_rinaldi60', '56-70-1141052143-12-64-44-52-8111-8655120-27898473-31', 'Rita', 'Rinaldi', 'franciskittu@gmail.com', 9537355, '4NKin5UDJ4IK1sXs6hze', true, false);


--
-- Name: utente_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('utente_id_seq', 24, true);


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
    ADD CONSTRAINT pagina_id_padre_fkey FOREIGN KEY (id_padre) REFERENCES pagina(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: pagina_id_sito_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY pagina
    ADD CONSTRAINT pagina_id_sito_fkey FOREIGN KEY (id_sito) REFERENCES sito(id) ON UPDATE CASCADE ON DELETE CASCADE;


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

