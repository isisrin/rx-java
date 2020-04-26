import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class L11_FlowableSample {

    public static void main(String[] args) throws Exception {
        /**
         * 데이터 통지는 create()함수에서 하고, 인자로 FlowableOnSubscribe를 받음
         * FlowableOnSubscribe.subscribe(0 함수를 통해 subscriber에 통지함
         * Flowable<String> flowable = Flowable.create(new FlowableOnSubscribe<String>() {
         * @Override
         * public void subscribe(FlowableEmitter<String> emitter) throws Exception {
         */
        Flowable<String> flowable = Flowable.create(emitter -> {
            String[] datas = { "헬로우 월드는",  "이제 지겨워요!" };
            for (String data : datas) {
                // 구독이 중지되면 처리 끝냄
                if (emitter.isCancelled()) {
                    return;
                }

                emitter.onNext(data);
            }

            emitter.onComplete();
        }, BackpressureStrategy.BUFFER);

        flowable.observeOn(Schedulers.computation())
                .subscribe(new Subscriber<String>() {

                    private Subscription subscription;

                    /**
                     * Flowable이 구독되고 데이터 통지가 준비됐을 떄 호출되는 메서드
                     * 데이터 개수 요청을 하기 위한 subscription을 받음
                     * @param subscription
                     */
                    @Override
                    public void onSubscribe(Subscription subscription) {
                        this.subscription = subscription;
                        this.subscription.request(1L);
                    }

                    @Override
                    public void onNext(String data) {
                        String threadName = Thread.currentThread().getName();
                        System.out.println(threadName + ": " + data);
                        /**
                         * 데이터를 처리한 뒤에 몇개를 받을 건지 subscription에 요청해야 함.
                         * 데이터 개수를 요청하지 않음 데이터 통지를 받을 수 엄슴
                         */
                        this.subscription.request(1L);
                    }

                    @Override
                    public void onError(Throwable t) {
                        String threadName = Thread.currentThread().getName();
                        System.out.println(threadName + ": error");
                    }

                    @Override
                    public void onComplete() {
                        String threadName = Thread.currentThread().getName();
                        System.out.println(threadName + ": 완료했읍니다요!!!");
                    }
                });
        Thread.sleep(1000L);
    }
}
