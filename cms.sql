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
    descrizione character varying(255),
    id_css integer,
    data_creazione date DEFAULT ('now'::text)::date
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

INSERT INTO immagine (id, nome, dimensione, file, digest, data_upload, id_utente, tipo, thumb, cover) VALUES (9, 'blind', 6304, 'images/30102-152575-91-26-59-8069-30-6468529816108-43-87.gif', '30102-152575-91-26-59-8069-30-6468529816108-43-87', '2014-02-14 12:24:30.32', 4, 'image/gif', NULL, false);
INSERT INTO immagine (id, nome, dimensione, file, digest, data_upload, id_utente, tipo, thumb, cover) VALUES (11, 'smile', 707, 'images/-24884-7447-61-40-415461-101123124-78-43-81-396665113.gif', '-24884-7447-61-40-415461-101123124-78-43-81-396665113', '2014-02-14 12:28:20.826', 4, 'image/gif', NULL, false);
INSERT INTO immagine (id, nome, dimensione, file, digest, data_upload, id_utente, tipo, thumb, cover) VALUES (12, 'yoyo', 34148, 'images/-98-90-27372327389-76-844-15-5821-5775116633110.jpg', '-98-90-27372327389-76-844-15-5821-5775116633110', '2014-02-14 12:29:13.283', 4, 'image/jpeg', NULL, false);
INSERT INTO immagine (id, nome, dimensione, file, digest, data_upload, id_utente, tipo, thumb, cover) VALUES (13, 'nevermind', 548121, 'images/978389-96-71661633190-125-68-1411688-5834-49748.jpg', '978389-96-71661633190-125-68-1411688-5834-49748', '2014-02-14 12:30:32.62', 4, 'image/jpeg', NULL, false);
INSERT INTO immagine (id, nome, dimensione, file, digest, data_upload, id_utente, tipo, thumb, cover) VALUES (14, 'plin', 6304, 'images/30102-152575-91-26-59-8069-30-6468529816108-43-87.gif', '30102-152575-91-26-59-8069-30-6468529816108-43-87', '2014-02-14 16:49:42.661', 4, 'image/gif', '', false);
INSERT INTO immagine (id, nome, dimensione, file, digest, data_upload, id_utente, tipo, thumb, cover) VALUES (10, 'nirvana', 1298117, 'images/65-79-50-23-114540-4042125-6-108-12812178-53-13-336847.jpg', '65-79-50-23-114540-4042125-6-108-12812178-53-13-336847', '2014-02-14 12:24:41.663', 4, 'image/jpeg', NULL, true);
INSERT INTO immagine (id, nome, dimensione, file, digest, data_upload, id_utente, tipo, thumb, cover) VALUES (19, 'Nirvana-01', 79151, 'images/-69-84-5727-30-6168-18050-2263-55-107-46-32-84-11852-64.jpg', '-69-84-5727-30-6168-18050-2263-55-107-46-32-84-11852-64', '2014-02-17 00:52:28.129', 8, 'image/jpeg', 'images/thumbs/-69-84-5727-30-6168-18050-2263-55-107-46-32-84-11852-64.jpg', false);
INSERT INTO immagine (id, nome, dimensione, file, digest, data_upload, id_utente, tipo, thumb, cover) VALUES (20, 'nirvana_wallpaper_6-800x600', 174050, 'images/-9361-83-5120497896-293-33123-384838-412-96-1515.jpg', '-9361-83-5120497896-293-33123-384838-412-96-1515', '2014-02-17 00:54:34.309', 8, 'image/jpeg', 'images/thumbs/-9361-83-5120497896-293-33123-384838-412-96-1515.jpg', false);
INSERT INTO immagine (id, nome, dimensione, file, digest, data_upload, id_utente, tipo, thumb, cover) VALUES (23, 'Killers', 173425, 'images/-1208-70103-19-26-5317-2236-9118-4991-4510488734559.jpg', '-1208-70103-19-26-5317-2236-9118-4991-4510488734559', '2014-02-17 15:50:08.011', 8, 'image/jpeg', 'images/thumbs/-1208-70103-19-26-5317-2236-9118-4991-4510488734559.jpg', false);
INSERT INTO immagine (id, nome, dimensione, file, digest, data_upload, id_utente, tipo, thumb, cover) VALUES (24, 'the_stooges_-_fun_house_-_front', 114359, 'images/16-215611-2969-6789115-117-26-16-94-1792-11780-956564.jpg', '16-215611-2969-6789115-117-26-16-94-1792-11780-956564', '2014-02-17 15:50:30.862', 8, 'image/jpeg', 'images/thumbs/16-215611-2969-6789115-117-26-16-94-1792-11780-956564.jpg', false);
INSERT INTO immagine (id, nome, dimensione, file, digest, data_upload, id_utente, tipo, thumb, cover) VALUES (25, 'south_saturn_delta', 174377, 'images/-1162084-4-5611-94-128-9184-31-31-12428-51-6542-100-64.jpg', '-1162084-4-5611-94-128-9184-31-31-12428-51-6542-100-64', '2014-02-17 15:51:02.197', 8, 'image/jpeg', 'images/thumbs/-1162084-4-5611-94-128-9184-31-31-12428-51-6542-100-64.jpg', false);
INSERT INTO immagine (id, nome, dimensione, file, digest, data_upload, id_utente, tipo, thumb, cover) VALUES (26, 'guitar', 313586, 'images/43-12498401816-1121116-30-5067959118-8730318-101.jpg', '43-12498401816-1121116-30-5067959118-8730318-101', '2014-02-17 15:59:03.373', 8, 'image/jpeg', 'images/thumbs/43-12498401816-1121116-30-5067959118-8730318-101.jpg', false);


