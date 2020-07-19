package reactive;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.function.BiFunction;
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
        Publisher<String> mapPublisher = mapPublisher(publisher, s -> "[" + s + "]");
//        Publisher<Integer> mapPublisher = mapPublisher(publisher, s -> s * 10);
//        Publisher<Integer> sumPublisher = sumPub(publisher);
        Publisher<String> reducePublisher = reducePub(publisher, "", (a,b) -> a+ "-" +b);
        reducePublisher.subscribe(LogSubscriber());
    }

    private static <T, R> Publisher<String> reducePub(Publisher<T> publisher, R init, BiFunction<R, T, R> integerBiFunction) {
        return subscriber ->
            publisher.subscribe(new DelegateSub<T, R>((Subscriber<? super R>) subscriber) {
                R result = init;

                @Override
                public void onNext(T integer) {
                    result = integerBiFunction.apply(result, integer);
                }

                @Override
                public void onComplete() {
                    subscriber.onNext(result);
                    subscriber.onComplete();
                }
            });
    }

/*    private static Publisher<Integer> sumPub(Publisher<Integer> publisher) {
        return subscriber -> publisher.subscribe(new DelegateSub(subscriber) {
            int sum = 0;

            @Override
            public void onNext(Integer integer) {
                sum += integer;
            }

            // 마무리 하기전에 sum을 보내고, 다 끝났음을 알려도 됩니당
            @Override
            public void onComplete() {
                subscriber.onNext(sum);
                subscriber.onComplete();
            }
        });
    }*/

    // T type이 들어오면 R타입을 리턴하도록..
    private static <T, R> Publisher<R> mapPublisher(Publisher<T> publisher, Function<T, R> integerFunction) {
        return subscriber -> publisher.subscribe(new DelegateSub<T, R>(subscriber) {
            @Override
            public void onNext(T integer) {
                subscriber.onNext(integerFunction.apply(integer));
            }
        });
    }

    private static <T> Subscriber<T> LogSubscriber() {
        return new Subscriber<T>() {
            @Override
            public void onSubscribe(Subscription subscription) {
                System.out.println("로그 섭스애오!! ");
                subscription.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(T integer) {
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
