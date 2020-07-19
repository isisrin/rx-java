package reactive;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * IteratorPublisher -> [Data1] -> Operator(데이터 변환!) -> [Data2] -> LogSubscriber
 *                              <- subscribe(LogSubscriber)
 *                              -> onSubscribe(subscription)
 *                              -> onNext
 *                              -> onComplete
 */
public class PubSub2 {
    public static void main(String[] args) {
        Publisher<Integer> publisher = IteratorPublisher(Stream.iterate(1, a -> a+1).limit(10).collect(Collectors.toList()));
        Publisher<Integer> mapPublisher = mapPublisher(publisher, s -> s * 10);
        mapPublisher.subscribe(LogSubscriber());
    }

    private static Publisher<Integer> mapPublisher(Publisher<Integer> publisher, Function<Integer, Integer> integerIntegerFunction) {
        return subscriber -> publisher.subscribe(new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription subscription) {
                System.out.println("맵 퍼블리셔는 온서브를 호출했읍니당!!");
                subscriber.onSubscribe(subscription);
            }

            @Override
            public void onNext(Integer integer) {
                subscriber.onNext(integerIntegerFunction.apply(integer));
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
        });

    }

    private static Subscriber<Integer> LogSubscriber() {
        return new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription subscription) {
                System.out.println("로그 섭스애오!! ");
                subscription.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(Integer integer) {
                System.out.println("로그 넥스트애오!! " + integer);
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("로그 에러애오!! ");
            }

            @Override
            public void onComplete() {
                System.out.println("로그 컴플이애오!! ");
            }
        };
    }

    private static Publisher<Integer> IteratorPublisher(Iterable<Integer> list) {
        return subscriber -> subscriber.onSubscribe(new Subscription() {
            @Override
            public void request(long l) {
                list.forEach(subscriber::onNext);
                subscriber.onComplete();
            }

            @Override
            public void cancel() {

            }
        });
    }
}
