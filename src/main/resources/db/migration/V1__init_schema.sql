-- 1. Bağımsız Tablolar (Önce bunlar oluşturulmalı ki diğerleri bağlanabilsin)
CREATE TABLE department (
                            id BIGSERIAL PRIMARY KEY,
                            name VARCHAR(255) NOT NULL
);

CREATE TABLE rank (
                      id BIGSERIAL PRIMARY KEY,
                      name VARCHAR(255) NOT NULL,
                      level INT NOT NULL
);

-- 2. Kullanıcı Tablosu (Departman ve Rütbeye bağlı)
CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       name VARCHAR(255),
                       email VARCHAR(255) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       department_id BIGINT,
                       rank_id BIGINT,
                       CONSTRAINT fk_user_department FOREIGN KEY (department_id) REFERENCES department (id),
                       CONSTRAINT fk_user_rank FOREIGN KEY (rank_id) REFERENCES rank (id)
);

-- 3. Belge Tablosu
CREATE TABLE documents (
                          id BIGSERIAL PRIMARY KEY,
                          title VARCHAR(255),
                          status VARCHAR(50) NOT NULL,
                          uploader_id BIGINT,
                          CONSTRAINT fk_document_uploader FOREIGN KEY (uploader_id) REFERENCES users (id)
);

-- 4. Onay Adımları Tablosu
CREATE TABLE approval_step (
                               id BIGSERIAL PRIMARY KEY,
                               document_id BIGINT,
                               approver_id BIGINT,
                               status VARCHAR(50) NOT NULL,
                               step_order INT NOT NULL,
                               CONSTRAINT fk_step_document FOREIGN KEY (document_id) REFERENCES document (id),
                               CONSTRAINT fk_step_approver FOREIGN KEY (approver_id) REFERENCES users (id)
);

-- 5. Belge Geçmişi Tablosu
CREATE TABLE document_history (
                                  id BIGSERIAL PRIMARY KEY,
                                  document_id BIGINT,
                                  action VARCHAR(50) NOT NULL,
                                  actor_id BIGINT,
                                  description TEXT,
                                  CONSTRAINT fk_history_document FOREIGN KEY (document_id) REFERENCES document (id),
                                  CONSTRAINT fk_history_actor FOREIGN KEY (actor_id) REFERENCES users (id)
);