--
-- Name: immagine_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('immagine_id_seq', 26, true);


--
-- Data for Name: pagina; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO pagina (id, titolo, body, id_padre, id_sito, modello) VALUES (3, 'homepage', '<div>questo è il corpo della homepage</div>', NULL, 5, false);
INSERT INTO pagina (id, titolo, body, id_padre, id_sito, modello) VALUES (5, 'pagina2', '<div>questo è il corpo della pagina2</div>', 3, 5, false);
INSERT INTO pagina (id, titolo, body, id_padre, id_sito, modello) VALUES (6, 'pagina3', '<div>questo è il corpo della pagina3 che è foglia della pagina 1</div>', 4, 5, false);
INSERT INTO pagina (id, titolo, body, id_padre, id_sito, modello) VALUES (7, 'pagina4', '<div>questo è il corpo della pagina4 che è figlia della pagina 1</div>', 4, 5, false);
INSERT INTO pagina (id, titolo, body, id_padre, id_sito, modello) VALUES (8, 'pagina5', '<div>questo è il corpo della pagina5 che è figlia di pagina 2</div>', 5, 5, false);
INSERT INTO pagina (id, titolo, body, id_padre, id_sito, modello) VALUES (9, 'pagina6', '<div>questo è il corpo della pagina6</div>', 3, 5, false);
INSERT INTO pagina (id, titolo, body, id_padre, id_sito, modello) VALUES (16, 'Homepage', '<p>Scrivere contenuto!</p>', NULL, 13, false);
INSERT INTO pagina (id, titolo, body, id_padre, id_sito, modello) VALUES (4, 'pagina1', '<div>questo è il corpo della pagina1</div>', 3, 5, true);
INSERT INTO pagina (id, titolo, body, id_padre, id_sito, modello) VALUES (20, 'Metal', '<blockquote><p>The gods made heavy metal and it&#39;s never gonna die!<br />&nbsp;Gli dei crearono l&#39;heavy metal, e mai morir&agrave;!<br />(Manowar, The Gods Made Heavy Metal)</p></blockquote><p>L&#39;heavy metal (spesso abbreviato in metal[1] in lingua italiana letteralmente &quot;metallo pesante&quot;), &egrave; un genere di musica rock[2][3][4][5]. Derivante dall&#39;hard rock, &egrave; caratterizzato da ritmi fortemente aggressivi e da un suono potente, ottenuto attraverso l&#39;enfatizzazione dell&#39;amplificazione e della distorsione delle chitarre, dei bassi, e, spesso, persino delle voci. Esiste una moltitudine di stili e sottogeneri dell&#39;heavy metal; di conseguenza esistono sottogeneri pi&ugrave; melodici e commerciali, ed altri dalle sonorit&agrave; estreme e underground.[6]</p><p>Gi&agrave; molto popolare negli anni settanta ed ottanta, ha continuato ad avere successo nei decenni seguenti e si &egrave; inoltre diversificato in numerosi sottogeneri. Moltissimi sono gli artisti e i gruppi ascritti al metal, sia nei meccanismi musicali ufficiali che in ambito underground.<br />Esso ha dato vita ad un movimento (cosiddetto movimento metal) prevalentemente apolitico che spaziava a volte nell&#39;anarchia, e sfruttava i simboli che sottolineassero una rottura con la morale vigente.</p>', 16, 13, false);
INSERT INTO pagina (id, titolo, body, id_padre, id_sito, modello) VALUES (26, 'Metallica', '<h1>METALLICA</h1>

<p>I Metallica (IPA: /mÉËt&aelig;lÉ¨kÉ/) sono un gruppo musicale thrash metal statunitense, formatosi a Los Angeles nel 1981. I loro primi lavori, grazie alla velocit&agrave; dei pezzi, tecnicismi strumentali ed aggressivit&agrave;, li hanno portati ad entrare nei &quot;Big Four&quot; del thrash metal, accanto a Slayer, Megadeth ed Anthrax.[2][3]</p>

<p>La band si guadagn&ograve; in poco tempo un crescente seguito di fan[4] e, con la pubblicazione di Master of Puppets nel 1986, raggiunse secondo molti l&#39;apice della carriera.[5] Il successo commerciale arriv&ograve; nel 1991 con l&#39;album omonimo, che esord&igrave; alla prima posizione della classifica Billboard 200.[6] Con questo lavoro la band ampli&ograve; molto la sua direzione musicale, rivolgendosi non pi&ugrave; ai soli fan del thrash e, grazie a sonorit&agrave; pi&ugrave; semplici ed orecchiabili, divennero una delle maggiori realt&agrave; musicali del periodo.[7][8][9]</p>

<p>Nel 2000 presentarono una causa contro Napster per la condivisione gratuita di materiale protetto da copyright prodotto dalla band senza il loro consenso.[10]</p>

<p>In carriera il gruppo ha vinto nove Grammy Awards ed ebbe cinque album consecutivi che esordirono al primo posto nella Billboard 200.[11] L&#39;album Metallica, inoltre, vendette oltre 15 milioni di copie negli Stati Uniti, e pi&ugrave; di 22 milioni di copie nel resto del mondo.[12]</p>

<p>Con pi&ugrave; di 100 milioni di dischi venduti,[13] di cui 60 milioni[14] nei soli Stati Uniti,[15] la band &egrave; annoverata come una delle formazioni di maggior successo nella storia dell&#39;heavy metal e del rock contemporaneo.</p>

<p>A novembre 2012 i Metallica fondarono la propria etichetta discografica indipendente, la Blackened Recordings.[16] Il primo disco pubblicato attraverso la loro etichetta &egrave; stato il DVD Quebec Magnetic, uscito il 10 dicembre dello stesso anno.</p>
', 20, 13, false);
INSERT INTO pagina (id, titolo, body, id_padre, id_sito, modello) VALUES (25, 'Iron Maiden', '<h1>IRON MAIDEN</h1>

<p>Gli Iron Maiden (IPA: [ËaÉªÌ¯É(É¹)n ËmeÉªÌ¯.dÉn]) sono un gruppo musicale heavy metal britannico, formatosi a Londra nel 1975[6] per iniziativa del bassista Steve Harris. Sono considerati uno dei gruppi pi&ugrave; importanti ed influenti del genere[1] e, assieme ad artisti come Saxon, Angel Witch, Samson, Def Leppard, Raven e Venom, fanno parte della New Wave of British Heavy Metal (N.W.O.B.H.M.),[7] corrente al cui sviluppo hanno fortemente contribuito.[8][9][10]</p>

<p>Pubblicarono il loro album di debutto nel 1980, diventando rapidamente uno dei gruppi pi&ugrave; rappresentativi della scena metal del periodo.[11][12] Poco dopo l&#39;uscita del loro secondo album, Killers, il cantante Paul Di&#39;Anno venne sostituito da Bruce Dickinson con cui il gruppo pubblic&ograve;, nel 1982, The Number of the Beast, uno dei pi&ugrave; importanti lavori della storia della band.[13] Per tutta la durata degli anni ottanta la band trov&ograve; il suo maggior successo commerciale con album come Piece of Mind, Powerslave, Somewhere in Time, Seventh Son of a Seventh Son che divennero presto dischi d&#39;oro e di platino in numerosi paesi.[14]</p>

<p>La formazione rimase intatta sino all&#39;abbandono di Adrian Smith che durante le registrazioni di No Prayer for the Dying (1990) venne sostituito da Janick Gers. Fear of the Dark (1992) fu l&#39;ultimo album della band con alla voce Dickinson, il quale venne sostituito l&#39;anno seguente da Blaze Bayley, con il quale gli Iron Maiden non trovarono il successo sperato.[12][15] Dickinson e Smith tornarono in gruppo nel 1999, rimpiazzando il cantante Blaze Bayley ma non il chitarrista Janick Gers, diventando cos&igrave; un sestetto. L&#39;anno seguente venne pubblicato un nuovo album: Brave New World, e di conseguenza ci fu il Brave New World Tour.</p>

<p>Il loro ultimo lavoro, The Final Frontier (2010), ha riscosso un ottimo successo raggiungendo anche la posizione numero quattro nella classifica Billboard 200,[16] superando cos&igrave; il risultato ottenuto con il precedente disco A Matter of Life and Death che si ferm&ograve; alla posizione numero nove.[17]</p>
', 20, 13, false);


--
-- Name: pagina_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('pagina_id_seq', 26, true);


--
-- Data for Name: sito; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO sito (id, header, footer, id_utente, homepage, descrizione, id_css, data_creazione) VALUES (5, '<div><h1>Questa &egrave; l\intestazione</h1></div>', '<div><h2>Questo è il pi&egrave; di pagina</h2></div>', 4, 3, 'rita_rinaldi', NULL, '2014-02-14');
INSERT INTO sito (id, header, footer, id_utente, homepage, descrizione, id_css, data_creazione) VALUES (13, '<p><img alt=\"guitar&quot;\" height=\"398\" src=\"images/43-12498401816-1121116-30-5067959118-8730318-101.jpg\" width=\"1119\" /></p>', '<p>info: rock4ever@gmail.com</p>', 8, 16, 'rock4ever', 13, '2014-02-15');


--
-- Name: sito_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('sito_id_seq', 13, true);


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

INSERT INTO utente (id, username, password, nome, cognome, email, spazio_disp_img, codice_attivazione, attivato, admin) VALUES (3, 'marta_proietti', '-1-128-29-111-54-107-6823-10-45265240-116-7123-1154210-44', 'marta', 'proietti', 'marta.proietti06@yahoo.it', 10000000, '1D6QEhruzTIfvinTRdVe', true, false);
INSERT INTO utente (id, username, password, nome, cognome, email, spazio_disp_img, codice_attivazione, attivato, admin) VALUES (2, 'franciskittu', 'password', 'francesco', 'proietti', 'franciskittu@gmail.com', 7369618, NULL, false, true);
INSERT INTO utente (id, username, password, nome, cognome, email, spazio_disp_img, codice_attivazione, attivato, admin) VALUES (4, 'rita_rinaldi', '-46-5830-1764-26111111-90-125-4966-3382-9609311081-38', 'Rita', 'Rinaldi', 'proietti.francesco.91@gmail.com', 3984838, '6nWcHul2zdEGEIJed4i2', true, false);
INSERT INTO utente (id, username, password, nome, cognome, email, spazio_disp_img, codice_attivazione, attivato, admin) VALUES (5, 'chebanca', '707539127-5617-8473-120-74-37-40-116-938-64-32-6548117', 'Claudio', 'Proietti', 'franciskittu@gmail.com', 10000000, 'Krmspoq3r26pDRY9Bez5', true, false);
INSERT INTO utente (id, username, password, nome, cognome, email, spazio_disp_img, codice_attivazione, attivato, admin) VALUES (8, 'rock4ever', '-120100-8-128-69-126126124-9611369-30-102-34-75695212512-80', 'Francesco', 'Proietti', 'proietti.francesco.91@gmail.com', 7318679, 'F5gEzXuQUUGHiWfchCAN', true, false);


--
-- Name: utente_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('utente_id_seq', 8, true);


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

