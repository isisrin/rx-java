package reactive;

import reactor.core.publisher.Flux;

public class ReactorEx {
    public static void main(String[] args) {
        //  Flux = 일종의 퍼블리셔!
        Flux.<Integer>create(s -> {
            s.next(1);
            s.next(2);
            s.next(3);
        })
        .log()              // onNext(1)  (로그 값)
        .map(s->s*10)       // 맵을 거쳐서 변환이 되어부러따요!
        .log()              // onNext(10) (로그 값)
        .subscribe(System.out::println);
    }
}
