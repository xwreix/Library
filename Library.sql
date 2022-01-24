--
-- PostgreSQL database dump
--

-- Dumped from database version 14.1
-- Dumped by pg_dump version 14.1

-- Started on 2022-01-24 14:00:25

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 232 (class 1259 OID 32825)
-- Name: archive; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.archive (
    id integer NOT NULL,
    bookcopyid integer,
    damage text,
    name character varying(60)
);


ALTER TABLE public.archive OWNER TO postgres;

--
-- TOC entry 231 (class 1259 OID 32824)
-- Name: archive_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.archive_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.archive_id_seq OWNER TO postgres;

--
-- TOC entry 3428 (class 0 OID 0)
-- Dependencies: 231
-- Name: archive_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.archive_id_seq OWNED BY public.archive.id;


--
-- TOC entry 217 (class 1259 OID 24619)
-- Name: author; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.author (
    id integer NOT NULL,
    name character varying(70) NOT NULL
);


ALTER TABLE public.author OWNER TO postgres;

--
-- TOC entry 216 (class 1259 OID 24618)
-- Name: author_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.author_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.author_id_seq OWNER TO postgres;

--
-- TOC entry 3429 (class 0 OID 0)
-- Dependencies: 216
-- Name: author_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.author_id_seq OWNED BY public.author.id;


--
-- TOC entry 226 (class 1259 OID 24710)
-- Name: authorphoto; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.authorphoto (
    id integer NOT NULL,
    authorid integer,
    img bytea
);


ALTER TABLE public.authorphoto OWNER TO postgres;

--
-- TOC entry 225 (class 1259 OID 24709)
-- Name: authorphoto_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.authorphoto_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.authorphoto_id_seq OWNER TO postgres;

--
-- TOC entry 3430 (class 0 OID 0)
-- Dependencies: 225
-- Name: authorphoto_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.authorphoto_id_seq OWNED BY public.authorphoto.id;


--
-- TOC entry 218 (class 1259 OID 24625)
-- Name: authorsbooks; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.authorsbooks (
    authorid integer,
    bookid integer
);


ALTER TABLE public.authorsbooks OWNER TO postgres;

--
-- TOC entry 214 (class 1259 OID 24599)
-- Name: book; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.book (
    id integer NOT NULL,
    originalname character varying(60),
    cost numeric(5,2) NOT NULL,
    priceforday numeric(5,2) NOT NULL,
    publyear integer,
    pageamount integer,
    nameinrus character varying(60)
);


ALTER TABLE public.book OWNER TO postgres;

--
-- TOC entry 213 (class 1259 OID 24598)
-- Name: book_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.book_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.book_id_seq OWNER TO postgres;

--
-- TOC entry 3431 (class 0 OID 0)
-- Dependencies: 213
-- Name: book_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.book_id_seq OWNED BY public.book.id;


--
-- TOC entry 220 (class 1259 OID 24639)
-- Name: bookcopy; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.bookcopy (
    id integer NOT NULL,
    bookid integer,
    damage text,
    registrdate date NOT NULL
);


ALTER TABLE public.bookcopy OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 24638)
-- Name: bookcopy_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.bookcopy_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.bookcopy_id_seq OWNER TO postgres;

--
-- TOC entry 3432 (class 0 OID 0)
-- Dependencies: 219
-- Name: bookcopy_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.bookcopy_id_seq OWNED BY public.bookcopy.id;


--
-- TOC entry 215 (class 1259 OID 24605)
-- Name: booksgenres; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.booksgenres (
    genreid integer,
    bookid integer
);


ALTER TABLE public.booksgenres OWNER TO postgres;

--
-- TOC entry 224 (class 1259 OID 24686)
-- Name: cover; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.cover (
    id integer NOT NULL,
    bookid integer,
    img bytea
);


ALTER TABLE public.cover OWNER TO postgres;

--
-- TOC entry 223 (class 1259 OID 24685)
-- Name: cover_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.cover_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.cover_id_seq OWNER TO postgres;

