<p align="center">
  <img src="https://github.com/user-attachments/assets/297c2434-d658-4d61-a633-66e7b06d7dcc" alt="Hi" width="150"/>
</p>

<h1 align="center">singlestore-Hikaricp-test</h1>

## 📖 Overview
SingleStore와 HikariCP 데이터베이스 커넥션 풀의 호환성을 검증하기 위한 샘플 프로젝트입니다. 🧪

이 코드는 관련 기술 블로그 포스트의 이해를 돕기 위해 작성되었습니다.

## ✨ 주요 테스트 내용
이 프로젝트는 다음 HikariCP의 주요 옵션들이 SingleStore 환경에서 정상적으로 동작하는지 확인합니다.
- ✅ **기본 연결** 및 CRUD(Create, Read, Update, Delete) 작업
- ✅ **connectionTimeout**: 커넥션 타임아웃 예외 처리 테스트
- ✅ **keepaliveTime**: 유휴 커넥션 유지 기능 테스트

## 🛠️ 시작하기 전에 (Prerequisites)
이 프로젝트를 실행하기 위해서는 아래 환경이 필요합니다.
- Java 11 이상
- Maven 3.6 이상
- 실행 중인 SingleStore 데이터베이스

## 📜 블로그 보러가기
<p align="center">
  <a href="https://a-platform.tistory.com/179">
    <img src="https://img.shields.io/badge/Tistory-Blog-FF5722?style=for-the-badge&logo=tistory&logoColor=white"/>
  </a>
  <a href="https://blog.naver.com/a-platformbiz/223978200645">
    <img src="https://img.shields.io/badge/Naver-Blog-03C75A?style=for-the-badge&logo=naver&logoColor=white"/>
  </a>
</p>
