-- 동네런 데이터베이스와 코스 테이블을 준비한다.
SET NAMES utf8mb4;

CREATE DATABASE IF NOT EXISTS dongne_run
    CHARACTER SET utf8mb4;

USE dongne_run;

CREATE TABLE IF NOT EXISTS running_course (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    type ENUM('LOOP', 'OUT_AND_BACK') NOT NULL,
    start_latitude DOUBLE NOT NULL,
    start_longitude DOUBLE NOT NULL,
    distance_meters INT NOT NULL,
    flat BOOLEAN NOT NULL,
    flat_evidence VARCHAR(255) NOT NULL,
    directions VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT chk_running_course_distance CHECK (distance_meters > 0)
);

-- 테스트 데이터다. 거리와 좌표는 화면 및 추천 계산 검증용이다.
-- LOOP의 기준 거리는 한 바퀴, OUT_AND_BACK의 기준 거리는 편도다.
INSERT INTO running_course (name, type, start_latitude, start_longitude,
                            distance_meters, flat, flat_evidence, directions)
SELECT '[예시] 400m 트랙', 'LOOP', 37.5252, 126.9295,
       400, TRUE, '테스트용 평지 가정. 현장 미확인.', '출발선을 기준으로 같은 방향으로 돌아요.'
WHERE NOT EXISTS (
    SELECT 1 FROM running_course WHERE name = '[예시] 400m 트랙'
);

INSERT INTO running_course (name, type, start_latitude, start_longitude,
                            distance_meters, flat, flat_evidence, directions)
SELECT '[예시] 하천 산책로', 'OUT_AND_BACK', 37.5291, 126.9366,
       1000, TRUE, '테스트용 평지 가정. 현장 미확인.', '출발점에서 산책로를 따라간 뒤, 같은 길로 돌아와요.'
WHERE NOT EXISTS (
    SELECT 1 FROM running_course WHERE name = '[예시] 하천 산책로'
);

SELECT id, name, type, distance_meters
FROM running_course
ORDER BY id;
