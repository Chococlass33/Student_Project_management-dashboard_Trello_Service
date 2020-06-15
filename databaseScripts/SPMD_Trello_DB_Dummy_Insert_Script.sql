/* SQL Script to insert dummy data */
INSERT INTO `spmd-trello`.`member`
(`id`,
`memberType`,
`fullName`,
`email`)
VALUES
('1',
'normal',
'Test Member 1',
'testMember1@gmail.com');

INSERT INTO `spmd-trello`.`member`
(`id`,
`memberType`,
`fullName`,
`email`)
VALUES
('2',
'normal',
'Test Member 2',
'testMember2@gmail.com'); 

INSERT INTO `spmd-trello`.`organization`
(`id`,
`name`,
`displayName`,
`description`,
`teamType`)
VALUES
('1',
'Testing Team',
'Testing Team',
'Team for testing',
'engineering-it');

INSERT INTO `spmd-trello`.`organization_member`
(`id`,
`idOrganization`,
`idMember`)
VALUES
('1',
'1',
'1');

INSERT INTO `spmd-trello`.`organization_member`
(`id`,
`idOrganization`,
`idMember`)
VALUES
('2',
'1',
'2');

INSERT INTO `spmd-trello`.`action`
(`id`,
`idMember`,
`type`,
`data`)
VALUES
('1',
'1',
'createCard',
'{"list": {"name": "To Do", "id": "1"}, "board": {"shortLink": "board123", "name": "Dummy Board", "id": "1"}}');

INSERT INTO `spmd-trello`.`action`
(`id`,
`idMember`,
`type`,
`data`)
VALUES
('2',
'1',
'addChecklistToCard',
'{"card": {"shortLink": "card123", "name": "First card", "id": "1"}, "board": {"shortLink": "board123", "name": "Dummy Board", "id": "1"}}');

INSERT INTO `spmd-trello`.`action`
(`id`,
`idMember`,
`type`,
`data`)
VALUES
('3',
'2',
'commentCard',
'{"list": {"name": "To Do", "id": "1"}, "board": {"shortLink": "board123", "name": "Dummy Board", "id": "1"}, "card": {"shortLink": "card123", "name": "First card", "id": "1"}}');


INSERT INTO `spmd-trello`.`board`
(`id`,
`idOrganization`,
`name`,
`description`,
`shortLink`)
VALUES
('1',
'1',
'Dummy Board',
'This is data of a dummy board',
'board123');

INSERT INTO `spmd-trello`.`board_membership`
(`id`,
`idBoard`,
`idMember`,
`memberType`)
VALUES
('1',
'1',
'1',
'admin');

INSERT INTO `spmd-trello`.`board_membership`
(`id`,
`idBoard`,
`idMember`,
`memberType`)
VALUES
('2',
'1',
'2',
'member');

INSERT INTO `spmd-trello`.`list`
(`id`,
`idBoard`,
`name`,
`pos`)
VALUES
('1',
'1',
'To Do',
12345);

INSERT INTO `spmd-trello`.`card`
(`id`,
`idList`,
`idBoard`,
`checkItemStates`,
`closed`,
`dateLastActivity`,
`description`,
`descData`,
`due`,
`dueCompleted`,
`name`,
`pos`,
`shortLink`)
VALUES
('1',
'1',
'1',
'{}',
0,
CURRENT_TIMESTAMP(),
'First card for dummy data',
null,
null,
0,
'First card',
12345,
'card123');


INSERT INTO `spmd-trello`.`checklist`
(`id`,
`idCard`,
`name`,
`pos`)
VALUES
('1',
'1',
'First checklist',
12345);


INSERT INTO `spmd-trello`.`checkitem`
(`id`,
`idChecklist`,
`name`,
`pos`,
`state`)
VALUES
('1',
'1',
'First checkitem',
12345,
'incomplete');

INSERT INTO `spmd-trello`.`label`
(`id`,
`idBoard`,
`name`,
`colour`)
VALUES
('1',
'1',
'Testing',
'green');

INSERT INTO `spmd-trello`.`card_label`
(`id`,
`idCard`,
`idLabel`)
VALUES
('1',
'1',
'1');

INSERT INTO `spmd-trello`.`card_member`
(`id`,
`idCard`,
`idMember`)
VALUES
('1',
'1',
'1');

INSERT INTO `spmd-trello`.`card_member`
(`id`,
`idCard`,
`idMember`)
VALUES
('2',
'1',
'2');

/**/