DELETE
FROM question;
DELETE
FROM answer_option;

DELETE
FROM user_account;

DELETE
FROM user_role;

INSERT INTO question (id, text, epoch_time)
VALUES (1, 'How was your day?', 1000);
INSERT INTO question (id, text, epoch_time)
VALUES (2, 'Would you like to have one dollar or two dollars or three dollars ?', 1000);
INSERT INTO question (id, text, epoch_time)
VALUES (3, 'No answer question?', 1000);

INSERT INTO answer_option (id, id_question, text)
VALUES (1000, 1, 'Good');
INSERT INTO answer_option (id, id_question, text)
VALUES (1001, 1, 'Bad');

INSERT INTO answer_option (id, id_question, text)
VALUES (1002, 2, 'One');
INSERT INTO answer_option (id, id_question, text)
VALUES (1003, 2, 'Two');
INSERT INTO answer_option (id, id_question, text)
VALUES (1004, 2, 'Three');


INSERT INTO user_account (id, username, password, enabled)
VALUES (1, 'admin', '$2a$10$U6.PGyvr5lcRSiwfkP7SgOE7.hcOCxLhAnd.UVkfs63iiRwD65o6G', true);

INSERT INTO user_account (id, username, password, enabled)
VALUES (2, 'user', '$2a$10$U6.PGyvr5lcRSiwfkP7SgOE7.hcOCxLhAnd.UVkfs63iiRwD65o6G', true);

INSERT INTO user_role(id, id_user, role_type)
values (1, 1, 'ADMIN');
INSERT INTO user_role(id, id_user, role_type)
values (2, 2, 'USER');