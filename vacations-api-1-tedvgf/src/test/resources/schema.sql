CREATE TABLE manager (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         name VARCHAR(255) NOT NULL

);

CREATE TABLE employee (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          vacation_days_left INT NOT NULL,
                          manager_id BIGINT,
                          FOREIGN KEY (manager_id) REFERENCES manager(id)

                      );


CREATE TABLE vacation_request (
                                  id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                  author_id BIGINT NOT NULL,
                                  employee_name VARCHAR(255) NOT NULL,
                                  number_of_vacation_days_requested bigint NOT NULL,
                                  status VARCHAR(255) NOT NULL,
                                  resolved_by_id BIGINT,
                                  request_created_at TIMESTAMP NOT NULL,
                                  vacation_start_date DATE NOT NULL,
                                  vacation_end_date DATE NOT NULL,
                                  manager_id BIGINT,
                                  FOREIGN KEY (author_id) REFERENCES employee(id),
                                  FOREIGN KEY (resolved_by_id) REFERENCES manager(id),
                                  FOREIGN KEY (manager_id) REFERENCES manager(id)
);
