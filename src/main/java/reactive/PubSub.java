package reactive;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.Arrays;
import java.util.Iterator;

public class PubSub {
    public static void main(String[] args) {
        Iterable<Integer> iterable = Arrays.asList(1,2,3,4,5);

        // Publisher <- Observable
        Publisher publisher = (subscriber) -> {
            Iterator<Integer> iterator = iterable.iterator();

            // Subscription -  버퍼갯수 조절
            subscriber.onSubscribe(new Subscription() {

                @Override
                public void request(long l) {
                    System.out.println("리퀘스트 할거에여!! " + l);

                    while (l-- > 0) {
                        if (iterator.hasNext()) {
                            subscriber.onNext(iterator.next());
                        } else {
                            subscriber.onComplete();
                            break;
                        }
                    }
                }

                @Override
                public void cancel() {
                    System.out.println("취소할거에여! " );
                }
            });
        };


        // Subscriber <- Observer
        Subscriber subscriber = new Subscriber() {
            Subscription subscription;
            @Override
            public void onSubscribe(Subscription subscription) {
                this.subscription = subscription;
                System.out.println("온 서브!!");
                // 여기서 몇개씩 받을지 정할 수 있당
                subscription.request(1);
            }

            @Override
            public void onNext(Object o) {
                System.out.println("온 넥!! " + o);
                // 내가 처리할 갯수를 지정해봤습니다!
                // 현재 버퍼사이즈 등을 고려해서 호출할지 말지도 정하기 가능입니다!
                subscription.request(1);

            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("온 에!! ");
            }

            @Override
            public void onComplete() {
                System.out.println("온 컴!");
            }
        };

        publisher.subscribe(subscriber);
    }
}
