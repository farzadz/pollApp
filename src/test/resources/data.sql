DELETE
FROM question;
DELETE
FROM answer_option;

INSERT INTO question (id, text, epoch_time)
VALUES (1, 'How was your day?', 1000);
INSERT INTO question (id, text, epoch_time)
VALUES (2, 'Would you like to have one dollar or two dollars or three dollars ?', 1000);
INSERT INTO question (id, text, epoch_time)
VALUES (3, 'No answer question?', 1000);

INSERT INTO answer_option (id, id_question, text, vote_count)
VALUES (1000, 1, 'Good', 2);
INSERT INTO answer_option (id, id_question, text, vote_count)
VALUES (1001, 1, 'Bad', 3);

INSERT INTO answer_option (id, id_question, text, vote_count)
VALUES (1002, 2, 'One', 3);
INSERT INTO answer_option (id, id_question, text, vote_count)
VALUES (1003, 2, 'Two', 3);
INSERT INTO answer_option (id, id_question, text, vote_count)
VALUES (1004, 2, 'Three', 4);
