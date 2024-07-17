-- Insert dummy managers
INSERT INTO manager (id, name) VALUES (1, 'Ada Lovelace');
INSERT INTO manager (id, name) VALUES (2, 'Isaac Newton');

-- Insert dummy employees
-- Ada Lovelace manages Albert Einstein, Marie Curie, and Charles Darwin
INSERT INTO employee (id, name, vacation_days_left, manager_id) VALUES (1, 'Albert Einstein', 30, 1);
INSERT INTO employee (id, name, vacation_days_left, manager_id) VALUES (2, 'Marie Curie', 25, 1);
INSERT INTO employee (id, name, vacation_days_left, manager_id) VALUES (5, 'Charles Darwin', 10, 1);

-- Isaac Newton manages Hedy Lamarr, Nikola Tesla, and Galileo Galilei
INSERT INTO employee (id, name, vacation_days_left, manager_id) VALUES (3, 'Hedy Lamarr', 15, 2);
INSERT INTO employee (id, name, vacation_days_left, manager_id) VALUES (4, 'Nikola Tesla', 20, 2);
INSERT INTO employee (id, name, vacation_days_left, manager_id) VALUES (6, 'Galileo Galilei', 18, 2);
