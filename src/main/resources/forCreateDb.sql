
CREATE TABLE IF NOT EXISTS PATIENT(ID BIGINT, FIRSTNAME VARCHAR(50) NOT NULL, LASTNAME VARCHAR(50)  NOT NULL, MIDDLENAME VARCHAR(50) NOT NULL, PHONE INT UNIQUE NOT NULL, PRIMARY KEY (ID));
INSERT INTO PATIENT (ID, FIRSTNAME, LASTNAME, MIDDLENAME, PHONE) VALUES (1, 'Lisov', 'Vseslav', 'Ulyanovich', 333444);
INSERT INTO PATIENT (ID, FIRSTNAME, LASTNAME, MIDDLENAME, PHONE) VALUES (2, 'Plyukhin', 'Alexey', 'Andreevich', 123456);
INSERT INTO PATIENT (ID, FIRSTNAME, LASTNAME, MIDDLENAME, PHONE) VALUES (3, 'Mikheev', 'Platon', 'Ermolaevich', 666777);
INSERT INTO PATIENT (ID, FIRSTNAME, LASTNAME, MIDDLENAME, PHONE) VALUES (4, 'Kimask', 'Alla', 'Fedorovna', 989712);
INSERT INTO PATIENT (ID, FIRSTNAME, LASTNAME, MIDDLENAME, PHONE) VALUES (5, 'Nutrihina', 'Miroslava', 'Iosifovna', 556677);
COMMIT;
CREATE TABLE IF NOT EXISTS DOCTOR(ID BIGINT, FIRSTNAME VARCHAR(50) NOT NULL, LASTNAME VARCHAR(50) NOT NULL, MIDDLENAME VARCHAR(50) NOT NULL, SPECIALIZATION VARCHAR(50) NOT NULL,PRIMARY KEY(ID));
INSERT INTO DOCTOR(ID, FIRSTNAME, LASTNAME, MIDDLENAME, SPECIALIZATION) VALUES(1, 'Solomina', 'Vlada', 'Alekseevna', 'Dantist');
INSERT INTO DOCTOR(ID, FIRSTNAME, LASTNAME, MIDDLENAME, SPECIALIZATION) VALUES(2, 'Lukyanov', 'Vladislav', 'Kondratievich', 'Therapist');
INSERT INTO DOCTOR(ID, FIRSTNAME, LASTNAME, MIDDLENAME, SPECIALIZATION) VALUES(3, 'Zhevlakov', 'Gregory', 'Yakubovich', 'Ophthalmologist');
INSERT INTO DOCTOR(ID, FIRSTNAME, LASTNAME, MIDDLENAME, SPECIALIZATION) VALUES(4, 'Golubev', 'Boris', 'Ivanovich', 'Dermatologist');
INSERT INTO DOCTOR(ID, FIRSTNAME, LASTNAME, MIDDLENAME, SPECIALIZATION) VALUES(5, 'Belarusians', 'Timur', 'Ostapovich', 'Allergist');
COMMIT;
CREATE TABLE IF NOT EXISTS PRIORITY(ID BIGINT, "NAME" VARCHAR(30), PRIMARY KEY (ID));
INSERT INTO PRIORITY(ID, "NAME") VALUES (1, 'Normal');
INSERT INTO PRIORITY(ID, "NAME") VALUES (2, 'Cito');
INSERT INTO PRIORITY(ID, "NAME") VALUES (3, 'Statim');
COMMIT;
CREATE TABLE IF NOT EXISTS RECIPE(ID BIGINT, DESCRIPTION VARCHAR(200) NOT NULL, ID_PATIENT BIGINT NOT NULL, ID_DOCTOR BIGINT NOT NULL, DATE_CREATE DATE DEFAULT CURRENT_DATE, DURATION BIGINT NOT NULL, ID_PRIORITY BIGINT NOT NULL, PRIMARY KEY(ID), CONSTRAINT FK_PATIENT FOREIGN KEY (ID_PATIENT) REFERENCES PATIENT(ID), CONSTRAINT FK_DOCTOR FOREIGN KEY (ID_DOCTOR) REFERENCES DOCTOR(ID), CONSTRAINT FK_PRIORITY FOREIGN KEY (ID_PRIORITY) REFERENCES PRIORITY(ID));
INSERT INTO RECIPE(ID, DESCRIPTION, ID_PATIENT, ID_DOCTOR, DATE_CREATE, DURATION, ID_PRIORITY) VALUES(1, 'Take aspirin once a day', 1, 2, TO_DATE('12/12/2017','DD/MM/YYYY'), 100, 1);
INSERT INTO RECIPE(ID, DESCRIPTION, ID_PATIENT, ID_DOCTOR, DATE_CREATE, DURATION, ID_PRIORITY) VALUES(2, 'Drink a teraflu twice a day', 2, 2, TO_DATE('16/11/2017','DD/MM/YYYY'), 100, 1);
INSERT INTO RECIPE(ID, DESCRIPTION, ID_PATIENT, ID_DOCTOR, DATE_CREATE, DURATION, ID_PRIORITY) VALUES(3, 'Take loratadine once a day', 5, 5, TO_DATE('23/10/2017','DD/MM/YYYY'), 100, 2);
INSERT INTO RECIPE(ID, DESCRIPTION, ID_PATIENT, ID_DOCTOR, DATE_CREATE, DURATION, ID_PRIORITY) VALUES(4, 'Take the centrin once a day', 4, 5, TO_DATE('08/07/2017','DD/MM/YYYY'), 100, 2);
INSERT INTO RECIPE(ID, DESCRIPTION, ID_PATIENT, ID_DOCTOR, DATE_CREATE, DURATION, ID_PRIORITY) VALUES(5, 'Smear gums with a gel parodied twice a day', 3, 1, TO_DATE('27/09/2017','DD/MM/YYYY'), 100, 3);
INSERT INTO RECIPE(ID, DESCRIPTION, ID_PATIENT, ID_DOCTOR, DATE_CREATE, DURATION, ID_PRIORITY) VALUES(6, 'To wear glasses with optical power of +1 diopter', 5, 3, TO_DATE('13/10/2017','DD/MM/YYYY'), 100, 2);
COMMIT;