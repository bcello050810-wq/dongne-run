# 동네런

사용자가 등록한 러닝 코스를 현재 위치에서 가까운 순으로 추천하고,
목표 거리에 맞는 바퀴 수와 왕복 횟수를 알려주는 웹 서비스입니다.

## 기술 스택

| 구분 | 사용 기술 |
|---|---|
| 프론트엔드 | React 19, Vite 7 |
| 백엔드 | Spring Boot 4, Spring Data JPA, Java 21 |
| 데이터베이스 | MySQL 8 |

## 폴더 구조

```
dongne-run/
├── backend/            Spring Boot API 서버
│   └── sql/setup.sql   DB, 테이블, 예시 데이터 생성 SQL
├── frontend/           React 화면
└── 개발계획서/          기획서 문서
```

## 실행 방법

### 1. 데이터베이스 준비

MySQL에 접속해 `backend/sql/setup.sql`을 실행합니다.
`dongne_run` 데이터베이스와 `running_course` 테이블이 만들어지고, 예시 코스 2개가 등록됩니다.

```bash
mysql -u root -p < backend/sql/setup.sql
```

### 2. 백엔드 실행 (http://localhost:8080)

DB 계정은 코드에 적지 않고 환경변수로 넘깁니다.

| 환경변수 | 필수 | 기본값 |
|---|---|---|
| `DB_USERNAME` | O | 없음 |
| `DB_PASSWORD` | O | 없음 |
| `DB_URL` | X | `jdbc:mysql://127.0.0.1:3306/dongne_run` |

**IntelliJ**: `BackendApplication` 실행 구성 → Modify options → Environment variables에
`DB_USERNAME=root;DB_PASSWORD=비밀번호`를 넣고 실행합니다.

**터미널**:

```bash
cd backend
DB_USERNAME=root DB_PASSWORD=비밀번호 ./gradlew bootRun
```

Windows PowerShell:

```powershell
cd backend
$env:DB_USERNAME="root"; $env:DB_PASSWORD="비밀번호"; ./gradlew.bat bootRun
```

실행 확인: 브라우저에서 http://localhost:8080/api/courses 를 열면 코스 목록 JSON이 나옵니다.

### 3. 프론트엔드 실행 (http://localhost:5173)

```bash
cd frontend
npm install
npm run dev
```

## API

| 메서드 | 경로 | 설명 | 상태 |
|---|---|---|---|
| GET | `/api/courses` | 전체 코스 목록 | 완료 |
| GET | `/api/courses/recommend` | 위치·목표 거리 기준 추천 | 예정 |
| GET | `/api/courses/{id}` | 코스 상세 | 예정 |
| POST | `/api/courses` | 코스 등록 | 예정 |

## 협업 규칙

- `main`에 직접 push하지 않고, `feature/작업이름` 브랜치에서 작업한 뒤 PR로 합칩니다.
- 커밋 메시지 머리말: `feat` 기능, `fix` 버그, `test` 테스트, `docs` 문서, `refactor` 구조 개선
- 작업한 날에는 Notion 개발일지에 한 일, PR 링크, 다음 할 일을 기록합니다.
