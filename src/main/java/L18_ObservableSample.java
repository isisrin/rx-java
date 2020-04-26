import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class L18_ObservableSample {

    public static void main(String[] args) throws Exception {
        /**
         * Observable<String> stringObservable = Observable.create(new ObservableOnSubscribe<String>() {
         * @Override
         * public void subscribe(ObservableEmitter<String> emitter) throws Exception {
         */
        Observable<String> stringObservable = Observable.create((emitter) -> {
            String[] datas = { "안녕 ", "RxJava는 처음이지?" };

            for (String data : datas) {
                if (emitter.isDisposed()) {
                    return;
                }

                emitter.onNext(data);
            }

            emitter.onComplete();
        }); // 데이터 배압이 없어서 생기는 족족 통지해 버린다

        stringObservable
                .observeOn(Schedulers.computation())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        // 암것도 안한다네요.. 몇개씩 받을 필요가 없이 때문인걸까용
                    }

                    @Override
                    public void onNext(String data) {
                        String threadName = Thread.currentThread().getName();
                        System.out.println(threadName + ": " + data);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
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
