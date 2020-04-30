import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 처리해야 할 데이터가 있지만 구독을 해지함
 * 첫 번째 스레드 데이터 : 1
 * 두 번째 스레드 데이터 : 1
 * 두번째 것 캔슬
 * 첫번째 것 캔슬
 */
public class L29_CompositeDisposableSample {
    public static void main(String[] args) throws Exception {
        CompositeDisposable compositeDisposable = new CompositeDisposable();

        compositeDisposable.add(Flowable.just(1,2,3)
            .doOnCancel(() -> System.out.println("첫번째 것 캔슬"))
            .observeOn(Schedulers.computation())   // 다른 스레드에서 실행시키고자 할 때
            .subscribe(data -> {
                Thread.sleep(100L);
                System.out.println("첫 번째 스레드 데이터 : " + data);
            }));

        compositeDisposable.add(Flowable.just(1,2,3)
            .doOnCancel(() -> System.out.println("두번째 것 캔슬"))
            .observeOn(Schedulers.computation())
            .subscribe(data -> {
                Thread.sleep(100L);
                System.out.println("두 번째 스레드 데이터 : " + data);
            }));

        Thread.sleep(150L);

        compositeDisposable.dispose();
    }
}
