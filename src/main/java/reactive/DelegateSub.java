package reactive;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class DelegateSub<T, R> implements Subscriber<T> {
    Subscriber subscriber;
    public DelegateSub(Subscriber<? super R> subscriber) {
        this.subscriber = subscriber;
    }

    @Override
    public void onSubscribe(Subscription subscription) {
        System.out.println("맵 퍼블리셔는 온서브를 호출했읍니당!!");
        subscriber.onSubscribe(subscription);
    }

    @Override
    public void onNext(T integer) {
        subscriber.onNext(integer);
    }

    @Override
    public void onError(Throwable throwable) {
        subscriber.onError(throwable);
    }

    @Override
    public void onComplete() {
        System.out.println("맵 퍼블리셔는 끝나쏘요!");
        subscriber.onComplete();
    }
}
