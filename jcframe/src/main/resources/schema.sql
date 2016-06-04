CREATE TABLE page (
	id	INTEGER PRIMARY KEY AUTOINCREMENT,
	cdate 	TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    mdate 	TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	type    text,
	title   text,
	content text,
	authorId integer,
	parentId integer,
	statusId integer,
	tags     text
);

INSERT into page (firstName,lastName,course,year) VALUES ("test","user","PhD", 2012);
INSERT into page (firstName,lastName,course,year) VALUES ("david","vittor","MBT", 2016);
INSERT into page (firstName,lastName,course,year) VALUES ("daniela","vittor","PhD", 2014);

CREATE TABLE page (
	id	INTEGER PRIMARY KEY AUTOINCREMENT,
	cdate 	TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    mdate 	TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	type    text NOT NULL DEFAULT 'blog',
	title   text NOT NULL,
	description text NOT NULL,
	content text NOT NULL,
	url     text NOT NULL,
	status  text NOT NULL,
	authorId integer NOT NULL,
	parentId integer,
	tags    text
);

CREATE TABLE pageComment (
	id	INTEGER PRIMARY KEY AUTOINCREMENT,
	cdate 	TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    mdate 	TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	blogId integer,
	title   text,
	content text,
	authorId integer,
	parentId integer,
	type text,
	status text NOT NULL,
	ip     text,
	requestmd text
);

CREATE TABLE pageRating (
	id	INTEGER PRIMARY KEY AUTOINCREMENT,
	cdate 	TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    mdate 	TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	blogId integer,
	rating  text,
	authorId integer,
	status text NOT NULL,
	ip     text,
	requestmd text
);


CREATE TABLE pageExtra (
	blogId integer NOT NULL,
	likesCount  integer,
	commentCount integer,
	ratingAvg integer,
	ratingCount integer,
	viewCount integer,
	readCount integer
);

CREATE TABLE user (
	id	INTEGER PRIMARY KEY AUTOINCREMENT,
	cdate 	TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    mdate 	TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	email text NOT NULL,
	username text NOT NULL,
	password text NOT NULL,
	fname text NOT NULL,
	lname text NOT NULL,
	url text NOT NULL,
	mobile text,
	description text,
	tags text,
	type text NOT NULL,
	status text NOT NULL,
	token text,
	image blob
);

CREATE TABLE userExtra (
	userId integer NOT NULL,
	loginCount  integer,
	lastLogin date,
	moneyPaid double,
	moneyOwed double,
	monthlyCharge double,
	pageCount integer
);

CREATE TABLE container (
	id	INTEGER PRIMARY KEY AUTOINCREMENT,
	cdate 	TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    mdate 	TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    name text NOT NULL,
	lxcid text,
	alias text,
	description text,
	tags text,
	status text NOT NULL,
	authorId integer NOT NULL,
	osId integer NOT NULL,
	pdate 	date
);

CREATE TABLE os (
	id	INTEGER PRIMARY KEY AUTOINCREMENT,
	cdate 	date NOT NULL DEFAULT now(),
	mdate 	date NOT NULL DEFAULT now(),
    distribution text NOT NULL,
    version text NOT NULL,
    architecture text NOT NULL,
    tags     text
);

CREATE TABLE box (
	id	INTEGER PRIMARY KEY AUTOINCREMENT,
	cdate 	date NOT NULL DEFAULT now(),
	mdate 	date NOT NULL DEFAULT now(),
	name text NOT NULL,
	containerId integer NOT NULL,
	userId integer NOT NULL,
	lxcid text,
	alias text,
	type text,
	description text,
	tags text,
	status text NOT NULL,
	authorId integer NOT NULL,
	parentId integer NOT NULL,
	pdate 	date,
	price double,
	cpulimit integer,
	cpuusage integer,
	memlimit integer,
	memusage integer,
	hddlimit integer,
	hddusage integer,
);

CREATE TABLE service (
	id	INTEGER PRIMARY KEY AUTOINCREMENT,
	cdate 	TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    mdate 	TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    name    text NOT NULL,
    version text NOT NULL,
    description text,
    status text NOT NULL,
    type text NOT NULL,
    tags     text
);

CREATE TABLE userService (
	id	INTEGER PRIMARY KEY AUTOINCREMENT,
	cdate 	TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    mdate 	TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    serviceId  integer NOT NULL,
    userId  integer NOT NULL,
    boxId  integer NOT NULL,
    name text NOT NULL,
    version text,
    linuxName text NOT NULL,
    description text,
    status text NOT NULL,
    type text NOT NULL DEFAULT 'package'
    tags     text
);

CREATE TABLE userPage (
	userId	INTEGER NOT NULL,
	pageId	INTEGER NOT NULL
);

CREATE TABLE serviceOS (
	serviceId	INTEGER NOT NULL,
	osId	INTEGER NOT NULL
);

CREATE TABLE financial (
	id	INTEGER PRIMARY KEY AUTOINCREMENT,
	cdate 	TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    mdate 	TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    userId  integer NOT NULL,
    month   integer NOT NULL,
    year   integer NOT NULL,
    description text NOT NULL,
    price   double,
    currency text NOT NULL DEFAULT 'AUD',
    status text NOT NULL,
    tags     text
);