--
-- TOC entry 3433 (class 0 OID 0)
-- Dependencies: 223
-- Name: cover_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.cover_id_seq OWNED BY public.cover.id;


--
-- TOC entry 228 (class 1259 OID 24804)
-- Name: damagephoto; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.damagephoto (
    id integer NOT NULL,
    copyid integer,
    img bytea
);


ALTER TABLE public.damagephoto OWNER TO postgres;

--
-- TOC entry 227 (class 1259 OID 24803)
-- Name: damagephoto_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.damagephoto_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.damagephoto_id_seq OWNER TO postgres;

--
-- TOC entry 3434 (class 0 OID 0)
-- Dependencies: 227
-- Name: damagephoto_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.damagephoto_id_seq OWNED BY public.damagephoto.id;


--
-- TOC entry 212 (class 1259 OID 24584)
-- Name: genre; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.genre (
    id integer NOT NULL,
    name character varying(30)
);


ALTER TABLE public.genre OWNER TO postgres;

--
-- TOC entry 211 (class 1259 OID 24583)
-- Name: genre_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.genre_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.genre_id_seq OWNER TO postgres;

--
-- TOC entry 3435 (class 0 OID 0)
-- Dependencies: 211
-- Name: genre_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.genre_id_seq OWNED BY public.genre.id;


--
-- TOC entry 222 (class 1259 OID 24653)
-- Name: issue; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.issue (
    id integer NOT NULL,
    readerid integer NOT NULL,
    bookcopyid integer NOT NULL,
    preliminarydate date NOT NULL,
    returndate date,
    rating integer,
    discount integer
);


ALTER TABLE public.issue OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 24652)
-- Name: issue_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.issue_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.issue_id_seq OWNER TO postgres;

--
-- TOC entry 3436 (class 0 OID 0)
-- Dependencies: 221
-- Name: issue_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.issue_id_seq OWNED BY public.issue.id;


--
-- TOC entry 230 (class 1259 OID 32786)
-- Name: payment; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.payment (
    id integer NOT NULL,
    readerid integer,
    paymdate date,
    amount numeric(5,2)
);


ALTER TABLE public.payment OWNER TO postgres;

--
-- TOC entry 229 (class 1259 OID 32785)
-- Name: payment_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.payment_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.payment_id_seq OWNER TO postgres;

--
-- TOC entry 3437 (class 0 OID 0)
-- Dependencies: 229
-- Name: payment_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.payment_id_seq OWNED BY public.payment.id;


--
-- TOC entry 210 (class 1259 OID 16402)
-- Name: reader; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.reader (
    id integer NOT NULL,
    surname character varying(50) NOT NULL,
    name character varying(50) NOT NULL,
    patronymic character varying(50),
    passportnumber character varying(9),
    dateofbirth date,
    email character varying(50) NOT NULL,
    address text
);


ALTER TABLE public.reader OWNER TO postgres;

--
-- TOC entry 209 (class 1259 OID 16401)
-- Name: reader_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.reader_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.reader_id_seq OWNER TO postgres;

--
-- TOC entry 3438 (class 0 OID 0)
-- Dependencies: 209
-- Name: reader_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.reader_id_seq OWNED BY public.reader.id;


--
-- TOC entry 3232 (class 2604 OID 32828)
-- Name: archive id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.archive ALTER COLUMN id SET DEFAULT nextval('public.archive_id_seq'::regclass);


--
-- TOC entry 3225 (class 2604 OID 24622)
-- Name: author id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.author ALTER COLUMN id SET DEFAULT nextval('public.author_id_seq'::regclass);


--
-- TOC entry 3229 (class 2604 OID 24713)
-- Name: authorphoto id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.authorphoto ALTER COLUMN id SET DEFAULT nextval('public.authorphoto_id_seq'::regclass);


--
-- TOC entry 3224 (class 2604 OID 24602)
-- Name: book id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book ALTER COLUMN id SET DEFAULT nextval('public.book_id_seq'::regclass);


