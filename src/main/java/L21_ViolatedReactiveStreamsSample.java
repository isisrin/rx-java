import io.reactivex.Flowable;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/**
 서브 스크라이빙 시작!
 서브 스크라이빙 끝   -- onSubscribe 가 끝나고 onNext가 호출 됨
 데이터 : 1
 데이터 : 2
 데이터 : 3
 통지 완료!!
 */
public class L21_ViolatedReactiveStreamsSample {
    public static void main(String[] args) {
        Flowable.range(1, 3)
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        System.out.println("서브 스크라이빙 시작!");
                        s.request(Long.MAX_VALUE);
                        System.out.println("서브 스크라이빙 끝");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        System.out.println("데이터 : " + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("통지 완료!!");
                    }
                });
    }
}
