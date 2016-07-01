CREATE TABLE student (
	id	INTEGER PRIMARY KEY AUTOINCREMENT,
	firstName 	text NOT NULL,
	lastName 	text,
	course		text,
	year		integer
);

INSERT into student (firstName,lastName,course,year) VALUES ("test","user","PhD", 2012);
INSERT into student (firstName,lastName,course,year) VALUES ("david","vittor","MBT", 2016);
INSERT into student (firstName,lastName,course,year) VALUES ("daniela","vittor","PhD", 2014);

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
	author integer NOT NULL,
	parent integer,
	tags    text
);

INSERT into page (title,description,content,url,status,author,parent,tags) VALUES ("page1","extract page1","<p style='color:blue;'>this is page1</p>", "/page1","OK",1,null,null);

CREATE TABLE user (
	id	INTEGER PRIMARY KEY AUTOINCREMENT,
	cdate 	TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    mdate 	TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	email text NOT NULL,
	username text NOT NULL,
	password text NOT NULL,
	firstname text NOT NULL,
	lastname text NOT NULL,
	url text NOT NULL,
	mobile text,
	description text,
	tags text,
	type text NOT NULL,
	status text NOT NULL,
	token text,
	image blob
);

INSERT into user (email, username, password, firstname, lastname, url, type, status) VALUES ("d@g.com","dv","dv","david", "vittor", "/dv", "ADMIN", "OK");

CREATE TABLE roles (
	id      INTEGER PRIMARY KEY AUTOINCREMENT,
	rolename text NOT NULL
);

INSERT INTO roles(rolename) VALUES ('user');
INSERT INTO roles(rolename) VALUES ('super');
INSERT INTO roles(rolename) VALUES ('admin');

CREATE TABLE userroles (
	userid INTEGER NOT NULL,
	roleid INTEGER NOT NULL,
	PRIMARY KEY (userid, roleid)
);

INSERT INTO userroles VALUES (1,1);
INSERT INTO userroles VALUES (1,2);
INSERT INTO userroles VALUES (2,3);