--
-- TOC entry 3226 (class 2604 OID 24642)
-- Name: bookcopy id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bookcopy ALTER COLUMN id SET DEFAULT nextval('public.bookcopy_id_seq'::regclass);


--
-- TOC entry 3228 (class 2604 OID 24689)
-- Name: cover id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cover ALTER COLUMN id SET DEFAULT nextval('public.cover_id_seq'::regclass);


--
-- TOC entry 3230 (class 2604 OID 24807)
-- Name: damagephoto id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.damagephoto ALTER COLUMN id SET DEFAULT nextval('public.damagephoto_id_seq'::regclass);


--
-- TOC entry 3223 (class 2604 OID 24587)
-- Name: genre id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.genre ALTER COLUMN id SET DEFAULT nextval('public.genre_id_seq'::regclass);


--
-- TOC entry 3227 (class 2604 OID 24656)
-- Name: issue id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.issue ALTER COLUMN id SET DEFAULT nextval('public.issue_id_seq'::regclass);


--
-- TOC entry 3231 (class 2604 OID 32789)
-- Name: payment id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.payment ALTER COLUMN id SET DEFAULT nextval('public.payment_id_seq'::regclass);


--
-- TOC entry 3222 (class 2604 OID 16405)
-- Name: reader id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reader ALTER COLUMN id SET DEFAULT nextval('public.reader_id_seq'::regclass);


--
-- TOC entry 3272 (class 2606 OID 32832)
-- Name: archive archive_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.archive
    ADD CONSTRAINT archive_pkey PRIMARY KEY (id);


--
-- TOC entry 3255 (class 2606 OID 24624)
-- Name: author author_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.author
    ADD CONSTRAINT author_pkey PRIMARY KEY (id);


--
-- TOC entry 3266 (class 2606 OID 24717)
-- Name: authorphoto authorphoto_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.authorphoto
    ADD CONSTRAINT authorphoto_pkey PRIMARY KEY (id);


--
-- TOC entry 3250 (class 2606 OID 24604)
-- Name: book book_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book
    ADD CONSTRAINT book_pkey PRIMARY KEY (id);


--
-- TOC entry 3259 (class 2606 OID 24646)
-- Name: bookcopy bookcopy_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bookcopy
    ADD CONSTRAINT bookcopy_pkey PRIMARY KEY (id);


--
-- TOC entry 3264 (class 2606 OID 24693)
-- Name: cover cover_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cover
    ADD CONSTRAINT cover_pkey PRIMARY KEY (id);


--
-- TOC entry 3268 (class 2606 OID 24811)
-- Name: damagephoto damagephoto_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.damagephoto
    ADD CONSTRAINT damagephoto_pkey PRIMARY KEY (id);


--
-- TOC entry 3243 (class 2606 OID 24677)
-- Name: genre genre_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.genre
    ADD CONSTRAINT genre_name_key UNIQUE (name);


--
-- TOC entry 3245 (class 2606 OID 24589)
-- Name: genre genre_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.genre
    ADD CONSTRAINT genre_pkey PRIMARY KEY (id);


--
-- TOC entry 3262 (class 2606 OID 24658)
-- Name: issue issue_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.issue
    ADD CONSTRAINT issue_pkey PRIMARY KEY (id);


--
-- TOC entry 3270 (class 2606 OID 32791)
-- Name: payment payment_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.payment
    ADD CONSTRAINT payment_pkey PRIMARY KEY (id);


--
-- TOC entry 3234 (class 2606 OID 16411)
-- Name: reader reader_email_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reader
    ADD CONSTRAINT reader_email_key UNIQUE (email);


--
-- TOC entry 3237 (class 2606 OID 16409)
-- Name: reader reader_passportnumber_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reader
    ADD CONSTRAINT reader_passportnumber_key UNIQUE (passportnumber);


