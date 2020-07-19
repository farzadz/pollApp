INSERT INTO user_account (id, username, password, enabled)
VALUES (1, 'admin', '$2a$10$U6.PGyvr5lcRSiwfkP7SgOE7.hcOCxLhAnd.UVkfs63iiRwD65o6G', true);

INSERT INTO user_account (id, username, password, enabled)
VALUES (2, 'user', '$2a$10$U6.PGyvr5lcRSiwfkP7SgOE7.hcOCxLhAnd.UVkfs63iiRwD65o6G', true);

INSERT INTO user_role(id, id_user, role_type)
values (1, 1, 'ADMIN');
INSERT INTO user_role(id, id_user, role_type)
values (2, 2, 'USER');
