# 캡스톤디자인 프로젝트

web 소셜 커뮤니티
#

### 개발인원
- 2명

### 일정
- 2022-08-03 ~ 2022-12-07
- 팀프로젝트

## 사용기술
#### Backend
- Springboot
- SpringSecurity
- Java

#### DB
- h2, mariaDB

#### Test
- Swagger, Postman

#### Deployment
- github action, s3 bucket, Elastic Beanstalk

#### Frontend
- react, redux

## 내용

<details>
  <summary><h4>회원가입</h4></summary>

  > <image src="https://github.com/apem5186/SocialGallery/assets/81023500/d7d845c4-9bd8-4e35-8842-a40d6176ead0"/>
  #### [관련 코드-Service](https://github.com/apem5186/SocialGallery/blob/a3bc91522f0dba12aaf48b0a72a136a95793c3f2/galleryBackend/src/main/java/com/socialgallery/gallerybackend/service/security/SignService.java#L96)

  #### [관련 코드-Controller](https://github.com/apem5186/SocialGallery/blob/a3bc91522f0dba12aaf48b0a72a136a95793c3f2/galleryBackend/src/main/java/com/socialgallery/gallerybackend/controller/v1/SignController.java#L52)
</details>

<details>
  <summary><h4>로그인</h4></summary>

  > <image src="https://github.com/apem5186/SocialGallery/assets/81023500/41d50244-74fb-4e20-b79e-539757df5b2b"/>
  #### [관련 코드-Service](https://github.com/apem5186/SocialGallery/blob/a3bc91522f0dba12aaf48b0a72a136a95793c3f2/galleryBackend/src/main/java/com/socialgallery/gallerybackend/service/security/SignService.java#L71)
  #### [관련 코드-Controller](https://github.com/apem5186/SocialGallery/blob/a3bc91522f0dba12aaf48b0a72a136a95793c3f2/galleryBackend/src/main/java/com/socialgallery/gallerybackend/controller/v1/SignController.java#L38)
</details>

<details>
  <summary><h4>메인 페이지</h4></summary>

  > <image src="https://github.com/apem5186/SocialGallery/assets/81023500/c148f4e7-2bae-467c-9258-3611b34680d6"/>
  #### [관련 코드-Controller](https://github.com/apem5186/SocialGallery/blob/a3bc91522f0dba12aaf48b0a72a136a95793c3f2/galleryBackend/src/main/java/com/socialgallery/gallerybackend/controller/post/PostController.java#L226)
</details>

<details>
  <summary><h4>게시글 업로드</summary>

  > <image src="https://github.com/apem5186/SocialGallery/assets/81023500/168a9386-9a4e-4e44-a1b3-30d5f9a694f0"/>
  #### [관련 코드-Controller](https://github.com/apem5186/SocialGallery/blob/d597943c3318092111fa9b22c8916fa877d684b0/galleryBackend/src/main/java/com/socialgallery/gallerybackend/controller/post/PostController.java#L59)
  #### [관련 코드-Service](https://github.com/apem5186/SocialGallery/blob/d597943c3318092111fa9b22c8916fa877d684b0/galleryBackend/src/main/java/com/socialgallery/gallerybackend/service/post/PostService.java#L90)
</details>

<details>
  <summary><h4>사이드바 & 검색</summary>

  > <image src="https://github.com/apem5186/SocialGallery/assets/81023500/2dfb99c7-1857-4b00-9103-40b67a84a571"/>
  #### [관련 코드](https://github.com/apem5186/SocialGallery/blob/d597943c3318092111fa9b22c8916fa877d684b0/galleryBackend/src/main/java/com/socialgallery/gallerybackend/service/post/PostService.java#L437)
</details>

<details>
  <summary><h4>댓글 등록</h4></summary>

  > <image src="https://github.com/apem5186/SocialGallery/assets/81023500/f753da2b-a1ec-43a9-b94d-99a9a39c76cf"/>
  #### [관련 코드-Service](https://github.com/apem5186/SocialGallery/blob/d597943c3318092111fa9b22c8916fa877d684b0/galleryBackend/src/main/java/com/socialgallery/gallerybackend/service/comment/CommentService.java#L55)
  #### [관련 코드-Controller](https://github.com/apem5186/SocialGallery/blob/d597943c3318092111fa9b22c8916fa877d684b0/galleryBackend/src/main/java/com/socialgallery/gallerybackend/controller/comment/CommentController.java#L32)
</details>


<details>
  <summary><h4>로그아웃</h4></summary>

  > <image src="https://github.com/apem5186/SocialGallery/assets/81023500/6abdadd8-8b72-46fc-a072-24944957a22a"/>
  #### [관련 코드-Service](https://github.com/apem5186/SocialGallery/blob/d597943c3318092111fa9b22c8916fa877d684b0/galleryBackend/src/main/java/com/socialgallery/gallerybackend/service/security/SignService.java#L146)
  #### [관련 코드-Controller](https://github.com/apem5186/SocialGallery/blob/d597943c3318092111fa9b22c8916fa877d684b0/galleryBackend/src/main/java/com/socialgallery/gallerybackend/controller/v1/SignController.java#L72)
</details>