--
-- TOC entry 3239 (class 2606 OID 16407)
-- Name: reader reader_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reader
    ADD CONSTRAINT reader_pkey PRIMARY KEY (id);


--
-- TOC entry 3241 (class 2606 OID 32771)
-- Name: reader uniq; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reader
    ADD CONSTRAINT uniq UNIQUE (passportnumber, email);


--
-- TOC entry 3257 (class 2606 OID 32777)
-- Name: author uniq_author; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.author
    ADD CONSTRAINT uniq_author UNIQUE (name);


--
-- TOC entry 3252 (class 2606 OID 32773)
-- Name: book uniq_book; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book
    ADD CONSTRAINT uniq_book UNIQUE (nameinrus);


--
-- TOC entry 3247 (class 2606 OID 32775)
-- Name: genre uniq_genre; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.genre
    ADD CONSTRAINT uniq_genre UNIQUE (name);


--
-- TOC entry 3253 (class 1259 OID 32781)
-- Name: author_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX author_index ON public.author USING btree (name);


--
-- TOC entry 3248 (class 1259 OID 32782)
-- Name: book_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX book_index ON public.book USING btree (nameinrus);


--
-- TOC entry 3260 (class 1259 OID 32784)
-- Name: issue_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX issue_index ON public.issue USING btree (readerid, bookcopyid);


--
-- TOC entry 3235 (class 1259 OID 32783)
-- Name: reader_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX reader_index ON public.reader USING btree (email);


--
-- TOC entry 3275 (class 2606 OID 24628)
-- Name: authorsbooks fk_author; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.authorsbooks
    ADD CONSTRAINT fk_author FOREIGN KEY (authorid) REFERENCES public.author(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 3281 (class 2606 OID 24718)
-- Name: authorphoto fk_author_photo; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.authorphoto
    ADD CONSTRAINT fk_author_photo FOREIGN KEY (authorid) REFERENCES public.author(id);


--
-- TOC entry 3274 (class 2606 OID 24613)
-- Name: booksgenres fk_book; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.booksgenres
    ADD CONSTRAINT fk_book FOREIGN KEY (bookid) REFERENCES public.book(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 3276 (class 2606 OID 24633)
-- Name: authorsbooks fk_book; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.authorsbooks
    ADD CONSTRAINT fk_book FOREIGN KEY (bookid) REFERENCES public.book(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 3277 (class 2606 OID 24647)
-- Name: bookcopy fk_book; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bookcopy
    ADD CONSTRAINT fk_book FOREIGN KEY (bookid) REFERENCES public.book(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 3279 (class 2606 OID 24664)
-- Name: issue fk_bookcopy; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.issue
    ADD CONSTRAINT fk_bookcopy FOREIGN KEY (bookcopyid) REFERENCES public.bookcopy(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 3280 (class 2606 OID 24694)
-- Name: cover fk_cover_book; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cover
    ADD CONSTRAINT fk_cover_book FOREIGN KEY (bookid) REFERENCES public.book(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 3282 (class 2606 OID 24812)
-- Name: damagephoto fk_damagephoto_bookcopy; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.damagephoto
    ADD CONSTRAINT fk_damagephoto_bookcopy FOREIGN KEY (copyid) REFERENCES public.bookcopy(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 3273 (class 2606 OID 24608)
-- Name: booksgenres fk_genre; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.booksgenres
    ADD CONSTRAINT fk_genre FOREIGN KEY (genreid) REFERENCES public.genre(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 3278 (class 2606 OID 24659)
-- Name: issue fk_reader; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.issue
    ADD CONSTRAINT fk_reader FOREIGN KEY (readerid) REFERENCES public.reader(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 3283 (class 2606 OID 32792)
-- Name: payment fk_reader_payment; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.payment
    ADD CONSTRAINT fk_reader_payment FOREIGN KEY (readerid) REFERENCES public.reader(id) ON UPDATE CASCADE ON DELETE CASCADE;


-- Completed on 2022-01-24 14:00:26

--
-- PostgreSQL database dump complete
--

