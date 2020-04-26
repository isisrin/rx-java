## RxJava 읽기!!!

#### 리액티브 프로그래밍
 * `무엇이 데이터 처리를 수행하는 가?` : 데이터를 소비하는 측!
 * 데이터를 생산하는 측은 데이터를 전달만 함
    * 데이터를 소비하는 측의 처리를 기다릴 필요가 없음
    

#### Flowable, Observable
 * Flowable 
    * 대량데이터를 처리할 때 (만건 이상?)
    * 네트워크 통신이나 DB 등의 I/O를 처리할 때
 * Observable
    * GUI 이벤트
    * 소량 데이터를 처리할 때
    * 데이터 처리가 동기이고 Java Stream 대신 사용할 때

> Observable이 Flowable보다 오버헤드가 적다네요