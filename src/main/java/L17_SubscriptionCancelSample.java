import io.reactivex.Flowable;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.TimeUnit;

public class L17_SubscriptionCancelSample {

    public static void main(String[] args) throws Exception {

        Flowable.interval(200L, TimeUnit.MILLISECONDS)
                .subscribe(new Subscriber<Long>() {

                    private Subscription subscription;
                    private long startTime;

                    @Override
                    public void onSubscribe(Subscription subscription) {
                        this.subscription = subscription;
                        this.startTime = System.currentTimeMillis();
                        this.subscription.request(Long.MAX_VALUE);
                    }

                    // 데이터는 호출한 횟수?
                    @Override
                    public void onNext(Long aLong) {
                        // onNext가 호출된 시점에서 500이 지났는지를 판단 if를 주석하면 구독은 안끝남!!
                        if ((System.currentTimeMillis() - startTime) > 500) {
                            subscription.cancel();
                            System.out.println("구독 해지 해부렀습니다.");
                            return;
                        }

                        System.out.println("data= " + aLong);

                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {
                        System.out.println("이거 왜 안대오 ㅠㅠ");
                    }
                });

        Thread.sleep(2000L);
    }
}
