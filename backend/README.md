# 앱테크 혜택 모음 - 백엔드 (Cloudflare Pages Functions)

Android 앱이 호출하는 API 서버입니다. 정적 파일 없이 API만 서빙하며,
혜택 데이터는 `data/apps.json`에 직접 정리해 넣습니다(별도 DB 없음).

## 로컬 개발

```bash
cd backend
npm install
npm run dev          # http://localhost:8788 에서 실행
```

확인:
```bash
curl http://localhost:8788/api/apps
curl http://localhost:8788/api/apps/example-app
```

## 새 앱/혜택 추가하는 방법

1. 사용자가 앱 링크를 대화창에 보낸다.
2. Claude가 그 페이지를 확인해서 받을 수 있는 혜택과 받는 방법을 정리한다.
3. `backend/data/apps.json`에 새 항목을 추가하거나 기존 항목을 수정한다.
4. 이미 설정된 Stop 훅이 변경 사항을 자동으로 커밋·푸시한다.
5. Cloudflare Pages와 GitHub 저장소를 연동해두면(아래 배포 섹션) push할 때마다
   자동으로 재배포되어 앱에 바로 반영된다.

## Cloudflare에 배포하기 (최초 1회, 사용자가 직접 진행)

Cloudflare 계정 인증이 필요한 단계라 이 부분은 저장소 소유자가 직접 진행해야 합니다.

1. Cloudflare 대시보드 → **Workers & Pages** → **Create application** → **Pages** →
   **Connect to Git**에서 이 GitHub 저장소(`hswTT17/APP-TECH`)를 선택
2. 빌드 설정:
   - **Root directory**: `backend`
   - **Build command**: (비워둠 — 별도 빌드 불필요)
   - **Build output directory**: `public`
3. 배포 완료 후 발급되는 URL(예: `https://app-tech-benefits.pages.dev`)을 확인
4. Android 앱 빌드 시 아래처럼 실제 배포 주소를 넘겨서 빌드:
   ```bash
   cd android
   ./gradlew :app:assembleRelease -PAPI_BASE_URL=https://app-tech-benefits.pages.dev/
   ```
   (지정하지 않으면 로컬 에뮬레이터용 기본값 `http://10.0.2.2:8788/`을 사용합니다)

CLI로 수동 배포하고 싶다면:
```bash
cd backend
npx wrangler login   # 최초 1회, 브라우저 인증 필요
npm run deploy
```

## 향후 외부 API 키가 필요해지면

지금은 외부 API 키가 필요 없지만, 나중에(예: 아이콘 자동 조회 등) 필요해지면:

```bash
cp .dev.vars.example .dev.vars   # 로컬 개발용, git에는 커밋되지 않음
# .dev.vars에 KEY=value 형태로 추가

npx wrangler pages secret put KEY_NAME   # 프로덕션 환경변수 등록
```

**Android 앱에는 어떤 API 키도 넣지 않습니다.** 모든 외부 API 호출은 이 백엔드를
거치도록 하고, 비밀 값은 Cloudflare 환경변수로만 관리합니다.
