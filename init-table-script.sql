create table COUNTRY(COUNTRY INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY constraint COUNTRY_PK primary key,NAME VARCHAR(100) NOT NULL constraint COUNTRY_NAME_UINDEX unique,LAT DOUBLE,LONG DOUBLE);
create table COVIDDATA(COVIDDATA INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY constraint COVIDDATA_PK	primary key, COUNTRY INTEGER NOT NULL constraint COVIDDATA_COUNTRY_FK	references COUNTRY,TRNDATE VARCHAR(10) NOT NULL,DATAKIND SMALLINT NOT NULL,QTY INTEGER NOT NULL,PROODQTY INTEGER NOT NULL);
alter table COVIDDATA add constraint COVIDDATA_UINDEX unique (COUNTRY,TRNDATE,DATAKIND);
CREATE SEQUENCE generateID MINVALUE 1 START WITH 1 INCREMENT BY 1;